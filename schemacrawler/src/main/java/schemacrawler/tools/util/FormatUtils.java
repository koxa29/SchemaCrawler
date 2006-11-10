/* 
 *
 * SchemaCrawler
 * http://sourceforge.net/projects/schemacrawler
 * Copyright (c) 2000-2006, Sualeh Fatehi.
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

package schemacrawler.tools.util;


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import schemacrawler.schema.DatabaseInfo;
import sf.util.Utilities;

/**
 * Utility.
 * 
 * @author sfatehi
 */
public final class FormatUtils
{
  
  /**
   * Internal storage for information. Read from text file.
   */
  private static String styleSheet = "";

  static
  {
    final byte[] text = Utilities.readFully(FormatUtils.class
      .getResourceAsStream("/schema_output.css"));
    styleSheet = new String(text);
  }  

  public static class TableCell
  {
  
    private String styleClass;
    private int colSpan = 1;
    private String innerHtml;
  
    public TableCell()
    {
      this(0, null, null);
    }
  
    public TableCell(String styleClass, String innerHtml)
    {
      this.styleClass = styleClass;
      this.innerHtml = innerHtml;
    }
    
    public TableCell(int colSpan, String styleClass, String innerHtml)
    {
      this.colSpan = colSpan;
      this.styleClass = styleClass;
      this.innerHtml = innerHtml;
    }
  
    public String toString()
    {
      StringBuffer buffer = new StringBuffer();
      buffer.append("<td");
      if (colSpan > 1)
      {
        buffer.append(" colspan='").append(colSpan).append("'");
      }
      if (!Utilities.isBlank(styleClass))
      {
        buffer.append(" class='").append(styleClass).append("'");
      }
      buffer.append(">");
      if (!Utilities.isBlank(innerHtml))
      {
        buffer.append(innerHtml);
      }
      else
      {
        buffer.append("&nbsp;");
      }
      buffer.append("</td>");
  
      return buffer.toString();
    }
  
  }

  public static class TableRow
  {

    private List cells;

    public TableRow(int colSpan)
    {
      this.cells = Arrays.asList(new TableCell[] {
        new TableCell(colSpan, null, null)
      });
    }

    public TableRow()
    {
      this.cells = new ArrayList();
    }

    public void addCell(TableCell cell)
    {
      cells.add(cell);
    }

    public String toString()
    {
      StringBuffer buffer = new StringBuffer();
      buffer.append("\t<tr>\n");
      for (Iterator iter = cells.iterator(); iter.hasNext();)
      {
        TableCell cell = (TableCell) iter.next();
        buffer.append("\t\t").append(cell).append("\n");
      }
      buffer.append("\t</tr>");

      return buffer.toString();
    }

  }

  public static class HtmlTable
  {

    private List rows;

    public HtmlTable()
    {
      this.rows = new ArrayList();
    }

    public void addRow(TableRow row)
    {
      rows.add(row);
    }

    public String toString()
    {
      StringBuffer buffer = new StringBuffer();
      buffer.append("<table>\n");
      for (Iterator iter = rows.iterator(); iter.hasNext();)
      {
        TableRow row = (TableRow) iter.next();
        buffer.append("\t").append(row).append("\n");
      }
      buffer.append("\n</table>");

      return buffer.toString();
    }

  }
  
  /**
   * HTML footer.
   */
  public static final String HTML_FOOTER = "</body>" + Utilities.NEWLINE
      + "</html>";
  /**
   * HTML header.
   */
  public static final String HTML_HEADER = ""
      + "<?xml version='1.0' encoding='UTF-8'?>" + Utilities.NEWLINE
      + "<!DOCTYPE html" + Utilities.NEWLINE
      + "     PUBLIC \"-//W3C//DTD HTML 4.01 Strict//EN\"" + Utilities.NEWLINE
      + "    \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">"
      + Utilities.NEWLINE + "<html xmlns='http://www.w3.org/1999/xhtml'>"
      + Utilities.NEWLINE + "<head>" + Utilities.NEWLINE
      + "  <title>SchemaCrawler Output</title>" + Utilities.NEWLINE
      + "  <style type='text/css'>" + Utilities.NEWLINE
      + styleSheet + Utilities.NEWLINE + "  " +
          "</style>" + Utilities.NEWLINE
      + "</head>" + Utilities.NEWLINE + "<body>" + Utilities.NEWLINE;

  private static final char QUOTE = '\"';
  private static final char SEPARATOR = ',';

  /**
   * Enclose the value in quotes and escape the quote and comma
   * characters that are inside.
   * 
   * @param value
   *        needs to be escaped and quoted
   * @return the value, escaped and quoted.
   */
  public static String escapeAndQuoteForCsv(final String value)
  {

    final int length = value.length();
    if (length == 0)
    {
      return "\"\"";
    }

    if (value.indexOf(SEPARATOR) < 0 && value.indexOf(QUOTE) < 0)
    {
      return value;
    }

    final StringBuffer sb = new StringBuffer(length);
    sb.append(QUOTE);
    for (int i = 0; i < length; i++)
    {
      final char c = value.charAt(i);
      switch (c)
      {
        case '\n':
          sb.append("\\n");
          break;
        case '\r':
          sb.append("\\r");
          break;
        case '\\':
          sb.append("\\\\");
          break;
        default:
          if (c == QUOTE)
          {
            sb.append("\\" + QUOTE);
          } else
          {
            sb.append(c);
          }
      }
    }
    sb.append(QUOTE);

    return sb.toString();

  }

  /**
   * Enclose the value in quotes and escape the quote and comma
   * characters that are inside.
   * 
   * @param value
   *        needs to be escaped and quoted
   * @return the value, escaped and quoted.
   */
  public static String escapeAndQuoteForExcelCsv(final String value)
  {

    final int length = value.length();
    if (length == 0)
    {
      return "\"\"";
    }

    if (value.indexOf(SEPARATOR) < 0 && value.indexOf(QUOTE) < 0)
    {
      return value;
    }

    final StringBuffer sb = new StringBuffer(length);
    sb.append(QUOTE);
    for (int i = 0; i < length; i++)
    {
      final char c = value.charAt(i);
      if (c == QUOTE)
      {
        sb.append(QUOTE + QUOTE);
      } else
      {
        sb.append(c);
      }
    }
    sb.append(QUOTE);

    return sb.toString();

  }

  /** Maximum output line length */
  public static final int MAX_LINE_LENGTH = 72;

  private FormatUtils()
  {
  }

  /**
   * Wraps text in HTML boldface tags.
   * 
   * @param name
   *        Text
   * @return HTML with boldface tags
   */
  public static String htmlBold(final String name)
  {
    return "<b>" + name + "</b>";
  }

  /**
   * Wraps text in HTML boldface tags.
   * 
   * @param name
   *        Text
   * @return HTML with boldface tags
   */
  public static String htmlAlignRight(final String name)
  {
    return "<div style='text-align: right'>" + name + "</div>";
  }

  /**
   * Prints database information.
   * 
   * @param databaseInfo
   *        Database information
   * @param out
   *        Output writer
   */
  public static void printDatabaseInfo(final DatabaseInfo databaseInfo,
      final PrintWriter out)
  {
    out.println(Utilities.repeat("-", MAX_LINE_LENGTH));
    out.println(databaseInfo.toString());
    out.println(Utilities.repeat("-", MAX_LINE_LENGTH));
    out.println();
    out.println();
    out.flush();
  }

}
