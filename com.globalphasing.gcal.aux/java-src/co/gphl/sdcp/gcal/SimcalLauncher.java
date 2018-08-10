/*******************************************************************************
 * Copyright (c) 2014, 2017 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/

package co.gphl.sdcp.gcal;

import java.io.File;
import java.io.Serializable;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.gphl.common.properties.PropertyDefinition;
import co.gphl.emulator.common.EmulatorApplicationSpec;

/**
 * @author pkeller
 *
 */
public class SimcalLauncher extends GcalLauncher implements Serializable {

    static private Logger logger =
            LoggerFactory.getLogger(SimcalLauncher.class);

    // FIXME! Consolidate all occurences of the "simcal" literal
    // into one place.
    public static final String appName = "simcal";
    
    /**
     * Property used to enable and set {@code --memory-pool} command-line option
     */
    public static final String MEMPOOL = "memory_pool";
    
    /**
     * Property used to enable and set {@code --hkl-scale} command-line option
     */
    public static final String HKLSCALE = "hklscale";
    
    // Define these property names here to avoid scattering them around too much,
    // but client classes must use them because they need to be set in the namelist
    // input before we get here.
    public static final String NRAYS = "nrays";
    public static final String BKGND = "background";
    public static final String SIMMODE = "sim_mode";
    public static final String BCGMODE = "bcg_mode";
    public static final int DEFNRAYS = 1000;
    public static final double DEFBKGND = 10.0;
    public static final int DEFSIMMODE = 1;
    public static final int DEFBCGMODE = -1;

    private static Map<String, String> propNames = null;
    private File hkli;
    
    public SimcalLauncher(String propNameNamespace, Properties properties,
            Writer stdoutWriter, Writer stderrWriter, boolean outputToFile, boolean redirectErrorStream) {
        super(SimcalLauncher.logger, EmulatorApplicationSpec.SIMCAL, propNameNamespace, properties,
                stdoutWriter, stderrWriter, outputToFile, redirectErrorStream);
        // FIXME! SDCP-226 OK, sort of, to do this here because simcal is only used
        // in an emulation context (c.f. hard-coded spec param), but we should really
        // have a kind of save/set or push/pop arrangement so we can restore the
        // previous static state. We should be doing this automatically in
        // super._pre/post_launch
        PropertyDefinition.State.setProperties(properties);
        
    }
    
    public void setHkli(File hkli) {
        this.hkli = hkli;
    }
    
    @Override
    protected void _pre_launch(File wdir, File input) {
        super._pre_launch(wdir, input);
        if ( this.hkli != null )
            this.args.put("--hkl", this.hkli.toString());
    }
    
    @Override
    protected Map<String, String> getPropNames() {
        
        if ( SimcalLauncher.propNames == null ) {
            SimcalLauncher.propNames = new HashMap<String, String>(super.getPropNames());
            propNames.put(SimcalLauncher.MEMPOOL, "--memory-pool");
            propNames.put(SimcalLauncher.HKLSCALE, "--hkl-scale");
        }
        
        return Collections.unmodifiableMap(SimcalLauncher.propNames);
        
    }

    /* (non-Javadoc)
     * @see co.gphl.sdcp.gcal.GcalLauncher#useOutputOpt()
     */
    @Override
    protected boolean useOutputOpt() {
        return true;
    }
        
}
