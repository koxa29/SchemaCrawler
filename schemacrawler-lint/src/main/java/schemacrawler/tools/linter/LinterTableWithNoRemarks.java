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
import schemacrawler.schema.Table;
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;

/**
 * Check that tables and columns) have remarks.
 *
 * @author Michèle Barré, Sualeh Fatehi
 */
public class LinterTableWithNoRemarks
  extends BaseLinter
{

  public LinterTableWithNoRemarks()
  {
    setSeverity(LintSeverity.low);
  }

  @Override
  public String getSummary()
  {
    return "should have remarks";
  }

  @Override
  protected void lint(final Table table, final Connection connection)
  {
    requireNonNull(table, "No table provided");

    if (!table.hasRemarks())
    {
      addTableLint(table, getSummary());
    }

    final ArrayList<String> columnsWithNoRemarks = findColumnsWithNoRemarks(table
      .getColumns());
    if (!columnsWithNoRemarks.isEmpty())
    {
      addTableLint(table, getSummary(), columnsWithNoRemarks);
    }
  }

  private ArrayList<String> findColumnsWithNoRemarks(final List<Column> columns)
  {
    final ArrayList<String> names = new ArrayList<>();
    for (final Column column: columns)
    {
      if (!column.hasRemarks())
      {
        names.add(column.getName());
      }
    }
    return names;
  }

}
