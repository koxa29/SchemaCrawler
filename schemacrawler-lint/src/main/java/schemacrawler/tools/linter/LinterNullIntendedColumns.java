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
package schemacrawler.tools.linter;


import static java.util.Objects.requireNonNull;
import static sf.util.Utility.isBlank;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import schemacrawler.filter.TableTypesFilter;
import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import schemacrawler.tools.lint.BaseLinter;

public class LinterNullIntendedColumns
  extends BaseLinter
{

  public LinterNullIntendedColumns()
  {
    setTableTypesFilter(new TableTypesFilter("TABLE"));
  }

  @Override
  public String getSummary()
  {
    return "column where NULL may be intended";
  }

  @Override
  protected void lint(final Table table, final Connection connection)
  {
    requireNonNull(table, "No table provided");

    final List<Column> nullDefaultValueMayBeIntendedColumns = findNullDefaultValueMayBeIntendedColumns(table
      .getColumns());
    for (final Column column: nullDefaultValueMayBeIntendedColumns)
    {
      addTableLint(table, getSummary(), column);
    }
  }

  private List<Column> findNullDefaultValueMayBeIntendedColumns(final List<Column> columns)
  {
    final List<Column> nullDefaultValueMayBeIntendedColumns = new ArrayList<>();
    for (final Column column: columns)
    {
      final String columnDefaultValue = column.getDefaultValue();
      if (!isBlank(columnDefaultValue)
          && columnDefaultValue.trim().equalsIgnoreCase("NULL"))
      {
        nullDefaultValueMayBeIntendedColumns.add(column);
      }
    }
    return nullDefaultValueMayBeIntendedColumns;
  }

}
