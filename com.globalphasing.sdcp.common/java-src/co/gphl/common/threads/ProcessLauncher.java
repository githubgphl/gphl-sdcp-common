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

package co.gphl.common.threads;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

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
	 * output and error of the process to {@code out} and {@code err}, and
	 * waits until the process has finished. If {@code err == null}, standard 
	 * error of the process will be merged with standard output.
	 * 
	 * <p>If any loggers are writing to the streams specified by {@code out}
	 * or {@code err}, it may be helpful to flush the logger's handlers,
	 * by {@link LoggerUtils#flush(java.util.logging.Logger)} or otherwise before
	 * calling this method.</p>
	 * 
	 * @param out
	 * @param err
	 * @throws TerminationException if the subprocess exits with a non-zero status
	 * @throws IOException
	 * @throws InterruptedException
	 * @see ProcessBuilder#redirectErrorStream(boolean)
	 */
	public void startAndWait(PrintStream out, PrintStream err)
	        throws TerminationException, IOException, InterruptedException {

		this.processBuilder.redirectErrorStream( err == null );
		Process process = this.processBuilder.start();

		StreamPrinter outPrinter = new StreamPrinter(process.getInputStream(), out);
		outPrinter.start();

		StreamPrinter errPrinter = null;
		if ( err != null ) {
			errPrinter = new StreamPrinter(process.getErrorStream(), err);
			errPrinter.start();
		}
		
		int status = process.waitFor();

		this.lastOutLine = outPrinter.getLastLine();
		this.lastErrLine = errPrinter == null ? this.lastOutLine : errPrinter.getLastLine();
		
		if (status != 0) {
			String cmd = "";
			for (String c : processBuilder.command())
				cmd += c + " ";
			throw new TerminationException("Command '" + cmd
					+ "'\n exited with status " + status);
		}
		
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
		
		this.startAndWait(System.out, System.err);
		
	}

    public String getLastOutLine() {
        return lastOutLine;
    }

    public String getLastErrLine() {
        return lastErrLine;
    }
	
	
}
