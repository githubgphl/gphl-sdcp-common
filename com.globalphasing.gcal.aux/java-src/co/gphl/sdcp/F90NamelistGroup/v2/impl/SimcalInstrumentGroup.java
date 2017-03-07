

/*
 * Namelist group type corresponding to SIMCAL_INSTRUMENT_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.VarnameComparator;

@SuppressWarnings("serial")
public final class SimcalInstrumentGroup
    extends GcalInstrumentGroup implements F90NamelistGroup {
   
    public SimcalInstrumentGroup(Integer lineNo) {
       super(SimcalInstrumentGroup.varnameComparator, null, lineNo);
    }
   
    public static final String groupName = "SIMCAL_INSTRUMENT_LIST";
    
    @Override
    public String getGroupName() {
        return SimcalInstrumentGroup.groupName;
    }

    public static final String polPlaneN    = "POL_PLANE_N";
    public static final String polFrac      = "POL_FRAC";
    
    protected static final List<String> varnameOrder =
            SimcalInstrumentGroup.initVarnameorder();
    
    private static List<String> initVarnameorder() {
        List<String> retval = new ArrayList<String>( GcalInstrumentGroup.varnameOrder );
        retval.add(SimcalInstrumentGroup.polPlaneN);
        retval.add(SimcalInstrumentGroup.polFrac);
        return Collections.unmodifiableList(retval);
    }
    
    private static final VarnameComparator varnameComparator =
            new VarnameComparator(SimcalInstrumentGroup.varnameOrder);
}

