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
 * Interface that encapsulates the specification of an application by defining a property.
 * To be valid, the value that is assigned to the property must be convertible to a
 * {@link Path} that:
 * 
 * <ul><li>exists on the filesystem</li>
 * <li>is {@link Path#isAbsolute() absolute}  </li>
 * <li>is {@link Files#isExecutable(Path) executable} </li></ul>
 * 
 * Note that the value is considered valid if the application is
 * not {@link #isEnabled() enabled}: this is to make early checks
 * that use {@link #isValid(boolean)} useful.
 * 
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
     * Tests whether an optional application has been enabled for use.
     * By default, applications are enabled (because they have a default
     * value that is not allowed to be an empty string).
     * 
     * @return {@code false} if an empty string has been explicitly assigned,
     * {@code true} otherwise. 
     */
    default boolean isEnabled() {
        return  !State.getState(this).getUnvalidatedPropValue().isEmpty();
    }
    
    @Override
    default String getPropValue() {
        throw new UnsupportedOperationException("BUG: you should have used getPath() here!");
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
     * Get the logical name of the application. This name is used in the names of
     * other {@link PropertyDefinition properties} that are used to control the
     * execution of the application, and is incorporated into the names of various
     * directories and files that are handled by the workflow.
     * 
     * @return logical name of the application
     */
    default String getAppName() {
        return State.getState(this).appName;
    }
    
    static class State extends PropertyDefinition.State {
        
        private static Logger logger = Logger.getLogger(State.class.getEnclosingClass().getName());
        
        
        private final PropertyDefinition binDirProperty;
        private final PropertyDefinition defaultLicDirProperty;
        private final String appName;
        
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
        
        private State(ApplicationSpec spec, String namespace, String appName, String defaultValue,
                PropertyDefinition binDirProperty, PropertyDefinition licDirProperty) {
            
            // We don't need to set the description in the state here.
            super(spec, namespace, appName + "." + BINSUFFIX, 
                    Objects.requireNonNull(defaultValue, "BUG: A WorkflowApplicationSpec must have a default value set"),
                    0, 1, "", null);
            
            this.appName = appName;
            
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
            
            if ( ! this.isValid(false) ) {
                logger.severe(String.format("Property %s has failed validation: "
                        + "cannot use application.\n"
                        + "See previous error message(s) from this logger", this.getPropName()) );
                logger.info("Please consider calling isValid() sooner!"); 
                throw new RuntimeException("Invalid specification for application " + this.appName);
            }
            
            return this.path;
        }
        
        @Override
        protected boolean isValid(boolean required) {
            if ( this.valid == null )
                this.setup(required);
            
            if ( this.valid == null )
                throw new IllegalStateException("BUG: setup() has been called, but this.valid is still null");
            
            return this.valid;
        }

        
        private void setup(boolean required) {

            String value = this.getUnvalidatedPropValue();
            
            // If the property has been defined with no argument (something like
            // -Dname) this implies that we are trying to unset/disable the use
            // of the application by assigning an empty string.
            if ( value == null || value.isEmpty() ) {
                if ( required ) {
                    this.valid = false;
                    logger.severe(String.format("Application %s has been disabled, but it is required in this context. "
                            + "Check the setting of property %s",
                            this.appName, this.getPropName()));
                }
                else
                    this.valid = true;
                return;
            }
            
            // We set this.path from the start, so that if we get here from getPath() we can 
            // report as much of the path as was set up if validation fails.
            // Cast to supertype to avoid UnsupportedOperationException above
            this.path = Paths.get(value);

            if ( !this.path.isAbsolute() ) {
                if ( ! this.binDirProperty.isValid(true) ) {
                    logger.severe(String.format("Directory property %s required here but invalid: property %s is also invalid",
                            this.binDirProperty.getPropName(), this.getPropName()) );
                    this.valid = false;
                    return;
                }
                String dirStr = this.binDirProperty.getPropValue();
                Path dir = null;
                if ( dirStr != null && !dirStr.isEmpty() )
                    dir = Paths.get(dirStr);
                
                if ( dir == null || !dir.isAbsolute() ) {
                    logger.severe( String.format("Neither property %s nor property %s are set to an absolute path: "
                            + "application %s is not available",
                            this.getPropName(), this.binDirProperty.getPropName(), this.defaultValue));
                    logger.info( String.format("The values set are '%s' and '%s' respectively",
                            Objects.toString(value, "<null>"), Objects.toString(dirStr, "<null>") ) );
                    this.valid = false;
                }
                
                if ( dir != null )
                    this.path = dir.resolve(this.path);
                
            }
            
            // If the path seems valid so far, check for existence/executability
            if ( this.valid == null ) {
                this.valid = Files.isExecutable(this.path);
                if ( ! this.valid )
                    logger.severe( String.format("File %s defined by property %s is not executable",
                            this.path, this.getPropName()));
            }
            
            // Finally, check that the licencing directory, if applicable, is valid
            if ( this.valid ) {
                String licPropName = this.property.getPropName().replaceFirst(BINSUFFIX + "$", BDGSUFFIX);
                String licPropValue = PropertyDefinition.State.queryProperties(licPropName);
                
                if ( licPropValue != null && !licPropValue.isEmpty() )
                    this.licPath = Paths.get(licPropValue);
                else if ( this.defaultLicDirProperty != null ) {
                    String licPathStr = Objects.toString(this.defaultLicDirProperty.getPropValue(), "");
                    if ( !licPathStr.isEmpty() ) {
                        this.licPath = Paths.get(licPathStr);
                        licPropName = this.defaultLicDirProperty.getPropName();
                    }
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
