

/*
 * Namelist group type corresponding to SIMCAL_INSTRUMENT_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.Arrays;

import co.gphl.common.namelist.F90NamelistGroup;

@SuppressWarnings("serial")
public final class SimcalInstrumentGroup
    extends GcalInstrumentGroup implements F90NamelistGroup {
   
    public SimcalInstrumentGroup(Integer lineNo) {
       super(SimcalInstrumentGroup.varnameComparator(), SimcalInstrumentGroup.groupName, lineNo);
    }
   
    public static final String groupName = "SIMCAL_INSTRUMENT_LIST";
    
    @Override
    public String getGroupName() {
        return SimcalInstrumentGroup.groupName;
    }

    private static SimcalInstrumentGroupComparator varnameComparator = null;

    private static SimcalInstrumentGroupComparator varnameComparator() {
        if ( SimcalInstrumentGroup.varnameComparator == null )
            SimcalInstrumentGroup.varnameComparator = new SimcalInstrumentGroupComparator();
        return SimcalInstrumentGroup.varnameComparator;
    }

    public static final String twoThetaAxis =    GcalInstrumentGroup.twoThetaAxis;
    public static final String omegaAxis =       GcalInstrumentGroup.omegaAxis;
    public static final String kappaAxis =       GcalInstrumentGroup.kappaAxis;
    public static final String phiAxis =         GcalInstrumentGroup.phiAxis;
    public static final String spindleAxis =     GcalInstrumentGroup.spindleAxis;
    public static final String trans1Axis =      GcalInstrumentGroup.trans1Axis;
    public static final String trans2Axis =      GcalInstrumentGroup.trans2Axis;
    public static final String trans3Axis =      GcalInstrumentGroup.trans3Axis;
    public static final String beam =            GcalInstrumentGroup.beam;
    public static final String polPlaneN = "POL_PLANE_N";
    public static final String polFrac = "POL_FRAC";
}

@SuppressWarnings("serial")
final class SimcalInstrumentGroupComparator
    extends co.gphl.common.namelist.AbstractKeyList {
    
    SimcalInstrumentGroupComparator() {
        this.keyOrder = Arrays.asList(new String [] {
            SimcalInstrumentGroup.twoThetaAxis,
            SimcalInstrumentGroup.omegaAxis,
            SimcalInstrumentGroup.kappaAxis,
            SimcalInstrumentGroup.phiAxis,
            SimcalInstrumentGroup.spindleAxis,
            SimcalInstrumentGroup.trans1Axis,
            SimcalInstrumentGroup.trans2Axis,
            SimcalInstrumentGroup.trans3Axis,
            SimcalInstrumentGroup.beam,
            SimcalInstrumentGroup.polPlaneN,
            SimcalInstrumentGroup.polFrac
           } );
        
        }

    @Override
    public String getListName() {
        throw new RuntimeException("Should not use this method in v2 namelist stuff!");
    }  
}
    