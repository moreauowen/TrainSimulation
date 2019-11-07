/*
 * Owen Moreau, Jamie Kerr, Griffin Campbell
 * Comp 2000 - Data Structures
 * Lab 3: Queue application - Train Simulation
 * Spring, 2019
 *
 */
package edu.wit.dcsn.comp2000.queueapp;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;


/**
 * @author Owen Moreau
 * @version 1.0.0
 */
public final class Station {
	// class-wide/shared information
	private static int nextId = 1; // enables automatic id assignment

	// per-instance fields
	private final int id; // unique id for this station

	private final Location location;
	private HashMap<Direction, Queue<Passenger>> platforms;
	private ArrayList<Passenger> platformsToExit;

	/**
	 * @param onRoute         the instance of the TrainRoute on which this Train
	 *                        operates
	 * @param positionOnRoute the specifications from the configuration file
	 */
	public Station(TrainRoute onRoute, int positionOnRoute) {
		id = Station.nextId++; // assign the next unique id

		// create a collection of platforms, determine the directions based on the route
		// style,
		// and create a pair of platforms indexable by the direction they service
		platforms = new HashMap<>();

		Direction oneDirection = onRoute.getStyle() == RouteStyle.LINEAR ? Direction.OUTBOUND : Direction.CLOCKWISE;
		platforms.put(oneDirection, new LinkedList<Passenger>());
		platforms.put(oneDirection.reverse(), new LinkedList<Passenger>());

		// create a collection of same platforms for passengers exiting
		platformsToExit = new ArrayList<Passenger>();

		// save the position along the route
		location = new Location(onRoute, positionOnRoute, Direction.STATIONARY);

	} // end 2-arg constructor

	/**
	 * Retrieves the location for this station
	 * 
	 * @return the location object for this station
	 */
	public Location getLocation() {
		return location;
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
	 * Validates passenger is at right destination
	 * 
	 * @param passenger
	 * @return true/false
	 */
	private boolean checkDestination(Passenger passenger) {
		if (passenger.getTo().equals(this.getLocation())) {
			return true;
		}

		return false;
	}

	/**
	 * Handles spawing of passengers, adds to appropriate platform based on
	 * destination
	 * 
	 * @param passenger
	 */
	public void stationArrival(Passenger passenger, Direction passengerDirection) {

		if (passengerDirection.equals(Direction.OUTBOUND)) // adds passengers to the outbound platform
		{
			platforms.get(passengerDirection).add(passenger);
		}
		if (passengerDirection.equals(Direction.INBOUND)) // adds passengers to the inbound platform
		{
			platforms.get(passengerDirection).add(passenger);
		}

		System.out.println("  [Passenger Spawn] " + passenger.toString() + " has arrived at " + this.toString());
		Logger.write("  [Passenger Spawn] " + passenger.toString() + " has arrived at " + this.toString());
	}

	public void board(Train train) {

		Direction trainDirection = train.getLocation().getDirection();

		while (!train.isFull() && !platforms.get(trainDirection).isEmpty()) {

			Passenger passenger = platforms.get(trainDirection).remove();
			train.board(passenger);
			System.out.println("  [Passenger Update] " + passenger.toString() + " has boarded " + train.toString());
			Logger.write("  [Passenger Update] " + passenger.toString() + " has boarded " + train.toString());
		}

	}

	/**
	 * Directs passengers to station exit
	 * 
	 * @param passenger
	 */
	private void stationExit(Passenger passenger) {
		passenger.setTimeEnded(TrainSimulation.tick);
		System.out.println("  [Passenger Exit] " + passenger.toString() + " has stepped off at " + this.toString()
				+ " at time " + passenger.getEndTime());
		Logger.write(passenger.toStringFull());
		platformsToExit.remove(passenger);
	}

	/**
	 * Adds passenger to platform from train based on inbound/outbound direction
	 * 
	 * @param passenger
	 */
	public void trainArrival(Passenger passenger) {

		platformsToExit.add(passenger);

		stationExit(passenger);

	}

	public int getID() {
		return this.id;
	}

	/**
	 * Unit test driver
	 * 
	 * @param args -unused-
	 * @throws FileNotFoundException see {@link Configuration#Configuration()}
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Configuration theConfig = new Configuration();

		TrainRoute theRoute = new TrainRoute(theConfig.getRoute());
		int[] theStationSpecs = theConfig.getStations();

		System.out.printf("Using configuration:%n\t%s%n", Arrays.toString(theStationSpecs));

		System.out.println("The result is:");

		for (int stationPosition : theStationSpecs) {
			Station aStation = new Station(theRoute, stationPosition);
			System.out.printf("\t%s is %s%n", aStation, aStation.getLocation());
		} // end foreach()
	} // end test driver main()

} // end class TrainRoute
