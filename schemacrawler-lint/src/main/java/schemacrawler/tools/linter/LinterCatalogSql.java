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

import schemacrawler.schema.Catalog;
import schemacrawler.schemacrawler.Config;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.tools.lint.BaseLinterCatalog;
import schemacrawler.utility.Query;

public class LinterCatalogSql
  extends BaseLinterCatalog
{

  private String message;
  private String sql;

  @Override
  public String getSummary()
  {
    return message;
  }

  @Override
  public void lint(final Catalog catalog, final Connection connection)
    throws SchemaCrawlerException
  {
    if (isBlank(sql))
    {
      return;
    }

    requireNonNull(catalog, "No catalog provided");
    requireNonNull(connection, "No connection provided");

    final Query query = new Query(message, sql);
    final Object queryResult = query.executeForScalar(connection);
    if (queryResult != null)
    {
      addLint(catalog, getSummary() + " " + queryResult, true);
    }
  }

  @Override
  protected void configure(final Config config)
  {
    requireNonNull(config, "No configuration provided");

    message = config.getStringValue("message", null);
    if (isBlank(message))
    {
      throw new IllegalArgumentException("No message provided");
    }

    sql = config.getStringValue("sql", null);
    if (isBlank(sql))
    {
      throw new IllegalArgumentException("No SQL provided");
    }
  }

}
