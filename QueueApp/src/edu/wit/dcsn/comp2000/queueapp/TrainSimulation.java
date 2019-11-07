/*
 * Owen Moreau, Jamie Kerr, Griffin Campbell
 * Comp 2000 - Data Structures 
 * Lab 3: Queue application - Train Simulation 
 * Spring, 2019
 *
 */
package edu.wit.dcsn.comp2000.queueapp;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;
import edu.wit.dcsn.comp2000.queueapp.Configuration.PairedLimit;
import edu.wit.dcsn.comp2000.queueapp.Configuration.TrainSpec;


/**
 * @author Owen Moreau
 * @version 1.0.0
 */
public class TrainSimulation {

	public static int tick = 1;

	/**
	 * @param args -unused-
	 */
	public static void main(String[] args) throws FileNotFoundException {

		// ----------------------------------------------
		// INITIAL SETUP
		// ----------------------------------------------

		Configuration theConfig = new Configuration();
		Logger.create(); // Logger
		Logger.write("Start Logger for Train Simulation");
		Logger.logConfiguration(new Configuration());

		TrainRoute theRoute = new TrainRoute(theConfig.getRoute());
		int[] theStationSpecs = theConfig.getStations();
		PairedLimit[] thePassengerSpecs = theConfig.getPassengers();
		TrainSpec[] theTrainSpecs = theConfig.getTrains();
		System.out.println("\n==================================================================\n");
		Logger.write("\n==================================================================\n");

		System.out.printf(" * Using configurations: *%n%n  [CONFIG] %-20s\t: %s%n  [CONFIG] %-20s\t: %s%n", "Stations",
				Arrays.toString(theStationSpecs), "Passengers", Arrays.toString(thePassengerSpecs));

		// create a pseudo-random number generator instance
		Random pseudoRandom = new Random(theConfig.getSeed());

		int minimumPassengers = thePassengerSpecs[Configuration.PASSENGERS_INITIAL].minimum;
		int maximumPassengers = thePassengerSpecs[Configuration.PASSENGERS_INITIAL].maximum;
		int newPassengerCount = minimumPassengers == maximumPassengers ? minimumPassengers
				: pseudoRandom.nextInt(maximumPassengers - minimumPassengers) + minimumPassengers + 1;

		// setup Stations for simulation
		for (int stationPosition : theStationSpecs) {
			Station aStation = new Station(theRoute, stationPosition);
			System.out.printf("  [CONFIG] %s is %s%n", aStation, aStation.getLocation());
			try {
				theRoute.addStation(aStation);
			} catch (NullPointerException e) {
				System.out.println("----------NPE-----------");
			}
		}

		// setup Trains for simulation
		for (TrainSpec aTrainSpecification : theTrainSpecs) {
			Train aTrain = new Train(theRoute, aTrainSpecification);
			System.out.printf("  [CONFIG] %s is %s with capacity %,d%n", aTrain, aTrain.getLocation(),
					aTrain.getCapacity());
			try {
				theRoute.addTrain(aTrain);
			} catch (NullPointerException e) {
				System.out.println("----------NPE-----------");
			}

		}
		System.out.println("\n==================================================================\n");
		Logger.write("\n==================================================================\n");

		// Instantiate initial Passengers
		System.out.printf(" Generating %d passengers before the simulation starts:%n%n", newPassengerCount);
		Logger.write(" Generating " + newPassengerCount + " passengers before the simulation starts:\n\n");

		for (int passengerCount = 1; passengerCount <= newPassengerCount; passengerCount++) {
			Passenger aPassenger = new Passenger(
					new Location(theRoute, theStationSpecs[pseudoRandom.nextInt(theStationSpecs.length)],
							Direction.NOT_APPLICABLE),
					new Location(theRoute, theStationSpecs[pseudoRandom.nextInt(theStationSpecs.length)],
							Direction.NOT_APPLICABLE),
					0);

			// this sends passengers to specific Queue
			theRoute.assignStation(aPassenger);

		}

		minimumPassengers = thePassengerSpecs[Configuration.PASSENGERS_PER_TICK].minimum;
		maximumPassengers = thePassengerSpecs[Configuration.PASSENGERS_PER_TICK].maximum;

		// Write to console and logger
		Logger.write("\n\n==============================================================");
		Logger.write("                   SIMULATION START                           ");
		Logger.write("==============================================================");

		System.out.println("\n\n==============================================================");
		System.out.println("                   SIMULATION START                           ");
		System.out.println("==============================================================");

		// - determine number of simulation loops from config file
		int simulationLoops = theConfig.getTicks();

		// - this loop runs the simulation
		for (int currentTime = 1; currentTime <= simulationLoops; currentTime++) {

			// Overview (each tick):
			// Passengers generated AND sent to correct platform
			// Each Passenger in Each Train must get off and return to Station
			// Each Queue dequeues Passengers IF the Train isn't empty
			// MOVE TRAINS

			// determine this tick's passenger count
			newPassengerCount = minimumPassengers == maximumPassengers ? minimumPassengers
					: pseudoRandom.nextInt(maximumPassengers - minimumPassengers) + minimumPassengers + 1;
			System.out.printf("%n%n[Tick %d]: Generating %d passengers (per-tick):%n%n", currentTime,
					newPassengerCount);
			Logger.write("\n\n[Tick " + currentTime + "]: Generating " + newPassengerCount + " passengers (per-tick):\n\n");
			
			// actually generate passengers
			for (int passengerCount = 1; passengerCount <= newPassengerCount; passengerCount++) {

				// instantiate new Passenger object
				Passenger aPassenger = new Passenger(
						new Location(theRoute, theStationSpecs[pseudoRandom.nextInt(theStationSpecs.length)],
								Direction.NOT_APPLICABLE),
						new Location(theRoute, theStationSpecs[pseudoRandom.nextInt(theStationSpecs.length)],
								Direction.NOT_APPLICABLE),
						currentTime);

				// add Passenger to Queue
				theRoute.assignStation(aPassenger);

			}

			theRoute.trainInteractions();

			// this tick should only update at the end of the SIMULATION LOOP
			tick++;
		}
		
		System.out.println("\n\n==============================================================");
		System.out.println("                     SIMULATION END                           ");
		System.out.println("==============================================================");
		
		Logger.write("\n\n==============================================================");
		Logger.write("                     SIMULATION END                           ");
		Logger.write("==============================================================");
		
		Logger.close();
	} // end main()

} // end class
