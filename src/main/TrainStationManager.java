package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import data_structures.HashTableSC;
import data_structures.LinkedListStack;
import data_structures.SimpleHashFunction;
import data_structures.ArrayList;
import data_structures.HashSet;

import interfaces.List;
import interfaces.Map;
import interfaces.Stack;

/**
 * Manages train stations and routes to calculate the shortest path and travel times.
 * It uses data structures like Maps, Stacks and lists to store station connections and to implement
 * Dijkstra's algorithm for finding the shortest paths from the starting station "Westside"(origin).
 * Maps are used throughout this class to maintain in order our stations and because they are good for accessability.
 * 
 * @author Wilson A Morales
 */
public class TrainStationManager {
	
    private Map<String, List<Station>> stations = new HashTableSC<String, List<Station>>(1, new SimpleHashFunction<String>());
    private Map<String, Station> shortestRoutes = new HashTableSC<String, Station>(1, new SimpleHashFunction<String>());
    private Map<String, Double> travelTimes = new HashTableSC<String, Double>(1, new SimpleHashFunction<String>());
    String origin = "Westside";
    
    /**
     * Constructor that initializes the manager and loads stations from a file.
     * Each line is read and processed into the station map which reflects the station network.
     * Utilizes lists to store stations which will be added to the map. 
     * This map will  store the stations and distances in a easy to access matter used in other methods. 
     * @param station_file the relative path to the CSV file containing station data
     */
	public TrainStationManager(String station_file) {	    
		try (BufferedReader stationReader = new BufferedReader(new FileReader("inputFiles/" + station_file))) {
            String line = stationReader.readLine();
            
            while ((line = stationReader.readLine()) != null) {
                String[] parts = line.split(",");
                String source = parts[0];
                String destination = parts[1];
                int distance = Integer.parseInt(parts[2]);
                
                Station station1 = new Station(destination, distance);
                Station station2 = new Station(source, distance);
                
                if (!stations.containsKey(source)) {
                	List<Station> list = new ArrayList<>();
					stations.put(source, list);
					stations.get(source).add(station1);
				}
                else {
                	stations.get(source).add(station1);
                }
                
                if (!stations.containsKey(destination)) {
                	List<Station> list = new ArrayList<>();
					stations.put(destination, list);
					stations.get(destination).add(station2);
				}
                else {
                	stations.get(destination).add(station2);               
                }
			}
			
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		findShortestDistance();
	}
	
	 /**
     * Implements Dijkstra's algorithm to find the shortest path from 'Westside' to all other stations and store it into a map.
     * A stack is used to represent all the stations that have been discovered and need to be visited(neighbors).
     * This stack is sorted to manage the station exploration order.
     * It also uses a set to store visited stations.
     */
	private void findShortestDistance() {
		HashSet<String> visited = new HashSet<String>();
		Stack<Station> unvisited = new LinkedListStack<Station>();
		
		for (String station : stations.getKeys()) {
			shortestRoutes.put(station, new Station (origin, Integer.MAX_VALUE));
		}
		
		unvisited.push(new Station(origin, 0));
		shortestRoutes.get(origin).setDistance(0);
		
		while (!unvisited.isEmpty()) {
	        Station currentStation = unvisited.pop();
	        visited.add(currentStation.getCityName());
	        
	        for (Station neighbor : stations.get(currentStation.getCityName())) {
	            String neighborName = neighbor.getCityName();

	            int currentShort = shortestRoutes.get(neighborName).getDistance(); // A
	            int shortestDistance = shortestRoutes.get(currentStation.getCityName()).getDistance(); // B
	            int distanceToNeighbor = neighbor.getDistance(); // C
	            int newDistance = shortestDistance + distanceToNeighbor; // B + C
	            
	            if (currentShort > newDistance) { // A > B+C
	                shortestRoutes.get(neighborName).setDistance(newDistance);
	                shortestRoutes.get(neighborName).setCityName(currentStation.getCityName());
	                
	                if (!visited.isMember(neighborName)) {
	                    unvisited.push(new Station(neighborName, newDistance));
	                    sortStack(new Station(neighborName, newDistance), unvisited);
	                }
	            }
	        }
	    }
		
	}

	/**
     * Sorts a stack of stations by distance in ascending order using a temporary stack to manage order.
     * This method is important for maintaining the queue behavior needed for Dijkstra's algorithm.
     * 
     * @param station the new station to be added to the stack
     * @param stackToSort the stack that needs to be sorted
     */
	public void sortStack(Station station, Stack<Station> stackToSort) {
	    stackToSort.push(station);

	    Stack<Station> helpStack = new LinkedListStack<>();
	    
	    while (!stackToSort.isEmpty()) {
	        Station currentStation = stackToSort.pop();
	        while (!helpStack.isEmpty() && helpStack.top().getDistance() > currentStation.getDistance()) {
	            stackToSort.push(helpStack.pop());
	        }
	        helpStack.push(currentStation);
	    }
	    while (!helpStack.isEmpty()) {
	        stackToSort.push(helpStack.pop());
	    }
	}

	/**
     * Calculates the travel time for each station from the origin based on the shortest path distances.
     * The time is calculated as the sum of travel time per kilometer plus additional time per stop.
     * 
     * @return a map of station names to their respective travel times in minutes
     */
	public Map<String, Double> getTravelTimes() {
		for (String route : shortestRoutes.getKeys()) {
		
			int stops = 0;
			String currentRoute = route;
			double distanceTime = shortestRoutes.get(route).getDistance() * 2.5;
			
			while (!currentRoute.equals(origin)) {
				stops++;
			    currentRoute = shortestRoutes.get(currentRoute).getCityName();
			}
			double stopsTime;
			
	        if (stops > 1) {
	        	stopsTime = (stops - 1) * 15;
	        } 
	        else {
	        	stopsTime = 0;
	        }		
	        
	        double totalTime = distanceTime + stopsTime;
			
			travelTimes.put(route, totalTime);
		}
		return travelTimes;
	}

	
    /**
     * Retrieves the current map of stations.
     * 
     * @return a map where the key is a station name and the value is a list of connected stations.
     */
    public Map<String, List<Station>> getStations() {
        return stations;
    }

    /**
     * Sets the map of stations to a new map.
     * 
     * @param cities a map of station names to lists of connected stations that will replace the current map.
     */
    public void setStations(Map<String, List<Station>> cities) {
        this.stations = cities;
    }

    /**
     * Retrieves the map containing the shortest routes from the origin station to all other stations.
     * 
     * @return a map of station names to their respective shortest route {@link Station} data.
     */
    public Map<String, Station> getShortestRoutes() {
        return shortestRoutes;
    }

    /**
     * Sets the map of shortest routes to a new map.
     * 
     * @param shortestRoutes a map of station names to {@link Station} objects representing the shortest routes.
     */
    public void setShortestRoutes(Map<String, Station> shortestRoutes) {
        this.shortestRoutes = shortestRoutes;
    }

	
	
	/**
	 * Returns the path to the station given. 
	 * The format is as follows: Westside->stationA->.....stationZ->stationName
	 * Each station is connected by an arrow and the trace ends at the station given.
	 * 
	 * @param stationName - Name of the station whose route we want to trace
	 * @return String representation of the path taken to reach stationName.
	 */
	public String traceRoute(String stationName) {
		if (stations.get(stationName) == null) {
			return "Invalid City";
		}
		
	    Stack<String> path = new LinkedListStack<>();
	    String currentStation = stationName;
	    
	    while (!currentStation.equals(origin)) {
	        Station nextStation = shortestRoutes.get(currentStation);

	        path.push(currentStation);        	   
	        currentStation = nextStation.getCityName();
	    }
	    path.push(origin); 

	    StringBuilder routeTracer = new StringBuilder();
	    
	    while (!path.isEmpty()) {
	        routeTracer.append(path.pop());
	        
	        if (!path.isEmpty()) {
	            routeTracer.append("->");
	        }
	    }
	    return routeTracer.toString();
	}

}



