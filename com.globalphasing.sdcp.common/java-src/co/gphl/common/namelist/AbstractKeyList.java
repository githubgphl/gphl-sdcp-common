/*******************************************************************************
 * Copyright © 2010, 2013 by Global Phasing Ltd. All rights reserved             
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// FIXME! This class is no longer abstract. It will be renamed as part of
// a larger F90 namelist  code cleanup
public class AbstractKeyList
    implements Comparator<String>, Serializable {

	private static final long serialVersionUID = 8267289551405291431L;

	private String listName;
	
	// Needed for serialisation of subclasses
	protected AbstractKeyList() {}
	
	public AbstractKeyList( List<String> keyOrder, Map<String, ValueType> valueTypeMap,
	                        String listName ) {
	    
	    this.listName = listName;
	    
	    if ( keyOrder != null && keyOrder.size() > 0 )
	        this.keyOrder = new ArrayList<String>(keyOrder);
	    
	    if ( valueTypeMap != null && valueTypeMap.size() > 0 )
	        this.valueTypeMap = new HashMap<String, ValueType>( valueTypeMap);
	}
	
    protected List<String> keyOrder = null;
    
    // Subclass instances should initialise this with upper-cased keys
    protected Map<String, ValueType> valueTypeMap = null;;
    
    // In the F90 namelist v1 code, subclass instances override this method and
    // return the name that is defined statically in the implementing class
    // FIXME! In the v2 code, this really isn't needed any longer: the class that
    // implements the particular type of namelist group or one of its instances
    // will always be available, so we can get at the group name from that.
    // When we have cleaned up all the v1 namelist stuff, we can get rid of this.
    public String getListName() {
        return this.listName;
    }

	
    /**
     * Returns the type of value associated with {@code keyName}.
     * Used when writing out namelist data to special-case handling of data.
     * 
     * @param keyName name of namelist variable
     * @return type of {@code keyName}'s value
     */
    public ValueType getValueType(String keyName) {
		
		if ( this.valueTypeMap == null )
			return null;
		
		keyName = keyName.toUpperCase();
		if ( this.valueTypeMap.containsKey(keyName) )
			return this.valueTypeMap.get(keyName);
		
		return null;
	}
        
    @Override
    public int compare(String arg0, String arg1) {
        
        int idx0 = -1, idx1 = -1 ;
        
        if ( keyOrder != null ) {
            idx0 = keyOrder.indexOf(arg0.toUpperCase());
            idx1 = keyOrder.indexOf(arg1.toUpperCase());;
        }
        
        if ( idx0 > -1 ) {
            if ( idx1 > -1 )
                return idx0 - idx1;
            else
                return -1;
        }
        else {
            if ( idx1 > -1 )
                return 1;
            else
                return arg0.compareToIgnoreCase(arg1);
        }
            
    }
    
    /**
     * Returns {@code true} if the specified key is a valid variable name
     * for the namelist group. Otherwise returns {@code null} if no variable
     * names have been defined for the namelist group, or {@code false} if
     * the allowable variable names have been defined but {@code keyName} is
     * not one of them.
     * 
     * @param keyName variable name whose presence is to be tested
     * @return {@code true} if the specified key is a valid variable name
     * for the namelist group
     */
    public Boolean contains(String keyName) {
        
        if ( this.keyOrder == null )
            return null;
        
        return this.keyOrder.contains(keyName.toUpperCase());
    }
    
    /**
     * Returns {@code namelistGroup}. Subclasses may override this method with one that
     * converts {@code namelistGroup} to a different class that implements a subtype
     * of {@code F90NamelistGroup}.
     * 
     * @param namelistGroup
     * @return {@code namelistGroup}
     */
    public F90NamelistGroup asTypedNamelistGroup(F90NamelistGroup namelistGroup) {
        return namelistGroup;
    }
    
}
