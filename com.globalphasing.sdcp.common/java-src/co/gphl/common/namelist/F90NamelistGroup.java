/*******************************************************************************
 * Copyright (c) 2010, 2014 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/
package co.gphl.common.namelist;

public interface F90NamelistGroup extends
        NamelistGroup {

    /** Returns name of namelist group (upper-cased).
     * 
     * @return group name
     */
    public String getGroupName();
    
    public F90NamelistData getOwningData();

    /**
     * @param owningData
     */
    void setOwningData(F90NamelistData owningData);
    
}
