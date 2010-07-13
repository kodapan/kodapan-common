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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kodapan.lang.reflect.ReflectionUtil;
import se.kodapan.lang.reflect.augmentation.annotations.Attribute;
import se.kodapan.lang.reflect.augmentation.annotations.BinaryAssociationClassEnd;
import se.kodapan.lang.reflect.augmentation.annotations.BinaryAssociationEnd;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author kalle
 * @since 2010-jul-13 07:08:30
 */
public class Mirror {

  public static Logger log = LoggerFactory.getLogger(Mirror.class);

  private static Map<Class, Mirror> mirrors = new HashMap<Class, Mirror>();

  public static Mirror reflect(Class type) {
    Mirror mirror = mirrors.get(type);
    if (mirror == null) {
      mirror = reflectLock(type);
    }
    return mirror;
  }

  private static synchronized Mirror reflectLock(Class type) {
    Mirror mirror;
    mirror = new Mirror(type);
    mirrors.put(type, mirror);
    mirror.reflect();
    return mirror;
  }

  private Class type;

  private Map<String, BeanPropertyAccessor> accessorByName = new HashMap<String, BeanPropertyAccessor>();
  private Map<String, se.kodapan.lang.reflect.augmentation.BinaryAssociationEnd> binaryAssociationEnds = new HashMap<String, se.kodapan.lang.reflect.augmentation.BinaryAssociationEnd>();
  private Map<String, se.kodapan.lang.reflect.augmentation.BinaryAssociationClassEnd> binaryAssociationClassEnds = new HashMap<String, se.kodapan.lang.reflect.augmentation.BinaryAssociationClassEnd>();
  private Map<String, se.kodapan.lang.reflect.augmentation.Attribute> attributes = new HashMap<String, se.kodapan.lang.reflect.augmentation.Attribute>();
  private Map<String, BeanPropertyAccessor> nonAugmentedBeanFields = new HashMap<String, BeanPropertyAccessor>();

  public Mirror(Class type) {
    this.type = type;
  }

  private void reflect() {

    log.info("Reflecting " + getType());

    Set<Class> allClasses = ReflectionUtil.gatherAllClasses(getType());
    for (Class _class : allClasses) {
      for (Field field : _class.getDeclaredFields()) {
        if (!Modifier.isFinal(field.getModifiers())
            && !Modifier.isStatic(field.getModifiers())) {

          if (field.getAnnotation(BinaryAssociationEnd.class) != null) {
            BeanPropertyAccessor accessor = new FieldPropertyAccessor(field);
            indexByName(accessor);
            binaryAssociationEnds.put(accessor.getName(), new se.kodapan.lang.reflect.augmentation.BinaryAssociationEnd(this, accessor));
          } else if (field.getAnnotation(BinaryAssociationClassEnd.class) != null) {
            BeanPropertyAccessor accessor = new FieldPropertyAccessor(field);
            indexByName(accessor);
            binaryAssociationClassEnds.put(accessor.getName(), new se.kodapan.lang.reflect.augmentation.BinaryAssociationClassEnd(accessor));
          } else if (field.getAnnotation(Attribute.class) != null) {
            BeanPropertyAccessor accessor = new FieldPropertyAccessor(field);
            attributes.put(accessor.getName(), new se.kodapan.lang.reflect.augmentation.Attribute(accessor));
            indexByName(accessor);
          } else {
            BeanPropertyAccessor accessor = new FieldPropertyAccessor(field);
            nonAugmentedBeanFields.put(accessor.getName(), accessor);
            indexByName(accessor);
          }
        }
      }

      for (Method method : _class.getDeclaredMethods()) {
        if (!Modifier.isStatic(method.getModifiers())) {
          if (method.getAnnotation(BinaryAssociationEnd.class) != null) {
            BeanPropertyAccessor accessor = new GetMethodPropertyAccessor(method);
            binaryAssociationEnds.put(accessor.getName(), new se.kodapan.lang.reflect.augmentation.BinaryAssociationEnd(this, accessor));
            indexByName(accessor);
          } else if (method.getAnnotation(BinaryAssociationClassEnd.class) != null) {
            BeanPropertyAccessor accessor = new GetMethodPropertyAccessor(method);
            binaryAssociationClassEnds.put(accessor.getName(), new se.kodapan.lang.reflect.augmentation.BinaryAssociationClassEnd(accessor));
            indexByName(accessor);
          } else if (method.getAnnotation(Attribute.class) != null) {
            BeanPropertyAccessor accessor = new GetMethodPropertyAccessor(method);
            attributes.put(accessor.getName(), new se.kodapan.lang.reflect.augmentation.Attribute(accessor));
            indexByName(accessor);
          }
        }
      }

    }

    for (se.kodapan.lang.reflect.augmentation.BinaryAssociationEnd end : binaryAssociationEnds.values()) {
      if (end.getBinaryAssociation() == null) {

        BinaryAssociation binaryAssociation;

        BinaryAssociationEnd annotation = end.getAccessor().getAnnotation(BinaryAssociationEnd.class);

        Mirror otherEndOwnerClass = Mirror.reflect(annotation.otherEndClass());
        se.kodapan.lang.reflect.augmentation.BinaryAssociationEnd otherEnd = otherEndOwnerClass.getBinaryAssociationEnds().get(annotation.otherEndName());

        if (otherEnd == null) {
          otherEnd = new se.kodapan.lang.reflect.augmentation.BinaryAssociationEnd(otherEndOwnerClass, null);
        }
        if (otherEnd.getBinaryAssociation() == null) {
          // it's created here
          binaryAssociation = new BinaryAssociation();
          binaryAssociation.getBinaryEnds()[0] = end;
          end.setBinaryAssociation(binaryAssociation);
          binaryAssociation.getBinaryEnds()[1] = otherEnd;
          otherEnd.setBinaryAssociation(binaryAssociation);

          //  otherEnd.setBinaryAssociation(binaryAssociation);
          // no, don't set that,
          // null is the indicator that it needs to be resolved when processed for that class!


          if (annotation.associationClass() != BinaryAssociationEnd.NO_ASSOCIATION_CLASS.class
              && binaryAssociation.getAssociationClassEnds() == null) {

            Mirror associationClass = Mirror.reflect(annotation.associationClass());

            if (associationClass.getBinaryAssociationClassEnds().size() != 2) {
              throw new RuntimeException();
            }

            binaryAssociation.setAssociationClass(associationClass);
            binaryAssociation.setAssociationClassEnds(new se.kodapan.lang.reflect.augmentation.BinaryAssociationClassEnd[2]);

            Iterator<se.kodapan.lang.reflect.augmentation.BinaryAssociationClassEnd> it = associationClass.getBinaryAssociationClassEnds().values().iterator();

            se.kodapan.lang.reflect.augmentation.BinaryAssociationClassEnd associationClassEnd;

            associationClassEnd = it.next();
            associationClassEnd.setBinaryAssociation(binaryAssociation);
            binaryAssociation.getAssociationClassEnds()[0] = associationClassEnd;

            associationClassEnd = it.next();
            associationClassEnd.setBinaryAssociation(binaryAssociation);
            binaryAssociation.getAssociationClassEnds()[1] = associationClassEnd;
          }


        }

      }

    }
  }


  private void indexByName(BeanPropertyAccessor accessor) {
    BeanPropertyAccessor previous = accessorByName.put(accessor.getName(), accessor);
    if (previous != null) {
      throw new RuntimeException("Two accessors in the same class can't have the same name! " + accessor.toString() + " and " + previous.toString());
    }
  }

  public Class getType() {
    return type;
  }

  public Map<String, BeanPropertyAccessor> getAccessorByName() {
    return accessorByName;
  }

  public Map<String, se.kodapan.lang.reflect.augmentation.BinaryAssociationEnd> getBinaryAssociationEnds() {
    return binaryAssociationEnds;
  }

  public Map<String, se.kodapan.lang.reflect.augmentation.BinaryAssociationClassEnd> getBinaryAssociationClassEnds() {
    return binaryAssociationClassEnds;
  }

  public Map<String, se.kodapan.lang.reflect.augmentation.Attribute> getAttributes() {
    return attributes;
  }

  public Map<String, BeanPropertyAccessor> getNonAugmentedBeanFields() {
    return nonAugmentedBeanFields;
  }
}
