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


import java.util.List;
import java.util.Optional;

/**
 * Represents a database function.
 *
 * @author Sualeh Fatehi
 */
public interface Function
  extends Routine
{

  /**
   * Gets the list of columns in ordinal order.
   *
   * @return Columns of the procedure
   */
  @Override
  List<FunctionColumn> getColumns();

  /**
   * Gets the procedure type.
   *
   * @return Procedure type
   */
  @Override
  FunctionReturnType getReturnType();

  /**
   * Gets the type of the routine body.
   *
   * @return Routine body type
   */
  @Override
  RoutineBodyType getRoutineBodyType();

  /**
   * Gets a column by name.
   *
   * @param name
   *        Name
   * @return Column of the procedure
   */
  @Override
  Optional<? extends FunctionColumn> lookupColumn(String name);

}
