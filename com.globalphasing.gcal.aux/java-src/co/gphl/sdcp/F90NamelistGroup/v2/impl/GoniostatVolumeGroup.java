

/*
 * Namelist group type corresponding to GONIOSTAT_VOLUME_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.Arrays;

import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public final class GoniostatVolumeGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public GoniostatVolumeGroup(Integer lineNo) {
       super(GoniostatVolumeGroup.varnameComparator(), GoniostatVolumeGroup.groupName, lineNo);
    }
   
    public static final String groupName = "GONIOSTAT_VOLUME_LIST";
    
    @Override
    public String getGroupName() {
        return GoniostatVolumeGroup.groupName;
    }

    private static GoniostatVolumeGroupComparator varnameComparator = null;

    private static GoniostatVolumeGroupComparator varnameComparator() {
        if ( GoniostatVolumeGroup.varnameComparator == null )
            GoniostatVolumeGroup.varnameComparator = new GoniostatVolumeGroupComparator();
        return GoniostatVolumeGroup.varnameComparator;
    }

    public static final String vertices = "VERTICES";
    public static final String triangles = "TRIANGLES";
    public static final String coneRadius = "CONE_RADIUS";
    public static final String coneHeight = "CONE_HEIGHT";
}

@SuppressWarnings("serial")
final class GoniostatVolumeGroupComparator
    extends co.gphl.common.namelist.AbstractKeyList {
    
    GoniostatVolumeGroupComparator() {
        this.keyOrder = Arrays.asList(new String [] {
            GoniostatVolumeGroup.vertices,
            GoniostatVolumeGroup.triangles,
            GoniostatVolumeGroup.coneRadius,
            GoniostatVolumeGroup.coneHeight
           } );
        
        }

    @Override
    public String getListName() {
        throw new RuntimeException("Should not use this method in v2 namelist stuff!");
    }  
}
    