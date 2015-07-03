

/*
 * Namelist group type corresponding to ERROR_MODEL_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public final class ErrorModelGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public ErrorModelGroup(Integer lineNo) {
       super(ErrorModelGroup.varnameOrder, null, lineNo);
    }
   
    public static final String groupName = "ERROR_MODEL_LIST";
    
    @Override
    public String getGroupName() {
        return ErrorModelGroup.groupName;
    }

    public static final String background = "BACKGROUND";
    public static final String beamSdDeg  = "BEAM_SD_DEG";
    public static final String lambdaSd   = "LAMBDA_SD";
    public static final String minZeta    = "MIN_ZETA";
    public static final String minLorentz = "MIN_LORENTZ";

    private static final List<String> varnameOrder = Collections.unmodifiableList(
        Arrays.asList(new String [] {
                ErrorModelGroup.background,
                ErrorModelGroup.beamSdDeg,
                ErrorModelGroup.lambdaSd,
                ErrorModelGroup.minZeta,
                ErrorModelGroup.minLorentz
        } ) );
}