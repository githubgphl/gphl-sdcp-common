/*
 * Copyright © 2014, 2015 by Global Phasing Ltd. All rights reserved
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

package co.gphl.sdcp.gcal;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import co.gphl.common.namelist.F90NamelistData;
import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistImpl;
import co.gphl.sdcp.F90Namelist.v2.GcalAuxGroupFactory;
import co.gphl.sdcp.F90NamelistGroup.v2.impl.ErrorModelGroup;
import co.gphl.sdcp.F90NamelistGroup.v2.impl.LoopCountGroup;
import co.gphl.sdcp.F90NamelistGroup.v2.impl.SimcalCrystalGroup;
import co.gphl.sdcp.F90NamelistGroup.v2.impl.SimcalOptionsGroup;

/**
 * @author pkeller
 *
 */

// TODO: Unify this class with code that does similar things on other code paths
public abstract class AbstractSimcalInputData extends F90NamelistImpl implements F90NamelistData {

    protected Set<UUID> addedIds = new HashSet<UUID>();
    
    protected File hkl_in, image_root;
    protected Properties props;
    protected String subdir_key_name, prefix_key_name, image_filename_end;
    File sample_in;
    protected F90NamelistData sample;
    
    // Implementing subclasses must set these fields from their input somehow,
    // depending on how they get the static part of the simcal input.
    protected F90NamelistGroup simcalOptionsGroup, errorModelGroup;
    
    /**
     * Abstract constructor for simcal input data. Constructors for implementing
     * subclasses must:
     * 
     * <ul>
     *   <li>call {@code super(builder);}</li>
     *   <li>populate the new instance with static simcal configuration data, starting
     *   with an instance of {@link LoopCountGroup} or a subclass</li>
     *   <li>call {@link #setSample()}</li>
     * </ul>
     * 
     * @param builder
     * @throws IOException
     */
    protected AbstractSimcalInputData(Builder<?> builder, File simcal_config) throws IOException{

        super(GcalAuxGroupFactory.factory(), "simcal", simcal_config);
        
        this.hkl_in=builder.hkl_in;
        this.image_root=builder.image_root;
        this.props=builder.props;
        this.subdir_key_name=builder.subdir_key_name;
        this.prefix_key_name=builder.prefix_key_name;
        this.image_filename_end=builder.image_filename_end;
        
        this.sample = new F90NamelistImpl(GcalAuxGroupFactory.factory(), builder.sample_in);
        this.sample_in = builder.sample_in;
    }
    
    protected void setSample() {
        for ( F90NamelistGroup crystal: this.sample ) {
            if ( crystal instanceof SimcalCrystalGroup ) {
                this.add(crystal);
                this.incrCount(LoopCountGroup.nCrystals);
            }
            else 
                throw new IllegalArgumentException( this.sample_in.toString() +
                        " contains extraneous data: expecting only " + SimcalCrystalGroup.groupName);
        }
        
    }
    
    protected void incrCount(String var) {
        
        LoopCountGroup lcGroup = (LoopCountGroup) this.get(0);
        Integer count = lcGroup.getIntegerValue(var);
        if ( count == null )
            count = 1;
        else
            ++count;
        lcGroup.put(var, count);
    }
    
    // Some properties are used to set variables in the namelist input.
    // Subclasses should call this method between instantiating the
    // simcal launcher and writing out the namelist input file.
    protected void setVarsFromProperties( String propNamePrefix ) {
        
        String strNrays = this.props.getProperty( propNamePrefix + SimcalLauncher.NRAYS );
        if ( strNrays != null && strNrays.length() > 0 )
            this.simcalOptionsGroup.put( SimcalOptionsGroup.nRays, Integer.parseInt(strNrays) );
        else
            this.simcalOptionsGroup.put( SimcalOptionsGroup.nRays, SimcalLauncher.DEFNRAYS);
        
        String strBkgnd = this.props.getProperty( propNamePrefix + SimcalLauncher.BKGND );
        if ( strBkgnd != null && strBkgnd.length() > 0 )
            this.errorModelGroup.put( ErrorModelGroup.background, Double.parseDouble(strBkgnd) );
        else
            this.simcalOptionsGroup.put( ErrorModelGroup.background, SimcalLauncher.DEFBKGND);
        
        String strSimMode = this.props.getProperty( propNamePrefix + SimcalLauncher.SIMMODE );
        if ( strSimMode != null && !strSimMode.isEmpty() )
            this.simcalOptionsGroup.put( SimcalOptionsGroup.simMode, Integer.parseInt(strSimMode) );
        else
            this.simcalOptionsGroup.put( SimcalOptionsGroup.simMode, SimcalLauncher.DEFSIMMODE);

        String strBcgMode = this.props.getProperty( propNamePrefix + SimcalLauncher.BCGMODE );
        if ( strBcgMode != null && !strBcgMode.isEmpty() )
            this.simcalOptionsGroup.put( SimcalOptionsGroup.bcgMode, Integer.parseInt(strBcgMode) );
        else
            this.simcalOptionsGroup.put( SimcalOptionsGroup.bcgMode, SimcalLauncher.DEFBCGMODE);
    }
    
    // See https://weblogs.java.net/blog/emcmanus/archive/2010/10/25/using-builder-pattern-subclasses
    // for an explanation of how to use this kind of pattern.

    public static abstract class Builder<T extends Builder<T>> {
        private File hkl_in;
        private File image_root;
        private Properties props;
        private String subdir_key_name;
        private String prefix_key_name;
        private String image_filename_end;
        
        private File sample_in;
        
        protected abstract T self();
        
        public T hkl_in(File hkl_in) {
            this.hkl_in=hkl_in;
            return self();
        }
        
        public T image_root(File image_root) {
            this.image_root=image_root;
            return self();
        }
        
        // N.B. Don't query or interpret the properties in this layer.
        // Treat them as an opaque object (apart from the namespace) and 
        // pass them through to SimcalLauncher
        public T props( Properties props) {
            this.props=props;
            return self();
        }
        
        public T subdir_key_name(String subdir_key_name) {
            this.subdir_key_name=subdir_key_name;
            return self();
        }
        
        public T prefix_key_name(String prefix_key_name) {
            this.prefix_key_name=prefix_key_name;
            return self();
        }
        
        public T image_filename_end(String image_filename_end) {
            this.image_filename_end=image_filename_end;
            return self();
        }
        
        public T sample_in(File sample_in) {
            this.sample_in = sample_in;
            return self();
        }
        
    }
    
    
}
