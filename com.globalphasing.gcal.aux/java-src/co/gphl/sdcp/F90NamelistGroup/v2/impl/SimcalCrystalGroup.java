

/*
 * Namelist group type corresponding to SIMCAL_CRYSTAL_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.Arrays;

import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public final class SimcalCrystalGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public SimcalCrystalGroup(Integer lineNo) {
       super(SimcalCrystalGroup.varnameComparator(), SimcalCrystalGroup.groupName, lineNo);
    }
   
    public static final String groupName = "SIMCAL_CRYSTAL_LIST";
    
    @Override
    public String getGroupName() {
        return SimcalCrystalGroup.groupName;
    }

    private static SimcalCrystalGroupComparator varnameComparator = null;

    private static SimcalCrystalGroupComparator varnameComparator() {
        if ( SimcalCrystalGroup.varnameComparator == null )
            SimcalCrystalGroup.varnameComparator = new SimcalCrystalGroupComparator();
        return SimcalCrystalGroup.varnameComparator;
    }

    public static final String sgName = "SG_NAME";
    public static final String cellDim = "CELL_DIM";
    public static final String cellAngDeg = "CELL_ANG_DEG";
    public static final String uMatAngDeg = "U_MAT_ANG_DEG";
    public static final String cellAAxis = "CELL_A_AXIS";
    public static final String cellBAxis = "CELL_B_AXIS";
    public static final String cellCAxis = "CELL_C_AXIS";
    public static final String orientMode = "ORIENT_MODE";
    public static final String resLimitDef = "RES_LIMIT_DEF";
    public static final String cellRefAngDeg = "CELL_REF_ANG_DEG";
    public static final String cellDimSd = "CELL_DIM_SD";
    public static final String cellAngSdDeg = "CELL_ANG_SD_DEG";
    public static final String uMatSdDeg = "U_MAT_SD_DEG";
    public static final String sigmaN = "SIGMA_N";
    public static final String bWilson = "B_WILSON";
    public static final String b6Wilson = "B6_WILSON";
}

@SuppressWarnings("serial")
final class SimcalCrystalGroupComparator
    extends co.gphl.common.namelist.AbstractKeyList {
    
    SimcalCrystalGroupComparator() {
        this.keyOrder = Arrays.asList(new String [] {
            SimcalCrystalGroup.sgName,
            SimcalCrystalGroup.cellDim,
            SimcalCrystalGroup.cellAngDeg,
            SimcalCrystalGroup.uMatAngDeg,
            SimcalCrystalGroup.cellAAxis,
            SimcalCrystalGroup.cellBAxis,
            SimcalCrystalGroup.cellCAxis,
            SimcalCrystalGroup.orientMode,
            SimcalCrystalGroup.resLimitDef,
            SimcalCrystalGroup.cellRefAngDeg,
            SimcalCrystalGroup.cellDimSd,
            SimcalCrystalGroup.cellAngSdDeg,
            SimcalCrystalGroup.uMatSdDeg,
            SimcalCrystalGroup.sigmaN,
            SimcalCrystalGroup.bWilson,
            SimcalCrystalGroup.b6Wilson
           } );

        this.valueTypeMap = new java.util.HashMap<String, co.gphl.common.namelist.ValueType>();
        this.valueTypeMap.put(SimcalCrystalGroup.sgName, co.gphl.common.namelist.ValueType.CHAR);
        
        }

    @Override
    public String getListName() {
        throw new RuntimeException("Should not use this method in v2 namelist stuff!");
    }  
}
    