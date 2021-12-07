import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import org.w3c.dom.Document;

public class StreetMap {
    Car[] cars;
    ArrayList<Intersection> intersections = new ArrayList<Intersection>();
    private boolean calculatingRoute = true;

    public Intersection getInterecionById(int id) {
        for (Intersection intersection : intersections) {
            if (intersection.getId() == id) {
                return intersection;
            }
        }
        return null;
    }

    // TODO: pathfinding function here
    public ArrayList<Integer> calculateRoute(int startId, int endId) {
        calculatingRoute = false;
        ArrayList<IntersectionAsNode> nodes = new ArrayList<>();
        nodes.add(new IntersectionAsNode(startId, 0, -1));
        System.out.println("S: " + startId + " E: " + endId);
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
        for (IntersectionAsNode node : nodes) {
            System.out.println(node.getId() + " " + node.getConnectLength() + " " + node.getParentId());
        }
        ArrayList<Integer> route = new ArrayList<>();
        int currentId = endId;
        while (currentId != -1) {
            for (IntersectionAsNode routeNode : nodes) {
                if (routeNode.getId() == currentId) {
                    route.add(currentId);
                    currentId = routeNode.getParentId();
                    break;
                }
            }
        }
        Collections.reverse(route);
        System.out.println("\n" + route.toString());
        System.out.println(" ");
        return route;
    }

    public void startCars(int numCars) {
        calculatingRoute = false;
        cars = new Car[numCars];
        for (Car car : cars) {
            int[] temp = {(int) (Math.random() * (intersections.size())), (int) (Math.random() * (intersections.size()))};
            while (temp[0] == temp[1]) {
                temp[0] = (int) (Math.random() * (intersections.size()));
                temp[1] = (int) (Math.random() * (intersections.size()));
            }
            car = new Car(temp[0], temp[1]);
            car.setRoute(calculateRoute(car.getStartRoute()[0], car.getStartRoute()[1]));
        }
    }

    public StreetMap(String mapFile, int numCars) {
        // Intersections2
        try {
            Scanner fileScanner = new Scanner(new File(mapFile));
            ArrayList<String> mapData = new ArrayList<>();
            while (fileScanner.hasNext()) {
                mapData.add(fileScanner.nextLine());
            }
            for (String intersection : mapData) {
                ArrayList<String> data = new ArrayList<>(Arrays.asList(intersection.split("\\.")));
                System.out.println(data.get(0));
                if (!data.get(0).equals("*")) {
                    this.intersections.add(new Intersection(Integer.parseInt(data.get(0))));
                    for (String connection : data) {
                        if (connection.length() == 1) {
                            continue;
                        }
                        int[] conTemp = {Integer.parseInt(connection.split(",")[0]), Integer.parseInt(connection.split(",")[1])};
                        this.getInterecionById(Integer.parseInt(data.get(0))).addConnection(conTemp);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);ap>
Exception in thread "main" ja
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(mapFile));
            document.getDocumentElement().normalize();

            NodeList docIntersections = document.getElementsByTagName("intersec");

            for (int i = 0; i < docIntersections.getLength(); i++) {
                this.intersections.add(new Intersection(Integer.parseInt( // add an intersection for each intersection in xml file
                        docIntersections.item(i)
                                .getAttributes()
                                .getNamedItem("id")
                                .getNodeValue()
                )));
                NodeList connections = docIntersections.item(i).getChildNodes();
                for (int n = 0; n < connections.getLength(); n++) {
                    if (connections.item(n).getNodeType() != Node.ELEMENT_NODE)
                        continue;
                    Element curElement = (Element) connections.item(n);
                    Node targetNode = curElement.getElementsByTagName("target").item(0).getChildNodes().item(0);
                    Node lengthNode = curElement.getElementsByTagName("length").item(0).getChildNodes().item(0);
                    int[] conTemp = {Integer.parseInt(targetNode.getNodeValue()), Integer.parseInt(lengthNode.getNodeValue())};
                    this.intersections.get(i).addConnection(conTemp);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        */
        // Cars
        startCars(numCars);
    }
}
