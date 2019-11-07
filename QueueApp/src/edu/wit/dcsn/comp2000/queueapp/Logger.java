/*
 * Owen Moreau, Jamie Kerr, Griffin Campbell
 * Comp 2000 - Data Structures
 * Lab 3: Queue application - Train Simulation
 * Spring, 2019
 *
 */
package edu.wit.dcsn.comp2000.queueapp;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * @author Owen Moreau
 * @version 1.0.0
 */
public final class Logger {

	// only one log can be open at a time
	private static PrintWriter log = null;

	/**
	 * prevent instantiation
	 */
	private Logger() {
	}

	/**
	 * Creates and opens the log file TrainSimulation.log in the default location
	 * (root folder). If the file already exists, it is replaced.
	 * 
	 * @throws FileNotFoundException most likely cause is the file is open in
	 *                               another application
	 */
	public static void create() throws FileNotFoundException {
		log = new PrintWriter("./data/TrainSimulation.log");
	}

	/**
	 * Closes the log file, releasing all allocated resources.
	 */
	public static void close() {
		log.close();
		log = null;
	}

	/**
	 * Writes all of the configuration parameters into the log
	 * 
	 * @param theConfiguration the configuration for this execution
	 */
	public static void logConfiguration(Configuration theConfiguration) {
		// introduce the information
		Logger.write("\n----------\nConfiguration:\n");

		// let Configuration format the output
		Logger.write(theConfiguration.toString());

		// separate the configuration from the rest of the log
		Logger.write("----------\n");

	}

	/**
	 * Appends the provided text to the log, including a terminating newline.
	 * 
	 * @param logEntry the text to append to the log.
	 */
	public static void write(String logEntry) {
		log.println(logEntry);
	}

	/**
	 * Unit test driver.
	 * 
	 * @param args -unused-
	 * @throws FileNotFoundException refer to {@link #create()}
	 */
	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("Starting Logger testing\n");

		System.out.println("create the log and insert an intro message");

		Logger.create(); // create the file

		Logger.write("Start Logger test");

		// we'll write the configuration to the log file
		System.out.println("dump the configuration into the log");
		Logger.logConfiguration(new Configuration());

		System.out.println("insert concluding message and close the log");
		Logger.write("Finish Logger test");

		Logger.close(); // release the resources

		System.out.println("\nFinished Logger testing");
	}

}
