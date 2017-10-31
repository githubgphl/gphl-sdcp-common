/*******************************************************************************
 * Copyright (c) 2014, 2017 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/


/*
 * Namelist group type corresponding to SIMCAL_CRYSTAL_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.VarnameComparator;

@SuppressWarnings("serial")
public final class SimcalCrystalGroup
    extends CrystalGroup implements F90NamelistGroup {
   
    public SimcalCrystalGroup(Integer lineNo) {
       super(SimcalCrystalGroup.varnameComparator, SimcalCrystalGroup.charVarnames, lineNo);
    }
   
    public static final String groupName = "SIMCAL_CRYSTAL_LIST";
    
    @Override
    public String getGroupName() {
        return SimcalCrystalGroup.groupName;
    }

    public static final String cellRefAngDeg = "CELL_REF_ANG_DEG";
    public static final String cellDimSd     = "CELL_DIM_SD";
    public static final String cellAngSdDeg  = "CELL_ANG_SD_DEG";
    public static final String uMatSdDeg     = "U_MAT_SD_DEG";
    public static final String sigmaN        = "SIGMA_N";
    public static final String bWilson       = "B_WILSON";
    public static final String b6Wilson      = "B6_WILSON";

    private static final List<String> varnameOrder =
            new ArrayList<String>( Arrays.asList(CrystalGroup.varnameOrder) ) ;

    static{
        SimcalCrystalGroup.varnameOrder.add(SimcalCrystalGroup.cellRefAngDeg);
        SimcalCrystalGroup.varnameOrder.add(SimcalCrystalGroup.cellDimSd);
        SimcalCrystalGroup.varnameOrder.add(SimcalCrystalGroup.cellAngSdDeg);
        SimcalCrystalGroup.varnameOrder.add(SimcalCrystalGroup.uMatSdDeg);
        SimcalCrystalGroup.varnameOrder.add(SimcalCrystalGroup.sigmaN);
        SimcalCrystalGroup.varnameOrder.add(SimcalCrystalGroup.bWilson);
        SimcalCrystalGroup.varnameOrder.add(SimcalCrystalGroup.b6Wilson);
    }

    private static final VarnameComparator varnameComparator =
            new VarnameComparator(SimcalCrystalGroup.varnameOrder);
    
    private static final Set<String> charVarnames = Collections.unmodifiableSet(
        new HashSet<String>(
                Arrays.asList( new String[] {
                        SimcalCrystalGroup.sgName
                } ) ) );
}