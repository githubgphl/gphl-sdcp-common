

/*
 * Namelist group type corresponding to LOOP_COUNT_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.Arrays;

import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public final class LoopCountGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public LoopCountGroup(Integer lineNo) {
       super(LoopCountGroup.varnameComparator(), LoopCountGroup.groupName, lineNo);
    }
   
    public static final String groupName = "LOOP_COUNT_LIST";
    
    @Override
    public String getGroupName() {
        return LoopCountGroup.groupName;
    }

    private static LoopCountGroupComparator varnameComparator = null;

    private static LoopCountGroupComparator varnameComparator() {
        if ( LoopCountGroup.varnameComparator == null )
            LoopCountGroup.varnameComparator = new LoopCountGroupComparator();
        return LoopCountGroup.varnameComparator;
    }

    public static final String nVertices = "N_VERTICES";
    public static final String nTriangles = "N_TRIANGLES";
    public static final String nSegments = "N_SEGMENTS";
    public static final String nOrients = "N_ORIENTS";
    public static final String nSweeps = "N_SWEEPS";
    public static final String nGoniostatSettings = "N_GONIOSTAT_SETTINGS";
    public static final String nCentredGoniostatSettings = "N_CENTRED_GONIOSTAT_SETTINGS";
    public static final String nDetectorSettings = "N_DETECTOR_SETTINGS";
    public static final String nBeamSettings = "N_BEAM_SETTINGS";
    public static final String nBeamstopSettings = "N_BEAMSTOP_SETTINGS";
    public static final String nCrystals = "N_CRYSTALS";
}

@SuppressWarnings("serial")
final class LoopCountGroupComparator
    extends co.gphl.common.namelist.AbstractKeyList {
    
    LoopCountGroupComparator() {
        this.keyOrder = Arrays.asList(new String [] {
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

    @Override
    public String getListName() {
        throw new RuntimeException("Should not use this method in v2 namelist stuff!");
    }  
}
    