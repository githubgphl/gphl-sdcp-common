

/*
 * Namelist group type corresponding to GONIOSTAT_SETTING_LIST
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
public final class GoniostatSettingGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public GoniostatSettingGroup(Integer lineNo) {
       super(GoniostatSettingGroup.varnameOrder, GoniostatSettingGroup.charVarnames, lineNo);
    }
   
    public static final String groupName = "GONIOSTAT_SETTING_LIST";
    
    @Override
    public String getGroupName() {
        return GoniostatSettingGroup.groupName;
    }

    public static final String id                      = "ID";
    public static final String omegaDeg                = "OMEGA_DEG";
    public static final String kappaDeg                = "KAPPA_DEG";
    public static final String phiDeg                  = "PHI_DEG";
    public static final String spindleDeg              = "SPINDLE_DEG";
    public static final String scanAxisNo              = "SCAN_AXIS_NO";
    public static final String alignedCrystalAxisOrder = "ALIGNED_CRYSTAL_AXIS_ORDER";

    private static final List<String> varnameOrder = Collections.unmodifiableList(
        Arrays.asList(new String [] {
                GoniostatSettingGroup.id,
                GoniostatSettingGroup.omegaDeg,
                GoniostatSettingGroup.kappaDeg,
                GoniostatSettingGroup.phiDeg,
                GoniostatSettingGroup.spindleDeg,
                GoniostatSettingGroup.scanAxisNo,
                GoniostatSettingGroup.alignedCrystalAxisOrder
        } ) );

    private static final Set<String> charVarnames = Collections.unmodifiableSet(
        new HashSet<String>(
                Arrays.asList( new String[] {
                        GoniostatSettingGroup.id
                } ) ) );
    
    // Added by hand.
    public static final String fAxisDeg = "%s_DEG";
    
    public static String settingName(int i) {
        return String.format( fAxisDeg, GcalInstrumentGroup.rotAxisOrder.get(i) );
    }
    
}
    