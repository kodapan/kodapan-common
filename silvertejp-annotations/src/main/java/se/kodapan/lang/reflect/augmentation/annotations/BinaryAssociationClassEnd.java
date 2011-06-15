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
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <pre>
 *         *
 * [Item]<---------[User]
 *            |
 *
 *            |
 *        [Rating]
 * </pre>
 *
 * Rating is the association class.
 *
 * <pre>
 * class Rating {
 *   @BinaryAssociationClassEnd(otherEndName = "items")
 *   private User user;
 *
 *   @BinaryAssociationClassEnd()
 *   private Item item;
 * }
 * </pre>
 *
 * @author karl wettin <mailto:karl.wettin@gmail.com>
 *         Date: 2007-maj-28
 *         Time: 05:20:24
 */
@Retention(RetentionPolicy.RUNTIME)

public @interface BinaryAssociationClassEnd {

  public abstract java.lang.Class otherEndClass();
  public abstract String otherEndName();
  
}
