/*
 * Copyright © 2010, 2015 by Global Phasing Ltd. All rights reserved
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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Writer;

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
 * <a href="http://stackoverflow.com/a/1732506/1866402">
 * http://stackoverflow.com/questions/1732455/redirect-process-output-to-stdout</a>.
 *
 * @see java.lang.ProcessBuilder
 * 
 * @author pkeller
 *
 */
public class StreamPrinter extends Thread {
    private InputStream is;
    private Writer outputWriter = null;
    private File outputFile;
    private boolean append;

    String lastLine = null;
    
    /**
     * Capture the line-by-line output of a stream and direct it to a {@link Writer}, a {@link File}, or both.
     * {@code outputWriter} is flushed after every line, so can be used where an immediate
     * response to new data on {@code inputStream} is needed. {@code outputFile} is not
     * flushed to reduce the likelihood of writes to it blocking. If output to only one of
     * {@code outputWriter} and {@code outputFile} is needed, use {@code null} for the
     * other one.
     * 
     * @param inputStream
     * @param outputWriter
     * @param outputFile
     * @param append if {@code true}, output is appended to {@code outputFile}.
     */
    public StreamPrinter(InputStream inputStream, Writer outputWriter, File outputFile, boolean append) {
        this.is = inputStream;
        this.outputWriter = outputWriter;
        this.outputFile = outputFile;
        this.append = append;
    }

    @Override
    public void run() {

        try {
            final Writer fileWriter =
                    this.outputFile == null ? null : new FileWriter(outputFile, this.append);

            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                String line=null;

                // For efficiency, don't test for destinations inside a
                // "while ( (line = br.readLine()) != null)" loop
                if ( this.outputWriter != null && fileWriter == null ) {
                    while ( (line = br.readLine()) != null) {
                        this.outputWriter.write(line + "\n");
                        this.outputWriter.flush();
                        this.lastLine = line;
                    }
                }
                else if ( this.outputWriter != null && fileWriter != null ) {
                    while ( (line = br.readLine()) != null) {
                        this.outputWriter.write(line + "\n");
                        this.outputWriter.flush();
                        fileWriter.write(line + "\n");
                        // Don't flush file writer.
                        this.lastLine = line;
                    }
                }
                else if ( this.outputWriter == null && fileWriter != null ) {
                    fileWriter.write(line + "\n");
                    this.lastLine = line;
                }
            }
            finally {
                if ( fileWriter != null )
                    fileWriter.close();
            }
        }
        catch (IOException e) {
            // Don't abort workflow over this: it may still run OK even if
            // the user-facing output isn't being written properly to the file
            e.printStackTrace();
        }
    }

    public String getLastLine() {
        return lastLine;
    }
    
}
