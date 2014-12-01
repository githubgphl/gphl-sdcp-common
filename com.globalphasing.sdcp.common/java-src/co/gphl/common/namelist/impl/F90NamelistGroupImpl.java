/*******************************************************************************
 * Copyright © 2010, 2013 by Global Phasing Ltd. All rights reserved             
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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import co.gphl.common.namelist.AbstractKeyList;
import co.gphl.common.namelist.F90NamelistData;
import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.F90NamelistValueException;
import co.gphl.common.namelist.NamelistGroup;

public class F90NamelistGroupImpl 
extends AbstractNamelistGroupImpl 
implements F90NamelistGroup {

    /**
     * 
     */
    private static final long serialVersionUID = 1758046791779837267L;
    private String groupName;
    private F90NamelistData owningData = null;

    /**
     * Creates a new instance sorted by {@code keyList}. The name of the
     * instance is given by {@code keyList.getListName()}.
     * 
     * @param keyList the comparator used to sort by key. May not be null.
     */
    public F90NamelistGroupImpl ( AbstractKeyList keyList ) {
    	this(keyList, keyList.getListName());
    }
    
    /**
     * Creates a new instance sorted by {@code keyList}.
     * 
     * @param keyList the comparator that will be used to sort this F90NamelistGroupImpl
     * by key. A <code>null</code> value indicates that the keys' <i>natural ordering</i>
     * should be used.
     * @param name name of new instance
     * 
     * @see java.util.TreeMap#TreeMap(Comparator)
     * 
     */
    protected F90NamelistGroupImpl( AbstractKeyList keyList, String name ) {
        super(keyList);
        this.groupName = name;
        this.maxLineLen = 70;
    }

    
    /**
     * Calls {@link #F90NamelistGroupImpl(AbstractKeyList, String, Integer)} with
     * {@code keyList.getListName()} as the second parameter.
     * 
     * @param keyList
     * @param lineNo
     */
    public F90NamelistGroupImpl( AbstractKeyList keyList, Integer lineNo ) {
    	this(keyList, keyList.getListName(), lineNo);
    }
    
    
    /**
     * Calls {@link #F90NamelistGroupImpl(Comparator, String)}, but also stores a line number
     * that can be output for diagnostic purposes. This is intended to be used when reading
     * namelist data from a file.
     * 
     * @param keyList - may be null to indicate natural ordering is to be used
     * @param name
     * @param lineNo
     */
    protected F90NamelistGroupImpl ( AbstractKeyList keyList, String name, Integer lineNo ) {
        this(keyList, name);
        this.lineNo = lineNo;
    }

    @Override
    public String getGroupName() {
        return groupName.toUpperCase();
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
	
}

