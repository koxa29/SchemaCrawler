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

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import schemacrawler.schema.Column;
import schemacrawler.schema.JavaSqlType.JavaSqlTypeGroup;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.Config;
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;

public class LinterTooManyLobs
  extends BaseLinter
{

  private int maxLargeObjectsInTable;

  public LinterTooManyLobs()
  {
    setSeverity(LintSeverity.low);

    maxLargeObjectsInTable = 1;
  }

  @Override
  public String getSummary()
  {
    return "too many binary objects";
  }

  @Override
  protected void configure(final Config config)
  {
    requireNonNull(config, "No configuration provided");

    maxLargeObjectsInTable = config.getIntegerValue("max-large-objects", 1);
  }

  @Override
  protected void lint(final Table table, final Connection connection)
  {
    requireNonNull(table, "No table provided");

    final ArrayList<Column> lobColumns = findLobColumns(getColumns(table));
    if (lobColumns.size() > maxLargeObjectsInTable)
    {
      addTableLint(table, getSummary(), lobColumns);
    }
  }

  private ArrayList<Column> findLobColumns(final List<Column> columns)
  {
    final ArrayList<Column> lobColumns = new ArrayList<>();
    for (final Column column: columns)
    {
      final JavaSqlTypeGroup javaSqlTypeGroup = column.getColumnDataType()
        .getJavaSqlType().getJavaSqlTypeGroup();
      if (javaSqlTypeGroup == JavaSqlTypeGroup.large_object)
      {
        lobColumns.add(column);
      }
    }
    return lobColumns;
  }

}
