/*******************************************************************************
 * Copyright (c) 2014, 2015 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.VarnameComparator;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public abstract class GcalSweepGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {

    public GcalSweepGroup( VarnameComparator varnameComparator, Set<String> charVarnames, Integer lineNo ) {
        super(varnameComparator, charVarnames, lineNo);
    }
    
    public static final String goniostatSettingId = "GONIOSTAT_SETTING_ID";
    public static final String centredGoniostatSettingId = "CENTRED_GONIOSTAT_SETTING_ID";
    public static final String beamSettingId = "BEAM_SETTING_ID";
    public static final String detectorSettingId = "DETECTOR_SETTING_ID";
    public static final String beamstopSettingId = "BEAMSTOP_SETTING_ID";
    public static final String startDeg = "START_DEG";

    protected static final String[] varnameOrder =
        new String [] {
                GcalSweepGroup.goniostatSettingId,
                GcalSweepGroup.centredGoniostatSettingId,
                GcalSweepGroup.beamSettingId,
                GcalSweepGroup.detectorSettingId,
                GcalSweepGroup.beamstopSettingId,
                GcalSweepGroup.startDeg
        };

    protected static final Set<String> charVarnames = Collections.unmodifiableSet(
        new HashSet<String>(
                Arrays.asList( new String[] {
                        GcalSweepGroup.goniostatSettingId,
                        GcalSweepGroup.centredGoniostatSettingId,
                        GcalSweepGroup.beamSettingId,
                        GcalSweepGroup.detectorSettingId,
                        GcalSweepGroup.beamstopSettingId,
                } ) ) );
    
    protected static final VarnameComparator varnameComparator = 
            new VarnameComparator(GcalSweepGroup.varnameOrder);
}
