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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * @author pkeller
 *
 */
public interface ApplicationSpec extends PropertyDefinition {

    
    /**
     * Suffix that is added to the property basename to form the name of
     * the property that specifies either:
     * 
     * <ul><li>the directory containing applications' executable files, or:</li>
     * <li>the actual executable for a particular application</li></ul>
     */
    public static String BINSUFFIX = "bin";
    
    
    /**
     * Suffix that is added to the property basename to form the name of
     * the property that specifies either:
     * 
     * <ul><li>the directory containing the {@code .licence} file for GPhL applications, or:</li>
     * <li>the directory containing a {@code .licence} file for a specific GPhL application</li></ul>
     */
    public static String BDGSUFFIX = "bdg_licence_dir";
    
    @Override
    default int getMinArgs() {
        return 0;
    }
    
    @Override
    default int getMaxArgs() {
        return 1;
    }
 
    /**
     * Tests for validity of the path to the application. To be
     * valid, if the application is {@link #isEnabled() enabled}
     * the path must:
     * 
     * <ul><li>exist on the filesystem</li>
     * <li>be absolute</li>
     * <li>be executable</li></ul>
     * 
     * Note that this method will return true if the application is
     * not enabled: it is intended to be used in early checks on the
     * property assignments, and from that point of view an optional
     * application that has been disabled is always valid.
     * 
     * @return {@code true} if the specification of the application is valid.
     */
    default boolean isValid() {
        return State.getState(this).isValid();
    }
    
    /**
     * Tests whether an optional application has been enabled for use.
     * By default, applications are enabled (because they have a default
     * value that is not allowed to be an empty string).
     * 
     * @return {@code false} if an empty string has been explicitly assigned,
     * {@code true} otherwise. 
     */
    default boolean isEnabled() {
        return !this.getPropValue().isEmpty();
    }
    
    default Path getPath() {
        return State.getState(this).getPath();
    }
    
    /**
     * Returns licencing directory, if one has been set.
     * 
     * @return application-specific licencing directory if set, otherwise
     * global licencing directory or {@code null} if none has been set.
     */
    default Path getLicencingDir() {
        return State.getState(this).getLicencingDir();
    }
    
    /**
     * Get the basename of the name of the property used to define the location or name
     * of the application executable file. This is used as a "logical" application name,
     * and is incorporated into the names of various directories and files that are
     * handled by the workflow.
     * 
     * @return basename of the property name
     */
    default String getBasename() {
        return State.getState(this).basename;
    }
    
    static class State extends PropertyDefinition.State {
        
        private static Logger logger = Logger.getLogger(State.class.getCanonicalName());
        
        
        private final PropertyDefinition binDirProperty;
        private final PropertyDefinition defaultLicDirProperty;
        private final String basename;
        
        private Boolean valid = null;
        private Path path = null;
        private Path licPath = null;

        public static void register(ApplicationSpec spec, String namespace, String basename,
                String defaultValue, PropertyDefinition binDirProperty, PropertyDefinition licDirProperty) {
            
            // We may reconsider this in the future if an application is allowed to be
            // disabled by default.
            if ( defaultValue == null || defaultValue.isEmpty() )
                throw new IllegalArgumentException("BUG: the default value for " + spec.getClass().getName() + 
                        " is null or an empty string. This is not allowed");
            
            PropertyDefinition.State.register(spec, new State(spec, namespace, 
                    basename, defaultValue, binDirProperty, licDirProperty));
            
        }
        
        private State(ApplicationSpec spec, String namespace, String basename, String defaultValue,
                PropertyDefinition binDirProperty, PropertyDefinition licDirProperty) {
            
            // We don't need to set the description in the state here.
            super(spec, namespace, basename + "." + BINSUFFIX, 
                    Objects.requireNonNull(defaultValue, "BUG: A WorkflowApplicationSpec must have a default value set"),
                    0, 1, "");
            
            this.basename = basename;
            
            if ( this.defaultValue.isEmpty() )
                throw new IllegalArgumentException("BUG: A WorkflowApplicationSpec cannot have an empty default value");
            
            this.binDirProperty =
                    Objects.requireNonNull(binDirProperty, "BUG: A property for the parent directory of an application spec must be specified");

            this.defaultLicDirProperty = licDirProperty;
            
        };

        protected static State getState(ApplicationSpec key) {
            // I don't really like this, but being typesafe with generics
            // in a static context doesn't seem to be possible.
            return (State) PropertyDefinition.State.getState(key);
        }
        
        private Path getLicencingDir() {

            // Side-effect here: not brilliant, but this facility is really
            // intended for development use, not deployment/production.
            this.getPath();
            
            return this.licPath;
        }
        
        private Path getPath() {
            
            if ( ! this.isValid() ) {
                logger.severe(String.format("Property %s has failed validation: "
                        + "cannot use application.\n"
                        + "See previous error message(s) from this logger", this.propName) );
                logger.info("Please consider calling isValid() sooner!"); 
                throw new RuntimeException("Invalid specification for application " + this.basename);
            }
            
            return this.path;
        }
        
        private boolean isValid() {
            if ( this.valid == null )
                this.setup();
            
            if ( this.valid == null )
                throw new IllegalStateException("BUG: setup() has been called, but this.valid is still null");
            
            return this.valid;
        }

        
        private void setup() {

            // We set this.path from the start, so that if we get here from getPath() we can 
            // report as much of the path as was set up if validation fails.
            String value = this.property.getPropValue();
            
            // If the property has been defined with no argument (something like
            // -Dname) this implies that we are trying to unset/disable the use
            // of the application by assigning an empty string.
            // value should never be null at this point, because the default value
            // of an ApplicationSpec is always a non-empty string.
            if ( value.isEmpty() ) {
                this.valid = true;
                return;
            }
            
            this.path = Paths.get(value);

            if ( !this.path.isAbsolute() ) {
                String dirStr = this.binDirProperty.getPropValue();
                Path dir = null;
                if ( dirStr != null && !dirStr.isEmpty() )
                    dir = Paths.get(dirStr);
                
                if ( dir == null || !dir.isAbsolute() ) {
                    logger.severe( String.format("Neither property %s nor property %s are set to an absolute path: "
                            + "application %s is not available",
                            this.propName, this.binDirProperty.getPropName(), this.defaultValue));
                    logger.info( String.format("The values set are '%s' and '%s' respectively",
                            value == null ? "<null>" : value,
                                    dirStr == null ? "<null>" : dirStr ) );
                    this.valid = false;
                }
                
                if ( dir != null )
                    this.path = dir.resolve(this.path);
                
            }
            
            // If the path seems valid so far, check for existence/executability
            if ( this.valid == null ) {
                this.valid = Files.isExecutable(this.path);
                if ( ! this.valid )
                    logger.severe( String.format("Required application %s defined by property %s is not executable: "
                            + "cannot continue",
                            this.path, this.defaultValue));
            }
            
            // Finally, check that the licencing directory, if applicable, is valid
            if ( this.valid ) {
                String licPropName = this.property.getPropName().replaceFirst(BINSUFFIX + "$", BDGSUFFIX);
                String licPropValue = PropertyDefinition.State.queryProperties(licPropName);
                
                if ( licPropValue != null && !licPropValue.isEmpty() )
                    this.licPath = Paths.get(licPropValue);
                else if ( this.defaultLicDirProperty != null ) {
                    this.licPath = Paths.get(this.defaultLicDirProperty.getPropValue());
                    licPropName = this.defaultLicDirProperty.getPropName();
                }
                
                if ( this.licPath != null ) {
                    Path licFilePath = licPath.resolve(".licence");
                    this.valid = this.licPath.isAbsolute() && Files.isDirectory(this.licPath) && 
                            Files.isReadable(this.licPath) && Files.isRegularFile(licFilePath) &&
                            Files.isReadable(licFilePath);
                    
                    if ( ! this.valid )
                        logger.severe( String.format("Validation of licencing directory '%s' specified by "
                                + "property %s failed:\n"
                                + "it must specify an absolute path to a readable directory that contains "
                                + "a readable file called '.licence'", 
                                this.licPath.toString(), licPropName) );
                }
                
            }
            
        }
    }
}
