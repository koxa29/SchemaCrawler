/*
 *
 * SchemaCrawler
 * http://www.schemacrawler.com
 * Copyright (c) 2000-2016, Sualeh Fatehi.
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

package schemacrawler.crawl;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import schemacrawler.schema.ResultsColumn;
import schemacrawler.schema.ResultsColumns;

/**
 * Represents a result set, a result of a query.
 *
 * @author Sualeh Fatehi
 */
class MutableResultsColumns
  extends AbstractNamedObject
  implements ResultsColumns
{

  private static final long serialVersionUID = 5204766782914559188L;

  private final NamedObjectList<MutableResultsColumn> columns = new NamedObjectList<>();

  MutableResultsColumns(final String name)
  {
    super(name);
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.ResultsColumns#getColumns()
   */
  @Override
  public List<ResultsColumn> getColumns()
  {
    return new ArrayList<ResultsColumn>(columns.values());
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.ResultsColumns#getColumnsListAsString()
   */
  @Override
  public String getColumnsListAsString()
  {
    String columnsList = "";
    final List<ResultsColumn> columns = getColumns();
    if (columns != null && columns.size() > 0)
    {
      final StringBuilder buffer = new StringBuilder(1024);
      for (int i = 0; i < columns.size(); i++)
      {
        if (i > 0)
        {
          buffer.append(", ");
        }
        final ResultsColumn column = columns.get(i);
        buffer.append(column.getFullName());
      }
      columnsList = buffer.toString();
    }
    return columnsList;
  }

  @Override
  public Iterator<ResultsColumn> iterator()
  {
    return getColumns().iterator();
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.ResultsColumns#lookupColumn(java.lang.String)
   */
  @Override
  public Optional<MutableResultsColumn> lookupColumn(final String name)
  {
    return columns.lookup(name);
  }

  void addColumn(final MutableResultsColumn column)
  {
    columns.add(column);
  }

}
