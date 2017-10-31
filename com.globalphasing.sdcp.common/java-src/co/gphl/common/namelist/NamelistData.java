/*******************************************************************************
 * Copyright (c) 2010, 2014 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/
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
