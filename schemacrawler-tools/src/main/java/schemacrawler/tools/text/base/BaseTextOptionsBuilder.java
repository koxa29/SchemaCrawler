/*
 *
 * SchemaCrawler
 * http://www.schemacrawler.com
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

package schemacrawler.tools.text.base;


import static java.util.Objects.requireNonNull;

import java.util.Map;

import schemacrawler.schemacrawler.Config;
import schemacrawler.schemacrawler.OptionsBuilder;

public abstract class BaseTextOptionsBuilder<O extends BaseTextOptions>
  implements OptionsBuilder<BaseTextOptions>
{

  protected static final String SCHEMACRAWLER_FORMAT_PREFIX = "schemacrawler.format.";

  private static final String NO_HEADER = SCHEMACRAWLER_FORMAT_PREFIX
                                          + "no_header";
  private static final String NO_FOOTER = SCHEMACRAWLER_FORMAT_PREFIX
                                          + "no_footer";
  private static final String NO_INFO = SCHEMACRAWLER_FORMAT_PREFIX + "no_info";
  private static final String APPEND_OUTPUT = SCHEMACRAWLER_FORMAT_PREFIX
                                              + "append_output";

  private static final String SHOW_UNQUALIFIED_NAMES = SCHEMACRAWLER_FORMAT_PREFIX
                                                       + "show_unqualified_names";

  private static final String SORT_ALPHABETICALLY_TABLES = SCHEMACRAWLER_FORMAT_PREFIX
                                                           + "sort_alphabetically.tables";
  private static final String SORT_ALPHABETICALLY_TABLE_COLUMNS = SCHEMACRAWLER_FORMAT_PREFIX
                                                                  + "sort_alphabetically.table_columns";

  private static final String SORT_ALPHABETICALLY_ROUTINES = SCHEMACRAWLER_FORMAT_PREFIX
                                                             + "sort_alphabetically.routines";
  private static final String SORT_ALPHABETICALLY_ROUTINE_COLUMNS = SCHEMACRAWLER_FORMAT_PREFIX
                                                                    + "sort_alphabetically.routine_columns";

  protected final O options;

  protected BaseTextOptionsBuilder(final O options)
  {
    this.options = requireNonNull(options);
  }

  public BaseTextOptionsBuilder<O> appendOutput()
  {
    options.setAppendOutput(true);
    return this;
  }

  @Override
  public BaseTextOptionsBuilder<O> fromConfig(final Map<String, String> map)
  {
    if (map == null)
    {
      return this;
    }

    final Config config = new Config(map);

    options.setNoFooter(config.getBooleanValue(NO_FOOTER));
    options.setNoHeader(config.getBooleanValue(NO_HEADER));
    options.setNoInfo(config.getBooleanValue(NO_INFO));
    options.setAppendOutput(config.getBooleanValue(APPEND_OUTPUT));

    options
      .setShowUnqualifiedNames(config.getBooleanValue(SHOW_UNQUALIFIED_NAMES));

    options.setAlphabeticalSortForTables(config
      .getBooleanValue(SORT_ALPHABETICALLY_TABLES,
                       options.isAlphabeticalSortForTables()));
    options.setAlphabeticalSortForTableColumns(config
      .getBooleanValue(SORT_ALPHABETICALLY_TABLE_COLUMNS,
                       options.isAlphabeticalSortForTableColumns()));

    options.setAlphabeticalSortForRoutines(config
      .getBooleanValue(SORT_ALPHABETICALLY_ROUTINES,
                       options.isAlphabeticalSortForRoutines()));

    options.setAlphabeticalSortForRoutineColumns(config
      .getBooleanValue(SORT_ALPHABETICALLY_ROUTINE_COLUMNS,
                       options.isAlphabeticalSortForRoutineColumns()));

    return this;
  }

  public BaseTextOptionsBuilder<O> hideFooter()
  {
    options.setNoFooter(true);
    return this;
  }

  public BaseTextOptionsBuilder<O> hideHeader()
  {
    options.setNoHeader(true);
    return this;
  }

  public BaseTextOptionsBuilder<O> hideInfo()
  {
    options.setNoInfo(true);
    return this;
  }

  public BaseTextOptionsBuilder<O> naturalSortTables()
  {
    options.setAlphabeticalSortForTables(false);
    return this;
  }

  public BaseTextOptionsBuilder<O> overwriteOutput()
  {
    options.setAppendOutput(false);
    return this;
  }

  public BaseTextOptionsBuilder<O> showFooter()
  {
    options.setNoFooter(false);
    return this;
  }

  public BaseTextOptionsBuilder<O> showHeader()
  {
    options.setNoHeader(false);
    return this;
  }

  public BaseTextOptionsBuilder<O> showInfo()
  {
    options.setNoInfo(false);
    return this;
  }

  public BaseTextOptionsBuilder<O> sortTableColumns()
  {
    options.setAlphabeticalSortForTableColumns(true);
    return this;
  }

  public BaseTextOptionsBuilder<O> sortTables()
  {
    options.setAlphabeticalSortForTables(true);
    return this;
  }

  @Override
  public Config toConfig()
  {
    final Config config = new Config();

    config.setBooleanValue(NO_FOOTER, options.isNoFooter());
    config.setBooleanValue(NO_HEADER, options.isNoHeader());
    config.setBooleanValue(NO_INFO, options.isNoInfo());
    config.setBooleanValue(APPEND_OUTPUT, options.isAppendOutput());

    config.setBooleanValue(SHOW_UNQUALIFIED_NAMES,
                           options.isShowUnqualifiedNames());

    config.setBooleanValue(SORT_ALPHABETICALLY_TABLES,
                           options.isAlphabeticalSortForTables());
    config.setBooleanValue(SORT_ALPHABETICALLY_TABLE_COLUMNS,
                           options.isAlphabeticalSortForTableColumns());

    config.setBooleanValue(SORT_ALPHABETICALLY_ROUTINES,
                           options.isAlphabeticalSortForRoutines());

    config.setBooleanValue(SORT_ALPHABETICALLY_ROUTINE_COLUMNS,
                           options.isAlphabeticalSortForRoutineColumns());

    return config;
  }

  @Override
  public O toOptions()
  {
    return options;
  }

  @Override
  public String toString()
  {
    return options.toString();
  }

}
