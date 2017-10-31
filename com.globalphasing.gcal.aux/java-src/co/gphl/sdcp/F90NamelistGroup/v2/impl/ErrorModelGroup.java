/*******************************************************************************
 * Copyright (c) 2014, 2015 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/


/*
 * Namelist group type corresponding to ERROR_MODEL_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import co.gphl.common.namelist.VarnameComparator;
import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public final class ErrorModelGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public ErrorModelGroup(Integer lineNo) {
       super(ErrorModelGroup.varnameComparator, null, lineNo);
    }
   
    public static final String groupName = "ERROR_MODEL_LIST";
    
    @Override
    public String getGroupName() {
        return ErrorModelGroup.groupName;
    }

    public static final String background = "BACKGROUND";
    public static final String beamSdDeg  = "BEAM_SD_DEG";
    public static final String lambdaSd   = "LAMBDA_SD";
    public static final String minZeta    = "MIN_ZETA";
    public static final String minLorentz = "MIN_LORENTZ";

    private static final VarnameComparator varnameComparator = 
        new VarnameComparator(new String [] {
                ErrorModelGroup.background,
                ErrorModelGroup.beamSdDeg,
                ErrorModelGroup.lambdaSd,
                ErrorModelGroup.minZeta,
                ErrorModelGroup.minLorentz
        } );
}