import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Collectors;

public class StreetMap {
    ArrayList<Car> cars;
    ArrayList<Intersection> intersections = new ArrayList<>();
    private float routeAverage;
    private boolean robustOutput;

    public Intersection getInterecionById(int id) {
        for (Intersection intersection : intersections) {
            if (intersection.getId() == id) {
                return intersection;
            }
        }
        return null;
    }

    private void outputData() {
        System.out.println("Average Route Length: " +  calculateAverageRouteLength(this.cars));
        System.out.println("Average Number of Stops: " + calculateAverageNumberOfStops(this.cars));
        System.out.println("Average Travel Time: " + calculateAverageTravelTime(this.cars));
    }

    private float calculateAverageNumberOfStops(ArrayList<Car> cars) {
        int stopsAverage = 0;
        for (Car car : cars) {
            stopsAverage += car.getRoute().size();
        }
        return (float) stopsAverage / (float) cars.size();
    }

    private float calculateAverageRouteLength(ArrayList<Car> cars) {
        float routeAverage = 0;
        for (Car car : cars) {
            routeAverage += (float) car.getRouteLength();
        }
        return routeAverage / (float) cars.size();
    }

    private float calculateAverageTravelTime(ArrayList<Car> cars) {
        float timeAverage = 0;
        float curTime;
        for (Car car : cars) {
            curTime = 0;
            for (int i = 0; i < car.getRoute().size()-1; i++) {
                int finalI = i;
                curTime += (Math.sqrt((2 * (((float) this.getInterecionById(car.getRoute().get(i))
                        .getConnections()
                        .stream()
                        .filter(ls -> ls[0] == car.getRoute().get(finalI +1))
                        .collect(Collectors.toList()).get(0)[1]) * ((float)40/3)) / 3)));
            }
            timeAverage += curTime;
        }
        return timeAverage / (float) cars.size();
    }

    public ArrayList<Car> getCars() {
        return cars;
    }

    private void startCars(int numCars) {
        cars = new ArrayList<>(Collections.nCopies(numCars, null));
        for (int i = 0; i < cars.size(); i++) {
            cars.set(i, new Car());
            int[] temp = {(int) (Math.random() * (intersections.size())), (int) (Math.random() * (intersections.size()))};
            while (temp[0] == temp[1]) {
                temp[0] = (int) (Math.random() * (intersections.size()));
                temp[1] = (int) (Math.random() * (intersections.size()));
            }
            cars.get(i).setStartRoute(temp);
            ArrayList<ArrayList<Integer>> tempRoute = calculateRoute(cars.get(i).getStartRoute()[0], cars.get(i).getStartRoute()[1]);
            cars.get(i).setRoute(tempRoute.get(0), tempRoute.get(1).get(0));
        }
        outputData();
    }

    private ArrayList<ArrayList<Integer>> calculateRoute(int startId, int endId) {
        ArrayList<IntersectionAsNode> nodes = new ArrayList<>();
        nodes.add(new IntersectionAsNode(startId, 0, -1));
        if (robustOutput) {
            System.out.println("S: " + startId + " E: " + endId);
        }
        while (true) {
            ArrayList<IntersectionAsNode> tempNodes = new ArrayList<>();
            ArrayList<IntersectionAsNode> removeNodes = new ArrayList<>();
            int closestUnexplored = Integer.MAX_VALUE;
            int closestFoundNodeId = -2;
            for (IntersectionAsNode shortestCheck : nodes) {
                if (shortestCheck.getConnectLength() < closestUnexplored && !shortestCheck.getVisited()) {
                    closestUnexplored = shortestCheck.getConnectLength();
                    closestFoundNodeId = shortestCheck.getId();
                }
            }
            if (closestFoundNodeId == -2) { break; }
            for (IntersectionAsNode node : nodes) {
                if (node.getId() !=  closestFoundNodeId) {
                    continue;
                }
                node.setVisited(true);
                ArrayList<int[]> connections = this.getInterecionById(node.getId()).getConnections();
                for (int[] connection : connections) {
                    if (connection[0] == node.getId()) {
                        continue;
                    }
                    boolean nodeAdded = false;
                    for (IntersectionAsNode addedNodeCheck : nodes) {
                        if (connection[0] == addedNodeCheck.getId()) {
                            nodeAdded = true;
                            if ((connection[1] + node.getConnectLength()) < addedNodeCheck.getConnectLength()) {
                                removeNodes.add(addedNodeCheck);
                                tempNodes.add(new IntersectionAsNode(connection[0], connection[1] + node.getConnectLength(), node.getId()));
                            }
                        }
                    }
                    if (!nodeAdded) {
                        tempNodes.add(new IntersectionAsNode(connection[0], connection[1] + node.getConnectLength(), node.getId()));
                    }
                }
            }
            nodes.removeAll(removeNodes);
            nodes.addAll(tempNodes);
            if (tempNodes.size() != 0) {
                if (tempNodes.get(0).getId() == endId) {
                    break;
                }
            }
        }
        if (robustOutput) {
            for (IntersectionAsNode node : nodes) {
                System.out.println(node.getId() + " " + node.getConnectLength() + " " + node.getParentId());
            }
        }
        ArrayList<Integer> route = new ArrayList<>();
        int routeLength = 0;
        int currentId = endId;
        while (currentId != -1) {
            for (IntersectionAsNode routeNode : nodes) {
                if (routeNode.getId() == currentId) {
                    if (currentId == endId) {
                        routeLength = routeNode.getConnectLength();
                    }
                    route.add(currentId);
                    currentId = routeNode.getParentId();
                    break;
                }
            }
        }
        Collections.reverse(route);
        if (robustOutput) {
            System.out.println("\n" + route);
            System.out.println("L: " + routeLength);
            System.out.println(" ");
        }
        ArrayList<ArrayList<Integer>> tempRoute = new ArrayList<>();
        tempRoute.add(route);
        ArrayList<Integer> moreTempStuff = new ArrayList<>();
        moreTempStuff.add(routeLength);
        tempRoute.add(moreTempStuff);
        return tempRoute;
    }

    // https://stackoverflow.com/questions/7708698/convert-arrayliststring-to-an-arraylistinteger-or-integer-array
    private ArrayList<Integer> getIntegerArray(ArrayList<String> stringArray) {
        ArrayList<Integer> result = new ArrayList<>();
        for(String stringValue : stringArray) {
            try {
                //Convert String to Integer, and store it into integer array list.
                result.add(Integer.parseInt(stringValue));
            } catch(NumberFormatException e) {
                //System.out.println("Could not parse " + nfe);
                e.printStackTrace();
            }
        }
        return result;
    }

    public StreetMap(String mapFile, int numCars, boolean robustOutput) {
        this.robustOutput = robustOutput;
        // Intersections
        try {
            Scanner fileScanner = new Scanner(new File(mapFile));
            ArrayList<String> mapData = new ArrayList<>();
            while (fileScanner.hasNext()) {
                mapData.add(fileScanner.nextLine());
            }
            ArrayList<Integer> superblockNodes = new ArrayList<>();
            for (String intersection : mapData) {
                ArrayList<String> data = new ArrayList<>(Arrays.asList(intersection.split("\\.")));
                if (data.get(0).charAt(0) != '*') {
                    if (data.get(0).charAt(0) == '%') {
                        data.remove(0);
                        superblockNodes.addAll(getIntegerArray(data));
                    } else {
                        this.intersections.add(new Intersection(Integer.parseInt(data.get(0))));
                        for (String connection : data) {
                            if (connection.split(",").length == 1) {
                                continue;
                            }
                            int[] conTemp = {Integer.parseInt(connection.split(",")[0]), Integer.parseInt(connection.split(",")[1])};
                            this.getInterecionById(Integer.parseInt(data.get(0))).addConnection(conTemp);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (robustOutput) {
            for (Intersection intersection : intersections) {
                System.out.print(intersection.getId() + " : ");
                for (int[] connection : intersection.getConnections()) {
                    System.out.print(connection[0] + " -> " + connection[1] + ", ");
                }
                System.out.println();
            }
        }
        // Cars
        startCars(numCars);
    }
}