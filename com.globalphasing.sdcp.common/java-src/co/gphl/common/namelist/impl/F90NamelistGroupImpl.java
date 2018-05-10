/*******************************************************************************
 * Copyright (c) 2010, 2017 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/
package co.gphl.common.namelist.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.gphl.common.namelist.F90NamelistData;
import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.F90NamelistValueException;
import co.gphl.common.namelist.NamelistGroup;
import co.gphl.common.namelist.VarnameComparator;

public abstract class F90NamelistGroupImpl extends AbstractNamelistGroupImpl
        implements F90NamelistGroup {

    /**
     * 
     */
    private static final long serialVersionUID = 1758046791779837267L;
    private F90NamelistData owningData = null;

    private static Matcher boolMatcher = Pattern.compile("(\\.?t).*|(\\.?f).*",
            Pattern.CASE_INSENSITIVE).matcher("");
    
    protected F90NamelistGroupImpl( VarnameComparator varnameComparator, Set<String> charVarnames, Integer lineNo) {
        super(varnameComparator, charVarnames, lineNo);
        this.maxLineLen = 70;
    }


    @Override
    public String[] put( String varName, String valueList ) {
        // Fortran90 identifiers start with a letter, then a sequence of letters,
        // digits and underscores
        if ( ! varName.matches("[A-Za-z]\\w*") )
            throw new F90NamelistValueException( varName + ": Not a legal Fortran identifier" );

        return super.put(varName, valueList);

    }

    @Override
    public String[] put( String varName, String[] values ) {
        // Fortran90 identifiers start with a letter, then a sequence of letters,
        // digits and underscores
        if ( ! varName.matches("[A-Za-z]\\w*") )
            throw new F90NamelistValueException( varName + ": Not a legal Fortran identifier" );

        return super.put(varName, values);

    }

    @Override
    public List<Boolean> putBooleanValue(String varName, Boolean value) {
        
        List<Boolean> retval = this.getBooleanList(varName);
        
        if ( value == null )
            this.remove(varName);
        else
            this.put(varName, value ? ".TRUE." : ".FALSE." );
        return retval;
        
    }
        
    @Override
    public List<Boolean> getBooleanList(String varName) {
        
        List<Boolean> retval = new ArrayList<>();
        String[] values = this.get(varName);
        
        if ( values != null ) {
            for ( String v: values ) {
                if ( v == null )
                    retval.add(null);
                else {
                    if ( ! F90NamelistGroupImpl.boolMatcher.reset(v).matches() )
                        throw new RuntimeException(
                                String.format("Input value '%s' is not a valid boolean!", v ) );
                    // We must have matched either group 1 or 2 here.
                    // group 1 => true, group 2 => false
                    retval.add( F90NamelistGroupImpl.boolMatcher.group(1) != null );
                }
            }
        }
        return retval;
    }
    
    @Override
    public Boolean[] appendBooleanValue(String varName, Boolean value) {
        
        List<Boolean> retval = this.getBooleanList(varName);
        if ( value == null )
            this.appendStringValue(varName, null);
        else
            this.appendStringValue(varName, value ? ".TRUE." : ".FALSE.");
        return retval.isEmpty() ? null : retval.toArray( new Boolean[retval.size()]);
        
        
    }
    
    @Override
    protected List<String> splitValueList ( String value ) {

        List<String> retval = super.splitValueList(value);
        // Remove all trailing nulls (to fit in with Fortran90 namelist semantics)
        // but leave first value in list even if it is a null, because a value sequence
        // must have at least one value in the namelist format.
        for ( int i = retval.size() - 1; i >=1 && retval.get(i) == null; i-- )
            retval.remove(i);

        return retval;

    }

    // Yucky method to split off the first element of a value sequence
    @Override
    protected String[] splitNextValue ( String value ) {

        String delimiter, terminator;
        String [] retval;

        // If the first character of the next token is ' or ", that character is
        // the delimiter of the next value.
        if ( value.charAt(0) == '\'' || value.charAt(0) == '"' ) {
            delimiter = Character.toString( value.charAt(0) );
            // Need zero-width look-behind assertion, so that we don't
            // break on embedded delimiters followed by whitespace/commas, such as
            // '''abc'' ''def'''
            terminator = String.format("(?<!%c)%c", value.charAt(0), value.charAt(0));
        }
        else {
            delimiter = "";
            terminator = "";
        }
        terminator += valueTerminator; 

        // Skip over opening delimiter, then find next one
        retval = value.substring(delimiter.length()).split(terminator, 2);

        // Form of regex in terminator ensures that tokens must be of length 2
        // at this point for any legal input. If it is only length 1, this indicates
        // an improperly delimited string constant.

        if ( retval.length != 2 ) {
            throw new F90NamelistValueException("No closing delimiter for string constant");
        }

        if ( delimiter.length() > 0 ) {
            // Check for illegal string constant
            if ( retval[0].matches( "[^" + delimiter + "]*" + delimiter + "[^" + delimiter + "].*") ) {
                throw new F90NamelistValueException( "Illegal Fortran string constant: contains single <" + 
                        delimiter + "> character");
            }
            // Collapse repeated delimiters (in Fortran 'a''b' is the same as "a'b")
            retval[0] = retval[0].replace(delimiter + delimiter, delimiter);
        }

        if ( retval[0] != null ) {
            // Trailing whitespace is meaningless in Fortran, so we get rid of it now.
            retval[0] = retval[0].replaceFirst("\\s+$", "");
            // If value was whitespace-only (i.e. equivalent to null)
            // it will now be empty, so test for that
            if ( retval[0].isEmpty() )
                retval[0] = null;
            
        }
        
        return retval;
    }

    // Always true for F90 namelist groups!
    @Override
    protected boolean areVarNamesUnique() {
        return true;
    }

    /* (non-Javadoc)
     * @see co.gphl.common.namelist.NamelistGroup#putAll(co.gphl.common.namelist.NamelistGroup)
     */
    @Override
    public void putAll(NamelistGroup group) {
        this.putAll(group.map());

    }

    /* (non-Javadoc)
     * @see co.gphl.common.namelist.NamelistGroup#map()
     */
    @Override
    public Map<String, String[]> map() {
        return Collections.unmodifiableMap(this);
    }

    @Override
    public F90NamelistData getOwningData() {
        return owningData;
    }

    @Override
    public void setOwningData(F90NamelistData owningData) {
        this.owningData = owningData;
    }

    public int getMaxLineLen() {
        return this.maxLineLen;
    }

    public void setMaxLineLen(int maxLineLen) {
        if ( maxLineLen < 2 )
            throw new IllegalArgumentException("Called with maxLineLen " + maxLineLen + ". Must be >=2");
        this.maxLineLen = maxLineLen;
    }

    protected void setMaxValsPerLine(int maxValsPerLine) {
        this.maxValsPerLine = maxValsPerLine;
    }
    
}

