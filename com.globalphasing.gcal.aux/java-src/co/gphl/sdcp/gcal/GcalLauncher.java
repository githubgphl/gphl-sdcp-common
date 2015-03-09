/*
 * Copyright © 2012, 2015 by Global Phasing Ltd. All rights reserved
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
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import co.gphl.common.io.logging.LoggerSetup;
import co.gphl.common.io.logging.LoggerUtils;
import co.gphl.common.threads.ProcessLauncher;
import co.gphl.common.threads.TerminationException;

/**
 * @author pkeller
 *
 */
public abstract class GcalLauncher implements Serializable {

    protected boolean dryrun = false;
    protected File bin;
    
    // Logger does not implement Serializable, so we mark it as transient
    // and reconnect it by hand during de-serialisation
    // See readObject method below.
    private transient Logger myLogger;
    private String loggerName;
    private Level loggerLevel;
    
    protected final String propNamePrefix, appName;
    protected final Properties properties;
    protected String outfileName;
    
    private static Map<String, String> propNames = null;
    protected Map<String, String> args = new HashMap<String, String>();
    protected Map<String, String> env = new HashMap<String, String>();
    
    private String stdoutFilename, stderrFilename;
    private Writer stdoutWriter, stderrWriter;
    
    // It may seem a bit fussy to define these as constants, but
    // doing so makes them visible in the Javadocs.
    
    /**
     * Property used to enable and set {@code --nthreads} command-line option
     */
    public static final String NTHREADS = "nthreads";
    
    /**
     * Name of property used to enable and set {@code OMP_STACKSIZE} environment variable
     */
    public static final String OMPSTACKSIZE = "ompstacksize";
    
    /**
     * Name of property used to enable and set {@code OMP_THREAD_LIMIT} environment variable
     */
    public static final String OMPTHREADLIMIT = "ompthreadlimit";
    
    /**
     * Name of property used to enable and set {@code OMP_NUM_THREADS} environment variable
     */
    public static final String OMPNUMTHREADS = "ompnumthreads";
    
    /**
     * Name of property used to specify path to executable
     */
    public static final String BIN = "bin";
    
    /**
     * Name of property used to specify whether or not to actually run executable
     */
    public static final String DRYRUN = "dry_run";
    
    /**
     * Name of property used to specify the directory containing the {@code .licence}
     * file. Only needed where the executable is a "bombed" one, i.e. needs a
     * GPhL licence file to run.
     */
    public static final String BDG_LICENCE_DIR = "bdg_licence_dir";
    
    protected Map<String, String> getPropNames() {

        if ( GcalLauncher.propNames == null ) {
            GcalLauncher.propNames = new HashMap<String, String>(); 
            // Values that start with "-" are command-line options
            // Values that start with uppercase letters are environment variables.
            propNames.put(GcalLauncher.NTHREADS, "--nthreads");
            propNames.put(GcalLauncher.OMPSTACKSIZE, "OMP_STACKSIZE");
            propNames.put(GcalLauncher.OMPTHREADLIMIT, "OMP_THREAD_LIMIT");
            propNames.put(GcalLauncher.OMPNUMTHREADS, "OMP_NUM_THREADS");
            propNames.put(GcalLauncher.BDG_LICENCE_DIR, "BDG_home");
        }

        return Collections.unmodifiableMap(GcalLauncher.propNames);

    }
    
    
    // Needed to allow subclasses to be made serialisable
    protected GcalLauncher() {
        // Need to assign to final fields in every constructor to keep the compiler happy.
        // The JVM uses some reflective black magic in defaultReadObject to restore their
        // state
        this.propNamePrefix = "";
        this.appName = "";
        this.properties = new Properties();
    };
    
    private void readObject( java.io.ObjectInputStream in )
            throws ClassNotFoundException, IOException {
        
        in.defaultReadObject();
        this.myLogger = LoggerSetup.getLogger(this.loggerName, this.loggerLevel);

    }
    
    /**
     * Constructor for the launcher for a Gcal-family application. Should
     * be invoked from subclasses.
     * 
     * @param logger A {@link Logger} to use for messages. If {@code null}, a default logger
     * that logs {@link Level#WARNING} messages will be used.
     * @param appName The name of the application that is going to be launched
     * @param propNameNamespace Prefix for property names. {@code appName} is appended to this prefix.
     * @param properties Properties object that contains properties required to launch application.
     * {@code if properties == null} {@link System#getProperties() the system properties} will be used instead.
     * @param stdoutWriter Destination for stdout. May be {@code null}
     * @param stderrWriter Destination for stderr. May be {@code null}
     * @param stdoutFilename Name of file in working directory to capture stdout. May be {@code null}
     * @param stderrFilename Name of file in working directory to capture stderr. May be {@code null}
     * 
     * @throws IllegalArgumentException {@code if ( appName == null || appName.length() == 0 )}
     * @see ProcessLauncher#startAndWait(Writer, Writer, File, File, boolean)
     */
    protected GcalLauncher(Logger logger,
            String appName, String propNameNamespace, Properties properties,
            Writer stdoutWriter, Writer stderrWriter, String stdoutFilename, String stderrFilename ) {
        this.myLogger = logger != null ? logger :
            LoggerSetup.getLogger(GcalLauncher.class.getSimpleName(), Level.WARNING);
        
        // Save, so that we can set myLogger up again on deserialisation
        this.loggerName = this.myLogger.getName();
        this.loggerLevel = this.myLogger.getLevel();
        
        if ( appName == null || appName.length() == 0 )
            throw new IllegalArgumentException("Must specify a non-null, non-zero-length application name");
        this.appName = appName;
        this.propNamePrefix = this.regulariseNamespace(propNameNamespace, appName);
        this.properties = properties != null ? properties : System.getProperties();

        this.setupProperties();
        
        if ( ! this.check_bin_ok() )
            this.myLogger.warning("Specified executable " +
                    ( this.bin == null ? "<null>" : this.bin.toString() ) + " is not executable");

        this.stdoutWriter = stdoutWriter;
        this.stderrWriter = stderrWriter;
        this.stdoutFilename = stdoutFilename;
        this.stderrFilename = stderrFilename;
        
    }
        
    public final void launch(File wdir, File input)
            throws TerminationException, IOException, InterruptedException {
        this._pre_launch(wdir, input);
        this._launch(wdir, input);
        this._post_launch(wdir, input);
    }
    
    protected void _pre_launch(File wdir, File input) {
        
        if ( ! this.check_bin_ok() ) {
            String binName = this.bin == null ? "<null>" : this.bin.toString();
            this.myLogger.severe("Cannot launch binary " + binName);
            throw new IllegalStateException("Cannot launch binary " + binName);
        }

    }
    
    private void _launch(File wdir, File infile) throws TerminationException, IOException, InterruptedException {

        this.setupProperties();

        if ( this.dryrun ) {
            this.myLogger.info("In dry-run mode: will return without running " + this.appName);
            return;
        }

        // Replace ".in" suffix with ".out" this way just in case ".in" is missing.
        // Avoids any risk of outfile having the same filename as in, which would
        // be a problem....
        this.outfileName = infile.toString().replaceFirst("\\.in$", "") + ".out";
        
        List<String> cmd = new ArrayList<String>(
                Arrays.asList( this.bin.toString(), "-i", infile.toString(), "-o", outfileName) );

        String val;
        for ( Entry<String, String> e: this.args.entrySet() ) {
            cmd.add(e.getKey());
            val = e.getValue();
            if ( val != null && val.length() > 0 )
                cmd.add(val);
        }

        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        processBuilder.directory(wdir);

        Map<String, String> procEnv = processBuilder.environment();
        if ( this.env != null )
            procEnv.putAll(this.env);
        
        long startTime = System.currentTimeMillis();
        this.myLogger.info("Starting " + cmd);
        LoggerUtils.flush(myLogger);
        ProcessLauncher launcher = new ProcessLauncher(processBuilder);
        launcher.startAndWait(
                this.stdoutWriter == null ? new PrintWriter(System.out): this.stdoutWriter,
                this.stderrWriter == null ? null : this.stderrWriter,
                this.stdoutFilename == null ? null : new File(wdir, this.stdoutFilename),
                this.stderrFilename == null ? null : new File(wdir, this.stderrFilename),
                false);
        this.myLogger.info(this.bin + " finished in " +
                (System.currentTimeMillis() - startTime)/1000.0 + "s");
        
        String lastErrLine = launcher.getLastErrLine();
        if ( lastErrLine == null || ! lastErrLine.trim().equals("NORMAL termination") )
            throw new TerminationException("Application " + cmd.get(0) + " terminated abnormally");
        
    }
    
    protected void _post_launch(File wdir, File infile) {
        
    }
    
    protected boolean check_bin_ok() {
        
        return this.bin != null && this.bin.isFile() && this.bin.canExecute();
        
    }
 
    protected void setupProperties() {
        
        // Set binary from system property if specified
        String binStr = this.properties.getProperty(this.propNamePrefix + GcalLauncher.BIN);
        if ( binStr != null )
            this.bin = new File(binStr);

        // Change dryrun from current setting if specified
        this.dryrun = propertyTrueFalse(this.propNamePrefix + GcalLauncher.DRYRUN, this.dryrun);

        // Now setup environment variables and command-line options that are specified
        // in system properties
        String propName, propVal, propArg, oldPropVal;
        Map<String, String> paramSet;
        
        for ( Entry<String, String> e: this.getPropNames().entrySet() ) {

            propName = this.propNamePrefix + e.getKey();
            propArg = e.getValue();
            if ( propArg.charAt(0) == '-' )
                paramSet = this.args;
            else if ( propArg.matches("^[A-Z].*") )
                paramSet = this.env;
            else
                throw new IllegalArgumentException("Can't handle property name mapping " +
                        propName + ":" + propArg);

            // If property has been assigned an empty string, and property has
            // an existing value with length > 0, we treat this as
            // removing the argument/environment variable from its list.
            // Otherwise, set the new value.
            
            // FIXME! This doesn't let us remove/unset an option/environment var
            // {that doesn't take an argument}/{whose effect only depends on whether
            // it is set or not} 
            propVal = this.properties.getProperty(propName);
            if ( propVal != null ) {
                oldPropVal = paramSet.get(propArg);
                if ( propVal.length() == 0 &&
                        oldPropVal != null && oldPropVal.length() > 0 )
                    paramSet.remove(propArg);
                else
                    paramSet.put(propArg, propVal);
            }
        }
    }

    private String regulariseNamespace(String namespace, String appName) {
        
        String retval = namespace;
        
        if ( retval == null || retval.length() == 0 ) {
            this.myLogger.warning("Null/zero-length namespace prefix specified for property names. This is a bad idea");
            return appName;
        }
        
        if ( retval.charAt(0) == '.' ) {
            this.myLogger.warning("Namespace '" + retval + "' starts with '.'. Removing");
            retval = retval.substring(1);
        }
        
        if ( ! retval.endsWith(".") )
            retval += '.';
        
        return retval + appName + '.';
        
    }
    
    // Helper method to decode boolean values that come in as strings from
    // properties. Eventually move somewhere public when more code needs to do this.
    private boolean propertyTrueFalse ( String propertyName, boolean def ) {
        String val = this.properties.getProperty(propertyName);
        if ( val == null || val.length() == 0 )
            return def;
        if ( val.matches("^[tTyY]") )
            return true;
        if ( val.matches("^[fFnN]") )
            return false;
        
        throw new RuntimeException("Don't understand value of '" + val + "' for property " 
                + propertyName +".\nShould begin with one of 'tTyYfFnN'");
        
    }
    
    public String getPropNamePrefix() {
        return this.propNamePrefix;
    }
    
}
