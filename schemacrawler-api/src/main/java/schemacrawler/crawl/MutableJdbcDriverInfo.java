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

package schemacrawler.crawl;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import schemacrawler.schema.JdbcDriverInfo;
import schemacrawler.schema.JdbcDriverProperty;

/**
 * JDBC driver information. Created from metadata returned by a JDBC
 * call, and other sources of information.
 *
 * @author Sualeh Fatehi sualeh@hotmail.com
 */
final class MutableJdbcDriverInfo
  implements JdbcDriverInfo
{

  private static final long serialVersionUID = 8030156654422512161L;

  private String driverName;
  private String driverClassName;
  private String driverVersion;
  private String connectionUrl;
  private boolean jdbcCompliant;
  private final Set<ImmutableJdbcDriverProperty> jdbcDriverProperties = new HashSet<>();

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.JdbcDriverInfo#getConnectionUrl()
   */
  @Override
  public String getConnectionUrl()
  {
    return connectionUrl;
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.JdbcDriverInfo#getDriverClassName()
   */
  @Override
  public String getDriverClassName()
  {
    return driverClassName;
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.JdbcDriverInfo#getDriverName()
   */
  @Override
  public String getDriverName()
  {
    return driverName;
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.JdbcDriverInfo#getDriverProperties()
   */
  @Override
  public Collection<JdbcDriverProperty> getDriverProperties()
  {
    final List<JdbcDriverProperty> properties = new ArrayList<JdbcDriverProperty>(jdbcDriverProperties);
    Collections.sort(properties);
    return properties;
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.JdbcDriverInfo#getDriverVersion()
   */
  @Override
  public String getDriverVersion()
  {
    return driverVersion;
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.JdbcDriverInfo#isJdbcCompliant()
   */
  @Override
  public boolean isJdbcCompliant()
  {
    return jdbcCompliant;
  }

  /**
   * {@inheritDoc}
   *
   * @see Object#toString()
   */
  @Override
  public String toString()
  {
    final StringBuilder info = new StringBuilder(1024);
    info.append("-- driver: ").append(getDriverName()).append(' ')
      .append(getDriverVersion()).append(System.lineSeparator());
    info.append("-- driver class: ").append(getDriverClassName())
      .append(System.lineSeparator());
    info.append("-- url: ").append(getConnectionUrl())
      .append(System.lineSeparator());
    info.append("-- jdbc compliant: ").append(isJdbcCompliant());
    return info.toString();
  }

  /**
   * Adds a JDBC driver property.
   *
   * @param jdbcDriverProperty
   *        JDBC driver property
   */
  void addJdbcDriverProperty(final ImmutableJdbcDriverProperty jdbcDriverProperty)
  {
    jdbcDriverProperties.add(jdbcDriverProperty);
  }

  void setConnectionUrl(final String connectionUrl)
  {
    this.connectionUrl = connectionUrl;
  }

  void setDriverName(final String driverName)
  {
    this.driverName = driverName;
  }

  void setDriverVersion(final String driverVersion)
  {
    this.driverVersion = driverVersion;
  }

  void setJdbcCompliant(final boolean jdbcCompliant)
  {
    this.jdbcCompliant = jdbcCompliant;
  }

  void setJdbcDriverClassName(final String jdbcDriverClassName)
  {
    driverClassName = jdbcDriverClassName;
  }

}
