/*******************************************************************************
 * Copyright (c) 2010, 2011 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/

package co.gphl.common.threads;

/**
 * Thrown when a {@link java.lang.Process} exits with a status
 * that indicates that it didn't complete correctly
 * 
 * @author pkeller
 *
 */
public class TerminationException extends Exception {

    public TerminationException() {
        super();
    }

    public TerminationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TerminationException(String message) {
        super(message);
    }

    public TerminationException(Throwable cause) {
        super(cause);
    }

    private static final long serialVersionUID = 5259835988686750755L;

}
