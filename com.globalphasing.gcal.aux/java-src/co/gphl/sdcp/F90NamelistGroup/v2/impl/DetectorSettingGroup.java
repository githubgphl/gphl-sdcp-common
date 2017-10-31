/*******************************************************************************
 * Copyright (c) 2014, 2015 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/


/*
 * Namelist group type corresponding to DETECTOR_SETTING_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import co.gphl.common.namelist.VarnameComparator;
import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public final class DetectorSettingGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public DetectorSettingGroup(Integer lineNo) {
       super(DetectorSettingGroup.varnameComparator, DetectorSettingGroup.charVarnames, lineNo);
    }
   
    public static final String groupName = "DETECTOR_SETTING_LIST";
    
    @Override
    public String getGroupName() {
        return DetectorSettingGroup.groupName;
    }

    public static final String id          = "ID";
    public static final String detCoord    = "DET_COORD";
    public static final String twoThetaDeg = "TWO_THETA_DEG";

    private static final VarnameComparator varnameComparator = 
        new VarnameComparator(new String [] {
                DetectorSettingGroup.id,
                DetectorSettingGroup.detCoord,
                DetectorSettingGroup.twoThetaDeg
        } );

    private static final Set<String> charVarnames = Collections.unmodifiableSet(
        new HashSet<String>(
                Arrays.asList( new String[] {
                        DetectorSettingGroup.id
                } ) ) );
}