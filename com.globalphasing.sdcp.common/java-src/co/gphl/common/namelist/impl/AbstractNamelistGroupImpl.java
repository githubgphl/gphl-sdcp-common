/*******************************************************************************
 * Copyright (c) 2010, 2017 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/
package co.gphl.common.namelist.impl;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import co.gphl.common.namelist.NamelistGroup;
import co.gphl.common.namelist.F90NamelistValueException;
import co.gphl.common.namelist.VarnameComparator;

/**
 * @author pkeller
 *
 */
public abstract class AbstractNamelistGroupImpl extends
    TreeMap<String, String[]> implements NamelistGroup {

    /**
     * 
     */
    private static final long serialVersionUID = -1132029170103736118L;

    // regex that matches the end of a value.
    // It may also match in the middle of a value: it is up to the particular
    // subclass to override the splitNextValue method to make sure that
    // this regex is used in a way that is suitable for the particular
    // namelist dialect that it uses.
    protected static final String valueTerminator = "($|\\s*[,\\s]\\s*)";

    // Where we are reading namelist data, we store the line number so that
    // we can give some useful diagnostics.
    protected Integer lineNo = null;

    // Approximate maximum line length on output.
    protected int maxLineLen;

    // Maximum number of values per line
    protected int maxValsPerLine;
    
    // Keep track of this explicitly, so we don't have to keep
    // fishing it back from the TreeMap and casting it.
    private VarnameComparator comparator = null;
    private Set<String> charVarnames = null;

    /**
     * Constructor for namelist groups that use a {@link VarnameComparator} and
     * a {@link Set} to define the ordering of variable names and which
     * variables hold string data.
     * 
     * @param varnameComparator
     * @param charVarnames
     * @param lineNo
     */
    protected AbstractNamelistGroupImpl ( VarnameComparator comparator, 
            Set<String> charVarnames,
            Integer lineNo) {
        super(comparator);
        this.comparator = comparator;
        this.charVarnames = charVarnames;
        this.lineNo = lineNo;
    }

    protected AbstractNamelistGroupImpl ( NamelistGroup group ) {
        // N.B. if group implements SortedMap, the ordering is maintained
        // in the new instance
        super(group.map());
    }

    public Integer getLineNo() {
        return this.lineNo;
    }

    @Override
    public Boolean accepts(String keyName) {
        return this.comparator == null ? null : this.comparator.contains(keyName);
    } 

    /**
     * If this method returns true, the namelist group allows
     * non-unique keys which are disambiguated by appending a
     * {@code =} character followed by a string to the actual
     * variable name used.
     * 
     * In the current implementation, a namelist group for which
     * this is true can only be instantiated with empty content,
     * populated programatically and written out. It cannot be 
     * instantiated by reading from a file.
     * 
     * The application is responsible for maintaining the list of
     * disambiguation suffixes.
     * 
     * @return whether or not variable names must be unique for this namelist group
     */
    protected abstract boolean areVarNamesUnique();

    protected List<String> splitValueList ( String valueList ) {
        
        if ( valueList == null )
            return Collections.emptyList();
        
        String myValueList = new String(valueList.trim());
        if ( myValueList.length() == 0 )
            return Collections.emptyList();
        
        String[] tokens;
        ArrayList<String> values = new ArrayList<String>();
        int rc;

        while ( myValueList != null && myValueList.length() > 0 ) {

            // First check for a repeat count
            if ( myValueList.matches("\\d+\\*.*") ) {
                tokens = myValueList.split("\\*", 2);
                // No need to re-trim here: spaces either side of the '*' mean something
                // other than 'r*v'. 'r* v' means r nulls followed by v; others are
                // syntactically wrong.
                myValueList = tokens[1];
                rc = Integer.parseInt(tokens[0]);           
            }
            else
                rc = 1;

            // Get next token
            tokens = splitNextValue(myValueList);

            // Retain rest of line for next iteration, discarding comments
            if ( tokens[1].length() > 0 && tokens[1].charAt(0) != '!' ) {
                myValueList = tokens[1];
            }
            else {
                myValueList = null;
            }

            for ( int i = 0; i < rc; i++ ) {
                // In Fortan-land, there is no distinction between a string that consists
                // of zero or more spaces and a null. Map to null in all these cases
                // (value has already been trim()-med )
                values.add( tokens[0] == null || tokens[0].length() == 0 ? null : tokens[0] );
            }

        }

        if ( values.size() == 0 )
            throw new F90NamelistValueException("A name-value subsequence must have at least one value");

        return values;

    }


    /**
     * Splits next token from value. Returns an array of up to two elements,
     * the first of which contains the next token, if any. The second element
     * will contain the rest of value, after removing the first token, including
     * separator characters.
     * 
     * This implementation simply splits on a regex. Subclasses should override
     * this method to deal with more complex requirements, such as character
     * delimiters.
     * 
     * @param value String representing remaining value list to be split
     * @return array of up to two elements
     */

    // FIXME! Use a java.util.StringTokenizer-style interface for this
    protected String[] splitNextValue ( String value ) {
        return value.split(valueTerminator, 2);        
    }

    public String[] put( String varName, String valueList ) {
        // TODO: have some check for duplicate values.
        // Should be optional, i.e. only check if some static member of this
        // class is set: allowing later assignments to a variable overwrite
        // earlier ones is normal F90 behaviour I think.

        // We need to special-case an empty value list because it seems that
        // the Fortran90 standard disallows a name-value subsequence list
        // with no values like this:
        //
        //   VAR=
        //
        // so we translate it to one with a single null like this:
        //
        //   VAR=,
        //
        List<String> values = this.splitValueList(valueList);
                return this.put(varName, values.toArray( new String[values.size()] ) );
    }

    public String[] put( String varName, Enum<?>[] valueArray ) {
        List<String> valueList = new ArrayList<String>();
        for ( Enum<?> e: valueArray) {
            valueList.add( e == null ? null : e.toString());
        }

        String[] values = new String[valueList.size()];
        return this.put(varName, valueList.toArray(values));
    }

    @Override
    public String[] put(String varName, Number value) {
        return this.put(varName, value == null ? null : value.toString());
    }

    @Override
    public String[] put ( String varName, List<?> values ) {

        if ( values == null )
            return null;
        
        String[] valueList = new String[values.size()];
        Object val;
        for ( int i = 0; i < valueList.length; i ++ ) {
            val = values.get(i);
            valueList[i] = val == null ? null : val.toString();
        }

        return this.put(varName, valueList);
    }

    @Override
    public String[] putStringValue(String varName, String value) {
        return this.put(varName, new String[]{value} );
    }
    
    @Override
    public String[] append(String varName, String valueList) {
    
        String[] vals = this.get(varName);
        if ( vals == null || vals.length == 0 )
            return this.put(varName, valueList);
        
        List<String> newValues = this.splitValueList(valueList);
        if ( newValues.size() == 0 )
            return Arrays.copyOf(vals, vals.length);
        
        String[] newVals = Arrays.copyOf(vals, vals.length + newValues.size());
        for ( int i=vals.length; newValues.size() > 0; i++ )
            newVals[i] = newValues.remove(0);
        
        return this.put(varName, newVals);
    }
    
    @Override
    public String[] append(String varName, Number value) {
        return this.append(varName, value == null ? null : value.toString());
    }

    @Override
    public String[] append(String varName, List<?> values) {

        String[] vals = this.get(varName);
        if ( vals == null || vals.length == 0 )
            return this.put(varName, values);
            
        if ( values == null || values.size() == 0 )
            return vals;
        
        String[] newVals = Arrays.copyOf(vals, vals.length + values.size());
        Object val;
        for ( int i = 0; i < values.size(); i++ ) {
            val = values.get(i);
            newVals[vals.length+i] = val == null ? null : val.toString();
        }
        return this.put(varName, newVals);
        
    }
    
    @Override
    public String[] appendStringValue(String varName, String value) {
        return this.append(varName, Arrays.asList( new String[]{value}) );
    }
    
    @Override
    public Date getTime(String varName, DateFormat dateFormat) {
        
        String strDate = this.getStringValue(varName);
        if ( strDate == null )
            return null;
        
        if ( dateFormat == null )
            dateFormat = DateFormat.getDateTimeInstance();
        
        try {
            return dateFormat.parse(strDate);
        } catch (ParseException e) {
            throw new RuntimeException("Cannot parse timestamp " + strDate, e);
        }
        
    }
    
    @Override
    public <T extends Number> List<T> getNumList(Class<T> type, String varName) {
       
        // This method will fail for the following subtypes of Number:
        // AtomicInteger, AtomicLong, BigDecimal, BigInteger
        // but we don't really care about that.
        try {
            // Roll on Java 8, which would let me use things like Double::valueOf
            // rather than using reflection......
            Method valueOf = type.getDeclaredMethod("valueOf", String.class);
            String[] vals = this.get(varName);
            
            List<T> retval = null;
            if ( vals == null || vals.length == 0 )
                retval = Collections.emptyList();
            else {
                retval = new ArrayList<T>(vals.length);
                for ( String value: vals ) {
                    retval.add( value == null ? null : type.cast(valueOf.invoke(null, value)) );
                }
            }
            return retval;
        } catch (NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new RuntimeException("BUG: Needs fixing!");
        }
        
    }
    
    @Override
    public List<Double> getDoubleList ( String varName ) {
        List<Double> retval = new ArrayList<Double>(3);
        for ( String value: this.get(varName) ) {
            retval.add( value == null ? null : Double.parseDouble(value) );
        }
        return retval;
    }

    @Override
    public List<Float> getFloatList ( String varName ) {
        List<Float> retval = new ArrayList<Float>(3);
        for ( String value: this.get(varName) ) {
            retval.add( value == null ? null : Float.parseFloat(value) );
        }
        return retval;
    }


    @Override
    public String getStringValue ( String varName ) throws RuntimeException {
        String retval = null;
        String[] vals = this.get(varName);
        if ( vals != null ) {
            if ( vals.length == 1 )
                retval = vals[0];
            else
                throw new RuntimeException("Namelist group " +
                        " variable " + varName + "; 0 or 1 values expected but " + 
                        vals.length + " found");
        }
        return retval;
    }

    @Override
    public <T extends Number> T getNumValue(Class<T> type, String varName) {
        
        String val = this.getStringValue(varName);
        if ( val == null )
            return null;
        if ( type == Double.class )
            return type.cast( Double.valueOf(val) );
        if ( type == Float.class )
            return type.cast( Float.valueOf(val) );
        if ( type == Integer.class )
            return type.cast( Integer.valueOf(val) );
        if ( type == Long.class )
            return type.cast( Long.valueOf(val) );
        if ( type == Short.class )
            return type.cast( Short.valueOf(val) );
        if ( type == Byte.class )
            return type.cast( Byte.valueOf(val) );
        
        throw new IllegalArgumentException("This method cannot return values of type " + type.getName() );
        
    }
    
    
    public Double getDoubleValue ( String varName ) {
        String val = this.getStringValue(varName);
        return val == null ? null : Double.valueOf(val);        
    }

    public Float getFloatValue ( String varName ) {
        String val = this.getStringValue(varName);
        return val == null ? null : Float.valueOf(val);        
    }

    public Integer getIntegerValue ( String varName ) {
        String val = this.getStringValue(varName);
        return val == null ? null : Integer.valueOf(val);        
    }

    public Long getLongValue ( String varName ) {
        String val = this.getStringValue(varName);
        return val == null ? null : Long.valueOf(val);        
    }


    public void write(Writer writer, String valueSeparator) throws IOException {

        // We use an iterator to loop over namelist group variables,
        // so that we can query the hasNext() method inside the loop
        Iterator< Map.Entry<String, String[]> > iter = this.entrySet().iterator();
        Map.Entry<String, String[]> entry;
        boolean nullOK = ( valueSeparator != null && valueSeparator.indexOf(',') > -1 );
        String outLine;
        int valsOnLine = 0;
        while ( iter.hasNext() ) {

            // Get next variable and type info if available
            entry = iter.next();
            String key = entry.getKey();

            // Cater for var names having been disambiguated with a '=...' suffix
            if ( !this.areVarNamesUnique() ) {
                key = ( key.split("=", 2) )[0];
            }
            key = key.toUpperCase();

            outLine = key;
            String sep = "=";
            // Iterate over values assigned to this variable
            for ( String v: entry.getValue() ) {

                // Always write out the separator before adding a new value
                outLine += sep;

                // Now check if the number of values already on this line
                // is already at the maximum, if specified
                if ( this.maxValsPerLine > 0 && valsOnLine >= this.maxValsPerLine ) {
                    writer.append( outLine + "\n");
                    outLine = "  ";
                    valsOnLine = 0;
                }
                
                if ( v == null || v.length() == 0 ) {
                    if ( ! nullOK )
                        throw new RuntimeException("Key '" + key + "' has one or more null values, but we are not using commas as separators");
                    else
                        // Null value: output separator only.
                        // Don't bother to check line length in this case.
                        outLine += valueSeparator;
                }

                else {
                    // Non-null value.

                    // First check line length.
                    // Need to do this to work around gfortran bug that doesn't parse
                    // the sequence "[^,]\n," correctly.
                    if ( outLine.length() > this.maxLineLen ) {
                        writer.append( outLine + "\n");
                        outLine = "  ";
                        // Don't reset valsOnLine here, because that works with
                        // "logical" lines. If we overflow the maximum line length,
                        // we want the logical start of line to re-sync
                    }

                    // Specific transformations of values on output:
                    // CHAR => single-quoted, with embedded single quotes doubled.
                    if ( charVarnames != null && charVarnames.contains(key) ) 
                        outLine += "'" + v.replace("'", "''") + "'";
                    else {
                        outLine += v;
                    }
                }
                valsOnLine++;
                sep = valueSeparator;
            }

            if ( outLine.length() > 2 )
                writer.append(outLine + "\n");

        }


    }

    @Override
    public boolean containsKey(String varName) {
        return super.containsKey(varName);
    }

    @Override
    public int size(String varName) {
        if ( ! this.containsKey(varName) )
            return 0;
        return this.get(varName).length;
    }

    @Override
    public String[] get(String varName) {
        String[] retval = super.get(varName);
        if ( retval != null )
            retval = Arrays.copyOf(retval, retval.length);
        return retval;
    }

    @Override
    public VarnameComparator comparator() {
        return this.comparator;
    }

}
