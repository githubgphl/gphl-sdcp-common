

/*
 * Namelist group type corresponding to SIMCAL_SWEEP_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.Arrays;

import co.gphl.common.namelist.F90NamelistGroup;

@SuppressWarnings("serial")
public final class SimcalSweepGroup
    extends GcalSweepGroup implements F90NamelistGroup {
   
    public SimcalSweepGroup(Integer lineNo) {
       super(SimcalSweepGroup.varnameComparator(), SimcalSweepGroup.groupName, lineNo);
    }
   
    public static final String groupName = "SIMCAL_SWEEP_LIST";
    
    @Override
    public String getGroupName() {
        return SimcalSweepGroup.groupName;
    }

    private static SimcalSweepGroupComparator varnameComparator = null;

    private static SimcalSweepGroupComparator varnameComparator() {
        if ( SimcalSweepGroup.varnameComparator == null )
            SimcalSweepGroup.varnameComparator = new SimcalSweepGroupComparator();
        return SimcalSweepGroup.varnameComparator;
    }

    public static final String goniostatSettingId =        GcalSweepGroup.goniostatSettingId;
    public static final String centredGoniostatSettingId = GcalSweepGroup.centredGoniostatSettingId;
    public static final String beamSettingId =             GcalSweepGroup.beamSettingId;
    public static final String detectorSettingId =         GcalSweepGroup.detectorSettingId;
    public static final String beamstopSettingId =         GcalSweepGroup.beamstopSettingId;
    public static final String startDeg =                  GcalSweepGroup.startDeg;
    public static final String exposure = "EXPOSURE";
    public static final String stepDeg = "STEP_DEG";
    public static final String nFrames = "N_FRAMES";
    public static final String imageNo = "IMAGE_NO";
    public static final String nameTemplate = "NAME_TEMPLATE";
    public static final String resLimit = "RES_LIMIT";
}

@SuppressWarnings("serial")
final class SimcalSweepGroupComparator
    extends co.gphl.common.namelist.AbstractKeyList {
    
    SimcalSweepGroupComparator() {
        this.keyOrder = Arrays.asList(new String [] {
            SimcalSweepGroup.goniostatSettingId,
            SimcalSweepGroup.centredGoniostatSettingId,
            SimcalSweepGroup.beamSettingId,
            SimcalSweepGroup.detectorSettingId,
            SimcalSweepGroup.beamstopSettingId,
            SimcalSweepGroup.startDeg,
            SimcalSweepGroup.exposure,
            SimcalSweepGroup.stepDeg,
            SimcalSweepGroup.nFrames,
            SimcalSweepGroup.imageNo,
            SimcalSweepGroup.nameTemplate,
            SimcalSweepGroup.resLimit
           } );

        this.valueTypeMap = new java.util.HashMap<String, co.gphl.common.namelist.ValueType>();
        this.valueTypeMap.put(SimcalSweepGroup.goniostatSettingId, co.gphl.common.namelist.ValueType.CHAR);
        this.valueTypeMap.put(SimcalSweepGroup.centredGoniostatSettingId, co.gphl.common.namelist.ValueType.CHAR);
        this.valueTypeMap.put(SimcalSweepGroup.beamSettingId, co.gphl.common.namelist.ValueType.CHAR);
        this.valueTypeMap.put(SimcalSweepGroup.detectorSettingId, co.gphl.common.namelist.ValueType.CHAR);
        this.valueTypeMap.put(SimcalSweepGroup.beamstopSettingId, co.gphl.common.namelist.ValueType.CHAR);
        this.valueTypeMap.put(SimcalSweepGroup.nameTemplate, co.gphl.common.namelist.ValueType.CHAR);
        
        }

    @Override
    public String getListName() {
        throw new RuntimeException("Should not use this method in v2 namelist stuff!");
    }  
}
    