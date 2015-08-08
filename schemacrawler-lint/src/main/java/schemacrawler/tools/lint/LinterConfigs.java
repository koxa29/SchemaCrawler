/*
 *
 * SchemaCrawler
 * http://www.schemacrawler.com
 * Copyright (c) 2000-2015, Sualeh Fatehi.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */
package schemacrawler.tools.lint;


import static java.util.Objects.requireNonNull;
import static sf.util.Utility.isBlank;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import schemacrawler.schemacrawler.Config;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import sf.util.ObjectToString;
import sf.util.Utility;

public class LinterConfigs
{

  private static final Logger LOGGER = Logger
    .getLogger(LinterConfig.class.getName());

  private static Element getSubElement(final Element element,
                                       final String tagName)
  {
    if (isBlank(tagName))
    {
      throw new IllegalArgumentException("Cannot get sub-element, since no name is provided");
    }
    requireNonNull(element, "Cannot get sub-element for tag " + tagName);

    final Element subElement;
    final NodeList nodeList = element.getElementsByTagName(tagName);
    if (nodeList != null && nodeList.getLength() > 0)
    {
      subElement = (Element) nodeList.item(0);
    }
    else
    {
      subElement = null;
    }

    return subElement;
  }

  private static String getTextValue(final Element element,
                                     final String tagName)
  {
    final String text;
    final Element subElement = getSubElement(element, tagName);
    if (subElement != null)
    {
      text = subElement.getFirstChild().getNodeValue();
    }
    else
    {
      text = null;
    }

    return text;
  }

  private static Config parseConfig(final Element configElement)
  {
    final Config config = new Config();
    if (configElement == null)
    {
      return config;
    }

    final NodeList propertiesList = configElement
      .getElementsByTagName("property");
    if (propertiesList != null && propertiesList.getLength() > 0)
    {
      for (int i = 0; i < propertiesList.getLength(); i++)
      {
        final Element propertyElement = (Element) propertiesList.item(i);
        final String name = propertyElement.getAttribute("name");
        final String value = propertyElement.getFirstChild().getNodeValue();
        if (!Utility.isBlank(name))
        {
          config.put(name, value);
        }
      }
    }

    return config;
  }

  private static List<LinterConfig> parseDocument(final Document document)
  {
    requireNonNull(document, "No document provided");

    final List<LinterConfig> linterConfigs = new ArrayList<>();

    final Element root = document.getDocumentElement();
    final NodeList linterNodesList = root.getElementsByTagName("linter");
    if (linterNodesList != null && linterNodesList.getLength() > 0)
    {
      for (int i = 0; i < linterNodesList.getLength(); i++)
      {
        final Node node = linterNodesList.item(i);
        if (node instanceof Element)
        {
          final LinterConfig linterConfig = parseLinterConfig((Element) node);
          if (linterConfig != null)
          {
            linterConfigs.add(linterConfig);
          }
        }
      }
    }

    return linterConfigs;
  }

  private static LinterConfig parseLinterConfig(final Element linterElement)
  {
    requireNonNull(linterElement, "No linter configuration provided");

    final String linterId;
    if (linterElement.hasAttribute("id"))
    {
      linterId = linterElement.getAttribute("id");
    }
    else
    {
      linterId = null;
    }
    if (isBlank(linterId))
    {
      LOGGER.log(Level.CONFIG,
                 "Not running linter, since linter id is not provided");
      return new LinterConfig("<unknown>", false);
    }

    final boolean runLinter;
    if (linterElement.hasAttribute("runLinter"))
    {
      runLinter = Boolean.valueOf(linterElement.getAttribute("runLinter"));
    }
    else
    {
      // Run linter by default
      runLinter = true;
    }

    final LinterConfig linterConfig = new LinterConfig(linterId, runLinter);

    final String severityValue = getTextValue(linterElement, "severity");
    if (!isBlank(severityValue))
    {
      try
      {
        final LintSeverity severity = LintSeverity.valueOf(severityValue);
        linterConfig.setSeverity(severity);
      }
      catch (final Exception e)
      {
        LOGGER.log(Level.CONFIG,
                   String.format("Could not set a severity of %s for linter %s",
                                 severityValue, linterId));
      }
    }

    final Config config = parseConfig(getSubElement(linterElement, "config"));
    linterConfig.putAll(config);

    return linterConfig;
  }

  private static Document parseXml(final InputSource xmlStream)
    throws ParserConfigurationException, SAXException, IOException
  {
    final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    final DocumentBuilder db = dbf.newDocumentBuilder();
    final Document dom = db.parse(xmlStream);
    return dom;
  }

  private final Map<String, LinterConfig> linterConfigsMap;

  public LinterConfigs()
  {
    linterConfigsMap = new HashMap<>();
  }

  public LinterConfig get(final String linterId)
  {
    return linterConfigsMap.get(linterId);
  }

  public void parse(final Reader reader)
    throws SchemaCrawlerException
  {
    requireNonNull(reader, "No input provided");

    final Document document;
    try
    {
      document = parseXml(new InputSource(reader));
    }
    catch (final Exception e)
    {
      throw new SchemaCrawlerException("Could not parse XML", e);
    }

    final List<LinterConfig> linterConfigs = parseDocument(document);
    for (final LinterConfig linterConfig: linterConfigs)
    {
      linterConfigsMap.put(linterConfig.getId(), linterConfig);
    }
  }

  /**
   * @return
   * @see java.util.Map#size()
   */
  public int size()
  {
    return linterConfigsMap.size();
  }

  @Override
  public String toString()
  {
    return ObjectToString.toString(this);
  }

}
