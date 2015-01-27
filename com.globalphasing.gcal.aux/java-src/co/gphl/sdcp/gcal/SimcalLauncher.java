/*
 * Copyright © 2012 by Global Phasing Ltd. All rights reserved
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

package co.gphl.sdcp.gcal;

import java.io.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import co.gphl.common.io.logging.LoggerSetup;

/**
 * @author pkeller
 *
 */
public class SimcalLauncher extends GcalLauncher implements Serializable {

    static private Logger logger =
            LoggerSetup.getLogger(SimcalLauncher.class.getSimpleName(), Level.INFO);
    
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
    public static final int DEFNRAYS = 1000;
    public static final double DEFBKGND = 10.0;
    

    private static Map<String, String> propNames = null;
    private File hkli;
    
    public SimcalLauncher(String propNameNamespace) {
        this(propNameNamespace, null);
    }
    
    public SimcalLauncher(String propNameNamespace, Properties properties) {
        super(SimcalLauncher.logger, "simcal", propNameNamespace, properties);
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
        
}
