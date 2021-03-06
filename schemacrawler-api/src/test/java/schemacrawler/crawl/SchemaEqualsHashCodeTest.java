package schemacrawler.crawl;


import org.junit.Ignore;
import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import schemacrawler.schema.SchemaReference;

@Ignore
public class SchemaEqualsHashCodeTest
{

  @Test
  public void equalsContract()
  {
    EqualsVerifier.forClass(MutableCatalog.class).verify();
    EqualsVerifier.forClass(SchemaReference.class).verify();
    EqualsVerifier.forClass(MutableTable.class).verify();
    EqualsVerifier.forClass(MutablePrimaryKey.class).verify();
    EqualsVerifier.forClass(MutableColumn.class).verify();
    EqualsVerifier.forClass(MutableForeignKey.class).verify();
    EqualsVerifier.forClass(MutableForeignKeyColumnReference.class).verify();
    EqualsVerifier.forClass(MutablePrivilege.class).verify();
  }

}
