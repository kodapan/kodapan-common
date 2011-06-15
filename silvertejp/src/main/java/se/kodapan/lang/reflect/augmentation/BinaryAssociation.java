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
 * @since 2010-jul-13 08:19:03
 */
public class BinaryAssociation {

  private BinaryAssociationEnd[] binaryEnds = new BinaryAssociationEnd[2];

  private Mirror associationClass;
  private BinaryAssociationClassEnd[] associationClassEnds;

  public Mirror getAssociationClass() {
    return associationClass;
  }

  public void setAssociationClass(Mirror associationClass) {
    this.associationClass = associationClass;
  }

  public int indexOf(BinaryAssociationEnd end) {
    return binaryEnds[0] == end ? 0 : 1;
  }

  public int indexOf(BinaryAssociationClassEnd end) {
    return associationClassEnds[0] == end ? 0 : 1;
  }

  public BinaryAssociationEnd getOtherEnd(BinaryAssociationEnd end) {
    return binaryEnds[0] == end ? binaryEnds[1] : binaryEnds[0];
  }

  public BinaryAssociationClassEnd getOtherEnd(BinaryAssociationClassEnd end) {
    return associationClassEnds[0] == end ? associationClassEnds[1] : associationClassEnds[0];
  }

  public BinaryAssociationEnd[] getBinaryEnds() {
    return binaryEnds;
  }

  public void setBinaryEnds(BinaryAssociationEnd[] binaryEnds) {
    this.binaryEnds = binaryEnds;
  }

  public BinaryAssociationClassEnd[] getAssociationClassEnds() {
    return associationClassEnds;
  }

  public void setAssociationClassEnds(BinaryAssociationClassEnd[] associationClassEnds) {
    this.associationClassEnds = associationClassEnds;
  }

}
