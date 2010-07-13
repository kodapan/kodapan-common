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

/**
 * @author karl wettin <mailto:karl.wettin@gmail.com>
 *         Date: 2007-maj-29
 *         Time: 03:08:34
 */
public class Multiplicity {
  public static final String DEFAULT = "0..1";

  public static boolean isMaximumOne(String expression) {
    return "0..1".equals(expression) || "1".equals(expression);
  }

}
