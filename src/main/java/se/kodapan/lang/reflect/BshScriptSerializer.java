package se.kodapan.lang.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author kalle
 * @since 2010-jul-16 06:22:29
 */
public class BshScriptSerializer {

  public static synchronized String getTmpName(Object bean) throws InterruptedException {
    Thread.sleep(1);
    String name;
    name = "__tmp" + bean.getClass().getSimpleName() + "" + System.currentTimeMillis();
    return name;
  }



  public static String getAttributeOrExpression(Object bean) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InterruptedException {
    return getAttributeOrExpression(getTmpName(bean), bean);
  }

  public static String getAttributeOrExpression(String name, Object bean) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InterruptedException {
    Map<Object, String> cache = new HashMap<Object, String>();
    StringBuilder bsh = new StringBuilder();
    getAttributeOrExpression(name, bsh, bean, cache);
    return bsh.toString();
  }

  public static String getAttributeOrExpression(Object bean,  Map<Object, String> cache) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InterruptedException {
    return getAttributeOrExpression(getTmpName(bean), bean, cache);
  }


  public static String getAttributeOrExpression(String name, Object bean,  Map<Object, String> cache) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InterruptedException {
    StringBuilder bsh = new StringBuilder();
    getAttributeOrExpression(name, bsh, bean, cache);
    return bsh.toString();
  }


  /**
   * @param output
   * @param bean
   * @param cache
   * @return an evaluatable expression or the name of an attribute that holds the bean instance.
   * @throws NoSuchMethodException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  public static String getAttributeOrExpression(StringBuilder output, Object bean, Map<Object, String> cache) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InterruptedException {
    String nameOrExpression = cache.get(bean);
    if (nameOrExpression == null) {
      nameOrExpression = getAttributeOrExpression(getTmpName(bean), output, bean, cache);
    }
    return nameOrExpression;
  }


  public static String getAttributeOrExpression(String name, StringBuilder output, Object object, Map<Object, String> cache) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InterruptedException {

    String nameOrExpression = cache.get(object);
    if (nameOrExpression != null) {
      return nameOrExpression;
    }


    StringBuilder bsh = new StringBuilder();


    if (object.getClass().isEnum()) {
      bsh./*append(name).append("=").*/append(object.getClass().getName()).append(".").append(((Enum) object).name());
      nameOrExpression = bsh.toString();

    } else if (object.getClass() == Boolean.class || object.getClass() == boolean.class) {
      bsh./*append(name).append("=").*/append(String.valueOf(object));
      nameOrExpression = bsh.toString();

    } else if (object.getClass() == Byte.class || object.getClass() == byte.class) {
      bsh./*append(name).append("=").*/append("(byte)").append(String.valueOf(object));
      nameOrExpression = bsh.toString();

    } else if (object.getClass() == Short.class || object.getClass() == short.class) {
      bsh./*append(name).append("=").*/append("(short)").append(String.valueOf(object));
      nameOrExpression = bsh.toString();

    } else if (object.getClass() == Integer.class || object.getClass() == int.class) {
      bsh./*append(name).append("=").*/append(String.valueOf(object));
      nameOrExpression = bsh.toString();

    } else if (object.getClass() == Long.class || object.getClass() == long.class) {
      bsh./*append(name).append("=").*/append(String.valueOf(object)).append("l");
      nameOrExpression = bsh.toString();

    } else if (object.getClass() == Float.class || object.getClass() == float.class) {
      bsh./*append(name).append("=").*/append(String.valueOf(object)).append("f");
      nameOrExpression = bsh.toString();

    } else if (object.getClass() == Double.class || object.getClass() == double.class) {
      bsh./*append(name).append("=").*/append(String.valueOf(object)).append("d");
      nameOrExpression = bsh.toString();

    } else if (object.getClass() == String.class) {
      bsh./*append(name).append("=").*/append("\"").append(((String) object).replaceAll("\"", "\\\"")).append("\"");
      nameOrExpression = bsh.toString();

    } else if (object.getClass() == Date.class) {
      bsh./*append(name).append("=").*/append("new java.util.Date(").append(String.valueOf(((Date) object).getTime())).append("l");
      nameOrExpression = bsh.toString();

    } else {

      nameOrExpression = name;
      cache.put(object, name); // avoid eternal loops

      output.append(name).append("=new ").append(object.getClass().getName()).append("();\n");

      Map<String, Field> fields = ReflectionUtil.gatherAllBeanFields(object.getClass());
      for (Map.Entry<String, Field> fieldEntry : fields.entrySet()) {
        Field field = fieldEntry.getValue();
        Method getter = ReflectionUtil.getGetter(field);
        Method setter = ReflectionUtil.getSetter(field);
        Object value = getter.invoke(object);
        if (value != null) {

          if (field.getType().isArray()) {
            // bean.setArray(new ObjectImpl[]);
            bsh.append(name).append(".").append(setter.getName()).append("(").append(".add(new ").append(field.getType().getComponentType().getName()).append("[").append(Array.getLength(object)).append("]").append(");\n");

            throw new UnsupportedOperationException("Arrays not supported.");

          } else if (Collection.class.isAssignableFrom(field.getType())) {
            Collection collection = (Collection) value;
            // bean.setCollection(new CollectionImpl());
            bsh.append(name).append(".").append(setter.getName()).append("(new ").append(collection.getClass().getName()).append("());\n");

            for (Object item : collection) {
              // bean.getCollection().add(item);
              bsh.append(name).append(".").append(getter.getName()).append("()").append(".add(").append(getAttributeOrExpression(output, item, cache)).append(");\n");
            }

          } else if (field.getType() == Map.class) {
            Map map = (Map) value;
            // bean.setMap(new MapImpl());
            bsh.append(name).append(".").append(setter.getName()).append("(new ").append(map.getClass().getName()).append("());\n");

            for (Map.Entry item : (Set<Map.Entry>) map.entrySet()) {
              // bean.getCollection().add(item);
              bsh.append(name).append(".").append(getter.getName()).append("(").append(".put(")
                  .append(getAttributeOrExpression(output, item.getKey(), cache)).append(", ")
                  .append(getAttributeOrExpression(output, item.getValue(), cache)).append(");\n");
            }


          } else {

            bsh.append(name).append(".").append(setter.getName()).append("(").append(getAttributeOrExpression(output, value, cache)).append(");\n");
          }

        }


      }

      output.append(bsh.toString());

    }

    
    cache.put(object, nameOrExpression);

    return nameOrExpression;
  }


}
