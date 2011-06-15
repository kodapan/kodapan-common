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

package se.kodapan.lang.reflect.augmentation.annotations;

import se.kodapan.lang.reflect.augmentation.Aggregation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An association between two classes is made up with one or two of these.
 *
 * @author karl wettin <mailto:karl.wettin@gmail.com>
 *         Date: 2007-maj-17
 *         Time: 03:47:17
 *
 * <pre>
 * class Content {
 * @AssociationEnd(otherEnd="content")
 * private List<Comment> comments;
 * }
 * <p/>
 * class Comment {
 * @AssociationEnd(clotherEnd="comments")
 * private List<Content> content;
 * <p/>
 * }
 * <p/>
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface BinaryAssociationEnd {

  public static final class NO_QUALIFIER {
  }

  public static final class NO_ASSOCIATION_CLASS {
  }

  public static final String OTHER_END_NOT_NAVIGATABLE = "";

  public abstract String multiplicity() default Multiplicity.DEFAULT;

  public abstract String[] stereotypes() default Stereotyped.NO_STEREOTYPES;

  public abstract java.lang.Class[] qualifier() default NO_QUALIFIER.class;

  /**
   * @return name of this end of the association navigated from the other end, or "" if other end is not navigatable.
   */
  public abstract String otherEndName() default OTHER_END_NOT_NAVIGATABLE;

  /**
   * @return name of class in other end of association.
   */
  public abstract java.lang.Class otherEndClass();

  public abstract java.lang.Class associationClass() default NO_ASSOCIATION_CLASS.class;

  /**
   * @return how tough the relationship is between the association ends.
   */
  public abstract Aggregation aggregation() default Aggregation.NOT_SPECIFIED;

  /**
   * @return if other end is not navigatable we need to specify that information in this end.
   */
  public abstract Aggregation otherEndAggregation() default Aggregation.NOT_SPECIFIED;

}
