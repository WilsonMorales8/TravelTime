package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import data_structures.HashTableSC;
import data_structures.LinkedListStack;
import data_structures.SimpleHashFunction;
import data_structures.ArrayList;
import data_structures.ArrayListStack;
import data_structures.HashSet;

import interfaces.List;
import interfaces.Map;
import interfaces.Stack;

public class TrainStationManager {
	
    private Map<String, List<Station>> stations = new HashTableSC<String, List<Station>>(1, new SimpleHashFunction<String>());
    private Map<String, Station> shortestRoutes = new HashTableSC<String, Station>(1, new SimpleHashFunction<String>());
    private Map<String, Double> travelTimes = new HashTableSC<String, Double>(1, new SimpleHashFunction<String>());
    String origin = "Westside";
    
	public TrainStationManager(String station_file) {	    
		try (BufferedReader stationReader = new BufferedReader(new FileReader("inputFiles/" + station_file))) {
            String line = stationReader.readLine(); // Skip header line
            
            //separate line by comas
            while ((line = stationReader.readLine()) != null) {
                String[] parts = line.split(",");
                String source = parts[0];
                String destination = parts[1];
                int distance = Integer.parseInt(parts[2]);
                
                // Create a new Station object for the destination and source
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

	
	public void sortStack(Station station, Stack<Station> stackToSort) {
	    stackToSort.push(station);

	    Stack<Station> helpStack = new LinkedListStack<>();
	    
	    while (!stackToSort.isEmpty()) {
	        Station current = stackToSort.pop();
	        while (!helpStack.isEmpty() && helpStack.top().getDistance() > current.getDistance()) {
	            stackToSort.push(helpStack.pop());
	        }
	        helpStack.push(current);
	    }
	    while (!helpStack.isEmpty()) {
	        stackToSort.push(helpStack.pop());
	    }
	}

	
	public Map<String, Double> getTravelTimes() {
		for (String stationName : shortestRoutes.getKeys()) {
		
		int stops = 0;
		String currentStation = stationName;
		double distanceTime = shortestRoutes.get(stationName).getDistance() * 2.5;
		
		while (!currentStation.equals(origin) && shortestRoutes.get(currentStation) != null) {
			stops++;
		    currentStation = shortestRoutes.get(currentStation).getCityName();
		}
		double stopsTime;
		
        if (stops > 1) {
        	stopsTime = (stops - 1) * 15;
        } 
        else {
        	stopsTime = 0;
        }		
        
        double totalTime = distanceTime + stopsTime;
		
		travelTimes.put(stationName, totalTime);
		}
		return travelTimes;
	}

	
	public Map<String, List<Station>> getStations() {
		return stations;
	}

	
	public void setStations(Map<String, List<Station>> cities) {
		stations = cities;
	}


	public Map<String, Station> getShortestRoutes() {
		return shortestRoutes;
	}

	
	public void setShortestRoutes(Map<String, Station> shortestRoutes) {
		this.shortestRoutes = shortestRoutes;
	}
	
	
	/**
	 * BONUS EXERCISE THIS IS OPTIONAL
	 * Returns the path to the station given. 
	 * The format is as follows: Westside->stationA->.....stationZ->stationName
	 * Each station is connected by an arrow and the trace ends at the station given.
	 * 
	 * @param stationName - Name of the station whose route we want to trace
	 * @return (String) String representation of the path taken to reach stationName.
	 */
	public String traceRoute(String stationName) {
		Stack<String> route = new ArrayListStack<>();
		String current = stationName;
		return stationName;
	}

}
























