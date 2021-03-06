/*******************************************************************************
 * Copyright (c) 2010, 2015 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/

package co.gphl.common.namelist;

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

    // Be very selective about adding methods to this interface from
    // Collection/List. Too many retained references to namelist groups
    // will eventually cause problems.


}
