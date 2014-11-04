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
/*
 * Created on 12/Nov/2003
 *  
 */
package org.fenixedu.academic.domain.reimbursementGuide;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.academic.util.State;
import org.fenixedu.bennu.core.domain.Bennu;

/**
 * This class contains all the information regarding a Reimbursement Guide. <br/>
 * 
 * @author <a href="mailto:joao.mota@ist.utl.pt">Jo�o Mota </a>
 */
public class ReimbursementGuide extends ReimbursementGuide_Base {

    final static Comparator<ReimbursementGuide> NUMBER_COMPARATOR = new BeanComparator("number");

    public ReimbursementGuide() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    /**
     * @return
     */
    public Calendar getCreationDate() {
        if (this.getCreation() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getCreation());
            return result;
        }
        return null;
    }

    /**
     * @param creationDate
     */
    public void setCreationDate(Calendar creationDate) {
        if (creationDate != null) {
            this.setCreation(creationDate.getTime());
        } else {
            this.setCreation(null);
        }
    }

    public ReimbursementGuideSituation getActiveReimbursementGuideSituation() {
        return (ReimbursementGuideSituation) CollectionUtils.find(getReimbursementGuideSituationsSet(), new Predicate() {
            @Override
            public boolean evaluate(Object obj) {
                ReimbursementGuideSituation situation = (ReimbursementGuideSituation) obj;
                return situation.getState().getState().equals(State.ACTIVE);
            }
        });
    }

    public static Integer generateReimbursementGuideNumber() {
        Collection<ReimbursementGuide> reimbursementGuides = Bennu.getInstance().getReimbursementGuidesSet();

        return (reimbursementGuides.isEmpty()) ? Integer.valueOf(1) : Collections.max(reimbursementGuides, NUMBER_COMPARATOR)
                .getNumber() + 1;
    }

    public boolean isPayed() {
        return getActiveReimbursementGuideSituation().isPayed();
    }

    @Deprecated
    public java.util.Date getCreation() {
        org.joda.time.YearMonthDay ymd = getCreationYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setCreation(java.util.Date date) {
        if (date == null) {
            setCreationYearMonthDay(null);
        } else {
            setCreationYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

}
