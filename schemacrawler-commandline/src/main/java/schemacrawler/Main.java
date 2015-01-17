/*
 *
 * SchemaCrawler
 * http://sourceforge.net/projects/schemacrawler
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

package schemacrawler;


import static java.util.Objects.requireNonNull;
import static sf.util.commandlineparser.CommandLineArgumentsUtility.flattenCommandlineArgs;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import schemacrawler.schemacrawler.Config;
import schemacrawler.schemacrawler.SchemaCrawlerCommandLineException;
import schemacrawler.tools.commandline.ApplicationOptionsParser;
import schemacrawler.tools.commandline.CommandLine;
import schemacrawler.tools.commandline.DatabaseServerTypeParser;
import schemacrawler.tools.databaseconnector.DatabaseConnector;
import schemacrawler.tools.databaseconnector.DatabaseConnectorRegistry;
import schemacrawler.tools.options.ApplicationOptions;
import schemacrawler.tools.options.DatabaseServerType;
import sf.util.commandlineparser.CommandLineArgumentsUtility;

/**
 * Main class that takes arguments for a database for crawling a schema.
 */
public final class Main
{

  public static void main(final String[] args)
    throws Exception
  {
    requireNonNull(args);

    final Config config = CommandLineArgumentsUtility.loadConfig(args);

    final ApplicationOptionsParser applicationOptionsParser = new ApplicationOptionsParser(config);
    final ApplicationOptions applicationOptions = applicationOptionsParser
      .getOptions();

    applicationOptions.applyApplicationLogLevel();
    LOGGER.log(Level.CONFIG, "Command line: " + Arrays.toString(args));

    try
    {
      final DatabaseServerTypeParser dbServerTypeParser = new DatabaseServerTypeParser(config);
      final DatabaseServerType dbServerType = dbServerTypeParser.getOptions();
      final DatabaseConnectorRegistry registry = new DatabaseConnectorRegistry();
      final DatabaseConnector dbConnector = registry
        .lookupDatabaseSystemIdentifier(dbServerType
          .getDatabaseSystemIdentifier());

      final boolean showHelp = args.length == 0 || args.length == 1
                               && Main.class.getCanonicalName().equals(args[0])
                               || applicationOptions.isShowHelp();

      final CommandLine commandLine;
      if (showHelp)
      {
        final boolean showVersionOnly = applicationOptions.isShowVersionOnly();
        commandLine = dbConnector.newHelpCommandLine(args, showVersionOnly);
      }
      else
      {
        commandLine = dbConnector
          .newCommandLine(flattenCommandlineArgs(config));
      }
      commandLine.execute();
    }
    catch (final SchemaCrawlerCommandLineException e)
    {
      final String errorMessage = e.getMessage();
      System.err.println(errorMessage);
      System.err.println("Re-run SchemaCrawler with the -? option for help");
      LOGGER.log(Level.SEVERE, "Command line: " + Arrays.toString(args), e);
    }
  }

  private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

  private Main()
  {
    // Prevent instantiation
  }

}
