

/*
 * Namelist group type corresponding to SIMCAL_OPTIONS_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.Arrays;

import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public final class SimcalOptionsGroup
    // FIXME! To make this namelist group loadable, add constructor(s) that
    // take the appropriate implementing type of org.apache.xmlbeans.XmlObject
    // as an argument
    // FIXME! To make this namelist group saveable, use the following
    // extends/implements clause, and add persist/persistAll methods.
    // extends PersistableNamelistGroupImpl implements PersistableNamelistGroup {
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public SimcalOptionsGroup(Integer lineNo) {
       super(SimcalOptionsGroup.varnameComparator(), SimcalOptionsGroup.groupName, lineNo);
    }
   
    public static final String groupName = "SIMCAL_OPTIONS_LIST";
    
    @Override
    public String getGroupName() {
        return SimcalOptionsGroup.groupName;
    }

    private static SimcalOptionsGroupComparator varnameComparator = null;

    private static SimcalOptionsGroupComparator varnameComparator() {
        if ( SimcalOptionsGroup.varnameComparator == null )
            SimcalOptionsGroup.varnameComparator = new SimcalOptionsGroupComparator();
        return SimcalOptionsGroup.varnameComparator;
    }

    public static final String psfGamma = "PSF_GAMMA";
    public static final String pixQuantile = "PIX_QUANTILE";
    public static final String imgBlending = "IMG_BLENDING";
    public static final String nRays = "N_RAYS";
    public static final String partMode = "PART_MODE";
    public static final String pertMode = "PERT_MODE";
    public static final String simMode = "SIM_MODE";
    public static final String genMode = "GEN_MODE";
    public static final String bcgMode = "BCG_MODE";
}

@SuppressWarnings("serial")
final class SimcalOptionsGroupComparator
    extends co.gphl.common.namelist.AbstractKeyList {
    
    SimcalOptionsGroupComparator() {
        this.keyOrder = Arrays.asList(new String [] {
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
        
    @Override
    public String getListName() {
        throw new RuntimeException("Should not use this method in v2 namelist stuff!");
    }  
}
    