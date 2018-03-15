/*******************************************************************************
 * Copyright (c) 2010, 2017 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/
package co.gphl.common.namelist;

import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface NamelistGroup {

    public Integer getLineNo();
    
    /**
     * Sets the value of {@varName} to an array of Strings.
     * {@code valueList} is parsed into a {@code String[]} according to
     * the rules of namelist values
     * @param varName
     * @param valueList
     * @return the previous value of {@code varName}, or {@code null}
     * if it was not previously present in the namelist group.
     */
    public String[] put(String varName, String valueList);
    public String[] put(String varName, String[] values);
    public String[] put(String varName, List<?> values);

    public String[] put(String varName, Number value);
    
    /**
     * Sets the value of {@varName} to a string value without
     * parsing the value further
     * 
     * @param varName
     * @param value
     * @return the previous value of {@code varName}, or {@code null}
     * if it was not previously present in the namelist group.
     */
    public String[] putStringValue(String varName, String value);

    public String[] append(String varName, Number value);
    public String[] append(String varName, List<?> values);
    public String[] append(String varName, String valueList);
    public String[] appendStringValue(String varName, String value);
    public Boolean[] appendBooleanValue(String varName, Boolean value);
    
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
     * Parses the value of the variable from a string into
     * the date/time it represents. 
     * 
     * @param varName Name of variable of namelist group
     * @param dateFormat Expected format of date string. If {@code null}
     * the locale-specific format will be used.
     * @return
     */
    public Date getTime(String varName, DateFormat dateFormat);
    
    
    /**
     * Parses a single value assigned to the variable from a string
     * into the UUID that it represents.
     * 
     * @param varName Name of variable of namelist group
     * @return
     * @throws RuntimeException if a non-empty value assigned
     * to {@code varName} cannot be parsed into a UUID
     */
    public UUID getUuid(String varName);
    
    /**
     * Returns a list of values that represent booleans
     * 
     * @param varName Name of variable of namelist group
     * @return value sequence
     */
    public List<Boolean> getBooleanList(String varName);
    
    /**
     * Returns value of varName as a List of Double's
     * 
     * @param varName Name of variable of namelist group
     * @return returns value sequence
     * @deprecated Use {@link #getNumList(Class, String)} instead.
     */
    @Deprecated
    public List<Double> getDoubleList(String varName);

    /**
     * Returns value of varName as a List of Float's
     * 
     * @param varName Name of variable of namelist group
     * @return returns value sequence
     * @deprecated Use {@link #getNumList(Class, String)} instead.
     */
    @Deprecated
    public List<Float>  getFloatList(String varName);

    /**
     * Gets value as a list of numeric types. Byte, Double, Float, Integer,
     * Long and Short are handled.
     * @param type
     * @param varName
     * @return value as a List
     * @see #getNumValue(Class, String)
     * @throws NumberFormatException if any member of the value cannot be
     * parsed as a number of {@code type}
     */
    public <T extends Number> List<T> getNumList(Class<T> type, String varName);
    
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
    
    /**
     * Gets value as a Double
     * @param varName
     * @return value
     * @deprecated Use {@link #getNumValue(Class, String)} instead: it handles
     * internally with all boxed primitive numeric types.
     */
    @Deprecated
    public Double       getDoubleValue(String varName);

    /**
     * Gets value as a Float
     * @param varName
     * @return value
     * @deprecated Use {@link #getNumValue(Class, String)} instead: it handles
     * internally with all boxed primitive numeric types.
     */
    @Deprecated
    public Float        getFloatValue(String varName);

    /**
     * Gets value as an Integer
     * @param varName
     * @return value
     * @deprecated Use {@link #getNumValue(Class, String)} instead: it handles
     * internally with all boxed primitive numeric types.
     */
    @Deprecated
    public Integer      getIntegerValue(String varName);
    
    /**
     * Gets value as a Long
     * @param varName
     * @return value
     * @deprecated Use {@link #getNumValue(Class, String)} instead: it handles
     * internally with all boxed primitive numeric types.
     */
    @Deprecated
    public Long         getLongValue(String varName);
    
    /**
     * Gets a value as a single numeric type. Byte, Double, Float, Integer,
     * Long and Short are handled.
     * 
     * @param type class of desired return type.
     * @param varName variable name
     * @return single numeric value, or {@code null}
     * @throws NumberFormatException if the value cannot be parsed as a number
     * of {@code type}
     */
    public <T extends Number> T getNumValue(Class<T> type, String varName);
    
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
