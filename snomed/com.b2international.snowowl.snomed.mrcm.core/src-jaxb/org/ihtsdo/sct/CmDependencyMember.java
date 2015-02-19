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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Type that represents an MRCM Dependency Test Member
 * 
 * <p>Java class for CmDependencyMember complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CmDependencyMember">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:ihtsdo-org/sct}SifComponent">
 *       &lt;attribute name="refDependencyUid" use="required" type="{urn:ihtsdo-org/sct}Uuid" />
 *       &lt;attribute name="refTestUid" use="required" type="{urn:ihtsdo-org/sct}Uuid" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CmDependencyMember")
public class CmDependencyMember
    extends SifComponent
{

    @XmlAttribute(name = "refDependencyUid", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String refDependencyUid;
    @XmlAttribute(name = "refTestUid", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String refTestUid;

    /**
     * Gets the value of the refDependencyUid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefDependencyUid() {
        return refDependencyUid;
    }

    /**
     * Sets the value of the refDependencyUid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefDependencyUid(String value) {
        this.refDependencyUid = value;
    }

    /**
     * Gets the value of the refTestUid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefTestUid() {
        return refTestUid;
    }

    /**
     * Sets the value of the refTestUid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefTestUid(String value) {
        this.refTestUid = value;
    }

}