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

import java.io.IOException;

/**
 * @author kalle
 * @since 2010-jul-10 00:10:27
 */
public class UnsupportedLocalVersion extends IOException {

  private int found;
  private int[] expected;

  public UnsupportedLocalVersion(int found, int... expected) {
    this.found = found;
    this.expected = expected;
  }

  @Override
  public String getMessage() {
    StringBuilder sb = new StringBuilder();
    sb.append("found ").append(found);
    sb.append(", expected ");
    for (int version : expected) {
      sb.append(version).append(", ");
    }
    sb.delete(sb.length() - 2, sb.length());
    return sb.toString();
  }
}
