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
import java.lang.reflect.Method;

/**
 * @author karl wettin <mailto:karl.wettin@gmail.com>
 *         Date: 2007-jun-03
 *         Time: 14:47:37
 */
public class GetMethodPropertyAccessor extends BeanPropertyAccessor {

  private Method method;

  public GetMethodPropertyAccessor(Method getter) {
    this.name = resolveName(getter);
    this.getter = getter;
    try {
      this.setter = setterFactory(getter);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    this.method = getter;
  }


  public <T extends Annotation> T getAnnotation(Class<T> type) {
    return method.getAnnotation(type);
  }

  public Annotation[] getAnnotations() {
    return method.getAnnotations();
  }

  public Annotation[] getDeclaredAnnotations() {
    return method.getDeclaredAnnotations();
  }

  public Method getMethod() {
    return method;
  }


  public String toString() {
    return method.toString();
  }
}
