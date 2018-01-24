import java.util.*;

public class DispatchCenter {
    public static String[] AREA_NAMES = {"Downtown", "Airport", "North", "South", "East", "West"};

    private int[][]  stats; // You'll need this for the last part of the assignment
    private HashMap<Integer, Taxi> taxis;
    private HashMap<String, ArrayList<Taxi>> areas;

    // Constructor
    public DispatchCenter() {
        // You'll need this for the last part of the assignment
        stats = new int[AREA_NAMES.length][AREA_NAMES.length];
        taxis = new HashMap<Integer, Taxi>();
        areas = new HashMap<String, ArrayList<Taxi>>();

        for(int i = 0; i < 6; i++) {
            areas.put(AREA_NAMES[i], new ArrayList<Taxi>());
        }

        for(int i = 0; i < 50; i++) {
            int rand = 100 + (int)(Math.random()* (899)) ;
            int rand2 = (int )(Math.random() * 6);
            Taxi aTaxi = new Taxi(rand);
            addTaxi(aTaxi, AREA_NAMES[(int)rand2]);
        }

    }

    public HashMap<String, ArrayList<Taxi>> getAreas() {return areas;}

    // You'll need this for the last part of the assignment
    public int[][]   getStats() { return stats; }


    // Update the statistics for a taxi going from the pickup location to the dropoff location
    public void updateStats(String pickup, String dropOff) {

    }

    // Determine the travel times from one area to another
    public static int computeTravelTimeFrom(String pickup, String dropOff) {
        return 0;
    }

    // Add a taxi to the hashmaps
    public void addTaxi(Taxi aTaxi, String area) {
        if (areas.containsKey(area))
            areas.get(area).add(aTaxi);
        taxis.put(aTaxi.getPlateNumber(), aTaxi);

    }

    // Return a list of all available taxis within a certain area
    private ArrayList<Taxi> availableTaxisInArea(String s) {
        ArrayList<Taxi> result = new ArrayList<Taxi>();

        for(Taxi t: areas.get(s)) {
            if (t.getAvailable())
                result.add(t);
        }
        return result;
    }

    // Return a list of all busy taxis
    public ArrayList<Taxi> getBusyTaxis() {
        ArrayList<Taxi> result = new ArrayList<Taxi>();

        for(int i = 0;  i < 6; i++) {
            for(Taxi t: areas.get(AREA_NAMES[i])) {
                if (!t.getAvailable())
                    result.add(t);
            }
        }

        return result;
    }

    // Find a taxi to satisfy the given request
    public Taxi sendTaxiForRequest(ClientRequest request) {
        String area = "";
        boolean pickedUp = false;
        if (availableTaxisInArea(request.getPickupLocation()).size() > 0) {

            Taxi minTime = availableTaxisInArea(request.getPickupLocation()).get(0);
            for (int i = 0; i < availableTaxisInArea(request.getPickupLocation()).size(); i++) {
                if (minTime.getEstimatedTimeToDest() > availableTaxisInArea(request.getPickupLocation()).get(i).getEstimatedTimeToDest())
                    minTime = availableTaxisInArea(request.getPickupLocation()).get(i);
            }

            areas.get(request.getPickupLocation()).remove(minTime);
            areas.get(request.getDropoffLocation()).add(minTime);
            minTime.setAvailable(false);

            if (request.getPickupLocation() == request.getDropoffLocation())
                minTime.setEstimatedTimeToDest(10);
            else {
                if (request.getPickupLocation() == "Airport") {
                    if (request.getDropoffLocation() == "East" || request.getDropoffLocation() == "Downtown")
                        minTime.setEstimatedTimeToDest(20);
                    else if (request.getDropoffLocation() == "North" || request.getDropoffLocation() == "South")
                        minTime.setEstimatedTimeToDest(40);
                    else
                        minTime.setEstimatedTimeToDest(60);
                } else if (request.getPickupLocation() == "East") {
                    if (request.getDropoffLocation() == "North" || request.getDropoffLocation() == "South"
                            || request.getDropoffLocation() == "Airport" || request.getDropoffLocation() == "Downtown")
                        minTime.setEstimatedTimeToDest(20);
                    else
                        minTime.setEstimatedTimeToDest(40);
                } else if (request.getPickupLocation() == "North" || request.getPickupLocation() == "South") {
                    if (request.getDropoffLocation() == "West" || request.getDropoffLocation() == "Downtown" ||
                            request.getDropoffLocation() == "East")
                        minTime.setEstimatedTimeToDest(20);
                    else
                        minTime.setEstimatedTimeToDest(40);
                } else {
                    if (request.getDropoffLocation() == "Airport")
                        minTime.setEstimatedTimeToDest(40);
                    else
                        minTime.setEstimatedTimeToDest(20);
                }
            }
            return minTime;
        }

        else {
            for (int i = 0; i < AREA_NAMES.length; i++) {
                if (request.getPickupLocation() != AREA_NAMES[i]) {
                    if (availableTaxisInArea(AREA_NAMES[i]).size() > 0) {
                        Taxi minTime = availableTaxisInArea(AREA_NAMES[i]).get(0);
                        for (int j = 0; j < availableTaxisInArea(AREA_NAMES[i]).size(); j++) {
                            if (minTime.getEstimatedTimeToDest() > availableTaxisInArea(AREA_NAMES[i]).get(j).getEstimatedTimeToDest())
                                minTime = availableTaxisInArea(AREA_NAMES[i]).get(j);
                        }
                        areas.get(AREA_NAMES[i]).remove(minTime);
                        areas.get(request.getDropoffLocation()).add(minTime);
                        minTime.setAvailable(false);

                        if (AREA_NAMES[i] == "Downtown") {
                            if (request.getPickupLocation() == request.getDropoffLocation()) {
                                if (request.getPickupLocation() == "West" || request.getPickupLocation() == "East" ||
                                        request.getPickupLocation() == "North" || request.getPickupLocation() == "South")
                                    minTime.setEstimatedTimeToDest(30);
                                else
                                    minTime.setEstimatedTimeToDest(50);
                            } else {
                                if (request.getPickupLocation() == "Airport") {
                                    if (request.getDropoffLocation() == "East")
                                        minTime.setEstimatedTimeToDest(60);
                                    else if (request.getDropoffLocation() == "North" || request.getDropoffLocation() == "South"
                                            || request.getDropoffLocation() == "Downtown")
                                        minTime.setEstimatedTimeToDest(80);
                                    else
                                        minTime.setEstimatedTimeToDest(100);
                                } else if (request.getPickupLocation() == "East") {
                                    if (request.getDropoffLocation() == "North" || request.getDropoffLocation() == "South"
                                            || request.getDropoffLocation() == "Airport" || request.getDropoffLocation() == "Downtown")
                                        minTime.setEstimatedTimeToDest(40);
                                    else
                                        minTime.setEstimatedTimeToDest(60);
                                } else if (request.getPickupLocation() == "North" || request.getPickupLocation() == "South") {
                                    if (request.getDropoffLocation() == "West" || request.getDropoffLocation() == "Downtown" ||
                                            request.getDropoffLocation() == "East")
                                        minTime.setEstimatedTimeToDest(40);
                                    else
                                        minTime.setEstimatedTimeToDest(60);
                                } else {
                                    if (request.getDropoffLocation() == "Airport")
                                        minTime.setEstimatedTimeToDest(80);
                                    else if (request.getDropoffLocation() == "North" || request.getDropoffLocation() == "South"
                                            || request.getDropoffLocation() == "Downtown")
                                        minTime.setEstimatedTimeToDest(40);
                                    else
                                        minTime.setEstimatedTimeToDest(60);
                                }
                            }
                            return minTime;
                        } else if (AREA_NAMES[i] == "North" || AREA_NAMES[i] == "South") {
                            if (request.getPickupLocation() == request.getDropoffLocation()) {
                                if (request.getPickupLocation() == "West" || request.getPickupLocation() == "East" ||
                                        request.getPickupLocation() == "South")
                                    minTime.setEstimatedTimeToDest(30);
                                else
                                    minTime.setEstimatedTimeToDest(50);
                            } else if (request.getPickupLocation() == "Downtown") {
                                if (request.getDropoffLocation() == "West" || request.getDropoffLocation() == "East" ||
                                        request.getDropoffLocation() == "North" || request.getDropoffLocation() == "South")
                                    minTime.setEstimatedTimeToDest(40);
                                else
                                    minTime.setEstimatedTimeToDest(60);
                            } else if (request.getPickupLocation() == "West") {
                                if (request.getDropoffLocation() == "Downtown" || request.getDropoffLocation() == "North" ||
                                        request.getDropoffLocation() == "South")
                                    minTime.setEstimatedTimeToDest(40);
                                else if (request.getDropoffLocation() == "East")
                                    minTime.setEstimatedTimeToDest(60);
                                else
                                    minTime.setEstimatedTimeToDest(80);
                            } else if (request.getPickupLocation() == "East") {
                                if (request.getDropoffLocation() == "Airport" || request.getDropoffLocation() == "North" ||
                                        request.getDropoffLocation() == "South" || request.getDropoffLocation() == "Downtown")
                                    minTime.setEstimatedTimeToDest(40);
                                else
                                    minTime.setEstimatedTimeToDest(60);
                            } else if (request.getPickupLocation() == "Airport") {
                                if (request.getDropoffLocation() == "East")
                                    minTime.setEstimatedTimeToDest(60);
                                else if (request.getDropoffLocation() == "North" || request.getDropoffLocation() == "South" ||
                                        request.getDropoffLocation() == "Downtown")
                                    minTime.setEstimatedTimeToDest(80);
                                else
                                    minTime.setEstimatedTimeToDest(100);
                            } else
                                minTime.setEstimatedTimeToDest(40);
                            return minTime;
                        } else if (AREA_NAMES[i] == "West") {
                            if (request.getPickupLocation() == request.getDropoffLocation()) {
                                if (request.getPickupLocation() == "North" || request.getPickupLocation() == "Downtown" ||
                                        request.getPickupLocation() == "South")
                                    minTime.setEstimatedTimeToDest(30);
                                else if (request.getPickupLocation() == "East")
                                    minTime.setEstimatedTimeToDest(50);
                                else if (request.getPickupLocation() == "Airport")
                                    minTime.setEstimatedTimeToDest(70);
                            } else {
                                if (request.getPickupLocation() == "North" || request.getDropoffLocation() == "South") {
                                    if (request.getDropoffLocation() == "East" || request.getDropoffLocation() == "Downtown" ||
                                            request.getDropoffLocation() == "West")
                                        minTime.setEstimatedTimeToDest(40);
                                    else
                                        minTime.setEstimatedTimeToDest(60);
                                } else if (request.getPickupLocation() == "Downtown") {
                                    if (request.getDropoffLocation() == "North" || request.getDropoffLocation() == "South" || request.getDropoffLocation() == "West" ||
                                            request.getDropoffLocation() == "East")
                                        minTime.setEstimatedTimeToDest(40);
                                    else
                                        minTime.setEstimatedTimeToDest(60);
                                } else if (request.getPickupLocation() == "East") {
                                    if (request.getDropoffLocation() == "North" || request.getDropoffLocation() == "South" || request.getDropoffLocation() == "Downtown" ||
                                            request.getDropoffLocation() == "Airport")
                                        minTime.setEstimatedTimeToDest(60);
                                    else
                                        minTime.setEstimatedTimeToDest(80);
                                } else if (request.getPickupLocation() == "Airport") {
                                    if (request.getDropoffLocation() == "East")
                                        minTime.setEstimatedTimeToDest(80);
                                    else if (request.getDropoffLocation() == "North" || request.getDropoffLocation() == "Downtown" || request.getDropoffLocation() == "South")
                                        minTime.setEstimatedTimeToDest(100);
                                    else
                                        minTime.setEstimatedTimeToDest(120);
                                }
                            }
                            return minTime;
                        } else if (AREA_NAMES[i] == "East") {
                            if (request.getPickupLocation() == request.getDropoffLocation()) {
                                if (request.getPickupLocation() == "North" || request.getPickupLocation() == "Downtown" ||
                                        request.getPickupLocation() == "South" || request.getPickupLocation() == "Airport")
                                    minTime.setEstimatedTimeToDest(30);
                                else if (request.getPickupLocation() == "West")
                                    minTime.setEstimatedTimeToDest(50);
                            } else {
                                if (request.getPickupLocation() == "North" || request.getDropoffLocation() == "South") {
                                    if (request.getDropoffLocation() == "East" || request.getDropoffLocation() == "Downtown" ||
                                            request.getDropoffLocation() == "West")
                                        minTime.setEstimatedTimeToDest(40);
                                    else
                                        minTime.setEstimatedTimeToDest(60);
                                } else if (request.getPickupLocation() == "Downtown") {
                                    if (request.getDropoffLocation() == "North" || request.getDropoffLocation() == "South" || request.getDropoffLocation() == "West")
                                        minTime.setEstimatedTimeToDest(40);
                                    else
                                        minTime.setEstimatedTimeToDest(60);
                                } else if (request.getPickupLocation() == "West") {
                                    if (request.getDropoffLocation() == "North" || request.getDropoffLocation() == "South" || request.getDropoffLocation() == "Downtown")
                                        minTime.setEstimatedTimeToDest(60);
                                    else
                                        minTime.setEstimatedTimeToDest(100);
                                } else if (request.getPickupLocation() == "Airport") {
                                    if (request.getDropoffLocation() == "East")
                                        minTime.setEstimatedTimeToDest(40);
                                    else if (request.getDropoffLocation() == "North" || request.getDropoffLocation() == "Downtown" || request.getDropoffLocation() == "South")
                                        minTime.setEstimatedTimeToDest(60);
                                    else
                                        minTime.setEstimatedTimeToDest(80);
                                }
                                return minTime;
                            }
                        } else if (AREA_NAMES[i] == "Airport") {
                            if (request.getPickupLocation() == request.getDropoffLocation()) {
                                if (request.getPickupLocation() == "North" || request.getPickupLocation() == "Downtown" ||
                                        request.getPickupLocation() == "South")
                                    minTime.setEstimatedTimeToDest(50);
                                else if (request.getPickupLocation() == "East")
                                    minTime.setEstimatedTimeToDest(30);
                                else if (request.getPickupLocation() == "West")
                                    minTime.setEstimatedTimeToDest(70);
                            } else {
                                if (request.getPickupLocation() == "North" || request.getPickupLocation() == "South") {
                                    if (request.getDropoffLocation() == "East" || request.getDropoffLocation() == "West" || request.getDropoffLocation() == "Downtown")
                                        minTime.setEstimatedTimeToDest(60);
                                    else
                                        minTime.setEstimatedTimeToDest(80);
                                } else if (request.getPickupLocation() == "East") {
                                    if (request.getDropoffLocation() == "North" || request.getDropoffLocation() == "South" || request.getDropoffLocation() == "Downtown" ||
                                            request.getDropoffLocation() == "Airport")
                                        minTime.setEstimatedTimeToDest(40);
                                    else
                                        minTime.setEstimatedTimeToDest(60);
                                } else if (request.getPickupLocation() == "West") {
                                    if (request.getDropoffLocation() == "North" || request.getDropoffLocation() == "Downtown" || request.getDropoffLocation() == "South")
                                        minTime.setEstimatedTimeToDest(80);
                                    else if (request.getDropoffLocation() == "East")
                                        minTime.setEstimatedTimeToDest(100);
                                    else
                                        minTime.setEstimatedTimeToDest(120);
                                } else if (request.getPickupLocation() == "Downtown") {
                                    if (request.getDropoffLocation() == "North" || request.getDropoffLocation() == "South" || request.getDropoffLocation() == "West" ||
                                            request.getDropoffLocation() == "East")
                                        minTime.setEstimatedTimeToDest(60);
                                    else
                                        minTime.setEstimatedTimeToDest(80);
                                }
                            }
                            return minTime;
                        }

                    }
                }
            }
        }


        return null;
    }
}