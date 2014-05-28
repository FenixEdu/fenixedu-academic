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
 * Created on Apr 22, 2004
 */
package net.sourceforge.fenixedu.domain.gratuity.masterDegree;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentStatus;
import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentType;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class SibsPaymentFileEntry extends SibsPaymentFileEntry_Base {

    public SibsPaymentFileEntry() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public SibsPaymentFileEntry(Integer year, Integer studentNumber, SibsPaymentType paymentType, Date transactionDate,
            Double payedValue, SibsPaymentFile sibsPaymentFile, SibsPaymentStatus paymentStatus) {
        this();
        this.setYear(year);
        this.setStudentNumber(studentNumber);
        this.setPaymentType(paymentType);
        this.setTransactionDate(transactionDate);
        this.setPayedValue(payedValue);
        this.setSibsPaymentFile(sibsPaymentFile);
        this.setPaymentStatus(paymentStatus);
    }

    public static List<SibsPaymentFileEntry> readNonProcessed() {
        final List<SibsPaymentFileEntry> result = new ArrayList<SibsPaymentFileEntry>();
        for (final SibsPaymentFileEntry sibsPaymentFileEntry : Bennu.getInstance().getSibsPaymentFileEntrysSet()) {
            if (sibsPaymentFileEntry.getPaymentStatus() != SibsPaymentStatus.PROCESSED_PAYMENT) {
                result.add(sibsPaymentFileEntry);
            }
        }
        return result;
    }

    public static List<SibsPaymentFileEntry> readByYearAndStudentNumberAndPaymentType(final Integer year,
            final Integer studentNumber, final SibsPaymentType sibsPaymentType) {
        final List<SibsPaymentFileEntry> result = new ArrayList<SibsPaymentFileEntry>();
        for (final SibsPaymentFileEntry sibsPaymentFileEntry : Bennu.getInstance().getSibsPaymentFileEntrysSet()) {
            if (sibsPaymentFileEntry.getYear().equals(year) && sibsPaymentFileEntry.getStudentNumber().equals(studentNumber)
                    && sibsPaymentFileEntry.getPaymentType() == sibsPaymentType) {
                result.add(sibsPaymentFileEntry);
            }
        }
        return result;
    }

    public static List<SibsPaymentFileEntry> readByYearAndStudentNumberAndPaymentTypeExceptFileEntry(
            SibsPaymentFileEntry paymentFileEntry) {
        final List<SibsPaymentFileEntry> result = new ArrayList<SibsPaymentFileEntry>();
        for (final SibsPaymentFileEntry sibsPaymentFileEntry : Bennu.getInstance().getSibsPaymentFileEntrysSet()) {
            if (sibsPaymentFileEntry != paymentFileEntry && sibsPaymentFileEntry.getYear().equals(paymentFileEntry.getYear())
                    && sibsPaymentFileEntry.getStudentNumber().equals(paymentFileEntry.getStudentNumber())
                    && sibsPaymentFileEntry.getPaymentType().equals(paymentFileEntry.getPaymentType())) {
                result.add(sibsPaymentFileEntry);
            }
        }
        return result;
    }

    @Deprecated
    public java.util.Date getTransactionDate() {
        org.joda.time.DateTime dt = getTransactionDateDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setTransactionDate(java.util.Date date) {
        if (date == null) {
            setTransactionDateDateTime(null);
        } else {
            setTransactionDateDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public boolean hasYear() {
        return getYear() != null;
    }

    @Deprecated
    public boolean hasPaymentType() {
        return getPaymentType() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasStudentNumber() {
        return getStudentNumber() != null;
    }

    @Deprecated
    public boolean hasSibsPaymentFile() {
        return getSibsPaymentFile() != null;
    }

    @Deprecated
    public boolean hasPayedValue() {
        return getPayedValue() != null;
    }

    @Deprecated
    public boolean hasTransactionDateDateTime() {
        return getTransactionDateDateTime() != null;
    }

    @Deprecated
    public boolean hasPaymentStatus() {
        return getPaymentStatus() != null;
    }

}
