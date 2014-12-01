

/*
 * Namelist group type corresponding to ERROR_MODEL_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.Arrays;

import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public final class ErrorModelGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public ErrorModelGroup(Integer lineNo) {
       super(ErrorModelGroup.varnameComparator(), ErrorModelGroup.groupName, lineNo);
    }
   
    public static final String groupName = "ERROR_MODEL_LIST";
    
    @Override
    public String getGroupName() {
        return ErrorModelGroup.groupName;
    }

    private static ErrorModelGroupComparator varnameComparator = null;

    private static ErrorModelGroupComparator varnameComparator() {
        if ( ErrorModelGroup.varnameComparator == null )
            ErrorModelGroup.varnameComparator = new ErrorModelGroupComparator();
        return ErrorModelGroup.varnameComparator;
    }

    public static final String background = "BACKGROUND";
    public static final String beamSdDeg = "BEAM_SD_DEG";
    public static final String lambdaSd = "LAMBDA_SD";
    public static final String minZeta = "MIN_ZETA";
    public static final String minLorentz = "MIN_LORENTZ";
}

@SuppressWarnings("serial")
final class ErrorModelGroupComparator
    extends co.gphl.common.namelist.AbstractKeyList {
    
    ErrorModelGroupComparator() {
        this.keyOrder = Arrays.asList(new String [] {
            ErrorModelGroup.background,
            ErrorModelGroup.beamSdDeg,
            ErrorModelGroup.lambdaSd,
            ErrorModelGroup.minZeta,
            ErrorModelGroup.minLorentz
           } );
        
        }

    @Override
    public String getListName() {
        throw new RuntimeException("Should not use this method in v2 namelist stuff!");
    }  
}
    