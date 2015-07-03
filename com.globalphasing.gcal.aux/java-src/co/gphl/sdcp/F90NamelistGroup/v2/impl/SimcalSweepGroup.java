

/*
 * Namelist group type corresponding to SIMCAL_SWEEP_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import co.gphl.common.namelist.F90NamelistGroup;

@SuppressWarnings("serial")
public final class SimcalSweepGroup
    extends GcalSweepGroup implements F90NamelistGroup {
   
    public SimcalSweepGroup(Integer lineNo) {
       super(SimcalSweepGroup.varnameOrder, SimcalSweepGroup.charVarnames, lineNo);
    }
   
    public static final String groupName = "SIMCAL_SWEEP_LIST";
    
    @Override
    public String getGroupName() {
        return SimcalSweepGroup.groupName;
    }

    public static final String goniostatSettingId        = GcalSweepGroup.goniostatSettingId;
    public static final String centredGoniostatSettingId = GcalSweepGroup.centredGoniostatSettingId;
    public static final String beamSettingId             = GcalSweepGroup.beamSettingId;
    public static final String detectorSettingId         = GcalSweepGroup.detectorSettingId;
    public static final String beamstopSettingId         = GcalSweepGroup.beamstopSettingId;
    public static final String startDeg                  = GcalSweepGroup.startDeg;
    public static final String exposure                  = "EXPOSURE";
    public static final String stepDeg                   = "STEP_DEG";
    public static final String nFrames                   = "N_FRAMES";
    public static final String imageNo                   = "IMAGE_NO";
    public static final String nameTemplate              = "NAME_TEMPLATE";
    public static final String resLimit                  = "RES_LIMIT";
    
    private static final List<String> _varnameOrder =
            new ArrayList<String>(GcalSweepGroup.varnameOrder);
    static {
        SimcalSweepGroup._varnameOrder.addAll(
            Arrays.asList(
                new String[] {
                        SimcalSweepGroup.exposure,
                        SimcalSweepGroup.stepDeg,
                        SimcalSweepGroup.nFrames,
                        SimcalSweepGroup.imageNo,
                        SimcalSweepGroup.nameTemplate,
                        SimcalSweepGroup.resLimit                        
                } ) );
    }
    private static final List<String> varnameOrder =
            Collections.unmodifiableList(SimcalSweepGroup._varnameOrder);

    private static final Set<String> _charVarnames =
            new HashSet<String>(GcalSweepGroup.charVarnames);
    static {
        SimcalSweepGroup._charVarnames.add(SimcalSweepGroup.nameTemplate);
    }
    private static final Set<String> charVarnames = 
            Collections.unmodifiableSet(SimcalSweepGroup._charVarnames);
    
}