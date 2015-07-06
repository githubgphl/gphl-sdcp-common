

/*
 * Namelist group type corresponding to LOOP_COUNT_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import co.gphl.common.namelist.VarnameComparator;
import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public final class LoopCountGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public LoopCountGroup(Integer lineNo) {
       super(LoopCountGroup.varnameComparator, null, lineNo);
    }
   
    public static final String groupName = "LOOP_COUNT_LIST";
    
    @Override
    public String getGroupName() {
        return LoopCountGroup.groupName;
    }

    public static final String nVertices                 = "N_VERTICES";
    public static final String nTriangles                = "N_TRIANGLES";
    public static final String nSegments                 = "N_SEGMENTS";
    public static final String nOrients                  = "N_ORIENTS";
    public static final String nSweeps                   = "N_SWEEPS";
    public static final String nGoniostatSettings        = "N_GONIOSTAT_SETTINGS";
    public static final String nCentredGoniostatSettings = "N_CENTRED_GONIOSTAT_SETTINGS";
    public static final String nDetectorSettings         = "N_DETECTOR_SETTINGS";
    public static final String nBeamSettings             = "N_BEAM_SETTINGS";
    public static final String nBeamstopSettings         = "N_BEAMSTOP_SETTINGS";
    public static final String nCrystals                 = "N_CRYSTALS";

    private static final VarnameComparator varnameComparator = 
        new VarnameComparator(new String [] {
                LoopCountGroup.nVertices,
                LoopCountGroup.nTriangles,
                LoopCountGroup.nSegments,
                LoopCountGroup.nOrients,
                LoopCountGroup.nSweeps,
                LoopCountGroup.nGoniostatSettings,
                LoopCountGroup.nCentredGoniostatSettings,
                LoopCountGroup.nDetectorSettings,
                LoopCountGroup.nBeamSettings,
                LoopCountGroup.nBeamstopSettings,
                LoopCountGroup.nCrystals
        } );
}