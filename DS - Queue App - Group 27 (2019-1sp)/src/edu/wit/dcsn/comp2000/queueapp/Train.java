/*
 * Dave Rosenberg 
 * Comp 2000 - Data Structures 
 * Lab 3: Queue application - Train Simulation 
 * Spring, 2019
 * 
 * Usage restrictions:
 * 
 * You may use this code for exploration, experimentation, and furthering your
 * learning for this course. You may not use this code for any other
 * assignments, in my course or elsewhere, without explicit permission, in
 * advance, from myself (and the instructor of any other course). Further, you
 * may not post or otherwise share this code with anyone other than current
 * students in my sections of this course. Violation of these usage restrictions
 * will be considered a violation of the Wentworth Institute of Technology
 * Academic Honesty Policy.
 */

/**
 * Representation of a train on a train route. A Train has a fixed, limited
 * capacity to carry Passengers. Passengers board() and disembark().
 * 
 * <p>
 * NOTE: This class is incomplete - you may want to restructure it based on your
 * implementation's requirements.
 * 
 * <p>
 * <b>WARNING</b>: Some CIRCULAR route functionality is not yet implemented!
 * 
 * <p>
 * Note: You may use this class, with or without modification, in your Comp
 * 2000, Queue application/Train Simulation solution. You must retain all
 * authorship comments. If you modify this, add your authorship to mine.
 */

package edu.wit.dcsn.comp2000.queueapp;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import edu.wit.dcsn.comp2000.queueapp.Configuration.TrainSpec;

/**
 * Representation of a train on a train route. A Train has a fixed, limited
 * capacity to carry Passengers. Passengers board() and disembark().
 * 
 * <p>
 * NOTE: This class is incomplete - you may want to restructure it based on your
 * implementation's requirements.
 * 
 * <p>
 * <b>WARNING</b>: Some CIRCULAR route functionality is not yet implemented!
 * 
 * <p>
 * Note: You may use this class, with or without modification, in your Comp
 * 2000, Queue application/Train Simulation solution. You must retain all
 * authorship comments. If you modify this, add your authorship to mine.
 * 
 * @author David M Rosenberg
 * @version 1.0.0 base version
 */
public final class Train {
	// class-wide/shared information
	private static int nextId = 1; // enables automatic id assignment

	// per-instance fields
	private final int id; // unique id for this train route

	private final int capacity;
	private Location currentLocation;
	private ArrayList<Passenger> passengers;

	/**
	 * @param onRoute            the instance of the TrainRoute on which this Train
	 *                           operates
	 * @param trainSpecification the specifications from the configuration file
	 */
	public Train(TrainRoute onRoute, TrainSpec trainSpecification) {
		id = Train.nextId++; // assign the next unique id

		// create an empty collection to hold Passengers while they're on board
		passengers = new ArrayList<>();

		// save the configuration parameters
		capacity = trainSpecification.capacity;
		currentLocation = new Location(onRoute, trainSpecification.location, trainSpecification.direction);

	} // end 2-arg constructor

	// public int getNumPassengers() {
	// int num = 0;
	// int
	// }

	public int getID() {
		return this.id;
	}

	/**
	 * determines if current size is greater than or equal to the max capacity
	 * 
	 * @return boolean
	 */
	public boolean isFull() {
		if (passengers.size() >= capacity)
			return true;

		else
			return false;
	}

	/**
	 * Retrieves the capacity (maximum number of Passengers simultaneously on this
	 * train) as specified in the configuration file
	 * 
	 * @return the capacity was set when the train was instantiated
	 */
	public int getCapacity() {
		return capacity;
	} // end getCapacity()

	/**
	 * Retrieves the current location along a route
	 * 
	 * @return the current location object
	 */
	public Location getLocation() {
		return currentLocation;
	} // end getLocation()

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%s %,d", getClass().getSimpleName(), id);
	} // end toString()

	/**
	 * compare the toLocation of all train passengers to the train's location gives
	 * the passenger to the station and removes the passenger from the train if its
	 * a match
	 */
	public void disembark(Station station) // compare all passengers' toLocation to the train's location
											// if == remove passenger and label passenger end time
	{
		int count = 0;
		for (int i = 0; i < passengers.size(); i++) {
			if (passengers.get(i).getTo().compareTo(this.getLocation()) == 0) {
				station.trainArrival(passengers.get(i));
				passengers.remove(i);
				i--;
				count++;
			}
		}
		if (count > 0) {
			System.out.println("  [Passenger Update] " + count + " Passenger(s) just left " + station.toString());
			Logger.write("  [Passenger Update] " + count + " Passenger(s) just left " + station.toString());
		}

	}

	/**
	 * takes a passenger from platform queue into train's passenger array unless at
	 * max capacity. if the passenger boards, return true. otherwise, return false
	 * 
	 * @param passenger
	 * @return whether the passenger boarded
	 */
	public void board(Passenger passenger)

	{
		if (passengers.size() < capacity) {
			passengers.add(passenger);
			passenger.setTimeBoarded(TrainSimulation.tick);

		}

		return;
	}

	// TODO complete this

	/**
	 * Unit test driver
	 * 
	 * @param args -unused-
	 * @throws FileNotFoundException see {@link Configuration#Configuration()}
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Configuration theConfig = new Configuration();
		TrainRoute theRoute = new TrainRoute(theConfig.getRoute());
		TrainSpec[] theTrainSpecs = theConfig.getTrains();

		System.out.printf("Using configuration:%n\t%s%n", Arrays.toString(theTrainSpecs));

		System.out.println("The result is:");

		for (TrainSpec aTrainSpecification : theTrainSpecs) {
			Train aTrain = new Train(theRoute, aTrainSpecification);
			System.out.printf("\t%s is %s with capacity %,d%n", aTrain, aTrain.currentLocation, aTrain.capacity);
		} // end foreach()

	} // end test driver main()

} // end class TrainRoute
