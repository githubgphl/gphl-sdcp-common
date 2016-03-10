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

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NamelistGroup {

    public Integer getLineNo();
    public String[] put(String varName, String valueList);
    public String[] put(String varName, String[] values);
    public String[] put(String varName, List<Double> values);

    public String[] put(String varName, Integer value);
    public String[] put(String varName, Long value);
    public String[] put(String varName, Double value);

    public void putAll(NamelistGroup group);
    public Map<String, String[]> map();
    public Set<String> keySet();

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
    public Boolean accepts(String keyName);

    /**
     * Returns the number of values assigned to {@code varName}. If {@code varName}
     * is not contained in the namelist group, this method returns 0. Use
     * {@link #containsKey(String)} to disambiguate between a value sequence of length
     * 0 or the assignment not being present in the namelist group
     * 
     * @param varName variable name
     * @return the number of values assigned to {@code varName}
     */
    public int size(String varName);

    /**
     * Returns value of varName as a List of Double's
     * 
     * @param varName Name of variable of namelist group
     * @return returns value sequence
     */
    public List<Double> getDoubleList(String varName);

    /**
     * Returns value of varName as a List of Float's
     * 
     * @param varName Name of variable of namelist group
     * @return returns value sequence
     */
    public List<Float>  getFloatList(String varName);

    /**
     * <p>Gets value if variable has one value only. Throws
     * {@code RuntimeException} if variable has more than one value;
     * returns null if variable has 0 values.</p>
     * 
     * <p>This function is intended to be used for variables that are known
     * to never be defined with more than one value. It should not be used
     * for cases where a value may have one or more values</p>
     * 
     * @param varName
     * @return value (or {@code null} if variable has no values assigned)
     */
    public String       getStringValue(String varName) throws RuntimeException;
    public Double       getDoubleValue(String varName);
    public Float        getFloatValue(String varName);
    public Integer      getIntegerValue(String varName);
    public Long         getLongValue(String varName);
    public boolean      containsKey(String varName);
    public String[]     get(String varName);
    public void         clear();
    public int          size();

    /**
     * Emits contents of namelist group to {@code writer}.
     * 
     * @param writer
     * @param valueSeparator Either null string or ", "
     * @throws IOException
     * @see NamelistData#setCommaSeparator(boolean)
     */
    public void write(Writer writer, String valueSeparator) throws IOException;

}
