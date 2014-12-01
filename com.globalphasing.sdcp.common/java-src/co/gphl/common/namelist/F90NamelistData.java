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

package co.gphl.common.namelist;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import co.gphl.common.namelist.impl.F90NamelistImpl;

/**
 * @author pkeller
 *
 */
public interface F90NamelistData
	extends NamelistData, Iterable<F90NamelistGroup> {

    public F90NamelistGroup newNamelistGroup ( String name, Integer lineNo, boolean addAtEnd );
    public F90NamelistGroup get(int index);
    public int size();
    public boolean add(F90NamelistGroup group);
    public boolean addAll(F90NamelistData data);

    /**
     * Allow the specification of a set of {@link F90NamelistGroup} types
     * that are included when processing namelist data. The use that is
     * made of this set of groups depends on the particular implementation.
     * The {@link F90NamelistImpl} class currently makes no use of this set.
     * 
     */
    public void includeGroups( Set<Class<? extends F90NamelistGroup>> groups );

    public void read(File file) throws IOException;
    
    // Be very selective about adding methods to this interface from
    // Collection/List. Too many retained references to namelist groups
    // will eventually cause problems.
    

}
