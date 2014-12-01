

/*
 * Namelist group type corresponding to DETECTOR_SETTING_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.Arrays;

import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public final class DetectorSettingGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public DetectorSettingGroup(Integer lineNo) {
       super(DetectorSettingGroup.varnameComparator(), DetectorSettingGroup.groupName, lineNo);
    }
   
    public static final String groupName = "DETECTOR_SETTING_LIST";
    
    @Override
    public String getGroupName() {
        return DetectorSettingGroup.groupName;
    }

    private static DetectorSettingGroupComparator varnameComparator = null;

    private static DetectorSettingGroupComparator varnameComparator() {
        if ( DetectorSettingGroup.varnameComparator == null )
            DetectorSettingGroup.varnameComparator = new DetectorSettingGroupComparator();
        return DetectorSettingGroup.varnameComparator;
    }

    public static final String id = "ID";
    public static final String detCoord = "DET_COORD";
    public static final String twoThetaDeg = "TWO_THETA_DEG";
}

@SuppressWarnings("serial")
final class DetectorSettingGroupComparator
    extends co.gphl.common.namelist.AbstractKeyList {
    
    DetectorSettingGroupComparator() {
        this.keyOrder = Arrays.asList(new String [] {
            DetectorSettingGroup.id,
            DetectorSettingGroup.detCoord,
            DetectorSettingGroup.twoThetaDeg
           } );

        this.valueTypeMap = new java.util.HashMap<String, co.gphl.common.namelist.ValueType>();
        this.valueTypeMap.put(DetectorSettingGroup.id, co.gphl.common.namelist.ValueType.CHAR);
        
        }

    @Override
    public String getListName() {
        throw new RuntimeException("Should not use this method in v2 namelist stuff!");
    }  
}
    