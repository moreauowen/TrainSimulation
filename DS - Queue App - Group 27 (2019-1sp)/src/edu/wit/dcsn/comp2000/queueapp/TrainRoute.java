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
import edu.wit.dcsn.comp2000.queueapp.Configuration.RouteSpec;


/**
 * @author Owen Moreau
 * @version 1.0.0
 */
public final class TrainRoute {
	// class-wide/shared information
	private static int nextId = 1; // enables automatic id assignment

	// per-instance fields
	private final int id; // unique id for this train route

	private final RouteStyle style;
	private final int length;

	private ArrayList<Station> stations;
	private ArrayList<Train> trains;

	public ArrayList<Train> getTrainsArrList() {
		return trains;
	}

	/**
	 * @param routeSpecification the route style and length from the configuration
	 *                           file
	 */
	public TrainRoute(RouteSpec routeSpecification) {
		id = TrainRoute.nextId++; // assign the next unique id

		// save the configuration parameters
		style = routeSpecification.style;
		length = routeSpecification.length;
		stations = new ArrayList<Station>();
		trains = new ArrayList<Train>();
	} // end 1-arg constructor

	/**
	 * takes in a passenger and gives them to the appropriate station
	 * 
	 * @param passenger
	 */
	public void assignStation(Passenger passenger) {
		for (Station station : stations) {
			if (passenger.getFrom().compareTo(station.getLocation()) == 0) {
				station.stationArrival(passenger, whichDirection(passenger.getFrom(), passenger.getTo()));
			}
		}
	}

	/**
	 * checks each train if at a station. if its at a station, run the train's
	 * disembark method and the station's board method
	 */
	public void trainInteractions() {
		System.out.println("\n  -----Board and Disembark Updates (if any)-----\n");
		Logger.write("\n  -----Board and Disembark Updates (if any)-----\n");
		for (Train train : trains) {
			Station trainAtStation = isAtStation(train);

			if (trainAtStation != null) {
				System.out.println(
						"  [Train Update] " + train.toString() + " has arrived at " + trainAtStation.toString());
				Logger.write("  [Train Update] " + train.toString() + " has arrived at " + trainAtStation.toString());
				train.disembark(trainAtStation);
				trainAtStation.board(train);
			}

			train.getLocation().move();
		}
		printTrainLayout();
		System.out.print("\n  All trains move one position!\n\n[End Tick]\n________________________________________"
				+ "______________________________");
		Logger.write("\n  All trains move one position!\n\n[End Tick]\n________________________________________"
				+ "______________________________");
	}

	public void printTrainLayout() {
		System.out.println("\n  -----Current Train Positions-----\n");
		Logger.write("\n  -----Current Train Positions-----\n");
		
		System.out.printf("  [Trains at Tick %d]%n", TrainSimulation.tick);
		Logger.write("  [Trains at Tick " + TrainSimulation.tick + "]\n");
		for (Train t : trains) {
			System.out.printf("   " + t.toString() + " at position " + t.getLocation().getPosition());
			Logger.write("   " + t.toString() + " at position " + t.getLocation().getPosition());
			Station s = isAtStation(t);
			if (s != null) {
				System.out.printf(" is at " + s.toString());
			}
			System.out.println();
		}

	}

	/**
	 * given a train, determine if its at a station. if it is, return the station.
	 * Otherwise, return null.
	 * 
	 * @param train
	 * @return
	 */
	public Station isAtStation(Train train) {
		for (Station station : stations) {
			if (train.getLocation().compareTo(station.getLocation()) == 0)

				return station;
		}
		return null;
	}

	/**
	 * Retrieves the route length as specified in the configuration file
	 * 
	 * @return the route length was set when the route was instantiated
	 */
	public int getLength() {
		return length;
	} // end getLength()

	/**
	 * Retrieves the route style as specified in the configuration file
	 * 
	 * @return the route style was set when the route was instantiated
	 */
	public RouteStyle getStyle() {
		return style;
	} // end getStyle()

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
	 * takes in a train and adds it to the train ArrayList
	 * 
	 * @param train
	 */
	public void addTrain(Train train) {
		trains.add(train);
	}

	/**
	 * takes in a station and adds it to the station ArrayList
	 * 
	 * @param station
	 */
	public void addStation(Station station) {
		stations.add(station);
	}

	/**
	 * Determines the direction an entity needs to travel to move from the 'from'
	 * station to the 'to' station. It takes into account the route style.
	 * 
	 * @param fromStationId starting point station id #
	 * @param toStationId   ending point station id #
	 * @return the Direction in which an entity must travel
	 */
	public Direction whichDirection(int fromStationId, int toStationId) {
		return whichDirection(stations.get(fromStationId).getLocation(), stations.get(toStationId).getLocation());
	}

	/**
	 * Determines the direction an entity needs to travel to move from the 'from'
	 * station to the 'to' station. It takes into account the route style.
	 * 
	 * @param fromStation starting point station instance
	 * @param toStation   ending point station instance
	 * @return the Direction in which an entity must travel
	 */
	public Direction whichDirection(Station fromStation, Station toStation) {
		return whichDirection(fromStation.getLocation(), toStation.getLocation());
	}

	/**
	 * Determines the Direction an entity must move to travel between <i>from</i>
	 * and <i>to</i>
	 * 
	 * <p>
	 * <b>WARNING</b>: As implemented, this method only works correctly for LINEAR
	 * routes!
	 * 
	 * @param fromLocation the Location at the start of travel
	 * @param toLocation   the Location at the destination
	 * @return the Direction in which an entity must travel
	 */
	public Direction whichDirection(Location fromLocation, Location toLocation) {
		Direction calculatedDirection = Direction.NOT_APPLICABLE;
		if (fromLocation.getRoute().equals(toLocation.getRoute())) // same route so continue
		{
			int comparison = fromLocation.compareTo(toLocation);
			if (comparison < 0) {
				calculatedDirection = Direction.OUTBOUND;
			} else if (comparison > 0) {
				calculatedDirection = Direction.INBOUND;
			} else // at same location
			{
				calculatedDirection = Direction.STATIONARY;
			}
		}

		return calculatedDirection;
	}
} // end class TrainRoute
