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

    @Override
    default int getMinArgs() {
        return 1;
    }
    
    @Override
    default int getMaxArgs() {
        return 1;
    }
 
    default boolean isValid() {
        return State.getState(this).isValid();
    }
    
    default Path getPath() {
        return State.getState(this).getPath();
    }
    

    static class State extends PropertyDefinition.State {
        
        private static Logger logger = Logger.getLogger(State.class.getCanonicalName());
        
        
        private final PropertyDefinition dirProperty;
        private Boolean valid = null;
        private Path path = null;

        public static void register(ApplicationSpec spec, String namespace, String propName,
                String defaultValue, PropertyDefinition dirProperty) {
            
            PropertyDefinition.State.register(spec, new State(spec, namespace, propName, defaultValue, dirProperty));
            
        }
        
        private State(ApplicationSpec spec, String namespace, String propName, String defaultValue, PropertyDefinition dirProperty) {
            
            // We don't need to set the description in the state here.
            super(spec, namespace, propName, 
                    Objects.requireNonNull(defaultValue, "BUG: A WorkflowApplicationSpec must have a default value set"),
                    1, 1, "");
            
            if ( this.defaultValue.isEmpty() )
                throw new IllegalArgumentException("BUG: A WorkflowApplicationSpec cannot have an empty default value");
            
            this.dirProperty =
                    Objects.requireNonNull(dirProperty, "BUG: A property for the parent directory of an application spec must be specified");

        };

        protected static State getState(ApplicationSpec key) {
            // I don't really like this, but being typesafe with generics
            // in a static context doesn't seem to be possible.
            return (State) PropertyDefinition.State.getState(key);
        }
        
        private Path getPath() {
            
            if ( ! this.isValid() ) {
                logger.severe(String.format("The path '%s' specified for property %s has failed validation. "
                        + "Cannot use application.\n"
                        + "See previous error message(s) from this logger", this.path, this.propName) );
                logger.info("Please consider calling isValid() sooner!"); 
                throw new RuntimeException("Invalid path specified for application " + this.defaultValue);
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
            this.path = Paths.get(value);

            if ( !this.path.isAbsolute() ) {
                String dirStr = this.dirProperty.getPropValue();
                Path dir = null;
                if ( dirStr != null && !dirStr.isEmpty() )
                    dir = Paths.get(dirStr);
                
                if ( dir == null || !dir.isAbsolute() ) {
                    logger.severe( String.format("Neither property %s nor property %s are set to an absolute path: "
                            + "application %s is not available",
                            this.propName, this.dirProperty.getPropName(), this.defaultValue));
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
        }
    }
}
