

/*
 * Namelist group type corresponding to SIMCAL_CRYSTAL_LIST
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
public final class SimcalCrystalGroup
    extends CrystalGroup implements F90NamelistGroup {
   
    public SimcalCrystalGroup(Integer lineNo) {
       super(SimcalCrystalGroup.varnameComparator, SimcalCrystalGroup.charVarnames, lineNo);
    }
   
    public static final String groupName = "SIMCAL_CRYSTAL_LIST";
    
    @Override
    public String getGroupName() {
        return SimcalCrystalGroup.groupName;
    }

    public static final String sgName        = CrystalGroup.sgName;
    public static final String cellDim       = CrystalGroup.cellDim;
    public static final String cellAngDeg    = CrystalGroup.cellAngDeg;
    public static final String uMatAngDeg    = CrystalGroup.uMatAngDeg;
    public static final String cellAAxis     = CrystalGroup.cellAAxis;
    public static final String cellBAxis     = CrystalGroup.cellBAxis;
    public static final String cellCAxis     = CrystalGroup.cellCAxis;
    public static final String orientMode    = CrystalGroup.orientMode;
    public static final String resLimitDef   = CrystalGroup.resLimitDef;
    public static final String cellRefAngDeg = "CELL_REF_ANG_DEG";
    public static final String cellDimSd     = "CELL_DIM_SD";
    public static final String cellAngSdDeg  = "CELL_ANG_SD_DEG";
    public static final String uMatSdDeg     = "U_MAT_SD_DEG";
    public static final String sigmaN        = "SIGMA_N";
    public static final String bWilson       = "B_WILSON";
    public static final String b6Wilson      = "B6_WILSON";

    private static final List<String> varnameOrder =
            new ArrayList<String>( Arrays.asList(CrystalGroup.varnameOrder) ) ;

    static{
        SimcalCrystalGroup.varnameOrder.add(SimcalCrystalGroup.cellRefAngDeg);
        SimcalCrystalGroup.varnameOrder.add(SimcalCrystalGroup.cellDimSd);
        SimcalCrystalGroup.varnameOrder.add(SimcalCrystalGroup.cellAngSdDeg);
        SimcalCrystalGroup.varnameOrder.add(SimcalCrystalGroup.uMatSdDeg);
        SimcalCrystalGroup.varnameOrder.add(SimcalCrystalGroup.sigmaN);
        SimcalCrystalGroup.varnameOrder.add(SimcalCrystalGroup.bWilson);
        SimcalCrystalGroup.varnameOrder.add(SimcalCrystalGroup.b6Wilson);
    }

    private static final VarnameComparator varnameComparator =
            new VarnameComparator(SimcalCrystalGroup.varnameOrder);
    
    private static final Set<String> charVarnames = Collections.unmodifiableSet(
        new HashSet<String>(
                Arrays.asList( new String[] {
                        SimcalCrystalGroup.sgName
                } ) ) );
}