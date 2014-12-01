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

import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 * @author pkeller
 *
 */
public class LoggerUtils {

	public static void flush ( Logger logger ) {
		
		for ( Handler h: logger.getHandlers() )
			h.flush();
	}
	
}
