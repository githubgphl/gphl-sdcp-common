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
public abstract class GcalSweepGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {

    public GcalSweepGroup(AbstractKeyList keyList, String name) {
        super(keyList, name, null);
    }
    
    public GcalSweepGroup(AbstractKeyList keyList, String name, Integer lineNo) {
        super(keyList, name, lineNo);
    }

    public static final String goniostatSettingId = "GONIOSTAT_SETTING_ID";
    public static final String centredGoniostatSettingId = "CENTRED_GONIOSTAT_SETTING_ID";
    public static final String beamSettingId = "BEAM_SETTING_ID";
    public static final String detectorSettingId = "DETECTOR_SETTING_ID";
    public static final String beamstopSettingId = "BEAMSTOP_SETTING_ID";
    public static final String startDeg = "START_DEG";
        

}
