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
        while (true) {
            ArrayList<IntersectionAsNode> tempNodes = new ArrayList<>();
            for (IntersectionAsNode node : nodes) {
                ArrayList<int[]> curNodeConnections = this.getInterecionById(node.getId()).getConnections();
                for (int[] connection : curNodeConnections) {
                    boolean nodeAdded = false;
                    for (IntersectionAsNode parentCheckNode : nodes) {
                        if (parentCheckNode.getId() == connection[0]) nodeAdded = true;
                    }
                    if (!nodeAdded) {
                        tempNodes.add(new IntersectionAsNode(connection[0], connection[1] + node.getConnectLength(), node.getId()));
                    }
                }
            }
            nodes.addAll(tempNodes);
            break;
        }
        for (IntersectionAsNode node : nodes) {
            System.out.println(node.getId() + " " + node.getConnectLength() + " " + node.getParentId());
        }
        System.out.println(" ");
        return new ArrayList<>();
    }

    public void startCars(int numCars) {
        calculatingRoute = false;
        cars = new Car[numCars];
        for (Car car : cars) {
            car = new Car((int) (Math.random() * intersections.size()), (int) (Math.random() * intersections.size()));
            car.setRoute(calculateRoute(car.getStartRoute()[0], car.getStartRoute()[1]));
        }
    }

    public StreetMap(String mapFile, int numCars) {
        // Intersections
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
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
        // Cars
        startCars(numCars);
    }
}
