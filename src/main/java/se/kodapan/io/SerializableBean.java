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

package se.kodapan.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kodapan.lang.reflect.ReflectionUtil;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * todo add annotation for local version of entity class
 * todo add annotation for refactored fields
 *
 *
 * @author kalle
 * @since 2010-jul-13 04:26:47
 */
public class SerializableBean implements Serializable, Externalizable {

  private static long serialVersionUID = 1l;
  public static Logger log = LoggerFactory.getLogger(SerializableBean.class);


  private static Map<Class, Map<Field, Method>> getters = new HashMap<Class, Map<Field, Method>>();
  private static Map<Class, Map<String, Method>> setters = new HashMap<Class, Map<String, Method>>();


  @Override
  public void writeExternal(ObjectOutput objectOutput) throws IOException {

    Map<Field, Method> getters = SerializableBean.getters.get(getClass());
    if (getters == null) {
      getters = new HashMap<Field, Method>();
      for (Field field : ReflectionUtil.gatherAllBeanFields(getClass()).values()) {
        try {
          getters.put(field, ReflectionUtil.getGetter(field));
        } catch (NoSuchMethodException e) {
          throw new RuntimeException(e);
        }
      }
      SerializableBean.getters.put(getClass(), getters);
    }

    objectOutput.writeInt(1); // local version
    objectOutput.writeInt(getters.size());
    for (Map.Entry<Field, Method> e : getters.entrySet()) {
      objectOutput.writeObject(e.getKey().getName());
      try {
        objectOutput.writeObject(e.getValue().invoke(this));
      } catch (IllegalAccessException e1) {
        throw new RuntimeException(e1);
      } catch (InvocationTargetException e1) {
        throw new RuntimeException(e1);
      }
    }


  }

  @Override
  public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {

    Map<String, Method> setters = SerializableBean.setters.get(getClass());
    if (setters == null) {
      setters = new HashMap<String, Method>();
      for (Field field : ReflectionUtil.gatherAllBeanFields(getClass()).values()) {
        try {
          setters.put(field.getName(), ReflectionUtil.getSetter(field));
        } catch (NoSuchMethodException e) {
          throw new RuntimeException(e);
        }
      }
      SerializableBean.setters.put(getClass(), setters);
    }

    int version = objectInput.readInt();
    if (version == 1) {
      int numberOfValues = objectInput.readInt();
      for (int i = 0; i < numberOfValues; i++) {
        String fieldName = (String)objectInput.readObject();
        Object fieldValue = objectInput.readObject();
        try {
          setters.get(fieldName).invoke(this, fieldValue);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
          throw new RuntimeException(e);
        }

      }

    } else {
      throw new UnsupportedLocalVersion(version, 1);
    }


  }


}
