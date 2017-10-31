/*******************************************************************************
 * Copyright (c) 2014, 2015 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/


/*
 * Namelist group type corresponding to GONIOSTAT_VOLUME_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import co.gphl.common.namelist.VarnameComparator;
import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public final class GoniostatVolumeGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public GoniostatVolumeGroup(Integer lineNo) {
       super(GoniostatVolumeGroup.varnameComparator, null, lineNo);
    }
   
    public static final String groupName = "GONIOSTAT_VOLUME_LIST";
    
    @Override
    public String getGroupName() {
        return GoniostatVolumeGroup.groupName;
    }

    public static final String vertices   = "VERTICES";
    public static final String triangles  = "TRIANGLES";
    public static final String coneRadius = "CONE_RADIUS";
    public static final String coneHeight = "CONE_HEIGHT";

    private static final VarnameComparator varnameComparator = 
        new VarnameComparator(new String [] {
                GoniostatVolumeGroup.vertices,
                GoniostatVolumeGroup.triangles,
                GoniostatVolumeGroup.coneRadius,
                GoniostatVolumeGroup.coneHeight
        } );
}