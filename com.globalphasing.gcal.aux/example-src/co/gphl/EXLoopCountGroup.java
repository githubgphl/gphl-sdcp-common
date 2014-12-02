/*
 * Copyright © 2014 by Global Phasing Ltd. All rights reserved
 *
 * This software is proprietary to and embodies the confidential
 * technology of Global Phasing Limited (GPhL).
 *
 * Any possession or use (including but not limited to duplication, reproduction
 * and dissemination) of this software (in either source or compiled form) is
 * forbidden except where an agreement with GPhL that permits such possession or
 * use is in force.
 *
 */

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
