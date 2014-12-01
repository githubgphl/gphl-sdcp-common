

/*
 * Namelist group type corresponding to DETECTOR_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.Arrays;

import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public final class DetectorGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public DetectorGroup(Integer lineNo) {
       super(DetectorGroup.varnameComparator(), DetectorGroup.groupName, lineNo);
    }
   
    public static final String groupName = "DETECTOR_LIST";
    
    @Override
    public String getGroupName() {
        return DetectorGroup.groupName;
    }

    private static DetectorGroupComparator varnameComparator = null;

    private static DetectorGroupComparator varnameComparator() {
        if ( DetectorGroup.varnameComparator == null )
            DetectorGroup.varnameComparator = new DetectorGroupComparator();
        return DetectorGroup.varnameComparator;
    }

    public static final String detXAxis = "DET_X_AXIS";
    public static final String detYAxis = "DET_Y_AXIS";
    public static final String detQx = "DET_QX";
    public static final String detQy = "DET_QY";
    public static final String detNx = "DET_NX";
    public static final String detNy = "DET_NY";
    public static final String detOrgX = "DET_ORG_X";
    public static final String detOrgY = "DET_ORG_Y";
    public static final String gain = "GAIN";
    public static final String dSensor = "D_SENSOR";
    public static final String detName = "DET_NAME";
}

@SuppressWarnings("serial")
final class DetectorGroupComparator
    extends co.gphl.common.namelist.AbstractKeyList {
    
    DetectorGroupComparator() {
        this.keyOrder = Arrays.asList(new String [] {
            DetectorGroup.detXAxis,
            DetectorGroup.detYAxis,
            DetectorGroup.detQx,
            DetectorGroup.detQy,
            DetectorGroup.detNx,
            DetectorGroup.detNy,
            DetectorGroup.detOrgX,
            DetectorGroup.detOrgY,
            DetectorGroup.gain,
            DetectorGroup.dSensor,
            DetectorGroup.detName
           } );

        this.valueTypeMap = new java.util.HashMap<String, co.gphl.common.namelist.ValueType>();
        this.valueTypeMap.put(DetectorGroup.detName, co.gphl.common.namelist.ValueType.CHAR);
        
        }

    @Override
    public String getListName() {
        throw new RuntimeException("Should not use this method in v2 namelist stuff!");
    }  
}
    