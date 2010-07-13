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

/**
 * @author kalle
 * @since 2010-jul-13 08:18:56
 */
public class BinaryAssociationEnd {

  private BeanPropertyAccessor accessor;

  public BinaryAssociationEnd(Mirror ownerClass, BeanPropertyAccessor accessor) {
    this.ownerClass = ownerClass;
    this.accessor = accessor;
    if (accessor != null) {
      se.kodapan.lang.reflect.augmentation.annotations.BinaryAssociationEnd annotation = accessor.getAnnotation(se.kodapan.lang.reflect.augmentation.annotations.BinaryAssociationEnd.class);
      navigatable = true;
      aggregation = annotation.aggregation();
      multiplicity = new Multiplicity(annotation.multiplicity());
      name = accessor.getName();
    }
  }

  private BinaryAssociation binaryAssociation;

  private Mirror ownerClass;
  private Aggregation aggregation;
  private Multiplicity multiplicity;
  private boolean navigatable;
  private String name;

  public BinaryAssociationClassEnd getAssociationClassEnd() {
    return getBinaryAssociation().getAssociationClassEnds()[getBinaryAssociation().indexOf(this)];
  }

  private Class qualification;

  public String getName() {
    return name;
  }

  public BinaryAssociationEnd getOtherEnd() {
    return getBinaryAssociation().getOtherEnd(this);
  }

  public boolean isNavigatable() {
    return navigatable;
  }

  public void setNavigatable(boolean navigatable) {
    this.navigatable = navigatable;
  }

  public Mirror getOwnerClass() {
    return ownerClass;
  }

  public void setOwnerClass(Mirror ownerClass) {
    this.ownerClass = ownerClass;
  }

  public Class getQualification() {
    return qualification;
  }

  public void setQualification(Class qualification) {
    this.qualification = qualification;
  }

  public BeanPropertyAccessor getAccessor() {
    return accessor;
  }

  public void setAccessor(BeanPropertyAccessor accessor) {
    this.accessor = accessor;
  }

  public Aggregation getAggregation() {
    return aggregation;
  }

  public void setAggregation(Aggregation aggregation) {
    this.aggregation = aggregation;
  }

  public BinaryAssociation getBinaryAssociation() {
    return binaryAssociation;
  }

  public void setBinaryAssociation(BinaryAssociation binaryAssociation) {
    this.binaryAssociation = binaryAssociation;
  }

  public Multiplicity getMultiplicity() {
    return multiplicity;
  }

  public void setMultiplicity(Multiplicity multiplicity) {
    this.multiplicity = multiplicity;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder();
    appendOwnerClassToString(this, buf);
    appendQualificationsToString(this, buf);
    appendAggregationToString(getAggregation(), buf);

    if (getOtherEnd().isNavigatable() && !isNavigatable()) {
      buf.append("<");
    }

    buf.append("-- ");


    if (getOtherEnd().getMultiplicity() != null) {
      buf.append("{");
      buf.append(getOtherEnd().getMultiplicity().getExpression());
      buf.append("} ");
    }


//    appendVisibilityToString(getOtherEnd(), buf);
    appendNameToString(getOtherEnd(), buf);

    if (getBinaryAssociation().getAssociationClass() != null) {
      buf.append(" -|- ");
    } else {
      buf.append("     ");
    }


//    appendVisibilityToString(this, buf);
    appendNameToString(this, buf);
    buf.append(" ");
    
    if (getMultiplicity() != null) {
      buf.append("{");
      buf.append(getMultiplicity().getExpression());
      buf.append("} ");
    }

    buf.append("--");
    if (isNavigatable() && !getOtherEnd().isNavigatable()) {
      buf.append(">");
    }

    appendAggregationToString(getOtherEnd().getAggregation(), buf);
    appendQualificationsToString(getOtherEnd(), buf);
    appendOwnerClassToString(getOtherEnd(), buf);

    return buf.toString();

  }

  private static void appendNameToString(BinaryAssociationEnd baend, StringBuilder buf) {
    if (baend.getName() != null) {
      buf.append(baend.getName());
    }
  }

  private static void appendOwnerClassToString(BinaryAssociationEnd baend, StringBuilder buf) {
    buf.append("[");
    if (baend.getOwnerClass() == null) {
      buf.append("<<unbound>>");
    } else {
      buf.append(baend.getOwnerClass().getType().getSimpleName());
    }    
    buf.append("]");
  }

//  private static void appendVisibilityToString(Visible visible, StringBuilder buf) {
//    if (visible.getVisibility() != null) {
//      switch (visible.getVisibility()) {
//        case PRIVATE:
//          buf.append("-");
//        case PROTECTED:
//          buf.append("#");
//        case PACKAGE:
//          buf.append("=");
//        case PUBLIC:
//          buf.append("+");
//      }
//    }
//  }

  private static void appendAggregationToString(Aggregation aggregation, StringBuilder buf) {
    if (aggregation != null) {
      switch (aggregation) {
        case AGGREGATED:
          buf.append("<>");
        case COMPOSITE:
          buf.append("<#>");
      }
    }
  }

  private static void appendQualificationsToString(BinaryAssociationEnd baend, StringBuilder buf) {
    if (baend.getQualification() != null) {
      buf.append("(");
      buf.append(baend.getOwnerClass().getType().getSimpleName());
      buf.append(")");
    }
  }

}
