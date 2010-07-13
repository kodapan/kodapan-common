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

package se.kodapan.lang.reflect.augmentation;

import se.kodapan.lang.reflect.augmentation.annotations.BinaryAssociationEnd;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * This class abstracts the use of annotations,
 * letting us use bean fields in classes
 * and getter or setter method of interfaces.
 *
 * @author karl wettin <mailto:karl.wettin@gmail.com>
 *         Date: 2007-jun-03
 *         Time: 14:34:12
 */
public abstract class BeanPropertyAccessor {

  public abstract <T extends Annotation> T getAnnotation(java.lang.Class<T> type);

  public abstract Annotation[] getAnnotations();

  public abstract Annotation[] getDeclaredAnnotations();
 
  protected String name;
  protected java.lang.reflect.Method getter;
  protected java.lang.reflect.Method setter;

  protected BeanPropertyAccessor() {
  }

  protected BeanPropertyAccessor(Method getter, String name) {
    this.name = name;
    this.getter = getter;
    try {
      setter = setterFactory(getter);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }


  public static Method getterFactory(java.lang.Class _class, String name) throws NoSuchMethodException {
    StringBuffer methodName = new StringBuffer(name.length() + 3);
    methodName.append("get");
    methodName.append(name);
    methodName.setCharAt(3, Character.toUpperCase(methodName.charAt(3)));
    return _class.getDeclaredMethod(methodName.toString());

  }


  public static Method setterFactory(java.lang.reflect.Method getter) throws NoSuchMethodException {
    int pos = getter.getName().startsWith("is") ? 2 : 3;

    StringBuffer methodName = new StringBuffer(getter.getName().length() + pos);
    methodName.append("set");
    methodName.append(resolveName(getter));
    methodName.setCharAt(3, Character.toUpperCase(methodName.charAt(3)));
    return getter.getDeclaringClass().getDeclaredMethod(methodName.toString(), getter.getReturnType());
  }

  /**
   * @return bean property getter method
   */
  public java.lang.reflect.Method getGetter() {
    return getter;
  }

  /**
   * @return bean property setter method
   */
  public java.lang.reflect.Method getSetter() {
    return setter;
  }

  /**
   * @param entity
   * @return current property value of entity
   */


  public Object get(Object entity) {
    try {
      return getGetter().invoke(entity);
    } catch (InvocationTargetException ite) {
      throw new RuntimeException(ite);
    } catch (IllegalAccessException iae) {
      throw new RuntimeException(iae);
    }
  }

  /**
   * @param entity
   * @param value
   * @return previous property value
   */
  public void set(Object entity, Object value) {
    try {
      getSetter().invoke(entity, value);
    } catch (InvocationTargetException ite) {
      throw new RuntimeException(ite);
    } catch (IllegalAccessException iae) {
      throw new RuntimeException(iae);
    }
  }

  public static String resolveName(Method method) {
    return resolveName(method.getName());
  }

  public static String resolveName(String methodName) {
    int pos = methodName.startsWith("is") ? 2 : 3;
    StringBuffer buf = new StringBuffer(methodName.length() - pos);
    buf.append(methodName.substring(pos));
    buf.setCharAt(0, Character.toLowerCase(buf.charAt(0)));
    return buf.toString();
  }

  public String getName() {
    return name;
  }


}
