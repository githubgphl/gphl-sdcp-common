package co.gphl.common.properties;
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


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * Interface that encapsulates the definition and value of a Java property.
 * This interface is intended to be implemented by enums.
 * 
 * @author pkeller
 *
 */
public interface PropertyDefinition {

    default String getPropName() {
        return State.getState(this).propName;
    }
    
    default String getDefaultValue() {
        return State.getState(this).defaultValue;
    }
    
    default int getMinArgs() {
        return State.getState(this).minArgs;
    }
    
    default int getMaxArgs() {
        return State.getState(this).maxArgs;
    }
    
    default String getDescription() {
        return State.getState(this).description;
    }
    
    
    /**
     * Returns the value that has been assigned to this property. If no value has been set
     * then the default value, if any, is returned.
     * 
     * @return value assigned to the property
     */
    default String getPropValue() {
        String val = State.properties == null ?  
                System.getProperty(getPropName(), getDefaultValue()) :
                    State.properties.getProperty(getPropName(), getDefaultValue());
        return val == null ? null : val.trim();
    }
    
    /**
     * Converts the value returned by {@link #getPropValue()} to {@code true} or {@code false}.
     * The rules of the conversion are as follows:
     * 
     * <list>
     * <li>{@code null} (i.e. property not specified, and has no default value or default is {@code null})
     * is converted to {@code false}</li>
     * <li>An empty string (i.e. property specified with no argument or default is {@code ""})
     * is converted to {@code true}</li>
     * <li>A string starting with {@code T} or {@code Y} (case insensitive)
     * is converted to {@code true}</li>
     * <li>A string starting with {@code F} or {@code N} (case insensitive)
     * is converted to {@code false}</li>
     * </list>
     * 
     * @return value assigned to the property as {@code true} or {@code false}
     * 
     * @throws UnsupportedOperationException if a value is assigned that cannot be converted
     * to a boolean according to the above rules.
     */
    default boolean getPropBoolValue() {
        String val = this.getPropValue();
        boolean retval = false;
        if ( val != null ) {
            if ( val.isEmpty() || "YyTt".indexOf(val.charAt(0)) > -1 )
                retval = true;
            else if ( "NnFf".indexOf(val.charAt(0) ) < 0 )
                throw new UnsupportedOperationException("Conversion of String value '"
                        + val + "' to boolean not supported");
        }
        return retval;
    }
    
    default boolean validateArgs() {
        
        String val = Objects.requireNonNull(this.getPropValue(),
                "BUG: this method should not be called for a property that hasn't been set");
        
        String[] args = val.split("\\s+");
        
        return args.length >= this.getMinArgs() && args.length <= this.getMaxArgs();
    }
    
    static class State {
        
        private static final Map<PropertyDefinition, State> map = new HashMap<>();

        private static Properties properties = null;
        
        protected final String propName;
        protected final String description;
        protected final String defaultValue;
        protected final int minArgs, maxArgs;
        protected final PropertyDefinition property;

        
        /**
         * Specify the properties which will be used by future calls to
         * {@link PropertyDefinition#getPropValue()}.
         * 
         * @param properties Properties to query, or {@code null}
         * to query {@link System#getProperties()}.
         */
        public static void setProperties(Properties properties) {
            State.properties = properties;
        }

        public static void register(PropertyDefinition property, String namespace,
                String propName, String defaultValue, int nArgs,
                String description) {
            
            register(property, namespace, propName, defaultValue,
                    nArgs, nArgs, description);
            
        }
        
        public static void register(PropertyDefinition property, String namespace,
                String propName, String defaultValue, int minArgs, int maxArgs,
                String description) {
        
            register(property, new State(property, namespace, propName,
                    defaultValue, minArgs, maxArgs, description ) );
        }
        
        protected static void register(PropertyDefinition property, 
                State state) {
            map.put(property, state);
        }
        
        /* This method of keeping track of instances and their state allows us to:
         * 
         * (1) hide every method of the State class except the method to register an instance
         * (2) avoid declaring a public getState() method in the outer interface definition
         * 
         * N.B. We don't store the value in the State instance, because 'this.getPropValue()'
         * won't work until after the new instance has been registered, and we want to use the
         * default implementation from PropertyDefinition for this.
         * 
         */
        protected State ( PropertyDefinition property, String namespace,
                String propName, String defaultValue, int minArgs, int maxArgs,
                String description ) {
            
            String myNs = Objects.requireNonNull(namespace, "BUG: Property namespace cannot be null");
            if ( myNs.isEmpty() )
                throw new IllegalArgumentException("BUG: Property namespace cannot be an empty string");
            
            if ( ! myNs.endsWith(".") )
                myNs += '.';

            String myName = Objects.requireNonNull(propName, "BUG: a property name cannot be null");
            if ( myName.isEmpty() )
                throw new IllegalArgumentException("BUG: a property name cannot be an empty string");

            this.propName = myNs + myName;            
            this.defaultValue = defaultValue;
            
            if ( minArgs < 0 || maxArgs < 0 || maxArgs < minArgs ) 
                throw new IllegalArgumentException(
                        String.format("BUG: bad values for minArgs/MaxArgs: %d/%d",
                                minArgs, maxArgs) );
            
            this.minArgs = minArgs;
            this.maxArgs = maxArgs;
            this.description = description;
            this.property = property;
            
        }
        
        protected static State getState(PropertyDefinition key) {
            return State.map.get(key);
        }
        
    }
    
}
