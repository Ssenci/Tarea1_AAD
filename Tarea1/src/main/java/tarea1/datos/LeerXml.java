package tarea1.datos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LeerXml {
	public static ArrayList<String> obtenerPaises() throws ParserConfigurationException, SAXException, IOException {
		ArrayList<String> listaPaises = new ArrayList<String>();
		File archivo = new File("archivos/paises.xml");

		// creamos una nueva instancia del documento y lo procesamos
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
		Document document = documentBuilder.parse(archivo);

		// obtenemos la lista de paises
		NodeList elementosPaises = document.getElementsByTagName("pais");
		for (int temp = 0; temp < elementosPaises.getLength(); temp++) {
			Node nodo = elementosPaises.item(temp);
			if (nodo.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) nodo;
				listaPaises.add(element.getElementsByTagName("nombre").item(0).getTextContent());
			}
		}
		return listaPaises;
	}
}
