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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InferenceRule.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="InferenceRule">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="distribution"/>
 *     &lt;enumeration value="normal-short"/>
 *     &lt;enumeration value="normal-edit"/>
 *     &lt;enumeration value="normal-long"/>
 *     &lt;enumeration value="normal-short-nested"/>
 *     &lt;enumeration value="normal-long-nested"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "InferenceRule")
@XmlEnum
public enum InferenceRule {


    /**
     * Current
     * 
     */
    @XmlEnumValue("distribution")
    DISTRIBUTION("distribution"),

    /**
     * Inactive no specified reason
     * 
     */
    @XmlEnumValue("normal-short")
    NORMAL_SHORT("normal-short"),

    /**
     * Duplicate
     * 
     */
    @XmlEnumValue("normal-edit")
    NORMAL_EDIT("normal-edit"),

    /**
     * Outdated
     * 
     */
    @XmlEnumValue("normal-long")
    NORMAL_LONG("normal-long"),

    /**
     * Moved elsewhere
     * 
     */
    @XmlEnumValue("normal-short-nested")
    NORMAL_SHORT_NESTED("normal-short-nested"),

    /**
     * Pending move
     * 
     */
    @XmlEnumValue("normal-long-nested")
    NORMAL_LONG_NESTED("normal-long-nested");
    private final String value;

    InferenceRule(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static InferenceRule fromValue(String v) {
        for (InferenceRule c: InferenceRule.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}