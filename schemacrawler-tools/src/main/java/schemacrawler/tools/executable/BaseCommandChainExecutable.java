/*
 * SchemaCrawler
 * http://www.schemacrawler.com
 * Copyright (c) 2000-2016, Sualeh Fatehi.
 * This library is free software; you can redistribute it and/or modify it under
 * the terms
 * of the GNU Lesser General Public License as published by the Free Software
 * Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 */
package schemacrawler.tools.executable;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import schemacrawler.schema.Catalog;
import schemacrawler.schemacrawler.SchemaCrawlerException;

/**
 * Allows chaining multiple executables with the same configuration. The
 * catalog is obtained just once, and passed on from executable to
 * executable for efficiency in execution.
 */
abstract class BaseCommandChainExecutable
  extends BaseStagedExecutable
{

  private static final Logger LOGGER = Logger
    .getLogger(BaseCommandChainExecutable.class.getName());

  private final List<Executable> executables;
  protected final CommandRegistry commandRegistry;

  protected BaseCommandChainExecutable(final String command)
    throws SchemaCrawlerException
  {
    super(command);

    commandRegistry = new CommandRegistry();
    executables = new ArrayList<>();
  }

  public final Executable addNext(final Executable executable)
  {
    if (executable != null)
    {
      executables.add(executable);
    }
    return executable;
  }

  protected final void executeChain(final Catalog catalog,
                                    final Connection connection)
                                      throws Exception
  {
    if (executables.isEmpty())
    {
      LOGGER.log(Level.INFO, "No commands to execute");
      return;
    }

    for (final Executable executable: executables)
    {
      if (executable instanceof BaseStagedExecutable)
      {
        ((BaseStagedExecutable) executable).executeOn(catalog, connection);
      }
    }
  }

}
