/*******************************************************************************
 * Copyright (c) 2014 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
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
