

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
import co.gphl.common.namelist.VarnameComparator;

@SuppressWarnings("serial")
public final class SimcalSweepGroup
    extends GcalSweepGroup implements F90NamelistGroup {
   
    public SimcalSweepGroup(Integer lineNo) {
       super(SimcalSweepGroup.varnameComparator, SimcalSweepGroup.charVarnames, lineNo);
    }
   
    public static final String groupName = "SIMCAL_SWEEP_LIST";
    
    @Override
    public String getGroupName() {
        return SimcalSweepGroup.groupName;
    }

    public static final String exposure                  = "EXPOSURE";
    public static final String stepDeg                   = "STEP_DEG";
    public static final String nFrames                   = "N_FRAMES";
    public static final String imageNo                   = "IMAGE_NO";
    public static final String nameTemplate              = "NAME_TEMPLATE";
    public static final String resLimit                  = "RES_LIMIT";
    
    private static final List<String> varnameOrder =
            new ArrayList<String>( Arrays.asList(GcalSweepGroup.varnameOrder) );
    static {
        SimcalSweepGroup.varnameOrder.add(SimcalSweepGroup.exposure);
        SimcalSweepGroup.varnameOrder.add(SimcalSweepGroup.stepDeg);
        SimcalSweepGroup.varnameOrder.add(SimcalSweepGroup.nFrames);
        SimcalSweepGroup.varnameOrder.add(SimcalSweepGroup.imageNo);
        SimcalSweepGroup.varnameOrder.add(SimcalSweepGroup.nameTemplate);
        SimcalSweepGroup.varnameOrder.add(SimcalSweepGroup.resLimit);
    }
            
    private static final VarnameComparator varnameComparator =
            new VarnameComparator(SimcalSweepGroup.varnameOrder);
    
    private static final Set<String> _charVarnames =
            new HashSet<String>(GcalSweepGroup.charVarnames);
    static {
        SimcalSweepGroup._charVarnames.add(SimcalSweepGroup.nameTemplate);
    }
    private static final Set<String> charVarnames = 
            Collections.unmodifiableSet(SimcalSweepGroup._charVarnames);
    
}