

/*
 * Namelist group type corresponding to SIMCAL_OPTIONS_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import co.gphl.common.namelist.VarnameComparator;
import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public final class SimcalOptionsGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public SimcalOptionsGroup(Integer lineNo) {
       super(SimcalOptionsGroup.varnameComparator, null, lineNo);
    }
   
    public static final String groupName = "SIMCAL_OPTIONS_LIST";
    
    @Override
    public String getGroupName() {
        return SimcalOptionsGroup.groupName;
    }

    public static final String psfGamma    = "PSF_GAMMA";
    public static final String pixQuantile = "PIX_QUANTILE";
    public static final String imgBlending = "IMG_BLENDING";
    public static final String nRays       = "N_RAYS";
    public static final String partMode    = "PART_MODE";
    public static final String pertMode    = "PERT_MODE";
    public static final String simMode     = "SIM_MODE";
    public static final String genMode     = "GEN_MODE";
    public static final String bcgMode     = "BCG_MODE";

    private static final VarnameComparator varnameComparator = 
        new VarnameComparator(new String [] {
                SimcalOptionsGroup.psfGamma,
                SimcalOptionsGroup.pixQuantile,
                SimcalOptionsGroup.imgBlending,
                SimcalOptionsGroup.nRays,
                SimcalOptionsGroup.partMode,
                SimcalOptionsGroup.pertMode,
                SimcalOptionsGroup.simMode,
                SimcalOptionsGroup.genMode,
                SimcalOptionsGroup.bcgMode
        } );
}