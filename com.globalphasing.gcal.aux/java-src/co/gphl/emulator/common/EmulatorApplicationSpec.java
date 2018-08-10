/**
 * Copyright © 2018 by Global Phasing Ltd. All rights reserved
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
package co.gphl.emulator.common;

import co.gphl.common.properties.ApplicationSpec;

/**
 * @author pkeller
 *
 */
public enum EmulatorApplicationSpec implements ApplicationSpec {

    SIMCAL,
    RECEN;
    
    EmulatorApplicationSpec() {
        
        String basename = this.name().toLowerCase();
        
        ApplicationSpec.State.register(this, EmulatorProperty.NAMESPACE, 
                basename, basename, EmulatorProperty.BLBINDIR);
        
    }

    
}
