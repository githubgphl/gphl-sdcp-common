/*******************************************************************************
 * Copyright (c) 2014 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/

package co.gphl.common.namelist.impl;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.F90NamelistGroupFactory;

/**
 * Abstract factory class for F90 Namelist Groups. This class can use delegate factories for two
 * purposes:
 * <ul>
 *   <li>Extending the namelist group types that this factory can produce instances for.
 *   The delegate factory should produce instances of {@link F90NamelistGroupImpl}
 *   </li>
 *   <li>Producing namelist group types that extend the capabilities of
 *   {@link F90NamelistGroupImpl} instances. The delegate factory should produce
 *   {@link F90NamelistGroupWrapper} instances.
 *   </li>
 * </ul>
 * 
 * @author pkeller
 *
 */
public abstract class F90NamelistGroupFactoryImpl
    // FIXME! Won't need to be serializable for ever.....
    implements F90NamelistGroupFactory, Serializable {

    protected final F90NamelistGroupFactory delegate;

    protected final Map<String, Class<? extends F90NamelistGroup>> groupMap
        = new HashMap<String, Class<? extends F90NamelistGroup>>();
    
    private static Class<?>[] integerArray = { Integer.class };
    
    /* (non-Javadoc)
     * @see co.gphl.common.namelist.F90NamelistGroupFactory#newInstance(java.lang.String, boolean, java.lang.Integer)
     */
    @Override
    public F90NamelistGroup newInstance(String groupName,
            boolean throwException, Integer lineNo) {
        
        String ucGroupName = groupName.toUpperCase();

        if ( ! this.groupMap.containsKey(ucGroupName)  ) {

            if ( this.delegate != null )
                // Delegate to next factory down the chain if we know nothing about this group name
                return this.delegate.newInstance(ucGroupName, throwException, lineNo);
            else {
                // No delegate factory: return null or throw exception.
                if ( throwException )
                    throw new IllegalArgumentException("Namelist group name " + ucGroupName
                            + " has no corresponding Java type");
                else
                    return null;
            }

        }
        else {
            // ... otherwise, we have to cater for two possibilities:
            // * Subclass of F90NamelistGroupImpl (subclass has two constructors: one with no
            //   arguments, the other with a single Integer argument)
            // * Subclass of F90NamelistGroupWrapper (subclass' constructor has a single
            //   F90NamelistGroup argument; we require the delegate factory to provide the
            //   actual argument for this case).
            
            Class<? extends F90NamelistGroup> type = this.groupMap.get(ucGroupName);
            Class<?> argClass = null;
            try {
                
                Constructor<? extends F90NamelistGroup> cons;
                if ( F90NamelistGroupImpl.class.isAssignableFrom(type) ) {

                    if ( lineNo == null )
                        return type.newInstance();
                    else {
                        argClass = Integer.class;
                        cons = type.getConstructor(F90NamelistGroupFactoryImpl.integerArray);
                        return cons.newInstance( new Object[] { lineNo } );
                    }
                }
                else if ( F90NamelistGroupWrapper.class.isAssignableFrom(type) ) {
                    F90NamelistGroup baseGroup = this.delegate.newInstance(ucGroupName, throwException, lineNo);
                    argClass = baseGroup.getClass();
                    cons = type.getConstructor(new Class<?>[] { argClass } );
                    return cons.newInstance( new Object[] {baseGroup} );
                }
                else
                    throw new RuntimeException("BUG: " + ucGroupName + " has been mapped to the class " + type.getName()
                            + " which is neither a subclass of " + F90NamelistGroupImpl.class.getSimpleName() 
                            + " nor of " + F90NamelistGroupWrapper.class.getSimpleName() );
            
            } catch (InstantiationException e) {
                throw new RuntimeException("BUG: " + ucGroupName + " has been mapped to the non-instantiable class " +
                            type.getName(), e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("BUG: the class " + type.getName() +
                            " or its constructor is not accessible", e );
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("BUG: Could not find constructor for " + type.getName() +
                        " taking a single " + ( argClass == null ? "unknown" : argClass.getSimpleName() )  + " argument", e);
            } catch (SecurityException e) {
                throw new RuntimeException("Security manager problem with " + type.getName(), e );
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("BUG: constructor for " + type.getName() +
                        " was called with incorrect/invalid arguments", e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Exception thrown by constructor for " + type.getName(), e);
            }
        }
    }

    protected F90NamelistGroupFactoryImpl(F90NamelistGroupFactory delegate) {
        this.delegate = delegate;
    }
    
    
}
