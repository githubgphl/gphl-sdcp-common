/*******************************************************************************
 * Copyright © 2010, 2015 by Global Phasing Ltd. All rights reserved             
 *                                                                              
 * This software is proprietary to and embodies the confidential                
 * technology of Global Phasing Limited (GPhL).                                 
 *                                                                              
 * Any possession or use (including but not limited to duplication, reproduction
 * and dissemination) of this software (in either source or compiled form) is   
 * forbidden except where an agreement with GPhL that permits such possession or
 * use is in force.                                                             
 *******************************************************************************/
package co.gphl.common.namelist.impl;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
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

    protected List<String> splitValueList ( String value ) {
        String myValue = new String(value.trim());
        String[] tokens;
        ArrayList<String> valueList = new ArrayList<String>();
        int rc;

        while ( myValue != null && myValue.length() > 0 ) {

            // First check for a repeat count
            if ( myValue.matches("\\d+\\*.*") ) {
                tokens = myValue.split("\\*", 2);
                // No need to re-trim here: spaces either side of the '*' mean something
                // other than 'r*v'. 'r* v' means r nulls followed by v; others are
                // syntactically wrong.
                myValue = tokens[1];
                rc = Integer.parseInt(tokens[0]);           
            }
            else
                rc = 1;

            // Get next token
            tokens = splitNextValue(myValue);

            // Retain rest of line for next iteration, discarding comments
            if ( tokens[1].length() > 0 && tokens[1].charAt(0) != '!' ) {
                myValue = tokens[1];
            }
            else {
                myValue = null;
            }

            for ( int i = 0; i < rc; i++ ) {
                // In Fortan-land, there is no distinction between a string that consists
                // of zero or more spaces and a null. Map to null in all these cases
                // (value has already been trim()-med )
                valueList.add( tokens[0] == null || tokens[0].length() == 0 ? null : tokens[0] );
            }

        }

        if ( valueList.size() == 0 )
            throw new F90NamelistValueException("A name-value subsequence must have at least one value");

        return valueList;

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
        List<String> values = ( valueList == null || valueList.length() == 0 ) ?
                Arrays.asList( new String[] {null} ) :
                    splitValueList(valueList);
                return this.put(varName, values.toArray( new String[values.size()] ) );
    }

    public String[] put( String varName, Enum<?>[] valueArray ) {
        List<String> valueList = new ArrayList<String>();
        for ( Enum<?> e: valueArray) {
            valueList.add(e.toString());
        }

        String[] values = new String[valueList.size()];
        return this.put(varName, valueList.toArray(values));
    }

    @Override
    public String[] put(String varName, Integer value) {
        return this.put(varName, value.toString());
    }

    @Override
    public String[] put(String varName, Long value) {
        return this.put(varName, value.toString());
    }

    @Override
    public String[] put(String varName, Double value) {
        return this.put(varName, value.toString());
    }

    @Override
    public String[] put ( String varName, List<Double> values ) {

        String[] valueList = new String[values.size()];
        for ( int i = 0; i < valueList.length; i ++ )
            valueList[i] = values.get(i).toString();

        return this.put(varName, valueList);
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
                // FIXME! We strip trailing whitespace to cope with F90 quoting rules
                // and F90 whitespace handling, so it should be done in
                // F90NamelistGroupImpl.splitNextValue, not here.
                retval = vals[0].replaceFirst("\\s+$", "");
            else
                throw new RuntimeException("Namelist group " +
                        " variable " + varName + "; 0 or 1 values allowed but " + 
                        vals.length + " found");
        }
        return retval;
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
                    }

                    // Specific transformations of values on output:
                    // CHAR => single-quoted, with embedded single quotes doubled.
                    if ( charVarnames != null && charVarnames.contains(key) ) 
                        outLine += "'" + v.replace("'", "''") + "'";
                    else {
                        outLine += v;
                    }
                }

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
        return super.get(varName);
    }

    @Override
    public VarnameComparator comparator() {
        return this.comparator;
    }

}
