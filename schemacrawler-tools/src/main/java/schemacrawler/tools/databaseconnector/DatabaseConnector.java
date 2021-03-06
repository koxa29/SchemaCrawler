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
package schemacrawler.tools.databaseconnector;


import static java.util.Objects.requireNonNull;
import static sf.util.Utility.isBlank;

import java.util.regex.Pattern;

import schemacrawler.schemacrawler.Config;
import schemacrawler.schemacrawler.ConnectionOptions;
import schemacrawler.schemacrawler.DatabaseConfigConnectionOptions;
import schemacrawler.schemacrawler.DatabaseConnectionOptions;
import schemacrawler.schemacrawler.DatabaseSpecificOverrideOptionsBuilder;
import schemacrawler.schemacrawler.Options;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.tools.executable.Executable;
import schemacrawler.tools.executable.SchemaCrawlerExecutable;

public abstract class DatabaseConnector
  implements Options
{

  private static final long serialVersionUID = 6133330582637434099L;

  protected static final DatabaseConnector UNKNOWN = new DatabaseConnector()
  {

    private static final long serialVersionUID = 3057770737518232349L;

  };

  private final DatabaseServerType dbServerType;
  private final String connectionHelpResource;
  private final String configResource;
  private final String informationSchemaViewsResourceFolder;
  private final Pattern connectionUrlPattern;

  protected DatabaseConnector(final DatabaseServerType dbServerType,
                              final String connectionHelpResource,
                              final String configResource,
                              final String informationSchemaViewsResourceFolder,
                              final String connectionUrlPrefix)
  {
    this.dbServerType = requireNonNull(dbServerType,
                                       "No database server type provided");

    if (isBlank(connectionHelpResource))
    {
      throw new IllegalArgumentException("No connection help resource provided");
    }
    this.connectionHelpResource = connectionHelpResource;

    this.configResource = configResource;
    this.informationSchemaViewsResourceFolder = informationSchemaViewsResourceFolder;

    if (isBlank(connectionUrlPrefix))
    {
      throw new IllegalArgumentException("No JDBC connection URL prefix provided");
    }
    connectionUrlPattern = Pattern.compile(connectionUrlPrefix);
  }

  private DatabaseConnector()
  {
    dbServerType = DatabaseServerType.UNKNOWN;
    connectionHelpResource = null;
    configResource = null;
    informationSchemaViewsResourceFolder = null;
    connectionUrlPattern = null;
  }

  /**
   * Checks if the database connection options are valid, the JDBC
   * driver class can be loaded, and so on. Throws an exception if there
   * is a problem.
   *
   * @throws SchemaCrawlerException
   *         If there is a problem with creating connection options.
   */
  public void checkDatabaseConnectionOptions()
    throws SchemaCrawlerException
  {
    final Config additionalConfig = new Config();
    additionalConfig.put("user", "fake");
    newDatabaseConnectionOptions(additionalConfig);
  }

  /**
   * Gets the complete bundled database configuration set. This is
   * useful in building the SchemaCrawler options.
   */
  public final Config getConfig()
  {
    final Config config = Config.loadResource(configResource);
    return config;
  }

  public String getConnectionHelpResource()
  {
    return connectionHelpResource;
  }

  public final Pattern getConnectionUrlPattern()
  {
    return connectionUrlPattern;
  }

  public DatabaseServerType getDatabaseServerType()
  {
    return dbServerType;
  }

  /**
   * Gets the complete bundled database specific configuration set,
   * including the SQL for information schema views.
   */
  public DatabaseSpecificOverrideOptionsBuilder getDatabaseSpecificOverrideOptionsBuilder()
  {
    final DatabaseSpecificOverrideOptionsBuilder databaseSpecificOverrideOptionsBuilder = new DatabaseSpecificOverrideOptionsBuilder();
    databaseSpecificOverrideOptionsBuilder.withInformationSchemaViews()
      .fromResourceFolder(informationSchemaViewsResourceFolder);

    return databaseSpecificOverrideOptionsBuilder;
  }

  public boolean isUnknownDatabaseSystem()
  {
    return dbServerType.isUnknownDatabaseSystem();
  }

  /**
   * Creates a datasource for connecting to a database. Additional
   * connection options are provided, from the command-line, and
   * configuration file.
   *
   * @param additionalConfig
   *        Configuration from the command-line, and from configuration
   *        files.
   */
  public ConnectionOptions newDatabaseConnectionOptions(final Config additionalConfig)
    throws SchemaCrawlerException
  {
    if (additionalConfig == null || additionalConfig.isEmpty())
    {
      throw new IllegalArgumentException("No connection configuration provided");
    }

    final Config config = getConfig();
    config.putAll(additionalConfig);

    final ConnectionOptions connectionOptions;
    if (dbServerType.isUnknownDatabaseSystem() || config.hasValue("url"))
    {
      connectionOptions = new DatabaseConnectionOptions(config);
    }
    else
    {
      connectionOptions = new DatabaseConfigConnectionOptions(config);
    }

    return connectionOptions;
  }

  public Executable newExecutable(final String command)
    throws SchemaCrawlerException
  {
    return new SchemaCrawlerExecutable(command);
  }

}
