/*
 * Copyright © 2014 by Global Phasing Ltd. All rights reserved
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

package co.gphl.common.namelist.impl;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
 *   {@link f90NamelistGroupWrapper} instances.
 *   </li>
 * </ul>
 * 
 * @author pkeller
 *
 */
public abstract class F90NamelistGroupFactoryImpl
    // FIXME! Won't need to be serializable for ever.....
    implements F90NamelistGroupFactory, Serializable {

    protected F90NamelistGroupFactory delegate = null;

    protected Map<String, Class<? extends F90NamelistGroup>> groupMap = null;
    
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
            try {
                
                Constructor<? extends F90NamelistGroup> cons;
                if ( F90NamelistGroupImpl.class.isAssignableFrom(type) ) {

                    if ( lineNo == null )
                        return type.newInstance();
                    else {
                        cons = type.getConstructor(F90NamelistGroupFactoryImpl.integerArray);
                        return cons.newInstance( new Object[] { lineNo } );
                    }
                }
                else if ( F90NamelistGroupWrapper.class.isAssignableFrom(type) ) {
                    F90NamelistGroup baseGroup = this.delegate.newInstance(ucGroupName, throwException, lineNo);
                    cons = type.getConstructor(new Class<?>[] { baseGroup.getClass() } );
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
                        " taking a single Integer argument", e);
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
