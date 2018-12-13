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


import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Interface that encapsulates the definition and value of a Java property.
 * This interface is intended to be implemented by enums.
 * 
 * @author pkeller
 *
 */
public interface PropertyDefinition {

    default String getPropName() {
        State state = State.getState(this);
        return state.namespace + state.basename;
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
    
    default boolean isValid(boolean required) {
        return State.getState(this).isValid(required);
    }

    /**
     * Returns the value that has been assigned to this property. If no value has been set
     * then the default value, if any, is returned.
     * 
     * @return value assigned to the property (without leading/trailing whitespace)
     */
    default String getPropValue() {
        return State.getState(this).getPropValue();
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
        return asBoolean(this.getPropValue());
   }
    
    default Double getPropDoubleValue() {
        String val = this.getPropValue();
        if ( val != null && !val.isEmpty() )
            return Double.parseDouble(val);
        else
            return null;
    }
    
    default boolean validateArgs() {
        
        String val = Objects.requireNonNull(this.getPropValue(),
                "BUG: this method should not be called for a property that hasn't been set");
        
        String[] args = val.split("\\s+");
        
        return args.length >= this.getMinArgs() && args.length <= this.getMaxArgs();
    }
    
    static boolean asBoolean(String val) {

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

    static boolean checkDir(String val) {
        Path path = Paths.get(val);

        try {
            if ( !Files.exists(path) )
                throw new NoSuchFileException(val);
            if ( !Files.isDirectory(path) )
                throw new NotDirectoryException(val);
            if ( !Files.isReadable(path) )
                throw new AccessDeniedException(val);
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
        
        return true;
    }
    
    static class State {
        
        static {
            // We could do something more sophisticated by subclassing SimpleFormatter
            // (e.g. by including the thread name and compressing the qualified classname
            // like logback does) but this should be OK for now.
            if ( System.getProperty("java.util.logging.SimpleFormatter.format") == null  )
                System.setProperty("java.util.logging.SimpleFormatter.format",
                        "%1$tH:%<tM:%<tS:%<tL %4$s %3$s %5$s%6$s%n");
            }
        
        private static Logger logger = Logger.getLogger(State.class.getEnclosingClass().getName());
        
        private static final Map<PropertyDefinition, State> map = new HashMap<>();
        // Map internal namespace ids to their true prefixes
        private static final Map<String, String> nsPrefixMap = new HashMap<>();

        private static Properties properties = null;
        
        protected final String basename;
        protected final String namespace;
        protected final String description;
        protected final String defaultValue;
        protected final int minArgs, maxArgs;
        protected final PropertyDefinition property;

        /* Validator function for the assigned value of the property.
         * If specified, the validation attempts to call validator.apply on the
         * value. The validation passes if no exception is thrown.
         * 
         * It would be better if it returned true/false rather than throwing
         * an exception: maybe in a future version.
         */
        private final Function<String, ?> validator;
        private Boolean valid = null;
        
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
                String description, Function<String, ?> validator) {
            
            register(property, namespace, propName, defaultValue,
                    nArgs, nArgs, description, validator);
            
        }
        
        public static void register(PropertyDefinition property, String namespace,
                String propName, String defaultValue, int minArgs, int maxArgs,
                String description, Function<String, ?> validator) {
        
            register(property, new State(property, namespace, propName,
                    defaultValue, minArgs, maxArgs, description, validator ) );
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
                String description, Function<String, ?> validator ) {
            
            String myNs = Objects.requireNonNull(namespace, "BUG: Property namespace cannot be null");
            if ( myNs.isEmpty() )
                throw new IllegalArgumentException("BUG: Property namespace cannot be an empty string");
            
            if ( ! myNs.endsWith(".") )
                myNs += '.';

            String myName = Objects.requireNonNull(propName, "BUG: a property name cannot be null");
            if ( myName.isEmpty() )
                throw new IllegalArgumentException("BUG: a property name cannot be an empty string");

            this.namespace = myNs;
            this.basename = myName;
            this.defaultValue = defaultValue;
            
            if ( minArgs < 0 || maxArgs < 0 || maxArgs < minArgs ) 
                throw new IllegalArgumentException(
                        String.format("BUG: bad values for minArgs/MaxArgs: %d/%d",
                                minArgs, maxArgs) );
            
            this.minArgs = minArgs;
            this.maxArgs = maxArgs;
            this.description = description;
            this.property = property;
            this.validator = validator;
        }
        
        protected static void registerNsPrefix(String id, String prefix) {
            String oldPrefix = State.nsPrefixMap.putIfAbsent(id, prefix);
            
            if ( oldPrefix != null ) {
                if ( prefix.equals(oldPrefix) )
                    logger.warning( "Multiple attempts to map the property namespace prefix '" + prefix +
                            "': check what is going on");
                else
                    throw new IllegalStateException("BUG: attempt to re-map a property namespace id from '" +
                            oldPrefix + "' to '" + prefix + "'");
            }
            
        }
        
        protected static State getState(PropertyDefinition key) {
            return State.map.get(key);
        }
        
        
        /**
         * Query statically-set properties with an arbitrary property name.
         * 
         * @param key property name
         * @return property value
         * 
         * @deprecated This is not a good approach: we need to find a better way.
         * 
         */
        @Deprecated protected static String queryProperties(String key) {
            return State.properties == null ? System.getProperty(key) : State.properties.getProperty(key);
        }
        
        protected String getPropName() {
            String nsPrefix = State.nsPrefixMap.get(this.namespace);
            return ( nsPrefix != null ? nsPrefix : this.namespace ) + 
                    this.basename;
        }
        
        protected String getUnvalidatedPropValue() {
            String val =  State.properties == null ?
                    System.getProperty(this.getPropName(), this.defaultValue) :
                        State.properties.getProperty(this.getPropName(), this.defaultValue);
            return val == null ? null : val.trim();
        }
        
        private String getPropValue() {
            
            if ( ! this.isValid(false) ) {
                logger.severe(String.format("Property %s has failed validation\n" +
                        "See previous error message(s) from this logger", this.getPropName()));
                logger.info("Please consider calling isValid() sooner!");
                throw new RuntimeException("Invalid value for property " + this.getPropName());
            }
            
            return this.getUnvalidatedPropValue();
        }
        
        protected boolean isValid(boolean required) {
            if ( this.valid == null )  {
                this.valid = true;
                
                String val = this.getUnvalidatedPropValue();
                List<String> args = val == null || val.isEmpty() ? Collections.emptyList() : Arrays.asList(val.split("\\s+"));
                int nArgs = args.size();
                
                if ( required ) {
                    if ( this.maxArgs == 0 )
                        throw new RuntimeException("BUG: called isValid(true) for " + this.getPropName() + 
                                " which has maxArgs==0. This makes no sense!");
                    else if ( val == null )
                        logger.severe(String.format("Property %s has not been assigned, but it "
                                + "is a required property for this application", this.getPropName()));
                }
                
                // For a required property, the number of arguments must always be within the specified range
                // For an optional property, the number of arguments must be within the range if any have been assigned
                if ( (required || val != null) && (nArgs < this.minArgs || nArgs > this.maxArgs) )
                    logger.severe(String.format("Property %s has %d arguments: should have a minimum of "
                            + "%d and a maximum of %d", this.getPropName(), nArgs, this.minArgs, this.maxArgs));
                
                if ( this.validator != null ) {

                    for ( String arg: args )
                    
                        try {
                            this.validator.apply(arg);
                        }
                        catch (Throwable t) {
                            this.valid = false;
                            
                            logger.logp(Level.SEVERE, this.getClass().getName(), "isValid",
                                    String.format("Value '%s' assigned to property %s has "
                                            + "failed validation", arg, this.getPropName()),
                                    t);
                        }
                }
            }

            return this.valid;
        }


    }
    
}
