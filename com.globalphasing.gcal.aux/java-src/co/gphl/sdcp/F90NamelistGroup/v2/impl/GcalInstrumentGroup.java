/*******************************************************************************
 * Copyright (c) 2014, 2017 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.VarnameComparator;

@SuppressWarnings("serial")
public abstract class GcalInstrumentGroup extends GcalGoniostatGroup implements
        F90NamelistGroup {

    protected GcalInstrumentGroup(VarnameComparator varnameComparator, Set<String> charVarnames, Integer lineNo) {
        super(varnameComparator, charVarnames, lineNo);
    }

    public static final String twoThetaAxis = "TWO_THETA_AXIS";
    public static final String spindleAxis = "SPINDLE_AXIS";
    public static final String beam = "BEAM";
    
    // Not an anti-pattern here, because we take care to ensure immutability
    protected static final List<String> varnameOrder =
            GcalInstrumentGroup.initVarnameOrder();

    private static List<String> initVarnameOrder() {
        List<String> retval = new ArrayList<String>(GcalGoniostatGroup.varnameOrder);
        retval.add(GcalInstrumentGroup.twoThetaAxis);
        retval.add(GcalInstrumentGroup.beam);
        return Collections.unmodifiableList(retval);
    }
    
}
