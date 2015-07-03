

/*
 * Namelist group type corresponding to SIMCAL_INSTRUMENT_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.gphl.common.namelist.F90NamelistGroup;

@SuppressWarnings("serial")
public final class SimcalInstrumentGroup
    extends GcalInstrumentGroup implements F90NamelistGroup {
   
    public SimcalInstrumentGroup(Integer lineNo) {
       super(SimcalInstrumentGroup.varnameOrder, null, lineNo);
    }
   
    public static final String groupName = "SIMCAL_INSTRUMENT_LIST";
    
    @Override
    public String getGroupName() {
        return SimcalInstrumentGroup.groupName;
    }

    public static final String twoThetaAxis = GcalInstrumentGroup.twoThetaAxis;
    public static final String omegaAxis    = GcalInstrumentGroup.omegaAxis;
    public static final String kappaAxis    = GcalInstrumentGroup.kappaAxis;
    public static final String phiAxis      = GcalInstrumentGroup.phiAxis;
    public static final String spindleAxis  = GcalInstrumentGroup.spindleAxis;
    public static final String trans1Axis   = GcalInstrumentGroup.trans1Axis;
    public static final String trans2Axis   = GcalInstrumentGroup.trans2Axis;
    public static final String trans3Axis   = GcalInstrumentGroup.trans3Axis;
    public static final String beam         = GcalInstrumentGroup.beam;
    public static final String polPlaneN    = "POL_PLANE_N";
    public static final String polFrac      = "POL_FRAC";
    
    protected static final List<String> _varnameOrder =
            new ArrayList<String>(GcalInstrumentGroup.varnameOrder);
    static {
        SimcalInstrumentGroup._varnameOrder.add(SimcalInstrumentGroup.polPlaneN);
        SimcalInstrumentGroup._varnameOrder.add(SimcalInstrumentGroup.polFrac);
    }
    
    private static final List<String> varnameOrder =
            Collections.unmodifiableList(_varnameOrder);
}

