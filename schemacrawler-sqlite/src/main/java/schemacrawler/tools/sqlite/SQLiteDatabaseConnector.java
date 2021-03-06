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
package schemacrawler.tools.sqlite;


import schemacrawler.schemacrawler.Config;
import schemacrawler.schemacrawler.ConnectionOptions;
import schemacrawler.schemacrawler.DatabaseSpecificOverrideOptionsBuilder;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.tools.databaseconnector.DatabaseConnector;
import schemacrawler.tools.databaseconnector.DatabaseServerType;

public final class SQLiteDatabaseConnector
  extends DatabaseConnector
{

  private static final long serialVersionUID = -926915070636247650L;

  public SQLiteDatabaseConnector()
  {
    super(new DatabaseServerType("sqlite", "SQLite"),
          "/help/Connections.sqlite.txt",
          "/schemacrawler-sqlite.config.properties",
          "/sqlite.information_schema",
          "jdbc:sqlite:.*");
  }

  @Override
  public DatabaseSpecificOverrideOptionsBuilder getDatabaseSpecificOverrideOptionsBuilder()
  {
    final DatabaseSpecificOverrideOptionsBuilder databaseSpecificOverrideOptionsBuilder = super.getDatabaseSpecificOverrideOptionsBuilder();
    databaseSpecificOverrideOptionsBuilder.identifierQuoteString("\"");
    return databaseSpecificOverrideOptionsBuilder;
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.tools.databaseconnector.DatabaseSystemConnector#newDatabaseConnectionOptions(schemacrawler.schemacrawler.Config)
   */
  @Override
  public ConnectionOptions newDatabaseConnectionOptions(final Config additionalConfig)
    throws SchemaCrawlerException
  {
    try
    {
      Class.forName("org.sqlite.JDBC");
    }
    catch (final ClassNotFoundException e)
    {
      throw new SchemaCrawlerException("Could not load SQLite JDBC driver", e);
    }

    return super.newDatabaseConnectionOptions(additionalConfig);
  }

}
