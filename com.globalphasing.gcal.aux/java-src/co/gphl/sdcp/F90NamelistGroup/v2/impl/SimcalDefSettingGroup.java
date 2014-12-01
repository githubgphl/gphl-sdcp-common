

/*
 * Namelist group type corresponding to SIMCAL_DEF_SETTING_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.Arrays;

import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public final class SimcalDefSettingGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public SimcalDefSettingGroup(Integer lineNo) {
       super(SimcalDefSettingGroup.varnameComparator(), SimcalDefSettingGroup.groupName, lineNo);
    }
   
    public static final String groupName = "SIMCAL_DEF_SETTING_LIST";
    
    @Override
    public String getGroupName() {
        return SimcalDefSettingGroup.groupName;
    }

    private static SimcalDefSettingGroupComparator varnameComparator = null;

    private static SimcalDefSettingGroupComparator varnameComparator() {
        if ( SimcalDefSettingGroup.varnameComparator == null )
            SimcalDefSettingGroup.varnameComparator = new SimcalDefSettingGroupComparator();
        return SimcalDefSettingGroup.varnameComparator;
    }

    public static final String goniostatSettingId = "GONIOSTAT_SETTING_ID";
    public static final String centredGoniostatSettingId = "CENTRED_GONIOSTAT_SETTING_ID";
    public static final String beamSettingId = "BEAM_SETTING_ID";
    public static final String detectorSettingId = "DETECTOR_SETTING_ID";
    public static final String beamstopSettingId = "BEAMSTOP_SETTING_ID";
    public static final String muAir = "MU_AIR";
    public static final String muSensor = "MU_SENSOR";
    public static final String exposure = "EXPOSURE";
}

@SuppressWarnings("serial")
final class SimcalDefSettingGroupComparator
    extends co.gphl.common.namelist.AbstractKeyList {
    
    SimcalDefSettingGroupComparator() {
        this.keyOrder = Arrays.asList(new String [] {
            SimcalDefSettingGroup.goniostatSettingId,
            SimcalDefSettingGroup.centredGoniostatSettingId,
            SimcalDefSettingGroup.beamSettingId,
            SimcalDefSettingGroup.detectorSettingId,
            SimcalDefSettingGroup.beamstopSettingId,
            SimcalDefSettingGroup.muAir,
            SimcalDefSettingGroup.muSensor,
            SimcalDefSettingGroup.exposure
           } );

        this.valueTypeMap = new java.util.HashMap<String, co.gphl.common.namelist.ValueType>();
        this.valueTypeMap.put(SimcalDefSettingGroup.goniostatSettingId, co.gphl.common.namelist.ValueType.CHAR);
        this.valueTypeMap.put(SimcalDefSettingGroup.centredGoniostatSettingId, co.gphl.common.namelist.ValueType.CHAR);
        this.valueTypeMap.put(SimcalDefSettingGroup.beamSettingId, co.gphl.common.namelist.ValueType.CHAR);
        this.valueTypeMap.put(SimcalDefSettingGroup.detectorSettingId, co.gphl.common.namelist.ValueType.CHAR);
        this.valueTypeMap.put(SimcalDefSettingGroup.beamstopSettingId, co.gphl.common.namelist.ValueType.CHAR);
        
        }

    @Override
    public String getListName() {
        throw new RuntimeException("Should not use this method in v2 namelist stuff!");
    }  
}
    