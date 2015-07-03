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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import co.gphl.common.namelist.AbstractKeyList;
import co.gphl.common.namelist.F90NamelistData;
import co.gphl.common.namelist.F90NamelistException;
import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.F90NamelistGroupFactory;
import co.gphl.common.namelist.F90NamelistValueException;

/**
 * Class to parse and/or output basic Fortran90 namelist input.<p>
 * 
 * It will not handle the full flexibility allowed by namelist input. It has the
 * following restrictions:
 * 
 * <ul>
 * <li>{@code &<namelist_group_name>} must be on a line by itself.</li>
 * 
 * <li>The name part of a name-value subsequence must be the first token on its line</li>
 * 
 * <li>Individual values in name-value subsequences cannot be split across lines</li>
 * 
 * <li>The terminating {@code '/'} of a namelist group must be on a line by itself</li>
 * 
 * <li>The whole token on the left hand side of the {@code '='} sign in a name-value
 * subsequence is used as the key of the {@link F90NamelistGroup} instances created
 * when namelist input is processed. Substring ranges, subscript and array section
 * expressions are not interpreted.</li>
 * </ul>
 * 
 * N.B. The restriction in earlier versions of this class that a name-value
 * subsequence cannot be split across multiple lines has been removed.
 * 
 * To create namelist data by reading from a file, a typical usage pattern is:
 * 
 * <pre>
 *    F90NamelistImpl namelistData = new F90NamelistImpl();
 *    namelistData.load(new File("filename.txt"));
 * </pre>
 * 
 * To create namelist data programmatically, with the intention of writing
 * it out to a file, a typical pattern is:
 * 
 * <pre>
 *    F90NamelistImpl namelistData = new F90NamelistImpl("xdsks");
 *    namelistData.addKeyList( XdsksSetupList.instance() );
 *    namelistData.addKeyList( XdsksSweepList.instance() );
 *    F90NamelistGroup group = namelistData.newNamelistGroup(XdsksSetupList.listName, null);
 *    ....
 * </pre>
 * 
 * where {@code XdsksSetupList} and {@code XdsksSweepList} are subtypes of
 * {@link AbstractKeyList} that are implemented (in this case) with a
 * singleton pattern.
 * 
 * @author Peter Keller
 *
 */
public class F90NamelistImpl
	extends ArrayList< F90NamelistGroup >
	implements F90NamelistData {

	private static final long serialVersionUID = 2387367286532154444L;
	private File nlFile = null;
	private LineNumberReader reader = null;
	private int curLineNumber = 0;
	
	private String appName = null;
	private Map<String, AbstractKeyList> keyLists = null;
	
	private F90NamelistGroupFactory nlgFactory = null;
	
    // Default is to use comma-separated F90 style values, but
    // XDS support for this is patchy. We allow applications to 
    // change this to space.
    protected String valueSeparator = ", ";
	
    protected Set<Class<? extends F90NamelistGroup>> includedGroups = null;
    
    // FIXME! Make factory compulsory when we have switched to v2.
    
	/**
	 * Creates new empty instance. If this instance will be used to write
	 * v1 namelist data to a file, in most cases the appropriate {@link AbstractKeyList}
	 * instances should be registered with the {@link #addKeyList(AbstractKeyList)}
	 * method. This is not needed for v2 namelist data.
	 */
	public F90NamelistImpl() {
	}
	
    /**
     * Creates new empty instance. If this instance will be used to write
     * v1 namelist data to a file, in most cases the appropriate {@link AbstractKeyList}
     * instances should be registered with the {@link #addKeyList(AbstractKeyList)}
     * method. This is not needed for v2 namelist data.
     * 
     * @param appName application name. Not currently used.
     */
    public F90NamelistImpl( String appName ) {
        this(null,appName);
    }
    
    /**
     * Creates new empty instance. If this instance will be used to write
     * v1 namelist data to a file, in most cases the appropriate {@link AbstractKeyList}
     * instances should be registered with the {@link #addKeyList(AbstractKeyList)}
     * method. This is not needed for v2 namelist data.
     * 
     * @param factory namelist group factory to be used to create v2 {@link F90NamelistGroup}
     * instances when reading namelist-format data from files. If {@code null} v1
     * heuristics will be used instead.
     */
    public F90NamelistImpl( F90NamelistGroupFactory factory ) {
        this(factory, null);
    }
    
    /**
     * Creates new empty instance. If this instance will be used to write
     * v1 namelist data to a file, in most cases the appropriate {@link AbstractKeyList}
     * instances should be registered with the {@link #addKeyList(AbstractKeyList)}
     * method. This is not needed for v2 namelist data.
     * 
     * @param factory namelist group factory to be used to create v2 {@link F90NamelistGroup}
     * instances when reading namelist-format data from files. If {@code null} v1
     * heuristics will be used instead.
     * @param appName application name. Not currently used.
     */
    public F90NamelistImpl( F90NamelistGroupFactory factory, String appName ) {
        if ( factory == null )
            throw new IllegalArgumentException("Must specify a namelist group factory");
        this.appName = ( appName == null ? "" : appName.toLowerCase() );
        this.nlgFactory = factory;
    }
	
	/**
	 * Populates empty instance from the contents of a namelist file.
	 * Throws a RuntimeException if size of instance is not zero
	 * or the instance does not have a factory defined.
	 * 
	 * @param nlFile File containing namelist input
	 * @throws IOException
	 */
    @Override
	public void read (File nlFile) throws IOException {

	    if ( this.size() > 0 )
	        throw new RuntimeException("The read method should only be called on an empty instance");
	    
	    // FIXME! make read method private, and shift nlFile to constructor arg together with factory
	    if ( this.nlgFactory == null )
	        throw new RuntimeException("Factory must have been defined to call read method");
	    
		this.nlFile = nlFile;

		try {

			this.reader = new LineNumberReader ( new FileReader(nlFile) );

			String[] nameValue;
			F90NamelistGroup curGroup = null;

			String varName = null, valueList = null;

			for ( String line = nextSignificantLine(this.reader); line != null;
			        line = nextSignificantLine(this.reader) ) {
				
                this.curLineNumber = reader.getLineNumber();
			    			    
                // Start of namelist group
				if ( line.charAt(0) == '&' ) {
					if ( curGroup != null || varName != null ) {
						throw newException( "'&' found, but current namelist group '" + 
								curGroup.getGroupName() + "' not terminated" );
					}
					else {
						curGroup = this.newNamelistGroup(line.substring(1), reader.getLineNumber() - 1, false);
					}
				}

				// End of namelist group
				else if ( line.charAt(0) == '/' ) {
					if ( curGroup == null ) {
						throw newException( "'/' found, but no matching line with '&'" );
					}
					else {
					    
					    // Except for an empty namelist group, we will still have the last
					    // assignment of the namelist group waiting to be added to the group object
					    if ( varName != null )
					        curGroup.put(varName.toUpperCase(), valueList);
					    
					    varName = null;
					    valueList = null;
					    
					    this.addAsTyped(curGroup);
					    curGroup.setOwningData(this);
						curGroup = null;
					}
				}
				
				// New or continued value assignment for current namelist group
				else {
                    
				    if ( curGroup == null )
                        throw newException( "Significant line found outside namelist group" );

                    nameValue = line.split("\\s*=\\s*", 2);
					
                    // Does this look like the start of a variable assignment?
                    if ( nameValue.length == 2 &&
		                    // Fortran90 identifiers start with a letter, then a sequence of letters,
		                    // digits and underscores
					        nameValue[0].matches("[A-Za-z]\\w*") ) {
						
					    
					    if ( varName != null )
                            // If this is not the first assignment of the group, we need to put
                            // the last one before starting to handle this one
                            curGroup.put(varName.toUpperCase(), valueList);
					        
					    varName = nameValue[0];
					    valueList = nameValue[1];
					    
					}
					else
					    // Gather this line as a continuation of the current assignment
					    // N.B. putting a " " here is a hack, and completely general
					    // namelist input will be broken by it.
					    valueList += " " + line;					
				}
				
			}
			
			if ( curGroup != null ) {
				throw newException( "End of file reached, but namelist group not terminated" );
			}

		}
		catch ( F90NamelistValueException e ){
			throw new F90NamelistException( errorMessage("Problem with input"), e );
		}
		finally {
			if ( reader != null )
				reader.close();
			this.nlFile = null;
			this.reader = null;
			curLineNumber = 0;
		}
	}

	/**
	 * Writes contents of instance to a file in namelist format.
	 * 
	 * @param nlFile file where namelist data should be written.
	 * @throws IOException if nFile does not exist, or variable type information
	 * cannot be loaded from property files named after the contained namelist groups
	 */
	@Override
	public void write ( File nlFile ) throws IOException {
	    
	    PrintWriter file = new PrintWriter(nlFile);
	    
	    for ( F90NamelistGroup group: this ) {
	        file.println("&" + group.getGroupName() );	        
	        group.write(file, this.valueSeparator);
	        file.println('/');   
	    }
	    
	    file.close();
	}

	
	private String nextSignificantLine(LineNumberReader reader) throws IOException {
	    
	    String retval = reader.readLine();
	    
	    while ( retval != null ) {
            // FIXME! This is a hack, and will break generalised namelist input
	        retval = retval.trim();
	        if ( retval.length() > 0 && retval.charAt(0) != '!' )
	            return retval;	        
	        retval = reader.readLine();
	    }
	    
	    return retval;
	}
	
	private String errorMessage ( String msg ) {
		if ( this.reader != null ) {
			return  "\n  " + this.nlFile.getAbsolutePath() + 
					": bad namelist data at line " + 
					String.valueOf( this.curLineNumber ) + ". " + msg ;
		}
		else if ( this.nlFile != null ) {
			return  "\n  " + this.nlFile.getAbsolutePath() + 
					": " + msg;
		}
		else {
			return "\n  " + msg;
		}		
	}
	
	protected F90NamelistException newException ( String msg ) {
		return new F90NamelistException ( errorMessage(msg) );				
	}

	/**
	 * Retrieve AbstractKeyList instance registered under {@code name}
	 * 
	 * @param name name under which {@code keyList} is registered (converted internally to upper case)
	 * @return Comparator instance, or null if no comparator registered under {@code name}
	 */
	private AbstractKeyList getKeyList( String name ) {
	    return keyLists == null ? null : this.keyLists.get(name.toUpperCase());
	}
	
    /**
     * Returns a new {@link F90NamelistGroup} called {@code name}.
     * For v1 namelist groups, if a {@link AbstractKeyList} has been registered under
     * {@code name}, it will determine the the sort order of the keys
     * of the new {@code F90NamelistGroup}.
     * 
     * Where the new namelist group is being created by reading data from a
     * namelist file, a line number may be provided to make diagnostics
     * more informative.
     * 
     * {@code if ( ! addAtEnd) } this method will not add the new namelist group to
     * the current namelist. {@link List#add(Object)}, {@link List#add(int, Object)} or
     * {@link List#set(int, Object)} should be used to do this.
     * 
     * @param name name of new namelist group
     * @param lineNo line number in file where namelist group starts. May be {@code null}.
     * @param addAtEnd if true, will add new namelist group to end of namelist data
     * @return new namelist group
     * @see #addKeyList(AbstractKeyList)
     */
    public F90NamelistGroup newNamelistGroup ( String name, Integer lineNo, boolean addAtEnd ) {

        F90NamelistGroup retval = null;
        retval = this.nlgFactory.newInstance(name, true, lineNo);
        if ( addAtEnd )
            this.add(retval);
        return retval;
    }

    private boolean addAsTyped( F90NamelistGroup namelistGroup ) {
        // If we know that we can always identify the type of the namelist group
        // from the group name alone, we could override List.add() and List.set().
        // As it is, we may need to use some of the data in the namelist group
        // itself to work out the type.
        AbstractKeyList keyList = this.getKeyList(namelistGroup.getGroupName());
        if ( keyList != null )
            this.add( keyList.asTypedNamelistGroup(namelistGroup) );
        else
            this.add(namelistGroup);
        return true;
    }
    
	/* (non-Javadoc)
	 * @see co.gphl.common.namelist.NamelistData#setCommaSeparator(boolean)
	 */
	@Override
	public void setCommaSeparator(boolean commaSeparated) {
        this.valueSeparator = commaSeparated ? ", " : " ";
	}
    
	@Override
	public boolean add(F90NamelistGroup e) {
	    super.add(e);
	    e.setOwningData(this);
	    return true;
	}
	
	public void add(int index, F90NamelistGroup element) {
	    super.add(index, element);
	    element.setOwningData(this);
	}
	
	public F90NamelistGroup set(int index, F90NamelistGroup e) {
	    F90NamelistGroup prevVal = this.get(index);
	    super.set(index, e);
	    e.setOwningData(this);
	    prevVal.setOwningData(null);
	    return prevVal;
	}

    /* (non-Javadoc)
     * @see co.gphl.common.namelist.F90NamelistData#addAll(co.gphl.common.namelist.F90NamelistData)
     */
    @Override
    public boolean addAll(F90NamelistData data) {
        for ( int i = 0; i < data.size(); i++ )
            this.add(data.get(i));
        return true;
    }
	
    /* (non-Javadoc)
     * @see co.gphl.common.namelist.F90NamelistData#includeGroups(java.util.Set)
     */
    @Override
    public void includeGroups(Set<Class<? extends F90NamelistGroup>> groups) {
        this.includedGroups = new HashSet<Class<? extends F90NamelistGroup>>(groups);
        
    }

}
