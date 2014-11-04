/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.util.stork;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.candidacyProcess.erasmus.StorkAttributeType;

public class Attribute {
    Integer id;
    StorkAttributeType type;
    Boolean mandatory;
    String value;

    public Attribute(Integer id, StorkAttributeType type, Boolean mandatory, String value) {
        setId(id);
        setType(type);
        setMandatory(mandatory);
        setValue(value);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer order) {
        this.id = order;
    }

    public StorkAttributeType getType() {
        return this.type;
    }

    public void setType(StorkAttributeType type) {
        this.type = type;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSemanticValue() {
        if (!isValueAssigned()) {
            return null;
        }

        return getValue();
    }

    public boolean isValueAssigned() {
        return !StringUtils.isEmpty(getValue()) && !"null".equals(getValue());
    }
}
