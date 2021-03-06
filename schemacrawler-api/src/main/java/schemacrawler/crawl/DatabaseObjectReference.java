package schemacrawler.crawl;


import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import schemacrawler.schema.DatabaseObject;
import schemacrawler.schema.PartialDatabaseObject;

class DatabaseObjectReference<D extends DatabaseObject>
  implements Serializable
{

  private static final long serialVersionUID = 1748828818899660921L;

  private Reference<D> databaseObjectRef;
  private D partial;

  DatabaseObjectReference(final D databaseObject, final D partial)
  {
    databaseObjectRef = new SoftReference<>(requireNonNull(databaseObject,
                                                           "Database object not provided"));

    this.partial = requireNonNull(partial,
                                  "Partial database object not provided");
    if (!(partial instanceof PartialDatabaseObject))
    {
      throw new IllegalArgumentException("Partial database object not provided");
    }

    if (!partial.equals(databaseObject))
    {
      throw new IllegalArgumentException("Inconsistent database object reference");
    }
  }

  @Override
  public boolean equals(final Object obj)
  {
    return partial.equals(obj);
  }

  /**
   * {@inheritDoc} Modification over the Reference, always returns a
   * non-null value.
   *
   * @see java.lang.ref.SoftReference#get()
   */
  public D get()
  {
    final D dereferencedDatabaseObject;
    if (databaseObjectRef != null)
    {
      dereferencedDatabaseObject = databaseObjectRef.get();
    }
    else
    {
      dereferencedDatabaseObject = null;
    }

    if (dereferencedDatabaseObject == null)
    {
      return partial;
    }
    else
    {
      return dereferencedDatabaseObject;
    }
  }

  @Override
  public int hashCode()
  {
    return partial.hashCode();
  }

  public boolean isPartialDatabaseObjectReference()
  {
    return this.get() instanceof PartialDatabaseObject;
  }

  @Override
  public String toString()
  {
    return partial.toString();
  }

  /**
   * Read saved content of the reference, construct new reference, and
   * the partial.
   *
   * @param in
   * @throws IOException
   * @throws ClassNotFoundException
   */
  private void readObject(final ObjectInputStream in)
    throws IOException, ClassNotFoundException
  {
    if (in != null)
    {
      partial = (D) in.readObject();
      databaseObjectRef = new WeakReference(in.readObject());
    }
  }

  /**
   * Write only content of the reference. A Reference itself is not
   * serializable.
   *
   * @param out
   * @throws java.io.IOException
   */
  private void writeObject(final ObjectOutputStream out)
    throws IOException
  {
    if (out != null)
    {
      out.writeObject(partial);
      out.writeObject(databaseObjectRef.get());
    }
  }

}
