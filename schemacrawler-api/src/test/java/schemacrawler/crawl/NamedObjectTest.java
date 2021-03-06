/*
 * SchemaCrawler
 * Copyright (c) 2000-2016, Sualeh Fatehi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package schemacrawler.crawl;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;
import schemacrawler.schema.TableType;

public class NamedObjectTest
{

  public static final TableType TABLE = new TableType("TABLE");

  @Test
  public void tableNames()
  {
    final String[] schemaNames = new String[] { "DBO", "PUBLIC" };
    final String[] tableNames = {
                                  "CUSTOMER",
                                  "CUSTOMERLIST",
                                  "INVOICE",
                                  "ITEM",
                                  "PRODUCT",
                                  "SUPPLIER" };

    MutableTable table;
    final NamedObjectList<Table> tables = new NamedObjectList<>();

    final MutableCatalog catalog = new MutableCatalog("DATABASE");
    for (final String schemaName: schemaNames)
    {
      final Schema schema = catalog.addSchema("CATALOG", schemaName);
      for (final String tableName: tableNames)
      {
        table = new MutableTable(schema, tableName);
        table.setTableType(TABLE);
        tables.add(table);
      }
    }
    assertEquals("", schemaNames.length * tableNames.length, tables.size());

  }

}
