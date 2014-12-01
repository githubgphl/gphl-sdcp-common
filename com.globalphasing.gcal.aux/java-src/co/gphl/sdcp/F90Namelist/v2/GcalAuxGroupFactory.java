

package co.gphl.sdcp.F90Namelist.v2;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.HashMap;

import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.F90NamelistGroupFactory;
import co.gphl.common.namelist.impl.F90NamelistGroupImpl;
import co.gphl.sdcp.F90NamelistGroup.v2.impl.*;

public final class GcalAuxGroupFactory implements F90NamelistGroupFactory {

    private static GcalAuxGroupFactory instance = null;
    
    private static Class<?>[] integerArray = { Integer.class };
    
    private Map<String, Class<? extends F90NamelistGroupImpl>> groupMap = null;
    
    private GcalAuxGroupFactory() {
        
        this.groupMap = new HashMap<String, Class<? extends F90NamelistGroupImpl>>();
        
        this.groupMap.put("BEAMSTOP_SETTING_LIST", BeamstopSettingGroup.class);
        this.groupMap.put("CENTRED_GONIOSTAT_SETTING_LIST", CentredGoniostatSettingGroup.class);
        this.groupMap.put("DETECTOR_LIST", DetectorGroup.class);
        this.groupMap.put("DETECTOR_SETTING_LIST", DetectorSettingGroup.class);
        this.groupMap.put("ERROR_MODEL_LIST", ErrorModelGroup.class);
        this.groupMap.put("GONIOSTAT_SETTING_LIST", GoniostatSettingGroup.class);
        this.groupMap.put("GONIOSTAT_VOLUME_LIST", GoniostatVolumeGroup.class);
        this.groupMap.put("LOOP_COUNT_LIST", LoopCountGroup.class);
        this.groupMap.put("SEGMENT_LIST", SegmentGroup.class);
        this.groupMap.put("SIMCAL_BEAM_SETTING_LIST", SimcalBeamSettingGroup.class);
        this.groupMap.put("SIMCAL_CRYSTAL_LIST", SimcalCrystalGroup.class);
        this.groupMap.put("SIMCAL_DEF_SETTING_LIST", SimcalDefSettingGroup.class);
        this.groupMap.put("SIMCAL_INSTRUMENT_LIST", SimcalInstrumentGroup.class);
        this.groupMap.put("SIMCAL_OPTIONS_LIST", SimcalOptionsGroup.class);
        this.groupMap.put("SIMCAL_SWEEP_LIST", SimcalSweepGroup.class);


    }
    
    public static GcalAuxGroupFactory factory() {
        if ( GcalAuxGroupFactory.instance == null )
            GcalAuxGroupFactory.instance = new GcalAuxGroupFactory();
        return GcalAuxGroupFactory.instance;
    }
    
    @Override
    public F90NamelistGroup newInstance(String groupName, boolean throwException, Integer lineNo ) {
        
        groupName = groupName.toUpperCase();
        
        if ( this.groupMap.containsKey(groupName) ) {
            Class<? extends F90NamelistGroup> type = this.groupMap.get(groupName);
            try {
                if ( lineNo == null )
                    return type.newInstance();
                else {
                    Constructor<? extends F90NamelistGroup> cons = type.getConstructor(GcalAuxGroupFactory.integerArray);
                    return cons.newInstance( new Object[] { lineNo } );
                }
            } catch (InstantiationException e) {
                throw new RuntimeException("BUG: " + groupName + " has been mapped to the non-instantiable class " +
                            type.getName(), e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("BUG: the class " + type.getName() +
                            " or its constructor is not accessible", e );
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("BUG: Could not find constructor for " + type.getName() +
                        " taking a single Integer argument", e);
            } catch (SecurityException e) {
                throw new RuntimeException("Security manager problem with " + type.getName(), e );
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("BUG: constructor for " + type.getName() +
                        " was called with incorrect/invalid arguments", e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Exception thrown by constructor for " + type.getName(), e);
            }
        }
        
        if ( throwException )
            throw new IllegalArgumentException("Namelist group name " + groupName
                    + " has no corresponding Java type");
        else
            return null;
    }

}
