

/*
 * Namelist group type corresponding to GONIOSTAT_VOLUME_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public final class GoniostatVolumeGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public GoniostatVolumeGroup(Integer lineNo) {
       super(GoniostatVolumeGroup.varnameOrder, null, lineNo);
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

    private static final List<String> varnameOrder = Collections.unmodifiableList(
        Arrays.asList(new String [] {
                GoniostatVolumeGroup.vertices,
                GoniostatVolumeGroup.triangles,
                GoniostatVolumeGroup.coneRadius,
                GoniostatVolumeGroup.coneHeight
        } ) );
}