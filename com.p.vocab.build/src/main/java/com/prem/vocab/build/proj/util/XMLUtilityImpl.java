package com.prem.vocab.build.proj.util;

import com.prem.vocab.build.proj.VocabBuildConstants;
import com.prem.vocab.build.proj.VocabBuildConstants.Examples;
import com.prem.vocab.build.proj.VocabBuildConstants.Meanings;
import com.prem.vocab.build.proj.VocabBuildConstants.Word;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;




public class XMLUtilityImpl
{
  String xmlFileName = "C:/APPLN_SERVERS/apache-tomcat-6.0.35/apache-tomcat-6.0.35/webapps/vocab-khajana/xml/khajana.xml";
  SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy");
  
  XMLUtility utility = null;
  
  public XMLUtilityImpl() throws ParserConfigurationException, SAXException, IOException
  {
    utility = XMLUtility.getInstance(xmlFileName);
  }
  

  public String saveTicket(String word, String type, List<String> meanings, List<String> examples)
    throws ParserConfigurationException, SAXException, IOException
  {
    Element rootNode = utility.getRootNodeElement();
    Node wordListElement = utility.getNthNodeOfType(
      VocabBuildConstants.WORD_LIST, rootNode, 0);
    Element mywordListElement = utility.getElement(wordListElement);
    Element myWordElement = utility
      .createElement(VocabBuildConstants.MY_WORD);
    
    Element wordElement = utility.createElement(VocabBuildConstants.Word.node);
    utility.appendCDATASection(wordElement, word, false);
    wordElement = utility.setAttribute(VocabBuildConstants.Word.TYPE, wordElement, type, true);
    myWordElement.appendChild(wordElement);
    
    Element meaningsElement = utility.createElement(VocabBuildConstants.Meanings.node);
    Element meaningElement; for (String meaning : meanings) {
      meaningElement = utility.createElement(VocabBuildConstants.Meanings.MEANING);
      utility.appendCDATASection(meaningElement, meaning, true);
      meaningsElement.appendChild(meaningElement);
    }
    
    myWordElement.appendChild(meaningsElement);
    
    Element examplesElement = utility.createElement(VocabBuildConstants.Examples.node);
    for (String example : examples) {
      Element exampleElement = utility.createElement(VocabBuildConstants.Examples.EXAMPLE);
      utility.appendCDATASection(exampleElement, example, true);
      examplesElement.appendChild(exampleElement);
    }
    myWordElement.appendChild(examplesElement);
    


    mywordListElement.appendChild(myWordElement);
    rootNode.appendChild(mywordListElement);
    
    utility.printToFile();
    return utility.getXMLString();
  }
  
  public List<HashMap<String, String>> getAllDescription() {
    List<HashMap<String, String>> descSet = new ArrayList();
    Element rootNode = utility.getRootNodeElement();
    Node wordListNode = utility.getNthNodeOfType(
      VocabBuildConstants.WORD_LIST, rootNode, 0);
    Element ticketHistoryListElement = utility.getElement(wordListNode);
    NodeList myWordNodeList = utility.getAllNodesOfType(
      VocabBuildConstants.MY_WORD, ticketHistoryListElement);
    
    for (int count = 0; count < myWordNodeList.getLength(); count++) {
      Node myWordNode = myWordNodeList.item(count);
      Element myWordElement = utility.getElement(myWordNode);
      if (myWordElement != null)
      {
        HashMap<String, String> myWordMap = new HashMap();
        


        Node wordNode = utility.getNthNodeOfType(VocabBuildConstants.Word.node, 
          myWordElement, 0);
        Element wordElement = utility.getElement(wordNode);
        String value = utility.getAttribute(VocabBuildConstants.Word.TYPE, wordElement);
        myWordMap.put(VocabBuildConstants.Word.TYPE.getName(), value);
        
        value = utility.getCharacterDataFromElement(wordElement);
        myWordMap.put(VocabBuildConstants.Word.node.getName(), value);
        



        Node meaningsNode = utility.getNthNodeOfType(
          VocabBuildConstants.Meanings.node, myWordElement, 0);
        Element meaningsElement = utility.getElement(meaningsNode);
        NodeList meaningNodeList = utility.getAllNodesOfType(
          VocabBuildConstants.Meanings.MEANING, meaningsElement);
        StringBuffer sb = new StringBuffer();
        for (int count1 = 0; count1 < meaningNodeList.getLength(); count1++) {
          Node meaningNode = meaningNodeList.item(count1);
          Element meaningElement = utility.getElement(meaningNode);
          if (meaningElement != null) {
            sb.append(utility.getCharacterDataFromElement(meaningElement));
            if (count1 < meaningNodeList.getLength() - 1) {
              sb.append("-->");
            }
          }
        }
        myWordMap.put(VocabBuildConstants.Meanings.node.getName(), sb.toString());
        


        Node examplesNode = utility.getNthNodeOfType(
          VocabBuildConstants.Examples.node, myWordElement, 0);
        Element examplesElement = utility.getElement(examplesNode);
        NodeList exampleNodeList = utility.getAllNodesOfType(
          VocabBuildConstants.Examples.EXAMPLE, examplesElement);
        sb = new StringBuffer();
        for (int count1 = 0; count1 < exampleNodeList.getLength(); count1++) {
          Node exampleNode = exampleNodeList.item(count1);
          Element exampleElement = utility.getElement(exampleNode);
          if (exampleElement != null) {
            sb.append(utility.getCharacterDataFromElement(exampleElement));
            if (count1 < exampleNodeList.getLength() - 1) {
              sb.append("-->");
            }
          }
        }
        myWordMap.put(VocabBuildConstants.Examples.node.getName(), sb.toString());
        































        descSet.add(myWordMap);
      }
    }
    
    return descSet;
  }
}