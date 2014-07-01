/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 6/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.util.Bundle;

import org.apache.struts.util.MessageResources;
import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author T�nia Pous�o
 * 
 */
public class PaymentPhase extends PaymentPhase_Base {

    public static Comparator<PaymentPhase> COMPARATOR_BY_END_DATE = new Comparator<PaymentPhase>() {
        @Override
        public int compare(PaymentPhase leftPaymentPhase, PaymentPhase rightPaymentPhase) {
            int comparationResult =
                    leftPaymentPhase.getEndDateYearMonthDay().compareTo(rightPaymentPhase.getEndDateYearMonthDay());
            return (comparationResult == 0) ? leftPaymentPhase.getExternalId().compareTo(rightPaymentPhase.getExternalId()) : comparationResult;
        }
    };

    public PaymentPhase() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public String getDescriptionFromMessageResourses() {
        MessageResources messages = MessageResources.getMessageResources(Bundle.APPLICATION);

        String newDescription = null;
        newDescription = messages.getMessage(super.getDescription());
        if (newDescription == null) {
            newDescription = super.getDescription();
        }
        return newDescription;
    }

    public void delete() {
        setGratuityValues(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public java.util.Date getEndDate() {
        org.joda.time.YearMonthDay ymd = getEndDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setEndDate(java.util.Date date) {
        if (date == null) {
            setEndDateYearMonthDay(null);
        } else {
            setEndDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getStartDate() {
        org.joda.time.YearMonthDay ymd = getStartDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setStartDate(java.util.Date date) {
        if (date == null) {
            setStartDateYearMonthDay(null);
        } else {
            setStartDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public boolean hasGratuityValues() {
        return getGratuityValues() != null;
    }

    @Deprecated
    public boolean hasValue() {
        return getValue() != null;
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEndDateYearMonthDay() {
        return getEndDateYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasStartDateYearMonthDay() {
        return getStartDateYearMonthDay() != null;
    }

}
