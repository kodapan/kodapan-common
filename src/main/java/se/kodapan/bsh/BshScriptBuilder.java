package se.kodapan.bsh;

import se.kodapan.lang.reflect.ReflectionUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;

/**
 * @author kalle
 * @since 2010-jul-16 06:22:29
 */
public class BshScriptBuilder {


  private StringBuilder bsh = new StringBuilder(4096);

  public void execute(String expression) {
    bsh.append(expression).append("\n");    
  }

  private Map<Object, String> instancesByAttribute = new HashMap<Object, String>();

  public void set(String attribute, Object instance) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InterruptedException {
    instancesByAttribute.put(instance, attribute);

    StringBuilder bsh = new StringBuilder();

    if (instance.getClass().isEnum()) {
      bsh.append(attribute).append("=").append(instance.getClass().getName()).append(".").append(((Enum) instance).name()).append(";\n");

    } else if (instance.getClass() == Boolean.class || instance.getClass() == boolean.class) {
      bsh.append(attribute).append("=").append(String.valueOf(instance)).append(";\n");

    } else if (instance.getClass() == Byte.class || instance.getClass() == byte.class) {
      bsh.append(attribute).append("=").append("(byte)").append(String.valueOf(instance)).append(";\n");

    } else if (instance.getClass() == Short.class || instance.getClass() == short.class) {
      bsh.append(attribute).append("=").append("(short)").append(String.valueOf(instance)).append(";\n");

    } else if (instance.getClass() == Integer.class || instance.getClass() == int.class) {
      bsh.append(attribute).append("=").append(String.valueOf(instance)).append(";\n");

    } else if (instance.getClass() == Long.class || instance.getClass() == long.class) {
      bsh.append(attribute).append("=").append(String.valueOf(instance)).append("l").append(";\n");

    } else if (instance.getClass() == Float.class || instance.getClass() == float.class) {
      bsh.append(attribute).append("=").append(String.valueOf(instance)).append("f").append(";\n");

    } else if (instance.getClass() == Double.class || instance.getClass() == double.class) {
      bsh.append(attribute).append("=").append(String.valueOf(instance)).append("d").append(";\n");

    } else if (instance.getClass() == String.class) {
      bsh.append(attribute).append("=").append(getString((String)instance)).append(";\n");

    } else if (instance.getClass() == Date.class) {
      bsh.append(attribute).append("=").append("new java.util.Date(").append(String.valueOf(((Date) instance).getTime())).append("l);\n");

    } else if (instance.getClass() == URI.class) {
      bsh.append(attribute).append("=").append("new java.net.URI(").append(getString(instance.toString())).append(");\n");

    } else if (instance.getClass() == java.net.URL.class) {
      bsh.append(attribute).append("=").append("new java.net.URL(").append(getString(instance.toString())).append(");\n");

    } else {

      this.bsh.append(attribute).append("=new ").append(instance.getClass().getName()).append("();\n");

      Map<String, Field> fields = ReflectionUtil.gatherAllBeanFields(instance.getClass());
      for (Map.Entry<String, Field> fieldEntry : fields.entrySet()) {
        Field field = fieldEntry.getValue();
        Method getter = ReflectionUtil.getGetter(field);
        Method setter = ReflectionUtil.getSetter(field);
        Object value = getter.invoke(instance);
        if (value != null) {

          if (field.getType().isArray()) {
            // bean.setArray(new ObjectImpl[]);
            bsh.append(attribute).append(".").append(setter.getName()).append("(").append(".add(new ").append(field.getType().getComponentType().getName()).append("[").append(Array.getLength(instance)).append("]").append(");\n");

            throw new UnsupportedOperationException("Arrays not supported.");

          } else if (Collection.class.isAssignableFrom(field.getType())) {
            Collection collection = (Collection) value;
            // bean.setCollection(new CollectionImpl());
            bsh.append(attribute).append(".").append(setter.getName()).append("(new ").append(collection.getClass().getName()).append("());\n");

            for (Object item : collection) {
              // bean.getCollection().add(item);
              bsh.append(attribute).append(".").append(getter.getName()).append("()").append(".add(").append(get(item)).append(");\n");
            }

          } else if (field.getType() == Map.class) {
            Map map = (Map) value;
            // bean.setMap(new MapImpl());
            bsh.append(attribute).append(".").append(setter.getName()).append("(new ").append(map.getClass().getName()).append("());\n");

            for (Map.Entry item : (Set<Map.Entry>) map.entrySet()) {
              // bean.getCollection().add(item);
              bsh.append(attribute).append(".").append(getter.getName()).append("(").append(".put(")
                  .append(get(item.getKey())).append(", ")
                  .append(get(item.getValue())).append(");\n");
            }


          } else {

            bsh.append(attribute).append(".").append(setter.getName()).append("(").append(get(value)).append(");\n");
          }

        }


      }


    }

    this.bsh.append(bsh.toString());
    
  }
  
  public String get(Object instance) throws InterruptedException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    String name = instancesByAttribute.get(instance);
    if (name == null) {
      name = getTmpName(instance);
      set(name, instance);
    }
    return name;
  }


  public static synchronized String getTmpName(Object bean) throws InterruptedException {
    Thread.sleep(1);
    String name;
    name = "__tmp" + bean.getClass().getSimpleName() + "" + System.currentTimeMillis();
    return name;
  }





  private static String getString(String input) {
    StringBuilder sb = new StringBuilder(input.length() + 50);
    sb.append("\"").append((input).replaceAll("\"", "\\\"")).append("\"");
    return sb.toString();
  }

  @Override
  public String toString() {
    return bsh.toString();
  }
}