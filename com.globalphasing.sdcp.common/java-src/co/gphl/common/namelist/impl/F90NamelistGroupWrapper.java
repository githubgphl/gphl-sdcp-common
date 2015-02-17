/*******************************************************************************
 * Copyright © 2014 by Global Phasing Ltd. All rights reserved             
 *                                                                              
 * This software is proprietary to and embodies the confidential                
 * technology of Global Phasing Limited (GPhL).                                 
 *                                                                              
 * Any possession or use (including but not limited to duplication, reproduction
 * and dissemination) of this software (in either source or compiled form) is   
 * forbidden except where an agreement with GPhL that permits such possession or
 * use is in force.                                                             
 *******************************************************************************/

package co.gphl.common.namelist.impl;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import co.gphl.common.namelist.F90NamelistData;
import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.NamelistGroup;

/**
 * Wrapper to be subclassed by classes that need to implement {@link F90NamelistGroup}
 * by delegation.
 * 
 * @author pkeller
 *
 */
public abstract class F90NamelistGroupWrapper implements F90NamelistGroup, Serializable {

    private F90NamelistGroup baseGroup = null;
    
    protected F90NamelistGroupWrapper(F90NamelistGroup baseGroup) {
        this.baseGroup = baseGroup;
    }

    @Override
    public String getGroupName() {
        return baseGroup.getGroupName();
    }

    @Override
    public F90NamelistData getOwningData() {
        return baseGroup.getOwningData();
    }

    @Override
    public void setOwningData(F90NamelistData owningData) {
        baseGroup.setOwningData(owningData);
    }

    @Override
    public Integer getLineNo() {
        return baseGroup.getLineNo();
    }

    @Override
    public String[] put(String varName, String valueList) {
        return baseGroup.put(varName, valueList);
    }

    @Override
    public String[] put(String varName, String[] values) {
        return baseGroup.put(varName, values);
    }

    @Override
    public String[] put(String varName, List<Double> values) {
        return baseGroup.put(varName, values);
    }

    @Override
    public String[] put(String varName, Integer value) {
        return baseGroup.put(varName, value);
    }

    @Override
    public String[] put(String varName, Long value) {
        return baseGroup.put(varName, value);
    }

    @Override
    public String[] put(String varName, Double value) {
        return baseGroup.put(varName, value);
    }

    @Override
    public void putAll(NamelistGroup group) {
        group.putAll(group);
    }

    @Override
    public Map<String, String[]> map() {
        return baseGroup.map();
    }

    @Override
    public int size(String varName) {
        return baseGroup.size(varName);
    }

    @Override
    public List<Double> getDoubleList(String varName) {
        return baseGroup.getDoubleList(varName);
    }

    @Override
    public List<Float> getFloatList(String varName) {
        return baseGroup.getFloatList(varName);
    }

    @Override
    public String getStringValue(String varName) throws RuntimeException {
        return baseGroup.getStringValue(varName);
    }

    @Override
    public Double getDoubleValue(String varName) {
        return baseGroup.getDoubleValue(varName);
    }

    @Override
    public Float getFloatValue(String varName) {
        return baseGroup.getFloatValue(varName);
    }

    @Override
    public Integer getIntegerValue(String varName) {
        return baseGroup.getIntegerValue(varName);
    }

    @Override
    public Long getLongValue(String varName) {
        return baseGroup.getLongValue(varName);
    }

    @Override
    public boolean containsKey(String varName) {
        return baseGroup.containsKey(varName);
    }

    @Override
    public String[] get(String varName) {
        return baseGroup.get(varName);
    }

    @Override
    public void clear() {
        baseGroup.clear();
    }
    
    @Override
    public int size() {
        return baseGroup.size();
    }

    @Override
    public void write(Writer writer, String valueSeparator) throws IOException {
        baseGroup.write(writer, valueSeparator);
    }
    
    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet( this.baseGroup.keySet() );
    }

    @Override
    public Boolean accepts(String keyName) {
        return baseGroup.accepts(keyName);
    }

}
