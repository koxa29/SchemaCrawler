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
package schemacrawler.schema;


import java.io.Serializable;

/**
 * A wrapper around java.sql.Types.
 *
 * @author Sualeh Fatehi
 */
public final class JavaSqlType
  implements Serializable, Comparable<JavaSqlType>
{

  public enum JavaSqlTypeGroup
  {
   unknown,
   binary,
   bit,
   character,
   id,
   integer,
   real,
   reference,
   temporal,
   url,
   xml,
   large_object,
   object;
  }

  private static final long serialVersionUID = 2614819974745473431L;

  /**
   * Unknown SQL data type.
   */
  public static final JavaSqlType UNKNOWN = new JavaSqlType(Integer.MAX_VALUE,
                                                            "<UNKNOWN>",
                                                            JavaSqlTypeGroup.unknown);
  private final int javaSqlType;
  private final String javaSqlTypeName;

  private final JavaSqlTypeGroup javaSqlTypeGroup;

  public JavaSqlType(final int javaSqlType,
                     final String javaSqlTypeName,
                     final JavaSqlTypeGroup javaSqlTypeGroup)
  {
    this.javaSqlType = javaSqlType;
    this.javaSqlTypeName = javaSqlTypeName;
    this.javaSqlTypeGroup = javaSqlTypeGroup;
  }

  @Override
  public int compareTo(final JavaSqlType otherSqlDataType)
  {
    return javaSqlTypeName.compareTo(otherSqlDataType.javaSqlTypeName);
  }

  @Override
  public boolean equals(final Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (obj == null)
    {
      return false;
    }
    if (getClass() != obj.getClass())
    {
      return false;
    }
    final JavaSqlType other = (JavaSqlType) obj;
    if (javaSqlType != other.javaSqlType)
    {
      return false;
    }
    if (javaSqlTypeGroup == null)
    {
      if (other.javaSqlTypeGroup != null)
      {
        return false;
      }
    }
    else if (!javaSqlTypeGroup.equals(other.javaSqlTypeGroup))
    {
      return false;
    }
    if (javaSqlTypeName == null)
    {
      if (other.javaSqlTypeName != null)
      {
        return false;
      }
    }
    else if (!javaSqlTypeName.equals(other.javaSqlTypeName))
    {
      return false;
    }
    return true;
  }

  /**
   * The java.sql.Types type.
   *
   * @return java.sql.Types type
   */
  public int getJavaSqlType()
  {
    return javaSqlType;
  }

  public JavaSqlTypeGroup getJavaSqlTypeGroup()
  {
    return javaSqlTypeGroup;
  }

  /**
   * The java.sql.Types type name.
   *
   * @return java.sql.Types type names
   */
  public String getJavaSqlTypeName()
  {
    return javaSqlTypeName;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + javaSqlType;
    result = prime * result
             + (javaSqlTypeGroup == null? 0: javaSqlTypeGroup.hashCode());
    result = prime * result
             + (javaSqlTypeName == null? 0: javaSqlTypeName.hashCode());
    return result;
  }

  @Override
  public String toString()
  {
    return String.format("%s\t%d\t%s",
                         javaSqlTypeName,
                         javaSqlType,
                         javaSqlTypeGroup);
  }

}
