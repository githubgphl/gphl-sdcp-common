/*******************************************************************************
 * Copyright (c) 2017 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/
package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.VarnameComparator;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

/**
 * @author pkeller
 *
 */
public abstract class GcalGoniostatGroup extends F90NamelistGroupImpl
        implements F90NamelistGroup {

    public static final String omegaAxis  = "OMEGA_AXIS";
    public static final String kappaAxis  = "KAPPA_AXIS";
    public static final String phiAxis    = "PHI_AXIS";
    public static final String trans1Axis = "TRANS_1_AXIS";
    public static final String trans2Axis = "TRANS_2_AXIS";
    public static final String trans3Axis = "TRANS_3_AXIS";

    // Not an anti-pattern here, because we take care to ensure immutability
    protected static final List<String> varnameOrder =
            Collections.unmodifiableList( Arrays.asList( new String[] {
                    GcalGoniostatGroup.omegaAxis,
                    GcalGoniostatGroup.kappaAxis,
                    GcalGoniostatGroup.phiAxis,
                    GcalGoniostatGroup.trans1Axis,
                    GcalGoniostatGroup.trans2Axis,
                    GcalGoniostatGroup.trans3Axis, } ) );
    
    // Added by hand
    public static final List<String> rotAxisOrder =
            Collections.unmodifiableList( Arrays.asList( "PHI", "KAPPA", "OMEGA" ) );
    private static final String fRotAxis="%s_AXIS";

    public static final List<Character> transAxisOrder =
            Collections.unmodifiableList( Arrays.asList( '1', '2', '3' ) );
    private static final String fTransAxis="TRANS_%c_AXIS";

    public static String rotAxisName(int i) {
        return String.format(fRotAxis, rotAxisOrder.get(i));
    }

    public static String transAxisName(int i) {
        return String.format(fTransAxis, transAxisOrder.get(i));
    }


    public GcalGoniostatGroup(VarnameComparator varnameComparator, Set<String> charVarnames, Integer lineNo) {
        super(varnameComparator, charVarnames, lineNo);
    }

}
