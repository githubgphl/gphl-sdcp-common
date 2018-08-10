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

import co.gphl.common.properties.PropertyDefinition;

/**
 * @author pkeller
 *
 */
public enum EmulatorProperty implements PropertyDefinition {
    BLBINDIR("bin", null, 1, "Absolute path to directory containing emulator-side applications");

    public static final String NAMESPACE = "co.gphl.beamline";

    EmulatorProperty(String name, String defaultValue, int nArgs, String description) {
        PropertyDefinition.State.register(this, EmulatorProperty.NAMESPACE, name, defaultValue, nArgs, description);
    }
    
}
