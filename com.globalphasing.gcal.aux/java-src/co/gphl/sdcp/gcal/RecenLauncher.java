/*******************************************************************************
 * Copyright (c) 2017 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/
package co.gphl.sdcp.gcal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FIXME! Make this reusable, so that we don't have to create
 * a new instance for each run.
 * 
 * @author pkeller
 *
 */
public class RecenLauncher extends GcalLauncher {

    private static Logger logger = 
            LoggerFactory.getLogger(RecenLauncher.class);
    
    public static final String appName = "recen";
    
    private static Map<String, String> propNames = null;
    
    public static final String INITXYZ = "initxyz";
    public static final String INITOKP = "initokp";
    
    private boolean okpSet = false;
    private String strOkp = "";
    
    // This regex for floats is inspired by the source for java.lang.Double in
    // OpenJDK
    // TODO: cater for exponents? CF says he is unlikely to output them though.
    private static final String floatRegex = "\\s*[+-]?((\\d+\\.?\\d*)|(\\.\\d+))";
    private static final String XYZGROUP = "XYZ";
    private static final String xyzRegex = "\\s*O,K,P =(" + floatRegex + "){3}\\s*"
            + "X,Y,Z =(?<" + XYZGROUP + ">"
                    + "(" + floatRegex +"){3}"  // Contents of capturing group XYZ
            + ")\\s*";
    private static final Pattern xyzPattern = Pattern.compile(xyzRegex);
    
    
    /**
     * @param propNameNamespace
     * @param properties
     * @param stdoutWriter
     * @param stderrWriter
     * @param outputToFile
     * @param redirectErrorStream
     */
    public RecenLauncher(
            String propNameNamespace, Properties properties,
            Writer stdoutWriter, Writer stderrWriter, boolean outputToFile,
            boolean redirectErrorStream) {
        super(RecenLauncher.logger, appName, propNameNamespace, properties, stdoutWriter,
                stderrWriter, outputToFile, redirectErrorStream);
    }

    public void setOkp( List<Double> okp ) {
        if ( Objects.requireNonNull(okp).size() != 3 )
            throw new IllegalArgumentException(
                    "Must specify a list of 3 numbers for (omega, kappa, phi)");
        this.strOkp = "";
        for ( Double s: okp )
            this.strOkp += " " + Double.toString(s);
        this.args.put("--okp", this.strOkp);
        this.okpSet = true;
    }
    
    /**
     * Set the argument to the {@code --init-okp} option to recen. Use this method
     * where the initial (Omega, Kappa, Phi) settings are not present in the
     * properties used to instantiate this launcher.
     * 
     * @param okp
     */
    public void setInitOkp( List<Double> okp ) {
        
        String strOkp = "";
        for ( Double s: okp )
            strOkp += s.toString() + " ";
        this.properties.setProperty(this.propNamePrefix + RecenLauncher.INITOKP, strOkp);
    }
    
    /**
     * Set the argument to the {@code --init-xyz} option to recen. Use this method
     * where the initial (X, Y, Z) settings are not present in the
     * properties used to instantiate this launcher.
     *
     * @param xyz
     */
    public void setInitXyz( List<Double> xyz ) {
        String strXyz = "";
        for (Double s: xyz )
            strXyz += s.toString() + " ";
        this.properties.setProperty(this.propNamePrefix + RecenLauncher.INITXYZ, strXyz);
    }
    
    public List<Double> getXyz() {
        
        if ( this.lastErrLine == null )
            throw new IllegalStateException("(X,Y,Z) only available after launch(File, File) method has been called");
        
        List<String> lines = new ArrayList<>();
        Matcher lastXyzMatched = null;
        try ( BufferedReader reader = new BufferedReader(new FileReader(this.getOutputFile()))  ) {
            String line;
            Matcher xyzMatcher;
            while ( ( line = reader.readLine() ) != null ) {
                xyzMatcher = xyzPattern.matcher(line);
                if ( xyzMatcher.matches() ) {
                    lines.add(line);
                    lastXyzMatched = xyzMatcher;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        List<Double> retval = null;
        
        if ( lines.size() == 0 )
            logger.info("No (X,Y,Z) values found in recen output" );
        else if ( lines.size() > 1 )
            logger.info("{} lines containing (X,Y,Z) values found in recen output: returning null", 
                    lines.size());
        else {
            String xyzStr = lastXyzMatched.group(RecenLauncher.XYZGROUP);
            retval = new ArrayList<>();
            logger.info("Found (X,Y,Z) values {}", xyzStr);
            for ( String s: xyzStr.trim().split("\\s+", 3) )
                retval.add( Double.valueOf(s) );
        }
        
        return retval;
    }
    
    @Override
    protected void _pre_launch(File wdir, File input) {
        super._pre_launch(wdir, input);
        if ( ! this.okpSet )
            throw new IllegalStateException("Cannot launch " + this.appName +
                    ": have you called setOkp() ?");
    }
    
    @Override
    protected void _post_launch(File wdir, File input) {
        super._post_launch(wdir, input);
        
        if ( this.stdout != null ) {
            this.strOkp = this.strOkp.replace(" ", "_");
            String newFileName = this.stdout.getName().replaceFirst("\\.stdout$", this.strOkp + ".stdout");
            File newFile = new File(wdir, newFileName);
            this.stdout.renameTo(newFile);
            this.stdout = newFile;
            
            if ( this.stderr != null ) {
                newFileName = this.stderr.getName().replaceFirst("\\.stderr$", this.strOkp + ".stderr");
                newFile = new File(wdir, newFileName);
                this.stderr.renameTo(newFile);
                this.stderr = newFile;
            }
        }
        
    }

    @Override
    public File getOutputFile() {
        return this.stdout;
    }
    
    @Override
    protected Map<String, String> getPropNames() {
        
        if ( RecenLauncher.propNames == null ) {
            RecenLauncher.propNames = new HashMap<>(super.getPropNames());
            RecenLauncher.propNames.put(INITXYZ, "--init-xyz");
            RecenLauncher.propNames.put(INITOKP, "--init-okp");
        }
        
        return Collections.unmodifiableMap(RecenLauncher.propNames);
        
    }

    /* (non-Javadoc)
     * @see co.gphl.sdcp.gcal.GcalLauncher#useOutputOpt()
     */
    @Override
    protected boolean useOutputOpt() {
        return false;
    }
 
    /**
     * External method to check that binary is usable.
     * 
     * @deprecated because we need to sort out a better way of doing this
     */
    @Deprecated @Override
    public boolean check_bin_ok() {
        return super.check_bin_ok();
    }
    
}
