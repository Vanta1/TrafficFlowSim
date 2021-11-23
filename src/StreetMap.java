import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
            }


        } catch (ParserConfigurationException | SAXException |
                IOException e) {
            e.printStackTrace();
        }

    }
}
