/*******************************************************************************
 * Copyright (c) 2012, 2017 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/

package co.gphl.sdcp.gcal;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.gphl.common.properties.ApplicationSpec;
import co.gphl.common.threads.ProcessLauncher;
import co.gphl.common.threads.TerminationException;

/**
 * @author pkeller
 *
 */
public abstract class GcalLauncher implements Serializable {

    private static Logger logger = LoggerFactory.getLogger(GcalLauncher.class);
    
    protected boolean dryrun = false;
    
    // Logger does not implement Serializable, so we mark it as transient
    // and reconnect it by hand during de-serialisation
    // See readObject method below.
    private transient Logger myLogger;
    private String loggerName;
    
    protected final String globalPropNamePrefix, propNamePrefix;
    protected final ApplicationSpec appSpec;
    protected final Properties properties;
    protected String outfileName;
    private boolean uniqueFilenames;
    
    private static Map<String, String> propNames = null;
    protected Map<String, String> args = new HashMap<String, String>();
    protected Map<String, String> env = new HashMap<String, String>();
    
    private boolean outputToFile, redirectError;
    private Writer stdoutWriter, stderrWriter;
    protected File stdout, stderr;
    protected String lastErrLine = null;
    
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
     * Name of property used to control whether or not to use {@link File#createTempFile(String, String)}
     * to generate unique names for the input and output files for this launch.
     */
    public static final String UNIQUE_FILENAMES = "unique_filenames";
    
    protected Map<String, String> getPropNames() {

        if ( GcalLauncher.propNames == null ) {
            GcalLauncher.propNames = new HashMap<String, String>(); 
            // Values that start with "-" are command-line options
            // Values that start with uppercase letters are environment variables.
            propNames.put(GcalLauncher.NTHREADS, "--nthreads");
            propNames.put(GcalLauncher.OMPSTACKSIZE, "OMP_STACKSIZE");
            propNames.put(GcalLauncher.OMPTHREADLIMIT, "OMP_THREAD_LIMIT");
            propNames.put(GcalLauncher.OMPNUMTHREADS, "OMP_NUM_THREADS");
        }

        return Collections.unmodifiableMap(GcalLauncher.propNames);

    }
    
    
    // Needed to allow subclasses to be made serialisable
    protected GcalLauncher() {
        // Need to assign to final fields in every constructor to keep the compiler happy.
        // The JVM uses some reflective black magic in defaultReadObject to restore their
        // state
        this.globalPropNamePrefix = "";
        this.propNamePrefix = "";
        this.appSpec = null;
        this.properties = new Properties();
    };
    
    private void readObject( java.io.ObjectInputStream in )
            throws ClassNotFoundException, IOException {
        
        in.defaultReadObject();
        this.myLogger = LoggerFactory.getLogger(this.loggerName);

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
     * @param outputToFile if {@code true}, captures stdout and stderr to file(s). The filename(s) are derived
     * from the name of the second argument to {@link #launch(File, File)}
     * @param redirectErrorStream combines stdout and stderr {@code if ( redirectError && stderrWriter == null ) }
     * 
     * @throws IllegalArgumentException {@code if ( appName == null || appName.length() == 0 )}
     * @see ProcessLauncher#startAndWait(Writer, Writer, File, File, boolean)
     * @see ProcessBuilder#redirectErrorStream()
     */
    protected GcalLauncher(Logger logger,
            ApplicationSpec appSpec,
            String propNameNamespace, Properties properties,
            Writer stdoutWriter, Writer stderrWriter,
            boolean outputToFile, boolean redirectErrorStream ) {
        this.myLogger = logger != null ? logger : GcalLauncher.logger;
        
        // Save, so that we can set myLogger up again on deserialisation
        this.loggerName = this.myLogger.getName();
        
        this.appSpec = appSpec;
        
        this.globalPropNamePrefix = GcalLauncher.regulariseNamespace(propNameNamespace);
        this.propNamePrefix = GcalLauncher.propNamePrefix(this.globalPropNamePrefix,
                this.appSpec.getDefaultValue());
        this.properties = properties != null ? properties : System.getProperties();

        this.setupProperties();
        
        this.stdoutWriter = stdoutWriter;
        this.stderrWriter = stderrWriter;
        this.outputToFile = outputToFile;
        this.redirectError = redirectErrorStream;
        
    }
        
    public final void launch(File wdir, File input)
            throws TerminationException, IOException, InterruptedException {
        this._pre_launch(wdir, input);
        this._launch(wdir, input);
        this._post_launch(wdir, input);
    }
    
    protected void _pre_launch(File wdir, File input) {
        
    }
    
    private void _launch(File wdir, File infile) throws TerminationException, IOException, InterruptedException {

        this.setupProperties();

        if ( this.dryrun ) {
            this.myLogger.info("In dry-run mode: will return without running " + this.appSpec.getDefaultValue());
            return;
        }
        
        List<String> cmd = new ArrayList<String>(
                Arrays.asList( this.appSpec.getPath().toString(),
                        "--input", infile.toString() ) );

        // If this application doesn't use --output, the implementing subclass is responsible
        // for setting this.outfileName if needed.
        if ( this.useOutputOpt() ) {
            // Replace ".in" suffix with ".out" this way just in case ".in" is missing.
            // Avoids any risk of outfile having the same filename as in, which would
            // be a problem....
            this.outfileName = infile.toString().replaceFirst("\\.(in|nml)$", "") + ".out";
            cmd.add("--output");
            cmd.add(this.outfileName);
        }
        
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
        
        if ( this.outputToFile ) {
            // Deal with the various forms of input filename that we are handling
            // including the transition to the .nml suffix (which might refer to
            // either an input or an output file).
            String stem = infile.getName().replaceFirst("\\.(in|nml)$", "").replaceFirst("_in$", "");
            this.stdout = new File( wdir, stem + ".stdout" );
            if ( ! this.redirectError )
                this.stderr = new File( wdir, stem + ".stderr" );
        }
        
        ProcessLauncher launcher = new ProcessLauncher(processBuilder);
        launcher.startAndWait(
                this.stdoutWriter == null ? new PrintWriter(System.out): this.stdoutWriter,
                this.stderrWriter == null ? null : this.stderrWriter,
                this.stdout, this.stderr, false, true);
        this.myLogger.info(this.appSpec.getPath().toString() + " finished in " +
                (System.currentTimeMillis() - startTime)/1000.0 + "s");
        
        this.lastErrLine = launcher.getLastErrLine();
        if ( this.lastErrLine == null || ! this.lastErrLine.trim().equals("NORMAL termination") )
            throw new TerminationException("Application " + cmd.get(0) + " terminated abnormally");
        
    }
    
    protected void _post_launch(File wdir, File infile) {
        
    }
    
    protected void setupProperties() {
        
        // Change dryrun from current setting if specified
        this.dryrun = propertyTrueFalse(this.propNamePrefix + GcalLauncher.DRYRUN, this.dryrun);

        // Use unique/temporary filenames?
        this.uniqueFilenames = propertyTrueFalse(this.propNamePrefix + GcalLauncher.UNIQUE_FILENAMES, false);
        
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
        
        // Set up licencing directory, if one has been specified
        Path licencingDir = this.appSpec.getLicencingDir();
        if ( licencingDir != null )
            this.env.put("BDG_home", licencingDir.toString());
        
    }

    private static String regulariseNamespace(String namespace) {
        
        String retval = namespace;
        
        if ( retval == null || retval.length() == 0 ) {
            GcalLauncher.logger.warn("Null/zero-length namespace prefix specified for property names. This is a bad idea");
            return "";
        }
        
        if ( retval.charAt(0) == '.' ) {
            GcalLauncher.logger.warn("Namespace '" + retval + "' starts with '.'. Removing");
            retval = retval.substring(1);
        }
        
        if ( ! retval.endsWith(".") )
            retval += '.';
        
        return retval;
        
    }
    
    public static String propNamePrefix(String namespace, String appName) {
        
        String retval = GcalLauncher.regulariseNamespace(namespace);
        
        
        if ( Objects.requireNonNull(appName).isEmpty() ) {
            GcalLauncher.logger.error("A non-empty string is required for the application name");
            throw new IllegalArgumentException();
        }
        
        return retval + appName + ".";
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
 
    public String getAppName() {
        return this.appSpec.getDefaultValue();
    }
    
    public File newInputFile( File wdir ) throws IOException {
        if ( this.uniqueFilenames )
            return File.createTempFile( this.appSpec.getDefaultValue() + "_", ".in", wdir );
        else
            return new File(wdir, this.appSpec.getDefaultValue() + ".in");
    }

    /**
     * Get the file that needs to be read for the results of the application.
     * 
     * @return file containing application's results, or {@code null}
     */
    public File getOutputFile() {
        return this.outfileName == null ? null : new File(this.outfileName);
    }

    /**
     * Does the application accept the {@code --output} option?
     * 
     * @return
     */
    protected abstract boolean useOutputOpt();
    
}
