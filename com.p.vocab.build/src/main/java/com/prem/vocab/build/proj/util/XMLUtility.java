package com.prem.vocab.build.proj.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.prem.vocab.build.proj.VocabBuildConstants;
import com.prem.vocab.build.proj.XMLBasicMethodsRequired;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class XMLUtility {
	private static Logger log = Logger.getLogger(XMLUtility.class);
	private static XMLUtility thisInstance = null;
	private static File xmlFile = null;
	private Document dom = null;

	private XMLUtility(String xmlFileName) throws ParserConfigurationException, SAXException, IOException {
		if (xmlFileName != null) {
			xmlFile = new File(xmlFileName);
			createDomFromXMLFile();
		}
	}

	public static XMLUtility getInstance(String xmlFileName)
			throws ParserConfigurationException, SAXException, IOException {
		if ((thisInstance == null) || (xmlFile == null)
				|| ((xmlFile != null) && (!xmlFile.getAbsolutePath().equals(xmlFileName)))) {
			thisInstance = new XMLUtility(xmlFileName);
		}
		return thisInstance;
	}

	private void createDomFromXMLFile() throws ParserConfigurationException, SAXException, IOException {
		try {
			InputStream inputData = new FileInputStream(xmlFile);
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			dom = docBuilder.parse(inputData);
			dom.getDocumentElement().normalize();
		} catch (FileNotFoundException e) {
			log.debug("FileNotFoundException : ", e);
			throw e;
		} catch (ParserConfigurationException e) {
			log.debug("ParserConfigurationException : ", e);
			throw e;
		} catch (SAXException e) {
			log.debug("SAXException : ", e);
			throw e;
		} catch (IOException e) {
			log.debug("IOException : ", e);
			throw e;
		}
	}

	public Element getRootNodeElement() {
		Element rootElemNode = null;
		if (dom != null) {
			dom.getDocumentElement().normalize();
			NodeList rootNodeList1 = dom.getElementsByTagName(VocabBuildConstants.ROOT.getName());
			Node rootNode = rootNodeList1.item(0);
			if (rootNode.getNodeType() == 1) {
				rootElemNode = (Element) rootNode;
				return rootElemNode;
			}
		} else {
			System.out.println("Document object is null. Returning null root object");
		}
		return rootElemNode;
	}

	public NodeList getAllNodesOfType(XMLBasicMethodsRequired xm, Element element) {
		NodeList nodeList = null;
		if ((element != null) && (xm != null)) {
			nodeList = element.getElementsByTagName(xm.getName());
		} else {
			String message = xm != null ? "" : "Given XMLBasicMethodsRequired object is null . ";
			message = message.length() > 0 ? message + " Also , " : "";
			message = element != null ? "" : "Given Element object is null . ";

			log.debug(message);
		}
		return nodeList;
	}

	public Node getNthNodeOfType(XMLBasicMethodsRequired xm, Element element, int position) {
		Node node = null;
		NodeList nodeList = getAllNodesOfType(xm, element);
		if ((nodeList != null) && (nodeList.getLength() > position)) {
			node = nodeList.item(position);
		} else {
			String message = xm != null ? "" : "Given XMLBasicMethodsRequired object is null . ";
			message = message.length() > 0 ? message + " Also , " : "";
			message = element != null ? "" : "Given Element object is null . ";

			log.debug(message);
		}
		return node;
	}

	public Element getElement(Node node) {
		Element element = null;
		if ((node != null) && (node.getNodeType() == 1)) {
			element = (Element) node;
		} else {
			String message = node != null ? "" : "Given Node object is null . ";
			log.debug(message);
		}
		return element;
	}

	public String getAttribute(XMLBasicMethodsRequired xm, Element element) {
		String attribute = null;
		if ((element != null) && (xm != null)) {
			attribute = element.getAttribute(xm.getName());
		} else {
			String message = xm != null ? "" : "Given XMLBasicMethodsRequired object is null . ";
			message = message.length() > 0 ? message + " Also , " : "";
			message = element != null ? "" : "Given Element object is null . ";

			log.debug(message);
		}
		return attribute;
	}

	public int getIntValue(String givenString) throws NumberFormatException {
		try {
			return (givenString != null) && (!givenString.trim().equalsIgnoreCase("")) ? Integer.parseInt(givenString)
					: 0;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			System.out.println("Exception in converting " + givenString + " to integer value.");

			throw e;
		}
	}

	public String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if ((child != null) && ((child instanceof CharacterData))) {
			CharacterData cd = (CharacterData) child;

			return cd.getData();
		}
		return "";
	}

	public Element createElement(XMLBasicMethodsRequired xm) {
		Element element = null;
		if ((xm != null) && (dom != null)) {
			element = dom.createElement(xm.getName());
		}
		return element;
	}

	public Element appendComment(Element element, String comment) {
		if ((dom != null) && (element != null) && (comment != null) && (!comment.trim().equalsIgnoreCase(""))) {
			element.appendChild(dom.createComment(comment.trim()));
		}
		return element;
	}

	public Element setAttribute(XMLBasicMethodsRequired xm, Element element, String value,
			boolean emptyOrNullValuesAllowed) {
		if ((element != null) && (xm != null)) {
			boolean neitherEmptyNorNull = (value != null) && (!value.trim().equalsIgnoreCase(""));
			if ((emptyOrNullValuesAllowed) || (neitherEmptyNorNull)) {
				element.setAttribute(xm.getName(), value != null ? value.trim() : "null");
			}
		}
		return element;
	}

	public Element appendCDATASection(Element element, String value, boolean emptyOrNullValuesAllowed) {
		if ((dom != null) && (element != null)) {
			boolean neitherEmptyNorNull = (value != null) && (!value.trim().equalsIgnoreCase(""));
			if ((emptyOrNullValuesAllowed) || (neitherEmptyNorNull)) {
				element.appendChild(dom.createCDATASection(value != null ? value.trim() : ""));
			}
		}
		return element;
	}

	public Element appendText(Element element, String value, boolean emptyOrNullValuesAllowed) {
		if ((dom != null) && (element != null)) {
			boolean neitherEmptyNorNull = (value != null) && (!value.trim().equalsIgnoreCase(""));
			if ((emptyOrNullValuesAllowed) || (neitherEmptyNorNull)) {
				element.appendChild(dom.createTextNode(value != null ? value.trim() : ""));
			}
		}
		return element;
	}

	public Document getDom() {
		return dom;
	}

	public void printToFile() throws IOException {
		try {
			OutputFormat format = new OutputFormat(dom);
			format.setIndenting(true);
			OutputStream outputStream = new FileOutputStream(xmlFile);
			XMLSerializer serializer = new XMLSerializer(outputStream, format);
			serializer.serialize(dom);
			outputStream.close();
		} catch (FileNotFoundException e) {
			log.debug("FileNotFoundException : ", e);
			throw e;
		} catch (IOException e) {
			log.debug("IOException : ", e);
			throw e;
		}
	}

	public String getXMLString() {
		String xmlContent = "<" + VocabBuildConstants.ROOT.getName() + ">" + "</" + VocabBuildConstants.ROOT.getName()
				+ ">";
		try {
			FileReader fl = new FileReader(xmlFile);
			BufferedReader bf = new BufferedReader(fl);
			String str = "";
			StringBuffer sb = new StringBuffer();
			while ((str = bf.readLine()) != null) {
				sb.append(str);
			}
			fl.close();

			xmlContent = sb.toString();
		} catch (Exception e) {
			log.debug("Error : ", e);
		}
		return xmlContent;
	}
}