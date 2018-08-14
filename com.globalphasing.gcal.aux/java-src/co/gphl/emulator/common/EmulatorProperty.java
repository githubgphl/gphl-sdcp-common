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
import co.gphl.common.properties.PropertyDefinition;

/**
 * @author pkeller
 *
 */
public enum EmulatorProperty implements PropertyDefinition {
    
    BDGHOME(ApplicationSpec.BDGSUFFIX, 1, null, "Directory containing GPhL '.licence' file"),
    BLBINDIR(ApplicationSpec.BINSUFFIX, 1, null, "Absolute path to directory containing emulator-side applications");

    public static final String NAMESPACE = "co.gphl.beamline";

    EmulatorProperty(String name, int nArgs, String defaultValue, String description) {
        PropertyDefinition.State.register(this, EmulatorProperty.NAMESPACE, name, defaultValue, nArgs, description, null);
    }
    
}
