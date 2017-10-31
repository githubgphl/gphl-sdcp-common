/*******************************************************************************
 * Copyright (c) 2014, 2015 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/


/*
 * Namelist group type corresponding to BEAMSTOP_SETTING_LIST
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
public final class BeamstopSettingGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public BeamstopSettingGroup(Integer lineNo) {
       super(BeamstopSettingGroup.varnameComparator, BeamstopSettingGroup.charVarnames, lineNo);
    }
   
    public static final String groupName = "BEAMSTOP_SETTING_LIST";
    
    @Override
    public String getGroupName() {
        return BeamstopSettingGroup.groupName;
    }

    public static final String id                = "ID";
    public static final String beamStopRadius    = "BEAM_STOP_RADIUS";
    public static final String beamStopSLength   = "BEAM_STOP_S_LENGTH";
    public static final String beamStopSDistance = "BEAM_STOP_S_DISTANCE";

    private static final VarnameComparator varnameComparator = 
        new VarnameComparator(new String [] {
                BeamstopSettingGroup.id,
                BeamstopSettingGroup.beamStopRadius,
                BeamstopSettingGroup.beamStopSLength,
                BeamstopSettingGroup.beamStopSDistance
        } );

    private static final Set<String> charVarnames = Collections.unmodifiableSet(
        new HashSet<String>(
                Arrays.asList( new String[] {
                        BeamstopSettingGroup.id
                } ) ) );
}