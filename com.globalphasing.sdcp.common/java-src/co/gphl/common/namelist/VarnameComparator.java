/*******************************************************************************
 * Copyright (c) 2015 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/

package co.gphl.common.namelist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


/**
 * @author pkeller
 *
 */
public final class VarnameComparator implements Comparator<String> {
    
    public VarnameComparator( String[] varnames ) {
        this(Arrays.asList(varnames));
    }
    
    public VarnameComparator( List<String> varnames ) {
        this.varnames = new ArrayList<String>(varnames);
    }
    
    private List<String> varnames;
    
    @Override
    public int compare(String arg0, String arg1) {
        
        int idx0 = -1, idx1 = -1 ;
        
        if ( varnames != null ) {
            idx0 = varnames.indexOf(arg0.toUpperCase());
            idx1 = varnames.indexOf(arg1.toUpperCase());;
        }
        
        if ( idx0 > -1 ) {
            if ( idx1 > -1 )
                return idx0 - idx1;
            else
                return -1;
        }
        else {
            if ( idx1 > -1 )
                return 1;
            else
                return arg0.compareToIgnoreCase(arg1);
        }
    }
    
    public boolean contains(String varname) {
        return this.varnames.contains(varname);
    }
    
}
