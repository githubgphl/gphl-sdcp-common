/**
 * Copyright © 2017 by Global Phasing Ltd. All rights reserved
 *
 * This software is proprietary to and embodies the confidential
 * technology of Global Phasing Limited (GPhL).
 *
 * Any possession or use (including but not limited to duplication, reproduction
 * and dissemination) of this software (in either source or compiled form) is
 * forbidden except where an agreement with GPhL that permits such possession or
 * use is in force.
 *
 */
package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.VarnameComparator;

/**
 * @author pkeller
 *
 */
public class RecenGroup extends GcalGoniostatGroup implements F90NamelistGroup {

    public RecenGroup(Integer lineNo) {
        super(RecenGroup.varnameComparator, null, lineNo);
    }

    public static final String home          = "HOME";
    public static final String crossSecOfSoc = "CROSS_SEC_OF_SOC";
    
    public static final String groupName = "RECEN_LIST";
    
    @Override
    public String getGroupName() {
        return RecenGroup.groupName;
    }

    protected static final List<String> varnameOrder = 
            RecenGroup.initVarnameorder();
    
    private static List<String> initVarnameorder() {
        List<String> retval = new ArrayList<String> (GcalGoniostatGroup.varnameOrder);
        retval.add(RecenGroup.home);
        retval.add(RecenGroup.crossSecOfSoc);
        return Collections.unmodifiableList(retval);
    }

    private static final VarnameComparator varnameComparator =
            new VarnameComparator(RecenGroup.varnameOrder);
    
}
