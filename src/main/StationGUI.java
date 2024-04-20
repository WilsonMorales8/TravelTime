package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import data_structures.HashTableSC;
import data_structures.SimpleHashFunction;

import interfaces.Map;

public class StationGUI {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private Map<String, Double> travelTimes;
    private Map<String, String> departureTimes = new HashTableSC<String, String>(1, new SimpleHashFunction<String>());

    public StationGUI(TrainStationManager manager) {
        this.travelTimes = manager.getTravelTimes(); 
        initializeDepartureTimes();
        initializeGUI();
    }

    private void initializeDepartureTimes() {
        departureTimes.put("Bugapest", "9:35 am");
        departureTimes.put("Dubay", "10:30 am");
        departureTimes.put("Berlint", "8:25 pm");
        departureTimes.put("Mosbull", "6:00 pm");
        departureTimes.put("Cayro", "6:40 am");
        departureTimes.put("Bostin", "10:25 am");
        departureTimes.put("Los Angelos", "12:30 pm");
        departureTimes.put("Dome", "1:30 pm");
        departureTimes.put("Takyo", "3:35 pm");
        departureTimes.put("Unstabul", "4:45 pm");
        departureTimes.put("Chicargo", "7:25 am");
        departureTimes.put("Loondun", "2:00 pm");
    }

    private void initializeGUI() {
        frame = new JFrame("Westside Train Station Schedule");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);

        model = new DefaultTableModel();
        model.addColumn("Station");
        model.addColumn("Departure");
        model.addColumn("Arrival");

        populateTable();

        table = new JTable(model);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void populateTable() {
        for (String station : departureTimes.getKeys()) {
            String departure = departureTimes.get(station);  
            Double travelTime = travelTimes.get(station);  
            String arrival = calculateArrivalTime(departure, travelTime);  
            
            model.addRow(new Object[]{station, departure, arrival});  
        }
    }

    private String calculateArrivalTime(String departure, Double travelTimeMinutes) {
        String[] parts = departure.split("[: ]");  
        int hour = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        String amPm = parts[2];

        minutes += travelTimeMinutes;  
        hour += minutes / 60; 
        minutes %= 60;  

        if (hour >= 12) {
            if (hour > 12) {
            	hour -= 12; 
            }
            if ("am".equals(amPm)) {
                amPm = "pm";
            } 
            else {
                amPm = "am"; 
            }
        }

        return String.format("%d:%02d %s", hour, minutes, amPm);  
    }

    public static void main(String[] args) {
        TrainStationManager manager = new TrainStationManager("stations.csv");
        new StationGUI(manager);
    }
}


















