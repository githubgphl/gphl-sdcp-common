

/*
 * Namelist group type corresponding to SIMCAL_DEF_SETTING_LIST
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
public final class SimcalDefSettingGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public SimcalDefSettingGroup(Integer lineNo) {
       super(SimcalDefSettingGroup.varnameComparator, SimcalDefSettingGroup.charVarnames, lineNo);
    }
   
    public static final String groupName = "SIMCAL_DEF_SETTING_LIST";
    
    @Override
    public String getGroupName() {
        return SimcalDefSettingGroup.groupName;
    }

    public static final String goniostatSettingId        = "GONIOSTAT_SETTING_ID";
    public static final String centredGoniostatSettingId = "CENTRED_GONIOSTAT_SETTING_ID";
    public static final String beamSettingId             = "BEAM_SETTING_ID";
    public static final String detectorSettingId         = "DETECTOR_SETTING_ID";
    public static final String beamstopSettingId         = "BEAMSTOP_SETTING_ID";
    public static final String muAir                     = "MU_AIR";
    public static final String muSensor                  = "MU_SENSOR";
    public static final String exposure                  = "EXPOSURE";

    private static final VarnameComparator varnameComparator = 
        new VarnameComparator(new String [] {
                SimcalDefSettingGroup.goniostatSettingId,
                SimcalDefSettingGroup.centredGoniostatSettingId,
                SimcalDefSettingGroup.beamSettingId,
                SimcalDefSettingGroup.detectorSettingId,
                SimcalDefSettingGroup.beamstopSettingId,
                SimcalDefSettingGroup.muAir,
                SimcalDefSettingGroup.muSensor,
                SimcalDefSettingGroup.exposure
        } );

    private static final Set<String> charVarnames = Collections.unmodifiableSet(
        new HashSet<String>(
                Arrays.asList( new String[] {
                        SimcalDefSettingGroup.goniostatSettingId,
                        SimcalDefSettingGroup.centredGoniostatSettingId,
                        SimcalDefSettingGroup.beamSettingId,
                        SimcalDefSettingGroup.detectorSettingId,
                        SimcalDefSettingGroup.beamstopSettingId
                } ) ) );
}