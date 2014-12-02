/*******************************************************************************
 * Copyright © 2010, 2012 by Global Phasing Ltd. All rights reserved             
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
 * Types of values that appear in namelist data. Used to
 * special-case certain types of values.
 * 
 * @author pkeller
 *
 */
public enum ValueType {

	/**
	 * Character type. Needs single-quoting on output for some
	 * styles of namelist output
	 */
	CHAR;
	
}
