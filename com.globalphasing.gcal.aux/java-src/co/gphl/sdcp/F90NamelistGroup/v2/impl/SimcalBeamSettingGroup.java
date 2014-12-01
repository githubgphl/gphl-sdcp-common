

/*
 * Namelist group type corresponding to SIMCAL_BEAM_SETTING_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.Arrays;

import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public final class SimcalBeamSettingGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public SimcalBeamSettingGroup(Integer lineNo) {
       super(SimcalBeamSettingGroup.varnameComparator(), SimcalBeamSettingGroup.groupName, lineNo);
    }
   
    public static final String groupName = "SIMCAL_BEAM_SETTING_LIST";
    
    @Override
    public String getGroupName() {
        return SimcalBeamSettingGroup.groupName;
    }

    private static SimcalBeamSettingGroupComparator varnameComparator = null;

    private static SimcalBeamSettingGroupComparator varnameComparator() {
        if ( SimcalBeamSettingGroup.varnameComparator == null )
            SimcalBeamSettingGroup.varnameComparator = new SimcalBeamSettingGroupComparator();
        return SimcalBeamSettingGroup.varnameComparator;
    }

    public static final String id = "ID";
    public static final String lambda = "LAMBDA";
    public static final String muAir = "MU_AIR";
    public static final String muSensor = "MU_SENSOR";
}

@SuppressWarnings("serial")
final class SimcalBeamSettingGroupComparator
    extends co.gphl.common.namelist.AbstractKeyList {
    
    SimcalBeamSettingGroupComparator() {
        this.keyOrder = Arrays.asList(new String [] {
            SimcalBeamSettingGroup.id,
            SimcalBeamSettingGroup.lambda,
            SimcalBeamSettingGroup.muAir,
            SimcalBeamSettingGroup.muSensor
           } );

        this.valueTypeMap = new java.util.HashMap<String, co.gphl.common.namelist.ValueType>();
        this.valueTypeMap.put(SimcalBeamSettingGroup.id, co.gphl.common.namelist.ValueType.CHAR);
        
        }

    @Override
    public String getListName() {
        throw new RuntimeException("Should not use this method in v2 namelist stuff!");
    }  
}
    