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

package co.gphl.common.threads;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;

import co.gphl.common.io.streams.StreamPrinter;
import co.gphl.common.io.logging.LoggerUtils;

/**
 * @author pkeller
 *
 */
public class ProcessLauncher {

    private ProcessBuilder processBuilder;
    private String lastOutLine, lastErrLine;

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
     * <p>If any loggers are writing to the streams specified by {@code stdout}
     * or {@code stderr}, it may be helpful to flush the logger's handlers,
     * by {@link LoggerUtils#flush(java.util.logging.Logger)} or otherwise before
     * calling this method.</p>
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
    public void startAndWait(Writer stdout, Writer stderr, File stdoutFile, File stderrFile, boolean append)
            throws TerminationException, IOException, InterruptedException {

        this.processBuilder.redirectErrorStream( stderr == null && stderrFile == null );
        String cmd = this.commandLine(80, "\\\n");
        Process process = this.processBuilder.start();

        StreamPrinter outPrinter = new StreamPrinter(process.getInputStream(), stdout, stdoutFile, append, cmd);
        outPrinter.start();

        StreamPrinter errPrinter = null;
        if ( ! this.processBuilder.redirectErrorStream() ) {
            errPrinter = new StreamPrinter(process.getErrorStream(), stderr, stderrFile, append, null);
            errPrinter.start();
        }

        int status = process.waitFor();

        // Make sure that we don't race ahead of the output
        outPrinter.join();
        if ( errPrinter != null )
            errPrinter.join();

        this.lastOutLine = outPrinter.getLastLine();
        this.lastErrLine = errPrinter == null ? this.lastOutLine : errPrinter.getLastLine();

        if (status != 0)
            throw new TerminationException("Command exited with status " + status
                    + ":\n" + this.commandLine(80, "\n") );

    }

    /**
     * Invokes {@link #startAndWait(PrintStream, PrintStream)} with
     * {@link System#out} and {@link System#err} as first and second parameters.
     * 
     * @throws TerminationException
     * @throws IOException
     * @throws InterruptedException
     */
    public void startAndWait() throws TerminationException,
    IOException, InterruptedException {

        this.startAndWait(new PrintWriter(System.out), new PrintWriter(System.err), null, null, false );

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

}
