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

import java.util.HashSet;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * @author pkeller
 *
 */
public class LoggerSetup {

    private static Level level = Level.INFO;
    
    /**
     * Specify the minimum {@link Level} that can be set by {@link #getLogger(String, Level)}.
     * Setting it to {@code null} means that there is no restriction. The default is
     * {@link Level#INFO}
     * 
     * @param level
     */
    public static void setLevel( Level level ) {
        LoggerSetup.level = level;
    }
    
	public static Logger getLogger ( String name, Level level ) {
		
	    // Don't go below minimum log level specified by invoking application.
	    if ( LoggerSetup.level != null && level != null && LoggerSetup.level.intValue() > level.intValue() )
	        level = LoggerSetup.level;
	    
		Logger retval = Logger.getLogger(name);
		// We can reduce the level (to make more messages get through)
		// but not increase it.
		if ( level != null &&
		        ( retval.getLevel() == null || retval.getLevel().intValue() > level.intValue() ) )
		    retval.setLevel(level);
		
    	if ( retval.getLevel() == null 
    			|| ( level != null && retval.getLevel().intValue() > level.intValue() ) )
    		retval.setLevel(level);

    	Set<Handler> allHandlers = LoggerSetup.getAllHandlers(retval);
    	
    	if ( allHandlers.size()  == 0 ) {
    	    Handler newHandler = new ConsoleHandler();
    	    newHandler.setLevel(level);
        	retval.addHandler( newHandler );
        	allHandlers.add( newHandler );
    	}
    	
    	for ( Handler handler: allHandlers ) {
    		if ( handler.getLevel() == null 
    				|| ( level != null && handler.getLevel().intValue() > level.intValue() ) )
    			handler.setLevel(level);
    		if ( handler.getFormatter() == null )
    			handler.setFormatter( new SimpleFormatter() );
    	}

    	return retval;
		
	}
	
	private static Set<Handler> getAllHandlers( Logger logger ) {
	    
        Set<Handler> retval = new HashSet<Handler>();

        do {
            for ( Handler h: logger.getHandlers() )
                retval.add(h);

        } while ( logger.getUseParentHandlers() && ( logger = logger.getParent() ) != null );
	    
	    return retval;
	    
	}
	
}
