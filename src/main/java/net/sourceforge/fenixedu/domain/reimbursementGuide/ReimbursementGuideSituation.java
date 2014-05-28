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
 * Created on 13/Nov/2003
 *  
 */

package net.sourceforge.fenixedu.domain.reimbursementGuide;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">Jo�o Mota </a> 13/Nov/2003
 * 
 */
public class ReimbursementGuideSituation extends ReimbursementGuideSituation_Base {

    public ReimbursementGuideSituation() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    /**
     * @return
     */
    public Calendar getModificationDate() {
        if (this.getModification() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getModification());
            return result;
        }
        return null;
    }

    /**
     * @param modificationDate
     */
    public void setModificationDate(Calendar modificationDate) {
        if (modificationDate != null) {
            this.setModification(modificationDate.getTime());
        } else {
            this.setModification(null);
        }
    }

    /**
     * @return Returns the officialDate.
     */
    public Calendar getOfficialDate() {
        if (this.getOfficial() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getOfficial());
            return result;
        }
        return null;
    }

    /**
     * @param officialDate
     *            The officialDate to set.
     */
    public void setOfficialDate(Calendar officialDate) {
        if (officialDate != null) {
            this.setOfficial(officialDate.getTime());
        } else {
            this.setOfficial(null);
        }
    }

    public boolean isPayed() {
        return getReimbursementGuideState().equals(ReimbursementGuideState.PAYED);
    }

    @Deprecated
    public java.util.Date getModification() {
        org.joda.time.YearMonthDay ymd = getModificationYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setModification(java.util.Date date) {
        if (date == null) {
            setModificationYearMonthDay(null);
        } else {
            setModificationYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getOfficial() {
        org.joda.time.YearMonthDay ymd = getOfficialYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setOfficial(java.util.Date date) {
        if (date == null) {
            setOfficialYearMonthDay(null);
        } else {
            setOfficialYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public boolean hasReimbursementGuide() {
        return getReimbursementGuide() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasState() {
        return getState() != null;
    }

    @Deprecated
    public boolean hasOfficialYearMonthDay() {
        return getOfficialYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasModificationYearMonthDay() {
        return getModificationYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasReimbursementGuideState() {
        return getReimbursementGuideState() != null;
    }

    @Deprecated
    public boolean hasRemarks() {
        return getRemarks() != null;
    }

    @Deprecated
    public boolean hasEmployee() {
        return getEmployee() != null;
    }

}
