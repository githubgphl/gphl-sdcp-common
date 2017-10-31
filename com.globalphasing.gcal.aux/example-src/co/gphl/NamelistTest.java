/*******************************************************************************
 * Copyright (c) 2014 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/
package co.gphl;

import java.io.File;
import java.io.IOException;

import co.gphl.common.namelist.F90NamelistData;
import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistImpl;
import co.gphl.sdcp.F90NamelistGroup.v2.impl.LoopCountGroup;

public class NamelistTest {

    public static void main(String[] args) throws IOException {

        if ( args.length != 1 )
            throw new RuntimeException("Need output filename as argument");
        
        F90NamelistData data = new F90NamelistImpl();
        
        // Start with a LOOP_COUNT_LIST
        
        F90NamelistGroup loopCounts = new LoopCountGroup(null);
        data.add(loopCounts);
        
        loopCounts.put(LoopCountGroup.nSegments, 120 );
        loopCounts.put(LoopCountGroup.nTriangles, 62);
        loopCounts.put(LoopCountGroup.nVertices, 64);
        loopCounts.put(LoopCountGroup.nCrystals, 1);
        
        
        data.write( new File(args[0]) );
        
        
    }

}
