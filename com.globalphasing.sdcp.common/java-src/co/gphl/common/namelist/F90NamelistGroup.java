/*
 * Copyright © 2010, 2013 by Global Phasing Ltd. All rights reserved
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
