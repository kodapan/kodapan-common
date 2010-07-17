/*
 * Copyright 2010 Kodapan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.kodapan.lang.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author kalle
 * @since 2010-jul-10 23:42:53
 */
public class ReflectionUtil {


  public static Method getGetter(Field field) throws NoSuchMethodException {
    StringBuilder getterName = new StringBuilder(field.getName().length() + 3);
    if (field.getType() == java.lang.Boolean.class || field.getType() == boolean.class) {
      getterName.append("is");
    } else {
      getterName.append("get");
    }
    getterName.append(field.getName().substring(0, 1).toUpperCase());
    getterName.append(field.getName().substring(1));
    return field.getDeclaringClass().getMethod(getterName.toString());
  }

  public static Method getSetter(Field field) throws NoSuchMethodException {
    StringBuilder setterName = new StringBuilder(field.getName().length() + 3);
    setterName.append("set");
    setterName.append(field.getName().substring(0, 1).toUpperCase());
    setterName.append(field.getName().substring(1));
    return field.getDeclaringClass().getMethod(setterName.toString(), field.getType());
  }

  public static Method getSetter(Object instance, String fieldName) throws NoSuchMethodException {
    return getSetter(instance.getClass(), fieldName);
  }

  public static Method getSetter(Class _class, String fieldName) throws NoSuchMethodException {
    return getSetter(gatherAllBeanFields(_class).get(fieldName));
  }

  public static Method getGetter(Class _class, String fieldName) throws NoSuchMethodException {
    return getGetter(gatherAllBeanFields(_class).get(fieldName));
  }

  public static Method getGetter(Object instance, String fieldName) throws NoSuchMethodException {
    return getGetter(instance.getClass(), fieldName);
  }

  public static Map<String, Field> gatherAllBeanFields(Class implementation) {
    return gatherAllBeanFields(gatherAllClasses(implementation));
  }

  public static Map<String, Field> gatherAllBeanFields(Set<Class> allClasses) {
    Map<String, Field> allFields = new HashMap<String, Field>();
    for (Class _class : allClasses) {
      for (Field field : Arrays.asList(_class.getDeclaredFields())) {
        if (!Modifier.isFinal(field.getModifiers())
            && !Modifier.isTransient(field.getModifiers())
            && !Modifier.isStatic(field.getModifiers())) {
          allFields.put(field.getName(), field);
        }
      }
    }
    return allFields;
  }

  public static Set<Class> gatherAllClasses(Class implementation) {
    Set<Class> classes = new HashSet<Class>();
    gatherAllClasses(implementation, classes);
    return classes;
  }

  public static void gatherAllClasses(Class implementation, Set<Class> classes) {
    classes.add(implementation);
    if (implementation.getSuperclass() != null && classes.add(implementation.getSuperclass())) {
      gatherAllClasses(implementation.getSuperclass(), classes);
    }
    for (Class _class : implementation.getInterfaces()) {
      if (classes.add(_class)) {
        gatherAllClasses(_class, classes);
      }
    }
  }

}
