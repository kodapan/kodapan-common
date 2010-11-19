/*
 * Copyright 2010 Kodapan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.kodapan.html;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.util.*;

/**
 * @author kalle
 * @since 2009-jul-12 16:58:57
 */
public class NekoHtmlTool {

  public static final XPath xpath = XPathFactory.newInstance().newXPath();

  public static interface NodeAcceptor {
    public abstract boolean accept(Node node);
  }

  public static Node findNode(Node node, NodeAcceptor acceptor) {
    List<Node> nodes = findNodes(node, acceptor);
    if (nodes.size() == 0) {
      return null;
    } else if (nodes.size() == 1) {
      return nodes.get(0);
    } else {
      throw new RuntimeException(nodes.size() + " nodes found but expected 0 or 1.");
    }
  }

  public static List<Node> findNodes(Node node, final NodeAcceptor acceptor) {
    final List<Node> nodes = new ArrayList<Node>();
    visitNodes(node, new PathVisitor() {
      @Override
      public Object visit(Node node, int[] path) {
        if (acceptor.accept(node)) {
          nodes.add(node);
        }
        return null;
      }
    });
    return nodes;
  }

  public static interface TextVisitor {
    public abstract void visit(Node node, String text, int[] path);
  }

  public static <T> T visitNodes(Node node, URI documentURI, Visitor<T> visitor) {
    T t;
    t = visitor.visit(node, documentURI);
    if (t != null) {
      return t;
    }
    for (int i = 0; i < node.getChildNodes().getLength(); i++) {
      t = visitNodes(node.getChildNodes().item(i), documentURI, visitor);
      if (t != null) {
        return t;
      }
    }
    return null;
  }

  public abstract static class Visitor<T> {
    public abstract T visit(Node node, URI documentURI);
  }

  public abstract static class PathVisitor<T> {
    public abstract T visit(Node node, int[] path);
  }


  public abstract static class PathTextVisitor<T> {
    public abstract T visit(Node node, String text, int[] path);
  }

  public static <T> T visitNodes(Node node, PathVisitor<T> visitor) {
    return visitNodes(node, visitor, new int[0]);
  }

  public static <T> T visitNodes(Node node, PathVisitor<T> visitor, int[] path) {
    for (int i = 0; i < node.getChildNodes().getLength(); i++) {
      int[] childpath = new int[path.length + 1];
      System.arraycopy(path, 0, childpath, 0, path.length);
      childpath[childpath.length - 1] = i;
      Node child = node.getChildNodes().item(i);
      T result = visitNodes(child, visitor, childpath);
      if (result != null) {
        return result;
      }
    }
    return visitor.visit(node, path);
  }


  public static void visitTextNodes(Node node, TextVisitor visitor) {
    visitTextNodes(node, visitor, new int[0]);
  }

  public static <T> T visitTextNodesAndReturn(Node node, PathTextVisitor<T> visitor, int[] path) {

    T result;

    if ("SCRIPT".equalsIgnoreCase(node.getNodeName())) {
      return null;
    }

    for (int i = 0; i < node.getChildNodes().getLength(); i++) {
      int[] childpath = new int[path.length + 1];
      System.arraycopy(path, 0, childpath, 0, path.length);
      childpath[childpath.length - 1] = i;
      Node child = node.getChildNodes().item(i);
      if (child.getClass().getName().equals("org.apache.xerces.dom.TextImpl")) {

        String text = child.getTextContent();
        if (text != null && !"".equals(text.replaceAll("[^a-zA-Z0-9]+", ""))) {
          result = visitor.visit(child, text, childpath);
          if (result != null) {
            return result;
          }
        }
      }
      result = visitTextNodesAndReturn(child, visitor, childpath);
      if (result != null) {
        return result;
      }
    }

    return null;
  }

  public static void visitTextNodes(Node node, TextVisitor visitor, int[] path) {

    if ("SCRIPT".equalsIgnoreCase(node.getNodeName())) {
      return;
    }

    for (int i = 0; i < node.getChildNodes().getLength(); i++) {
      int[] childpath = new int[path.length + 1];
      System.arraycopy(path, 0, childpath, 0, path.length);
      childpath[childpath.length - 1] = i;
      Node child = node.getChildNodes().item(i);
      if (child.getClass().getName().equals("org.apache.xerces.dom.TextImpl")) {

        String text = child.getTextContent();
        if (text != null && !"".equals(text.replaceAll("[^a-zA-Z0-9]+", ""))) {
          visitor.visit(child, text, childpath);
        }
      }
      visitTextNodes(child, visitor, childpath);
    }
  }

  public static String getText(Node node) {
    StringBuilder sb = new StringBuilder();
    getText(node, sb);
    return sb.toString();
  }

  public static void getText(Node node, StringBuilder sb) {

    if (node.getClass().getName().equals("org.apache.xerces.dom.TextImpl")
        && node.getParentNode() != null
        && !"script".equalsIgnoreCase(node.getParentNode().getLocalName())) {
      String text = node.getTextContent();
      if (text != null) {
        text = text.trim();
        if (!"".equals(text)) {
          sb.append(node.getTextContent());
          sb.append("\n");
        }
      }
    }
    Node child = node.getFirstChild();
    while (child != null) {
      getText(child, sb);
      child = child.getNextSibling();
    }
  }


  public static void print(Node node) {
    print(node, "");
  }

  public static void print(Node node, String indent) {
    if (isTextNode(node)) {
      String text = node.getTextContent();
      if (text != null) {
        text = text.trim();
        if (!"".equals(text)) {
          System.out.println(indent + " " + node.getClass().getName() + " " + node.getTextContent());
        }
      }
    }
    Node child = node.getFirstChild();
    int pos = 0;
    while (child != null) {
      print(child, indent + "." + pos);
      child = child.getNextSibling();
      pos++;
    }
  }

  public static Node findNodeById(Node root, String id) {

    try {

      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();
      XPathExpression expr = xpath.compile("//*[@id='" + id + "']");

      Object result = expr.evaluate(root, XPathConstants.NODESET);
      NodeList nodes = (NodeList) result;
      if (nodes.getLength() > 1) {
        throw new RuntimeException("More than one node with attribute id=" + id);
      } else if (nodes.getLength() == 0) {
        return null;
      } else {
        return nodes.item(0);
      }

    } catch (XPathExpressionException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }

    return null;
  }

  public static Node getNode(Node root, int... path) {
    List<Integer> foor = new ArrayList<Integer>();
    for (int i : path) {
      foor.add(i);
    }
    return getNode(root, foor);
  }

  public static Node getNode(Node root, Iterable<Integer> path) {
    Node current = root;
    for (int index : path) {
      NodeList childNodes = current.getChildNodes();
      if (childNodes.getLength() < index) {
        return null;
      }
      current = childNodes.item(index);
      if (current == null) {
        return null;
      }
    }
    return current;
  }

  public static Node findNodeByNodeName(Node parent, final String tag) {
    List<Node> nodes = findNodesByNodeName(parent, tag);
    if (nodes == null) {
      return null;
    } else if (nodes.size() == 1) {
      return nodes.get(0);
    } else {
      throw new RuntimeException(nodes.size() + " nodes found, expected 0 or 1.");
    }
  }

  public static List<Node> findNodesByNodeName(Node parent, final String tag) {
    final List<Node> nodes = new ArrayList<Node>();
    visitNodes(parent, new PathVisitor<List<Node>>() {
      @Override
      public List<Node> visit(Node node, int[] path) {
        if (tag.equals(node.getNodeName())) {
          nodes.add(node);
        }
        return null;
      }
    });
    if (nodes.size() == 0) {
      return null;
    } else {
      return nodes;
    }
  }


  public static List<Node> getFormParameters(Node form) {


    final List<Node> nodes = new ArrayList<Node>();
    visitNodes(form, new PathVisitor<List<Node>>() {
      @Override
      public List<Node> visit(Node node, int[] path) {
        if ("SELECT".equals(node.getNodeName())) {
          nodes.add(node);

        } else if ("INPUT".equals(node.getNodeName())
            || "BUTTON".equals(node.getNodeName())) {
          nodes.add(node);
        }
        return null;
      }
    });
    if (nodes.size() == 0) {
      return null;
    } else {
      return nodes;
    }
  }

  public static String getAttributeValue(Node node, String attribute, boolean caseSensitive) {
    if (caseSensitive) {
      return getAttributeValue(node, attribute);
    }

    if (node.getAttributes() == null) {
      return null;
    }
    NamedNodeMap attributeNodes = node.getAttributes();
    Map<String, String> matches = new HashMap<String, String>();
    for (int i=0; i<attributeNodes.getLength(); i++) {
      Node attributeNode = attributeNodes.item(i);
      if (attributeNode.getNodeName().equalsIgnoreCase(attribute)) {
        matches.put(attributeNode.getNodeName(), attributeNode.getTextContent());
      }
    }
    if (matches.size() == 0) {
      return null;
    } else if (matches.size() == 1) {
      return matches.entrySet().iterator().next().getValue();
    } else {
      throw new RuntimeException("Multiple case insensitive matches!");
    }
  }

  public static String getAttributeValue(Node node, String attribute) {
    if (node.getAttributes() == null) {
      return null;
    }
    Node attributeNode = node.getAttributes().getNamedItem(attribute);
    if (attributeNode == null) {
      return null;
    }
    return attributeNode.getTextContent();
  }

  public static Node findNodeByAttribute(Node node, final String name, final String value) {
    List<Node> nodes = findNodesByAttribute(node, name, value);
    if (nodes == null) {
      return null;
    }
    if (nodes.size() != 1) {
      throw new RuntimeException("Expected a single node with that attribute");
    }
    return nodes.get(0);
  }

  public static List<Node> findNodesByAttribute(Node node, final String name, final String value) {
    final List<Node> nodes = new ArrayList<Node>();
    visitNodes(node, new PathVisitor() {
      @Override
      public Object visit(Node node, int[] path) {
        String tmp = getAttributeValue(node, name);
        if (tmp != null && tmp.equals(value)) {
          nodes.add(node);
        }
        return null;
      }
    });
    return nodes.size() == 0 ? null : nodes;
  }

  public static String toXML(Node node) {
    StringWriter sw = new StringWriter(1024);
    try {
      writeXML(node, sw);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return sw.toString();
  }

  public static void writeXML(Node node, Writer out) throws IOException {

    if ("#text".equals(node.getNodeName())) {
      // no empty text nodes!
      if (!"".equals(normalizeText(node).replaceAll("\\s+", ""))) {
        out.write(node.getTextContent());
      }
    } else {

      out.write("<");
      out.write(node.getNodeName());
      if (node.getAttributes() != null) {
        for (int i = 0; i < node.getAttributes().getLength(); i++) {
          out.write(" ");
          Node attribute = node.getAttributes().item(i);
          out.write(attribute.getNodeName());
          out.write("=\"");
          out.write(attribute.getTextContent());
          out.write("\"");
        }
      }

      if (node.getChildNodes().getLength() == 0) {

        out.write("/>");

      } else {

        out.write(">");

        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
          writeXML(node.getChildNodes().item(i), out);
        }

        out.write("</");
        out.write(node.getNodeName());
        out.write(">");

      }
    }
  }


  /**
   * Pattern \\s does not match (char)160
   */
  public static String replaceNonBreakingSpaces(String text) {
    // replace &NBSP; with normal whitespace
    text = text.replaceAll(new String(new char[]{160}), " ");

    return text;
  }

  public static String normalizeText(Node node) {
    return normalizeText(node.getTextContent());
  }

  public static String normalizeText(String text) {
    // replace &NBSP; with normal whitespace
    text = text.replaceAll(new String(new char[]{160}), " ");
    text = text.replaceAll("\\s+", " ");
    text = text.trim();
    return text;
  }


  public static String toText(Node node) {
    StringWriter out = new StringWriter(1024);
    try {
      writeText(node, out);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return out.toString();
  }


  public static void writeText(Node node, final Writer out) throws IOException {
    if (isTextNode(node)) {
      out.write(normalizeText(node));
      out.write("\n");
    } else {
      NodeList children = node.getChildNodes();
      if (children != null) {
        for (int i = 0; i < children.getLength(); i++) {
          writeText(children.item(i), out);
        }
      }
    }

  }

  public static boolean isTextNode(Node node) {
    return node.getParentNode() != null
//        && node.getParentNode().getNodeName().equals("#comment")    // todo why is this commented out?
        && "#text".equalsIgnoreCase(node.getNodeName())
        && !"STYLE".equalsIgnoreCase(node.getParentNode().getNodeName())
        && !"SCRIPT".equalsIgnoreCase(node.getParentNode().getNodeName());
  }


  public static Node findFirstSiblingNodeByNodeName(Node node, String nodeName) {
    while ((node = node.getNextSibling()) != null) {
      if (nodeName.equals(node.getNodeName())) {
        return node;
      }
    }
    return null;
  }

  public static Node findFirstParentNodeByNodeName(Node node, String nodeName) {
    while ((node = node.getParentNode()) != null) {
      if (nodeName.equals(node.getNodeName())) {
        return node;
      }
    }
    return null;
  }

  public static String getXPath(Node node) {
    return getXPath(node, node.getOwnerDocument());
  }

  public static String getXPath(Node node, Node root) {

    while (isTextNode(node)) {
      node = node.getParentNode();
    }
    Node in = node;

    StringBuilder out = new StringBuilder();

    while (node != null && !root.equals(node)) {
      int i = 0;
      Node sibling = node;
      while ((sibling = sibling.getPreviousSibling()) != null) {
        if (!isTextNode(sibling)) {
          if (sibling.getNodeName().equals(node.getNodeName())) {
            i++;
          }
        }
      }

      StringBuilder path = new StringBuilder();
      path.append("/").append(node.getNodeName());
      if (i > 0) {
        path.append("[position() = ").append(String.valueOf(i + 1)).append("]");
      }
      out.insert(0, path);

      node = node.getParentNode();
      while (isTextNode(node)) {
        node = node.getParentNode();
      }
    }

    if (!root.equals(in.getOwnerDocument())) {
      out.deleteCharAt(0);
    }

//    try {
//      XPathFactory factory = XPathFactory.newInstance();
//      XPath xp = factory.newXPath();
//      XPathExpression exp = xp.compile(out.toString());
//      Node foo = (Node)exp.evaluate(root, XPathConstants.NODE);
//      System.currentTimeMillis();
//    } catch (Exception e) {
//      e.printStackTrace();
//    }


    return out.toString();
  }

  public static int[] getPath(Node node) {
    List<Integer> path = new ArrayList<Integer>();
    while (node.getParentNode() != null) {
      int i = 0;
      Node tmp = node;
      while ((tmp = tmp.getPreviousSibling()) != null) {
        i++;
      }
      path.add(0, i);
      node = node.getParentNode();
    }
    int[] result = new int[path.size()];
    for (int i = 0; i < path.size(); i++) {
      result[i] = path.get(i);
    }
    return result;
  }

  public static void removeEmptyTextNodes(Node node) {
    NodeList children = node.getChildNodes();
    List<Node> nodes = new ArrayList<Node>();
    for (int i=0; i<children.getLength(); i++) {
      nodes.add(children.item(i));
    }
    for (Iterator<Node> it = nodes.iterator(); it.hasNext();) {
      Node child = it.next();
      if ("#text".equalsIgnoreCase(child.getNodeName())
          && "".equals(normalizeText(child).replaceAll("\\s+", ""))) {
        node.removeChild(child);
        it.remove();
      }
    }
    for (Node child : nodes) {
      removeEmptyTextNodes(child);
    }
  }
}
