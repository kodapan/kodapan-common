package se.kodapan.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author kalle
 * @since 2010-jul-17 11:43:52
 */
public class SerializableTool {

  public static <T> T clone(T object) {
    if (object == null) {
      return null;
    }
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(object);
      oos.close();
      ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
      object = (T)ois.readObject();
      ois.close();
      baos.close();
      return object;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
