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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Type that represents a member of Reference Set
 * 
 * <p>Java class for SifRefSetMember complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SifRefSetMember">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:ihtsdo-org/sct}SifComponent">
 *       &lt;attribute name="refSetId" use="required" type="{urn:ihtsdo-org/sct}SctReference" />
 *       &lt;attribute name="componentId" use="required" type="{urn:ihtsdo-org/sct}SctReference" />
 *       &lt;attribute name="tagCode" type="{urn:ihtsdo-org/sct}CodeText" />
 *       &lt;attribute name="annotation" type="{urn:ihtsdo-org/sct}Term" />
 *       &lt;attribute name="scope" type="{urn:ihtsdo-org/sct}CodeText" />
 *       &lt;attribute name="priority" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="acceptability" type="{urn:ihtsdo-org/sct}Acceptability" />
 *       &lt;attribute name="correctness" type="{urn:ihtsdo-org/sct}Correctness" />
 *       &lt;attribute name="degreeOfSynonymy" type="{urn:ihtsdo-org/sct}DegreeOfSynonymy" />
 *       &lt;attribute name="childScope" type="{urn:ihtsdo-org/sct}CodeText" />
 *       &lt;attribute name="childId" type="{urn:ihtsdo-org/sct}SctReference" />
 *       &lt;attribute name="defaultToSubtype" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="displayOrder" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="defaultToSupertype" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="validFrom" type="{urn:ihtsdo-org/sct}UtcTime" />
 *       &lt;attribute name="validTo" type="{urn:ihtsdo-org/sct}UtcTime" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SifRefSetMember")
@XmlSeeAlso({
    RefSetMember.class
})
public class SifRefSetMember
    extends SifComponent
{

    @XmlAttribute(name = "refSetId", required = true)
    protected String refSetId;
    @XmlAttribute(name = "componentId", required = true)
    protected String componentId;
    @XmlAttribute(name = "tagCode")
    protected String tagCode;
    @XmlAttribute(name = "annotation")
    protected String annotation;
    @XmlAttribute(name = "scope")
    protected String scope;
    @XmlAttribute(name = "priority")
    protected Integer priority;
    @XmlAttribute(name = "acceptability")
    protected Short acceptability;
    @XmlAttribute(name = "correctness")
    protected Short correctness;
    @XmlAttribute(name = "degreeOfSynonymy")
    protected Short degreeOfSynonymy;
    @XmlAttribute(name = "childScope")
    protected String childScope;
    @XmlAttribute(name = "childId")
    protected String childId;
    @XmlAttribute(name = "defaultToSubtype")
    protected Boolean defaultToSubtype;
    @XmlAttribute(name = "displayOrder")
    protected Integer displayOrder;
    @XmlAttribute(name = "defaultToSupertype")
    protected Boolean defaultToSupertype;
    @XmlAttribute(name = "validFrom")
    protected String validFrom;
    @XmlAttribute(name = "validTo")
    protected String validTo;

    /**
     * Gets the value of the refSetId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefSetId() {
        return refSetId;
    }

    /**
     * Sets the value of the refSetId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefSetId(String value) {
        this.refSetId = value;
    }

    /**
     * Gets the value of the componentId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComponentId() {
        return componentId;
    }

    /**
     * Sets the value of the componentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComponentId(String value) {
        this.componentId = value;
    }

    /**
     * Gets the value of the tagCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTagCode() {
        return tagCode;
    }

    /**
     * Sets the value of the tagCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTagCode(String value) {
        this.tagCode = value;
    }

    /**
     * Gets the value of the annotation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnnotation() {
        return annotation;
    }

    /**
     * Sets the value of the annotation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnnotation(String value) {
        this.annotation = value;
    }

    /**
     * Gets the value of the scope property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScope() {
        return scope;
    }

    /**
     * Sets the value of the scope property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScope(String value) {
        this.scope = value;
    }

    /**
     * Gets the value of the priority property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPriority(Integer value) {
        this.priority = value;
    }

    /**
     * Gets the value of the acceptability property.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getAcceptability() {
        return acceptability;
    }

    /**
     * Sets the value of the acceptability property.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setAcceptability(Short value) {
        this.acceptability = value;
    }

    /**
     * Gets the value of the correctness property.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getCorrectness() {
        return correctness;
    }

    /**
     * Sets the value of the correctness property.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setCorrectness(Short value) {
        this.correctness = value;
    }

    /**
     * Gets the value of the degreeOfSynonymy property.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getDegreeOfSynonymy() {
        return degreeOfSynonymy;
    }

    /**
     * Sets the value of the degreeOfSynonymy property.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setDegreeOfSynonymy(Short value) {
        this.degreeOfSynonymy = value;
    }

    /**
     * Gets the value of the childScope property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChildScope() {
        return childScope;
    }

    /**
     * Sets the value of the childScope property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChildScope(String value) {
        this.childScope = value;
    }

    /**
     * Gets the value of the childId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChildId() {
        return childId;
    }

    /**
     * Sets the value of the childId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChildId(String value) {
        this.childId = value;
    }

    /**
     * Gets the value of the defaultToSubtype property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDefaultToSubtype() {
        return defaultToSubtype;
    }

    /**
     * Sets the value of the defaultToSubtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDefaultToSubtype(Boolean value) {
        this.defaultToSubtype = value;
    }

    /**
     * Gets the value of the displayOrder property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDisplayOrder() {
        return displayOrder;
    }

    /**
     * Sets the value of the displayOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDisplayOrder(Integer value) {
        this.displayOrder = value;
    }

    /**
     * Gets the value of the defaultToSupertype property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDefaultToSupertype() {
        return defaultToSupertype;
    }

    /**
     * Sets the value of the defaultToSupertype property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDefaultToSupertype(Boolean value) {
        this.defaultToSupertype = value;
    }

    /**
     * Gets the value of the validFrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValidFrom() {
        return validFrom;
    }

    /**
     * Sets the value of the validFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValidFrom(String value) {
        this.validFrom = value;
    }

    /**
     * Gets the value of the validTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValidTo() {
        return validTo;
    }

    /**
     * Sets the value of the validTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValidTo(String value) {
        this.validTo = value;
    }

}