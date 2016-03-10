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
