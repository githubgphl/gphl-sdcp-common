/*
 * Copyright © 2010-2011 by Global Phasing Ltd. All rights reserved
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

package co.gphl.common.io.streams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Class to use a {@link PrintStream} to output the data being received
 * by an {@link InputStream}. A typical usage pattern is:
 * 
 * <pre>
 *  Process process = processBuilder.start();
 *  StreamPrinter errPrinter = new StreamPrinter(process.getErrorStream(), System.err);
 *  StreamPrinter outPrinter = new StreamPrinter(process.getInputStream(), System.out);
 *  errPrinter.start();
 *  outPrinter.start();
 *  int status = process.waitFor();
 * </pre>
 *
 * Adapted from a suggestion by Boris Daich at
 * <a href="http://stackoverflow.com/questions/1732455/redirect-process-output-to-stdout">
 * http://stackoverflow.com/questions/1732455/redirect-process-output-to-stdout</a>.
 *
 * @see java.lang.ProcessBuilder
 * 
 * @author pkeller
 *
 */
public class StreamPrinter extends Thread {
    InputStream is;
    PrintStream os;

    String lastLine = null;
    
    // reads everything from is until empty. 
    public StreamPrinter(InputStream is, PrintStream os) {
        this.is = is;
        this.os = os;
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null) {
                os.println(line);
                this.lastLine = line;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();  
        }
    }

    public String getLastLine() {
        return lastLine;
    }
    
}
