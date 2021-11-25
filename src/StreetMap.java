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

    public Intersection getInterecionById(int id) {
        for (Intersection intersection : intersections) {
            if (intersection.getId() == id) {
                return intersection;
            }
        }
        return null;
    }

    public ArrayList<Integer> calculateRoute(int startId, int endId) {
        ArrayList<Integer> route = new ArrayList<>();
        Intersection start = this.getInterecionById(startId);

        return route;
    }

    public StreetMap(String mapFile, int numCars) {
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

    }
}
