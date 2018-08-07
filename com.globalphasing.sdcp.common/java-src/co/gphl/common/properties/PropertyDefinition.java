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


import java.util.Objects;

/**
 * @author pkeller
 *
 */
public interface PropertyDefinition {

    String getPropName();
    String getDefaultValue();
    int getMinArgs();
    int getMaxArgs();
    String getDescription();
    
    
    /**
     * Returns the value that has been assigned to this property. If no value has been set
     * then the default value, if any, is returned.
     * 
     * @return value assigned to the property
     */
    default String getPropValue() {
        String val = System.getProperty(getPropName(), getDefaultValue());
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
    
}
