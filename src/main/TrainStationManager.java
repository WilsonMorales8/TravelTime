package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import data_structures.HashTableSC;
import data_structures.SimpleHashFunction;
import data_structures.ArrayList;

import interfaces.List;
import interfaces.Map;
import interfaces.Stack;

public class TrainStationManager {
	
    private Map<String, List<Station>> stations = new HashTableSC<String, List<Station>>(1, new SimpleHashFunction<String>());
    private Map<String, Station> shortestRoutes = new HashTableSC<String, Station>(1, new SimpleHashFunction<String>());
    private Map<String, Double> travelTime = new HashTableSC<String, Double>(1, new SimpleHashFunction<String>());
	
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
                	stations.get(destination).add(station2);                }
			}
			
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private void findShortestDistance() {
				
	}

	
	public void sortStack(Station station, Stack<Station> stackToSort) {
		
	}
	
	
	public Map<String, Double> getTravelTimes() {
		return travelTime;
		// 5 minutes per kilometer
		// 15 min per station	
	}

	public Map<String, List<Station>> getStations() {
		return this.stations;
	}

	
	public void setStations(Map<String, List<Station>> cities) {
		
	}


	public Map<String, Station> getShortestRoutes() {
		return this.shortestRoutes;
	}

	
	public void setShortestRoutes(Map<String, Station> shortestRoutes) {
		
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
		// Remove if you implement the method, otherwise LEAVE ALONE
		throw new UnsupportedOperationException();
	}

}