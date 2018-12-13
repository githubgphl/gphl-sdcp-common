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
package co.gphl.common.properties;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

/**
 * Enumeration of a set of Java property names that have their namespace (prefix)
 * set dynamically by the application that makes use of them. Setting the prefix happens
 * after the static initialisation phase of the enum. The only uses of the
 * namespace prefix are to construct the complete property name for:
 * 
 * <ul><li>querying a {@link java.util.Properties} instance for the property value, and:</li>
 * <li>logging and error reporting</li><ul>
 * 
 * @author pkeller
 *
 */
public enum GeneralProperty implements PropertyDefinition {

    DEVMODE ("devMode", 0, 1, "F", "Enable extra developer-mode properties",
            PropertyDefinition::asBoolean)
    ;
    
    @Override
    public String getPropName() {
        // Use post-initialisation prefix rather than the one that was stored internally
        // at static initialisation time
        return Namespace.getPrefix() + State.getState(this).basename;
    }

    
    // We use this for diagnostics;
    private static final String CLASSNAME = GeneralProperty.class.getName();
    
    GeneralProperty(String propName, int minArgs, int maxArgs,
            String defaultValue, String description, Function<String, ?> validator ) {
        
        PropertyDefinition.State.register(this, Namespace.internalId, propName, defaultValue,
                minArgs, maxArgs, description, validator);
        
    }
    
    public static class Namespace {
        // We use a random string as a placeholder for the namespace.
        // In the future, we might map between UUIDs and multiple namespace strings
        // but for now we just have one.
        private static final String internalId = UUID.randomUUID().toString() + ".";
        
        private static String prefix = null;
        
        
        /**
         * Set the namespace (or prefix) that is used for the names of
         * property definitions in this class.
         * If the provided namespace does not end in a {@code '.'} character,
         * one will be appended.
         * 
         * @param namespace namespace for property names defined in this class
         * 
         * @throws IllegalStateException if an application calls this method more than once
         * @throws NullPointerException if {@code namespace == null}
         * @throws IllegalArgumentException if {@link String#isEmpty() namespace.isEmpty()}
         * 
         */
        public static void setNamespace(String namespace) {
            
            if ( prefix != null )
                throw new IllegalStateException("The namespace for properties defined in " + CLASSNAME + 
                        " has already been set to '" + prefix + "': it may not be changed");
            
            prefix = Objects.requireNonNull(namespace,
                    "Null namespace for " + CLASSNAME + " is not allowed");
            
            if ( prefix.isEmpty() ) {
                prefix = null;
                throw new IllegalArgumentException("An empty namespace for " + CLASSNAME + 
                        " is not allowed");
            }
            
            if ( ! prefix.endsWith(".") )
                prefix += ".";
            
            PropertyDefinition.State.registerNsPrefix(internalId, prefix);
            
        }
        
        public static String getPrefix() {
            return prefix;
        }
    }
    
    

}
