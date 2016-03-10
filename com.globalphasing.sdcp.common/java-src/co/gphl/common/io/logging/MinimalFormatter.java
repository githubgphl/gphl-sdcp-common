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
package co.gphl.common.io.logging;

import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class MinimalFormatter extends SimpleFormatter {

    @Override
    public String format( LogRecord record ) {
        return record.getLevel() + ": " + record.getMessage() + "\n";
    }

}
