

/*
 * Namelist group type corresponding to SIMCAL_BEAM_SETTING_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public final class SimcalBeamSettingGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public SimcalBeamSettingGroup(Integer lineNo) {
       super(SimcalBeamSettingGroup.varnameOrder, SimcalBeamSettingGroup.charVarnames, lineNo);
    }
   
    public static final String groupName = "SIMCAL_BEAM_SETTING_LIST";
    
    @Override
    public String getGroupName() {
        return SimcalBeamSettingGroup.groupName;
    }

    public static final String id       = "ID";
    public static final String lambda   = "LAMBDA";
    public static final String muAir    = "MU_AIR";
    public static final String muSensor = "MU_SENSOR";

    private static final List<String> varnameOrder = Collections.unmodifiableList(
        Arrays.asList(new String [] {
                SimcalBeamSettingGroup.id,
                SimcalBeamSettingGroup.lambda,
                SimcalBeamSettingGroup.muAir,
                SimcalBeamSettingGroup.muSensor
        } ) );

    private static final Set<String> charVarnames = Collections.unmodifiableSet(
        new HashSet<String>(
                Arrays.asList( new String[] {
                        SimcalBeamSettingGroup.id
                } ) ) );
}