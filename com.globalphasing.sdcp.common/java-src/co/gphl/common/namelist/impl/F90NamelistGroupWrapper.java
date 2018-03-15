/*******************************************************************************
 * Copyright (c) 2014, 2017 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/

package co.gphl.common.namelist.impl;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
    public String[] put(String varName, List<?> values) {
        return baseGroup.put(varName, values);
    }

    @Override
    public String[] put(String varName, Number value) {
        return baseGroup.put(varName, value);
    }

    @Override public String[] putStringValue(String varName, String value) {
        return baseGroup.putStringValue(varName, value);
    }
    @Override
    public String[] append(String varName, String value) {
        return baseGroup.append(varName, value);
    }

    @Override
    public String[] append(String varName, Number value) {
        return baseGroup.append(varName, value);
    }

    @Override
    public String[] appendStringValue(String varName, String value) {
        return this.baseGroup.append(varName, Arrays.asList( new String[] {value}) );
    }
    
    @Override
    public Boolean[] appendBooleanValue(String varName, Boolean value) {
        return this.baseGroup.appendBooleanValue(varName, value);
    }
    
    @Override
    public String[] append(String varName, List<?> values) {
        return baseGroup.append(varName, values);
    }
    
    @Override
    public void putAll(NamelistGroup group) {
        this.baseGroup.putAll(group);
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
    public <T extends Number> List<T> getNumList(Class<T> type, String varName) {
        return baseGroup.getNumList(type, varName);
    }
    
    @Override
    public List<Boolean> getBooleanList(String varName) {
        return baseGroup.getBooleanList(varName);
    }
    
    @Override @Deprecated
    public List<Double> getDoubleList(String varName) {
        return baseGroup.getDoubleList(varName);
    }

    @Override @Deprecated
    public List<Float> getFloatList(String varName) {
        return baseGroup.getFloatList(varName);
    }

    @Override
    public String getStringValue(String varName) throws RuntimeException {
        return baseGroup.getStringValue(varName);
    }

    @Override
    public Date getTime(String varName, DateFormat dateFormat) {
        return baseGroup.getTime(varName, dateFormat);
    }
    
    @Override
    public UUID getUuid(String varName) {
        return baseGroup.getUuid(varName);
    }
    
    @Override
    public <T extends Number> T getNumValue(Class<T> type, String varName) {
        return baseGroup.getNumValue(type, varName);
    }
    
    @Override @Deprecated
    public Double getDoubleValue(String varName) {
        return baseGroup.getDoubleValue(varName);
    }

    @Override @Deprecated
    public Float getFloatValue(String varName) {
        return baseGroup.getFloatValue(varName);
    }

    @Override @Deprecated
    public Integer getIntegerValue(String varName) {
        return baseGroup.getIntegerValue(varName);
    }

    @Override @Deprecated
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
