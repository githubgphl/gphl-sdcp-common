/*
 * Copyright © 2010-2011 by Global Phasing Ltd. All rights reserved
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
package co.gphl.common.namelist;

import java.io.File;
import java.io.IOException;


/**
 * Minimal interface for data in namelist form.
 * 
 * @author pkeller
 *
 */
public interface NamelistData {

    /**
     * Namelist data must support writing their contents to a file
     * 
     * @param file
     * @throws IOException
     */
    public void write( File file ) throws IOException;
    
    /**
     * Sets or unsets the use of a comma character as a value separator.
     * Default is set.
     * 
     * @param commaSeparated
     */
    public void setCommaSeparator( boolean commaSeparated );


}
