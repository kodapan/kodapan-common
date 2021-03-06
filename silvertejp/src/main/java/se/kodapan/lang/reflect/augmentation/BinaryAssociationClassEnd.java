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
 * @since 2010-jul-13 08:19:11
 */
public class BinaryAssociationClassEnd {

  private BeanPropertyAccessor accessor;

  public BinaryAssociationClassEnd(BeanPropertyAccessor accessor) {
    this.accessor = accessor;

    
  }

  private BinaryAssociation binaryAssociation;



  public BeanPropertyAccessor getAccessor() {
    return accessor;
  }

  public void setAccessor(BeanPropertyAccessor accessor) {
    this.accessor = accessor;
  }

  public BinaryAssociation getBinaryAssociation() {
    return binaryAssociation;
  }

  public void setBinaryAssociation(BinaryAssociation binaryAssociation) {
    this.binaryAssociation = binaryAssociation;
  }
}
