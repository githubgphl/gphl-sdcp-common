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

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import co.gphl.common.namelist.AbstractKeyList;
import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public abstract class GcalInstrumentGroup extends F90NamelistGroupImpl implements
        F90NamelistGroup {

    protected GcalInstrumentGroup(AbstractKeyList keyList, String name, Integer lineNo) {
        super(keyList, name, lineNo);
    }

    public static final String twoThetaAxis = "TWO_THETA_AXIS";
    public static final String omegaAxis = "OMEGA_AXIS";
    public static final String kappaAxis = "KAPPA_AXIS";
    public static final String phiAxis = "PHI_AXIS";
    public static final String spindleAxis = "SPINDLE_AXIS";
    public static final String trans1Axis = "TRANS_1_AXIS";
    public static final String trans2Axis = "TRANS_2_AXIS";
    public static final String trans3Axis = "TRANS_3_AXIS";
    public static final String beam = "BEAM";
    
    public static final String[] goniostatAxisVarnames = {
        GcalInstrumentGroup.omegaAxis,
        GcalInstrumentGroup.kappaAxis,
        GcalInstrumentGroup.phiAxis
    };
    
    
}
