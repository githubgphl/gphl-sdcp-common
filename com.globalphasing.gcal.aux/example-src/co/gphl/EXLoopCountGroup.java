/*******************************************************************************
 * Copyright (c) 2014 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/

package co.gphl;

import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;
import co.gphl.common.namelist.impl.F90NamelistGroupWrapper;
import co.gphl.sdcp.F90NamelistGroup.v2.impl.LoopCountGroup;

/**
 * Example of extending the functionality of an implementation of
 * {@link F90NamelistGroupImpl} using {@link F90NamelistGroupWrapper}
 * 
 * @author pkeller
 *
 */
public class EXLoopCountGroup extends F90NamelistGroupWrapper implements
        F90NamelistGroup {

    public EXLoopCountGroup(int nVertices, int nTriangles, int nSegments, int nCrystals) {
        super( new LoopCountGroup(null) );
        this.put(LoopCountGroup.nVertices, nVertices);
        this.put(LoopCountGroup.nTriangles, nTriangles);
        this.put(LoopCountGroup.nSegments, nSegments);
        this.put(LoopCountGroup.nCrystals, nCrystals);
    }

    
}
