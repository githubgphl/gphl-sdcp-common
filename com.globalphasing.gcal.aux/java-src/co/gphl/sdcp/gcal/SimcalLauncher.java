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
    
    private static Map<String, String> propNames = null;
    private File hkli;
    private Double hklscale;
    
    public SimcalLauncher(String propNameNamespace) {
        this(propNameNamespace, null);
    }
    
    public SimcalLauncher(String propNameNamespace, Properties properties) {
        super(SimcalLauncher.logger, "simcal", propNameNamespace, properties);
    }
    
    public void setHkli(File hkli, Double hklscale) {
        this.hkli = hkli;
        this.hklscale = hklscale;
    }
    
    @Override
    protected void _pre_launch(File wdir, File input) {
        super._pre_launch(wdir, input);
        if ( this.hkli != null )
            this.args.put("--hkl", this.hkli.toString());
        if ( this.hklscale != null )
            this.args.put("--hkl-scale", Double.toString(this.hklscale));
    }
    
    @Override
    protected Map<String, String> getPropNames() {
        
        if ( SimcalLauncher.propNames == null ) {
            SimcalLauncher.propNames = new HashMap<String, String>(super.getPropNames());
            propNames.put(SimcalLauncher.MEMPOOL, "--memory-pool");
        }
        
        return Collections.unmodifiableMap(SimcalLauncher.propNames);
        
    }
        
}
