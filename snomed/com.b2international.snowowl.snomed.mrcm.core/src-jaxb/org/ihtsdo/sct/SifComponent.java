/*
 * Copyright 2011-2015 B2i Healthcare Pte Ltd, http://b2i.sg
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *///
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.01 at 10:42:55 AM CET 
//


package org.ihtsdo.sct;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * The general component type redefined to require inclusion of a uid attribute
 * 
 * <p>Java class for SifComponent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SifComponent">
 *   &lt;complexContent>
 *     &lt;restriction base="{urn:ihtsdo-org/sct}SifComponent">
 *       &lt;attribute name="uid" use="required" type="{urn:ihtsdo-org/sct}Uuid" />
 *       &lt;attribute name="changeTime" use="required" type="{urn:ihtsdo-org/sct}UtcTime" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SifComponent")
@XmlSeeAlso({
    CmTestRelationship.class,
    CmConstraint.class,
    CmDependencyMember.class,
    CmTestDependency.class,
    CmTestCardinality.class,
    SifRsmMrcm.class,
    SifRefSetMember.class,
    SifRefSetMrcm.class
})
public class SifComponent
    extends OriginalSifComponent
{


}