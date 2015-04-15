

/*
 * Namelist group type corresponding to SIMCAL_CRYSTAL_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.gphl.common.namelist.AbstractKeyList;
import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.ValueType;

@SuppressWarnings("serial")
public final class SimcalCrystalGroup
    extends CrystalGroup implements F90NamelistGroup {
   
    public SimcalCrystalGroup(Integer lineNo) {
       super(SimcalCrystalGroup.varnameComparator(), lineNo);
    }
   
    public static final String groupName = "SIMCAL_CRYSTAL_LIST";
    
    @Override
    public String getGroupName() {
        return SimcalCrystalGroup.groupName;
    }

    private static AbstractKeyList varnameComparator() {
        if ( SimcalCrystalGroup.varnameComparator == null )
            SimcalCrystalGroup.initComparator();
        return SimcalCrystalGroup.varnameComparator;
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
    public static final String cellDimSd = "CELL_DIM_SD";
    public static final String cellAngSdDeg = "CELL_ANG_SD_DEG";
    public static final String uMatSdDeg = "U_MAT_SD_DEG";
    public static final String sigmaN = "SIGMA_N";
    public static final String bWilson = "B_WILSON";
    public static final String b6Wilson = "B6_WILSON";
    
    // FIXME! Ugh! sort this out properly!
    private static List<String> keyOrder = null;
    private static Map<String, ValueType> valueTypeMap = null;
    private static AbstractKeyList varnameComparator = null;

    private static void initComparator() {
        
        SimcalCrystalGroup.keyOrder = new ArrayList<String>(CrystalGroup.getKeyOrder());
        SimcalCrystalGroup.keyOrder.addAll(
            Arrays.asList( new String[] {
                    SimcalCrystalGroup.cellRefAngDeg,
                    SimcalCrystalGroup.cellDimSd,
                    SimcalCrystalGroup.cellAngSdDeg,
                    SimcalCrystalGroup.uMatSdDeg,
                    SimcalCrystalGroup.sigmaN,
                    SimcalCrystalGroup.bWilson,
                    SimcalCrystalGroup.b6Wilson } )
                );
        
        SimcalCrystalGroup.valueTypeMap = new HashMap<String, co.gphl.common.namelist.ValueType>();
        SimcalCrystalGroup.valueTypeMap.put(SimcalCrystalGroup.sgName, co.gphl.common.namelist.ValueType.CHAR);
     
        SimcalCrystalGroup.varnameComparator = new AbstractKeyList(SimcalCrystalGroup.keyOrder,
            SimcalCrystalGroup.valueTypeMap, SimcalCrystalGroup.groupName);
        
    }
    
}
    