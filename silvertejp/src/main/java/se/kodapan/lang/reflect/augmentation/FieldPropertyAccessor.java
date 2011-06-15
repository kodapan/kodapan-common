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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * @author karl wettin <mailto:karl.wettin@gmail.com>
 *         Date: 2007-jun-03
 *         Time: 14:47:37
 */
public class FieldPropertyAccessor extends BeanPropertyAccessor {

  private Field field;

  public FieldPropertyAccessor(Field field) {
    this.name = field.getName();
    try {
      this.getter = getterFactory(field);
      this.setter = setterFactory(getter);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    this.field = field;
  }


  public <T extends Annotation> T getAnnotation(Class<T> type) {
    return field.getAnnotation(type);
  }

  public Annotation[] getAnnotations() {
    return field.getAnnotations();
  }

  public Annotation[] getDeclaredAnnotations() {
    return field.getDeclaredAnnotations();
  }

  public Field getField() {
    return field;
  }

  public static Method getterFactory(Field field) throws NoSuchMethodException {
    StringBuffer methodName = new StringBuffer(field.getName().length() + 3);
    if (field.getType() == Boolean.class || field.getType() == boolean.class) {
      methodName.append("is");
    } else {
      methodName.append("get");
    }
    int pos = methodName.length();
    methodName.append(field.getName());
    methodName.setCharAt(pos, Character.toUpperCase(methodName.charAt(pos)));
    return field.getDeclaringClass().getDeclaredMethod(methodName.toString());
  }


  public String toString() {
    return field.toString();
  }
}
