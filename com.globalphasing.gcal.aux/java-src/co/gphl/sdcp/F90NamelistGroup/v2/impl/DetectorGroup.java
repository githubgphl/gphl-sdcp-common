

/*
 * Namelist group type corresponding to DETECTOR_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public final class DetectorGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public DetectorGroup(Integer lineNo) {
       super(DetectorGroup.varnameOrder, DetectorGroup.charVarnames, lineNo);
    }
   
    public static final String groupName = "DETECTOR_LIST";
    
    @Override
    public String getGroupName() {
        return DetectorGroup.groupName;
    }

    public static final String detXAxis = "DET_X_AXIS";
    public static final String detYAxis = "DET_Y_AXIS";
    public static final String detQx    = "DET_QX";
    public static final String detQy    = "DET_QY";
    public static final String detNx    = "DET_NX";
    public static final String detNy    = "DET_NY";
    public static final String detOrgX  = "DET_ORG_X";
    public static final String detOrgY  = "DET_ORG_Y";
    public static final String gain     = "GAIN";
    public static final String dSensor  = "D_SENSOR";
    public static final String detName  = "DET_NAME";

    private static final List<String> varnameOrder = Collections.unmodifiableList(
        Arrays.asList(new String [] {
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
        } ) );

    private static final Set<String> charVarnames = Collections.unmodifiableSet(
        new HashSet<String>(
                Arrays.asList( new String[] {
                        DetectorGroup.detName
                } ) ) );
}