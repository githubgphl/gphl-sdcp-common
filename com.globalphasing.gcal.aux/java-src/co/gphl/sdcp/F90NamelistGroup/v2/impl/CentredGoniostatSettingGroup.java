/*******************************************************************************
 * Copyright (c) 2014, 2015 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/


/*
 * Namelist group type corresponding to CENTRED_GONIOSTAT_SETTING_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import co.gphl.common.namelist.VarnameComparator;
import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public final class CentredGoniostatSettingGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public CentredGoniostatSettingGroup(Integer lineNo) {
       super(CentredGoniostatSettingGroup.varnameComparator, CentredGoniostatSettingGroup.charVarnames, lineNo);
    }
   
    public static final String groupName = "CENTRED_GONIOSTAT_SETTING_LIST";
    
    @Override
    public String getGroupName() {
        return CentredGoniostatSettingGroup.groupName;
    }

    public static final String id                 = "ID";
    public static final String goniostatSettingId = "GONIOSTAT_SETTING_ID";
    public static final String trans1             = "TRANS_1";
    public static final String trans2             = "TRANS_2";
    public static final String trans3             = "TRANS_3";

    private static final VarnameComparator varnameComparator = 
        new VarnameComparator(new String [] {
                CentredGoniostatSettingGroup.id,
                CentredGoniostatSettingGroup.goniostatSettingId,
                CentredGoniostatSettingGroup.trans1,
                CentredGoniostatSettingGroup.trans2,
                CentredGoniostatSettingGroup.trans3
        } );

    private static final Set<String> charVarnames = Collections.unmodifiableSet(
        new HashSet<String>(
                Arrays.asList( new String[] {
                        CentredGoniostatSettingGroup.id,
                        CentredGoniostatSettingGroup.goniostatSettingId
                } ) ) );
    
    
    // Added by hand
    public static final String fTrans = "TRANS_%c";
    public static String settingName(int i) {
        return String.format(fTrans, GcalInstrumentGroup.transAxisOrder.get(i));
    }
    
}
   