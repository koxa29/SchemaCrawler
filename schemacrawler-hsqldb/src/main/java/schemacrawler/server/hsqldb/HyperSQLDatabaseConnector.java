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
package schemacrawler.server.hsqldb;


import schemacrawler.tools.databaseconnector.DatabaseConnector;
import schemacrawler.tools.databaseconnector.DatabaseServerType;

public final class HyperSQLDatabaseConnector
  extends DatabaseConnector
{

  private static final long serialVersionUID = 5148345984002037384L;

  public HyperSQLDatabaseConnector()
  {
    super(new DatabaseServerType("hsqldb", "HyperSQL DataBase"),
          "/help/Connections.hsqldb.txt",
          "/schemacrawler-hsqldb.config.properties",
          "/hsqldb.information_schema",
          "jdbc:hsqldb:.*");
  }

}
