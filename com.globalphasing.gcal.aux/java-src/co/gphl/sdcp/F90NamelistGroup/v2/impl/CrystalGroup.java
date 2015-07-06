

/*
 * Namelist group type corresponding to CRYSTAL_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import co.gphl.common.namelist.VarnameComparator;
import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public class CrystalGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public CrystalGroup(Integer lineNo) {
       super(CrystalGroup.varnameComparator, CrystalGroup.charVarnames, lineNo);
    }
    
    protected CrystalGroup( VarnameComparator varnameComparator, Set<String> charVarnames, Integer lineNo ) {
        super(varnameComparator, charVarnames, lineNo);
    }
   
    public static final String groupName = "CRYSTAL_LIST";
    
    @Override
    public String getGroupName() {
        return CrystalGroup.groupName;
    }

    public static final String sgName      = "SG_NAME";
    public static final String cellDim     = "CELL_DIM";
    public static final String cellAngDeg  = "CELL_ANG_DEG";
    public static final String uMatAngDeg  = "U_MAT_ANG_DEG";
    public static final String cellAAxis   = "CELL_A_AXIS";
    public static final String cellBAxis   = "CELL_B_AXIS";
    public static final String cellCAxis   = "CELL_C_AXIS";
    public static final String orientMode  = "ORIENT_MODE";
    public static final String resLimitDef = "RES_LIMIT_DEF";

    protected static final String[] varnameOrder =
            new String [] {
        CrystalGroup.sgName,
        CrystalGroup.cellDim,
        CrystalGroup.cellAngDeg,
        CrystalGroup.uMatAngDeg,
        CrystalGroup.cellAAxis,
        CrystalGroup.cellBAxis,
        CrystalGroup.cellCAxis,
        CrystalGroup.orientMode,
        CrystalGroup.resLimitDef
    };
    
    private static final VarnameComparator varnameComparator = 
        new VarnameComparator( CrystalGroup.varnameOrder );

    private static final Set<String> charVarnames = Collections.unmodifiableSet(
        new HashSet<String>(
                Arrays.asList( new String[] {
                        CrystalGroup.sgName
                } ) ) );
}