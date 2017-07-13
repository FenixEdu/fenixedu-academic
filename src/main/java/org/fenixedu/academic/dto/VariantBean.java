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
package org.fenixedu.academic.dto;

import java.io.Serializable;
import java.util.Date;

import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.DomainObject;

public class VariantBean implements Serializable {

    public static enum Type {
        INTEGER, STRING, DATE, MULTI_LANGUAGE_STRING, DOMAIN_REFERENCE, YEAR_MONTH_DAY, LOCAL_DATE
    };

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    private Object value;
    private Type type;

    public VariantBean() {
        value = null;
    }

    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        this.type = type;
    }

    public Integer getInteger() {
        return (Integer) this.value;
    }

    public void setInteger(Integer value) {
        this.value = value;
        setType(Type.INTEGER);
    }

    public Date getDate() {
        return (Date) this.value;
    }

    public void setDate(Date date) {
        this.value = date;
        setType(Type.DATE);
    }

    public String getString() {
        return (String) this.value;
    }

    public void setString(String string) {
        this.value = string;
        setType(Type.STRING);
    }

    public LocalizedString getMLString() {
        return (LocalizedString) this.value;
    }

    public void setMLString(LocalizedString value) {
        this.value = value;
        setType(Type.MULTI_LANGUAGE_STRING);
    }

    public DomainObject getDomainObject() {
        return Type.DOMAIN_REFERENCE.equals(type) ? ((DomainObject) (this.value)) : null;
    }

    public void setDomainObject(DomainObject domainObject) {
        this.value = domainObject;
        setType(Type.DOMAIN_REFERENCE);
    }

    public void setYearMonthDay(YearMonthDay date) {
        this.value = date;
        setType(Type.YEAR_MONTH_DAY);
    }

    public YearMonthDay getYearMonthDay() {
        return Type.YEAR_MONTH_DAY.equals(getType()) ? (YearMonthDay) this.value : null;
    }

    public void setLocalDate(LocalDate date) {
        this.value = date;
        setType(Type.LOCAL_DATE);
    }

    public LocalDate getLocalDate() {
        return Type.LOCAL_DATE.equals(getType()) ? (LocalDate) this.value : null;
    }

}
