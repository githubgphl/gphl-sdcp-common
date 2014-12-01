

/*
 * Namelist group type corresponding to SEGMENT_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.Arrays;

import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public final class SegmentGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public SegmentGroup(Integer lineNo) {
       super(SegmentGroup.varnameComparator(), SegmentGroup.groupName, lineNo);
    }
   
    public static final String groupName = "SEGMENT_LIST";
    
    @Override
    public String getGroupName() {
        return SegmentGroup.groupName;
    }

    private static SegmentGroupComparator varnameComparator = null;

    private static SegmentGroupComparator varnameComparator() {
        if ( SegmentGroup.varnameComparator == null )
            SegmentGroup.varnameComparator = new SegmentGroupComparator();
        return SegmentGroup.varnameComparator;
    }

    public static final String segXAxis = "SEG_X_AXIS";
    public static final String segYAxis = "SEG_Y_AXIS";
    public static final String segNxLimits = "SEG_NX_LIMITS";
    public static final String segNyLimits = "SEG_NY_LIMITS";
    public static final String segOrgX = "SEG_ORG_X";
    public static final String segOrgY = "SEG_ORG_Y";
    public static final String segCoord = "SEG_COORD";
}

@SuppressWarnings("serial")
final class SegmentGroupComparator
    extends co.gphl.common.namelist.AbstractKeyList {
    
    SegmentGroupComparator() {
        this.keyOrder = Arrays.asList(new String [] {
            SegmentGroup.segXAxis,
            SegmentGroup.segYAxis,
            SegmentGroup.segNxLimits,
            SegmentGroup.segNyLimits,
            SegmentGroup.segOrgX,
            SegmentGroup.segOrgY,
            SegmentGroup.segCoord
           } );
        
        }

    @Override
    public String getListName() {
        throw new RuntimeException("Should not use this method in v2 namelist stuff!");
    }  
}
    