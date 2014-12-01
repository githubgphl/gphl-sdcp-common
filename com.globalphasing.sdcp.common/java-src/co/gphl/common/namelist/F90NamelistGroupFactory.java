/*******************************************************************************
 * Copyright © 2014 by Global Phasing Ltd. All rights reserved             
 *                                                                              
 * This software is proprietary to and embodies the confidential                
 * technology of Global Phasing Limited (GPhL).                                 
 *                                                                              
 * Any possession or use (including but not limited to duplication, reproduction
 * and dissemination) of this software (in either source or compiled form) is   
 * forbidden except where an agreement with GPhL that permits such possession or
 * use is in force.                                                             
 *******************************************************************************/

package co.gphl.common.namelist;

/**
 * @author pkeller
 *
 */
public interface F90NamelistGroupFactory {

    /**
     * Returns a new {@link F90NamelistGroup} instance of the implementing type
     * corresponding to the value of {@code groupName}.
     * 
     * @param groupName Name of Fortran90 namelist group
     * @param throwException If {@code true}, throw an exception if the factory does not define
     * a type corresponding to {@code groupName}. If {@code false}, return {@code null}
     * instead.
     * @param lineNo Line number of namelist group in namelist format input data
     * (may be {@code null})
     * @return {@code F90NamelistGroup} instance
     */
    F90NamelistGroup newInstance(String groupName, boolean throwException, Integer lineNo);

}
