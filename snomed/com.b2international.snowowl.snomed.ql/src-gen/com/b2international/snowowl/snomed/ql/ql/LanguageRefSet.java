/**
 * Copyright 2019 B2i Healthcare Pte Ltd, http://b2i.sg
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.b2international.snowowl.snomed.ql.ql;

import com.b2international.snowowl.snomed.ecl.ecl.Script;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Language Ref Set</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.b2international.snowowl.snomed.ql.ql.LanguageRefSet#getRefset <em>Refset</em>}</li>
 * </ul>
 *
 * @see com.b2international.snowowl.snomed.ql.ql.QlPackage#getLanguageRefSet()
 * @model
 * @generated
 */
public interface LanguageRefSet extends EObject
{
  /**
   * Returns the value of the '<em><b>Refset</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Refset</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Refset</em>' containment reference.
   * @see #setRefset(Script)
   * @see com.b2international.snowowl.snomed.ql.ql.QlPackage#getLanguageRefSet_Refset()
   * @model containment="true"
   * @generated
   */
  Script getRefset();

  /**
   * Sets the value of the '{@link com.b2international.snowowl.snomed.ql.ql.LanguageRefSet#getRefset <em>Refset</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Refset</em>' containment reference.
   * @see #getRefset()
   * @generated
   */
  void setRefset(Script value);

} // LanguageRefSet
