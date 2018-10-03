/*******************************************************************************
 * Copyright (c) 2010, 2015 Global Phasing Ltd.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *******************************************************************************/

package co.gphl.common.threads;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import co.gphl.common.io.streams.StreamPrinter;

/**
 * @author pkeller
 *
 */
public class ProcessLauncher implements Runnable {

    private ProcessBuilder processBuilder;
    private String lastOutLine, lastErrLine;

    private Writer stdout, stderr;
    private File stdoutFile, stderrFile;
    private boolean append, captureLastLine;
    private Integer status = null;
    
    /**
     * Creates a launcher ready to run a process specified by {@code processBuilder}.
     * 
     * 
     * @param processBuilder
     * @throws IllegalArgumentException unless {@code processBuilder}
     * has an absolute working directory defined
     */
    public ProcessLauncher(ProcessBuilder processBuilder) {

        File dir = processBuilder.directory();
        if ( dir == null || ! dir.isDirectory() || !dir.isAbsolute() )
            throw new IllegalArgumentException(
                    "processBuilder needs to have an absolute directory specified");

        this.processBuilder = processBuilder;
    }

    /**
     * Starts the process specified to the constructor, copies the standard
     * output and error of the process to {@code stdout}/{@code stdoutFile}
     * and {@code stderr}/{@code stderrFile}, and waits until the process has finished.
     * {@code if (stderr == null && stderrFile == null)}, standard 
     * error of the process will be merged with standard output.
     * 
     * @param stdout
     * @param stderr
     * @param stdoutFile
     * @param stderrFile
     * @throws TerminationException if the subprocess exits with a non-zero status
     * @throws IOException
     * @throws InterruptedException
     * @see ProcessBuilder#redirectErrorStream(boolean)
     */
    public void startAndWait(Writer stdout, Writer stderr, File stdoutFile, File stderrFile,
            boolean append, boolean captureLastLine)
            throws TerminationException, IOException, InterruptedException {

        this.stdout = stdout;
        this.stderr = stderr;
        this.stdoutFile = stdoutFile;
        this.stderrFile = stderrFile;
        this.append = append;
        this.captureLastLine = captureLastLine;
        
        this.run();
        
    }

    /**
     * Invokes {@link #startAndWait(Writer, Writer, File, File, boolean, boolean)} as
     * {@code this.startAndWait(new PrintWriter(System.out), new PrintWriter(System.err), null, null, false, captureLastLine )}
     * 
     * @param captureLastLine if {@code true}, an attempt will be made to capture the last lines
     * of stdout and stderr
     * 
     * @throws TerminationException
     * @throws IOException
     * @throws InterruptedException
     */
    public void startAndWait(boolean captureLastLine) throws TerminationException,
    IOException, InterruptedException {

        this.startAndWait(new PrintWriter(System.out), new PrintWriter(System.err), null, null, false, captureLastLine );

    }

    public String getLastOutLine() {
        return lastOutLine;
    }

    public String getLastErrLine() {
        return lastErrLine;
    }

    private String commandLine(int width, String lineBreak) {

        StringBuilder retval = new StringBuilder();
        int curWidth = 0;

        for (String token: this.processBuilder.command() ) {

            if ( curWidth > 0 ) {
                retval.append(' ');
                curWidth++;

                if ( curWidth + token.length() > width ) {
                    retval.append(lineBreak);
                    curWidth = 0;
                }
            }

            retval.append(token);
            curWidth += token.length();
        }

        retval.append("\n\n");

        return retval.toString();

    }

    public void run() {
        
        this.processBuilder.redirectErrorStream( stderr == null && stderrFile == null );
        String cmd = this.commandLine(80, "\\\n");
        Process process;
        try {
            process = this.processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        StreamPrinter outPrinter = new StreamPrinter(process.getInputStream(), stdout, stdoutFile,
                append, cmd, 0, 0, captureLastLine);
        outPrinter.start();

        StreamPrinter errPrinter = null;
        if ( ! this.processBuilder.redirectErrorStream() ) {
            errPrinter = new StreamPrinter(process.getErrorStream(), stderr, stderrFile,
                    append, null, 0, 0, captureLastLine);
            errPrinter.start();
        }

        try {
            this.status = process.waitFor();

            // Make sure that we don't race ahead of the output
            outPrinter.join();
            if ( errPrinter != null )
                errPrinter.join();
        }
        catch ( InterruptedException e) {
            throw new RuntimeException(e);
        }
        
        this.lastOutLine = captureLastLine ? outPrinter.getLastLine() : null;
        this.lastErrLine = errPrinter == null || captureLastLine ? this.lastOutLine : errPrinter.getLastLine();

        if (this.status != 0)
            throw new RuntimeException(new TerminationException("Command exited with status " + status
                    + ":\n" + this.commandLine(80, "\n") ));


    }
    
}
