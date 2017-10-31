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

public class NamelistTest2 {

    public static void main(String[] args) throws IOException {

        if ( args.length != 1 )
            throw new RuntimeException("Need output filename as argument");
        
        F90NamelistData data = new F90NamelistImpl();
        
        // Start with a LOOP_COUNT_LIST
        
        F90NamelistGroup loopCounts = new EXLoopCountGroup(64, 62, 120, 1);
        data.add(loopCounts);
        
        data.write( new File(args[0]) );

    }

}
