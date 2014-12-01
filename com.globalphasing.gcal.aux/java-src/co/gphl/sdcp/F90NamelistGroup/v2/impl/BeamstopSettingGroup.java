

/*
 * Namelist group type corresponding to BEAMSTOP_SETTING_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.Arrays;

import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public final class BeamstopSettingGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public BeamstopSettingGroup(Integer lineNo) {
       super(BeamstopSettingGroup.varnameComparator(), BeamstopSettingGroup.groupName, lineNo);
    }
   
    public static final String groupName = "BEAMSTOP_SETTING_LIST";
    
    @Override
    public String getGroupName() {
        return BeamstopSettingGroup.groupName;
    }

    private static BeamstopSettingGroupComparator varnameComparator = null;

    private static BeamstopSettingGroupComparator varnameComparator() {
        if ( BeamstopSettingGroup.varnameComparator == null )
            BeamstopSettingGroup.varnameComparator = new BeamstopSettingGroupComparator();
        return BeamstopSettingGroup.varnameComparator;
    }

    public static final String id = "ID";
    public static final String beamStopRadius = "BEAM_STOP_RADIUS";
    public static final String beamStopSLength = "BEAM_STOP_S_LENGTH";
    public static final String beamStopSDistance = "BEAM_STOP_S_DISTANCE";
}

@SuppressWarnings("serial")
final class BeamstopSettingGroupComparator
    extends co.gphl.common.namelist.AbstractKeyList {
    
    BeamstopSettingGroupComparator() {
        this.keyOrder = Arrays.asList(new String [] {
            BeamstopSettingGroup.id,
            BeamstopSettingGroup.beamStopRadius,
            BeamstopSettingGroup.beamStopSLength,
            BeamstopSettingGroup.beamStopSDistance
           } );
        }
        
    @Override
    public String getListName() {
        throw new RuntimeException("Should not use this method in v2 namelist stuff!");
    }  
}
    