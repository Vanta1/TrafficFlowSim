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
    ArrayList<Street> streets = new ArrayList<Street>();
    ArrayList<Intersection> intersections = new ArrayList<Intersection>();

    public StreetMap(String mapFile, int numCars) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(mapFile));
            document.getDocumentElement().normalize();

            NodeList docIntersections = document.getElementsByTagName("intersec");

            // TODO: constructing graph with streets

            for (int i = 0; i < docIntersections.getLength(); i++) {
                this.intersections.add(new Intersection(Integer.parseInt( // add an intersection for each intersection in xml file
                        docIntersections.item(i)
                                .getAttributes()
                                .getNamedItem("id")
                                .getNodeValue()
                )));
                System.out.println(this.intersections.get(i).getId());
                NodeList connections = docIntersections.item(i).getChildNodes();
                for (int n = 0; n < connections.getLength(); n++) {
                    if (connections.item(n).getNodeType() != Node.ELEMENT_NODE)
                        continue;
                    Element curElement = (Element) connections.item(n);
                    Node nls = curElement.getElementsByTagName("target").item(0).getChildNodes().item(0);
                    System.out.println(nls.getNodeValue());
                    this.intersections.get(i).addConnection(Integer.parseInt(nls.getNodeValue()));
                    // TODO make this filter work: if (this.streets.stream().filter(street -> !street.getConnections().equals()))
                    int[] toTemp = {this.intersections.get(i).getId(), Integer.parseInt(nls.getNodeValue())};
                    this.streets.add(new Street(1, toTemp));
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }
}
