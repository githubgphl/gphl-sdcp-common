

package co.gphl.sdcp.F90Namelist.v2;

import java.io.Serializable;
import co.gphl.common.namelist.F90NamelistGroupFactory;
import co.gphl.common.namelist.impl.F90NamelistGroupFactoryImpl;
import co.gphl.sdcp.F90NamelistGroup.v2.impl.*;

public final class GcalAuxGroupFactory
    extends F90NamelistGroupFactoryImpl
    // FIXME! Won't need to be serializable for ever.....
    implements F90NamelistGroupFactory, Serializable {

    private static GcalAuxGroupFactory instance = null;
    
    private GcalAuxGroupFactory() {
        super(null);
                
        this.groupMap.put("BEAMSTOP_SETTING_LIST",          BeamstopSettingGroup.class);
        this.groupMap.put("CENTRED_GONIOSTAT_SETTING_LIST", CentredGoniostatSettingGroup.class);
        this.groupMap.put("DETECTOR_LIST",                  DetectorGroup.class);
        this.groupMap.put("DETECTOR_SETTING_LIST",          DetectorSettingGroup.class);
        this.groupMap.put("ERROR_MODEL_LIST",               ErrorModelGroup.class);
        this.groupMap.put("GONIOSTAT_SETTING_LIST",         GoniostatSettingGroup.class);
        this.groupMap.put("GONIOSTAT_VOLUME_LIST",          GoniostatVolumeGroup.class);
        this.groupMap.put("LOOP_COUNT_LIST",                LoopCountGroup.class);
        this.groupMap.put("SEGMENT_LIST",                   SegmentGroup.class);
        this.groupMap.put("SIMCAL_BEAM_SETTING_LIST",       SimcalBeamSettingGroup.class);
        this.groupMap.put("SIMCAL_CRYSTAL_LIST",            SimcalCrystalGroup.class);
        this.groupMap.put("SIMCAL_DEF_SETTING_LIST",        SimcalDefSettingGroup.class);
        this.groupMap.put("SIMCAL_INSTRUMENT_LIST",         SimcalInstrumentGroup.class);
        this.groupMap.put("SIMCAL_OPTIONS_LIST",            SimcalOptionsGroup.class);
        this.groupMap.put("SIMCAL_SWEEP_LIST",              SimcalSweepGroup.class);

    }
    
    public static GcalAuxGroupFactory factory() {
        if ( GcalAuxGroupFactory.instance == null )
            GcalAuxGroupFactory.instance = new GcalAuxGroupFactory();
        return GcalAuxGroupFactory.instance;
    }
    

}
