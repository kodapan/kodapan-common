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
 * @author karl wettin <mailto:karl.wettin@gmail.com>
 *         Date: 2007-maj-17
 *         Time: 03:47:25
 */
@Retention(RetentionPolicy.RUNTIME)

/**
 * A class attribute definition.
 */
public @interface Attribute {

  /**
   * Defaults to "0..1"
   * @return A machine readable multiplicity value.
   */
  public abstract String multiplicity() default Multiplicity.DEFAULT;

  /**
   * Defaults to no stereotypes. 
   * @return Attribute meta data.
   */
   public abstract String[] stereotypes() default Stereotyped.NO_STEREOTYPES;
  
}
