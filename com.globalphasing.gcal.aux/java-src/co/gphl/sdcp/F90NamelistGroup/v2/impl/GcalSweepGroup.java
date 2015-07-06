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
