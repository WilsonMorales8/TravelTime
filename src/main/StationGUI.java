package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import data_structures.HashTableSC;
import data_structures.SimpleHashFunction;

import interfaces.Map;

/**
 * A graphical user interface (GUI) for displaying train station schedules.
 * This GUI includes a table showing departure and arrival times for each station,
 * and allows for searching the route from Westside to a specific station.
 * It utilizes {@link TrainStationManager} to retrieve and calculate travel times.
 * 
 * @author Wilson A Morales
 */
public class StationGUI {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private Map<String, Double> travelTimes;
    private Map<String, String> departureTimes = new HashTableSC<String, String>(1, new SimpleHashFunction<String>());
    private TrainStationManager manager;

    /**
     * Constructor for initializing the StationGUI with a reference to a {@link TrainStationManager}.
     * It sets up the travel times and departure times and initializes the GUI components.
     *
     * @param manager The TrainStationManagern that provides the travel time data.
     */
    public StationGUI(TrainStationManager manager) {
    	this.manager = manager;
        this.travelTimes = manager.getTravelTimes(); 
        departureTimes();
        createGUI();
    }

    /**
     * Initializes the hardcoded departure times for known stations.
     * These times are used in conjunction with calculated travel times to display schedules.
     */
    private void departureTimes() {
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

    /**
     * Sets up the main window and its components for the GUI.
     * This method initializes the table, panels, and other interface elements.
     */
    private void createGUI() {
        frame = new JFrame("Westside Train Station Schedule");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 300);

        model = new DefaultTableModel();
        model.addColumn("Station");
        model.addColumn("Departure");
        model.addColumn("Arrival");

        setupGUI();

        table = new JTable(model);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        
        JPanel panel = new JPanel();
        JTextField textField = new JTextField(10);
        JButton button = new JButton("Search Route");
        JLabel label = new JLabel();

        button.addActionListener(e -> {
            String stationName = textField.getText();
            String route = manager.traceRoute(stationName);
            label.setText(route);
        });

        panel.add(textField);
        panel.add(button);
        panel.add(label);

        frame.add(panel, BorderLayout.SOUTH);
        
        
        frame.setVisible(true);
    }

    /**
     * Populates the table with station data, including calculating arrival times based on
     * departure times and travel times.
     */
    private void setupGUI() {
        for (String station : departureTimes.getKeys()) {
            String departure = departureTimes.get(station);  
            Double travelTime = travelTimes.get(station);  
            String arrival = calculateArrivalTime(departure, travelTime);  
            
            model.addRow(new Object[]{station, departure, arrival});  
        }
    }

    /**
     * Calculates the arrival time for a station based on its departure time and travel time.
     *
     * @param departure The departure time as a string in the format "HH:mm am/pm".
     * @param travelTime The travel time to the station in minutes.
     * @return The calculated arrival time as a string in the same format.
     */
    private String calculateArrivalTime(String departure, Double travelTime) {
        String[] parts = departure.split("[: ]");  
        int hour = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        String time = parts[2];

        minutes += travelTime;  
        hour += minutes / 60; 
        minutes %= 60;  

        if (hour >= 12) {
            if (hour > 12) {
            	hour -= 12; 
            }
            if ("am".equals(time)) {
                time = "pm";
            } 
            else {
                time = "am"; 
            }
        }

        return String.format("%d:%02d %s", hour, minutes, time);  
    }

    /**
     * Main method to launch the GUI.
     * It initializes the {@link TrainStationManager} with a CSV file and creates the GUI.
     *
     * @param args
     */
    public static void main(String[] args) {
        TrainStationManager manager = new TrainStationManager("stations.csv");
        new StationGUI(manager);
    }
}
