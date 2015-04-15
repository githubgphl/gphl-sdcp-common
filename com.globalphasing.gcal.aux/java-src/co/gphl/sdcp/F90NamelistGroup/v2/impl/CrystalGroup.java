

/*
 * Namelist group type corresponding to CRYSTAL_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.gphl.common.namelist.AbstractKeyList;
import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.ValueType;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public class CrystalGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public CrystalGroup(Integer lineNo) {
       super(CrystalGroup.varnameComparator(), CrystalGroup.groupName, lineNo);
    }
    
    protected CrystalGroup(AbstractKeyList keyList, Integer lineNo) {
        super(keyList);
    }
    
    public static final String groupName = "CRYSTAL_LIST";
    
    @Override
    public String getGroupName() {
        return CrystalGroup.groupName;
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
    
    // FIXME! Ugh! sort this out properly!
    private static List<String> keyOrder = null;
    private static Map<String, ValueType> valueTypeMap = null;
    private static AbstractKeyList varnameComparator = null;

    private static AbstractKeyList varnameComparator() {
        if ( CrystalGroup.varnameComparator == null )
            CrystalGroup.initComparator();
        return CrystalGroup.varnameComparator;
    }

    protected static List<String> getKeyOrder() {
        if ( CrystalGroup.keyOrder == null )
            CrystalGroup.initComparator();
        return CrystalGroup.keyOrder;
    }
    
    private static void initComparator() {
        
        CrystalGroup.keyOrder = Arrays.asList(new String [] {
                CrystalGroup.sgName,
                CrystalGroup.cellDim,
                CrystalGroup.cellAngDeg,
                CrystalGroup.uMatAngDeg,
                CrystalGroup.cellAAxis,
                CrystalGroup.cellBAxis,
                CrystalGroup.cellCAxis,
                CrystalGroup.orientMode,
                CrystalGroup.resLimitDef
               } );
        CrystalGroup.keyOrder = Collections.unmodifiableList(CrystalGroup.keyOrder);
        
        CrystalGroup.valueTypeMap= new HashMap<String, co.gphl.common.namelist.ValueType>();
        CrystalGroup.valueTypeMap.put(CrystalGroup.sgName, co.gphl.common.namelist.ValueType.CHAR);
        CrystalGroup.valueTypeMap = Collections.unmodifiableMap(CrystalGroup.valueTypeMap);
        
        CrystalGroup.varnameComparator =
                new AbstractKeyList( CrystalGroup.keyOrder, CrystalGroup.valueTypeMap, CrystalGroup.groupName);
    }
    
    
}
    