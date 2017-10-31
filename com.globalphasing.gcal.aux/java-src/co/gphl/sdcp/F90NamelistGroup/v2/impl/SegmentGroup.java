/*******************************************************************************
 * Copyright (c) 2014, 2015 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/


/*
 * Namelist group type corresponding to SEGMENT_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import co.gphl.common.namelist.VarnameComparator;
import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public final class SegmentGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public SegmentGroup(Integer lineNo) {
       super(SegmentGroup.varnameComparator, null, lineNo);
    }
   
    public static final String groupName = "SEGMENT_LIST";
    
    @Override
    public String getGroupName() {
        return SegmentGroup.groupName;
    }

    public static final String segXAxis    = "SEG_X_AXIS";
    public static final String segYAxis    = "SEG_Y_AXIS";
    public static final String segNxLimits = "SEG_NX_LIMITS";
    public static final String segNyLimits = "SEG_NY_LIMITS";
    public static final String segOrgX     = "SEG_ORG_X";
    public static final String segOrgY     = "SEG_ORG_Y";
    public static final String segCoord    = "SEG_COORD";

    private static final VarnameComparator varnameComparator = 
        new VarnameComparator(new String [] {
                SegmentGroup.segXAxis,
                SegmentGroup.segYAxis,
                SegmentGroup.segNxLimits,
                SegmentGroup.segNyLimits,
                SegmentGroup.segOrgX,
                SegmentGroup.segOrgY,
                SegmentGroup.segCoord
        } );
}