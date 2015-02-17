

/*
 * Namelist group type corresponding to CENTRED_GONIOSTAT_SETTING_LIST
 */

package co.gphl.sdcp.F90NamelistGroup.v2.impl;

import java.util.Arrays;

import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;

@SuppressWarnings("serial")
public final class CentredGoniostatSettingGroup
    extends F90NamelistGroupImpl implements F90NamelistGroup {
   
    public CentredGoniostatSettingGroup(Integer lineNo) {
       super(CentredGoniostatSettingGroup.varnameComparator(), CentredGoniostatSettingGroup.groupName, lineNo);
    }
   
    public static final String groupName = "CENTRED_GONIOSTAT_SETTING_LIST";
    
    @Override
    public String getGroupName() {
        return CentredGoniostatSettingGroup.groupName;
    }

    private static CentredGoniostatSettingGroupComparator varnameComparator = null;

    private static CentredGoniostatSettingGroupComparator varnameComparator() {
        if ( CentredGoniostatSettingGroup.varnameComparator == null )
            CentredGoniostatSettingGroup.varnameComparator = new CentredGoniostatSettingGroupComparator();
        return CentredGoniostatSettingGroup.varnameComparator;
    }

    public static final String id = "ID";
    public static final String goniostatSettingId = "GONIOSTAT_SETTING_ID";
    public static final String trans1 = "TRANS_1";
    public static final String trans2 = "TRANS_2";
    public static final String trans3 = "TRANS_3";
    
    // Added by hand
    public static final String fTrans = "TRANS_%c";
    public static String settingName(int i) {
        return String.format(fTrans, GcalInstrumentGroup.transAxisOrder.get(i));
    }
    
}

@SuppressWarnings("serial")
final class CentredGoniostatSettingGroupComparator
    extends co.gphl.common.namelist.AbstractKeyList {
    
    CentredGoniostatSettingGroupComparator() {
        this.keyOrder = Arrays.asList(new String [] {
            CentredGoniostatSettingGroup.id,
            CentredGoniostatSettingGroup.goniostatSettingId,
            CentredGoniostatSettingGroup.trans1,
            CentredGoniostatSettingGroup.trans2,
            CentredGoniostatSettingGroup.trans3
           } );

        this.valueTypeMap = new java.util.HashMap<String, co.gphl.common.namelist.ValueType>();
        this.valueTypeMap.put(CentredGoniostatSettingGroup.id, co.gphl.common.namelist.ValueType.CHAR);
        this.valueTypeMap.put(CentredGoniostatSettingGroup.goniostatSettingId, co.gphl.common.namelist.ValueType.CHAR);
        
        }
        
    @Override
    public String getListName() {
        throw new RuntimeException("Should not use this method in v2 namelist stuff!");
    }  
}
    