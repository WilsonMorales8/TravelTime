package main;

/**
 * Represents a train station with a name and a distance. 
 * The distance variable in this class is used to store the distance between cityName and another station in the network.
 * 
 * @author Wilson A Morales
 */
public class Station {
    
    private String cityName; 
    private int distance;
    
    /**
     * Constructs a new Station with the specified name and distance.
     * 
     * @param name The name of the city where the station is located.
     * @param dist The distance from the station to a specified reference station.
     */
    public Station(String name, int dist) {
        this.cityName = name;
        this.distance = dist;
    }
    
    /**
     * Returns the name of the city for this station.
     * 
     * @return A string representing the city name.
     */
    public String getCityName() {
        return cityName;
    }
    
    /**
     * Sets the name of the city for this station.
     * 
     * @param cityName The new city name for this station.
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    
    /**
     * Returns the distance of this station.
     * 
     * @return An integer representing the distance in kilometers.
     */
    public int getDistance() {
        return distance;
    }
    
    /**
     * Sets the distance of this station.
     * 
     * @param distance The new distance in kilometers.
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Station other = (Station) obj;
        return this.getCityName().equals(other.getCityName()) && this.getDistance() == other.getDistance();
    }
    
    @Override
    public String toString() {
        return "(" + this.getCityName() + ", " + this.getDistance() + ")";
    }
}
