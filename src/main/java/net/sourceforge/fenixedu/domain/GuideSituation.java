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
package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.util.State;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class GuideSituation extends GuideSituation_Base {

    public GuideSituation() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public GuideSituation(GuideState situation, String remarks, Date date, Guide guide, State state) {
        this();
        this.setRemarks(remarks);
        this.setGuide(guide);
        this.setSituation(situation);
        this.setDate(date);
        this.setState(state);
    }

    public void delete() {
        setGuide(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public java.util.Date getDate() {
        org.joda.time.YearMonthDay ymd = getDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setDate(java.util.Date date) {
        if (date == null) {
            setDateYearMonthDay(null);
        } else {
            setDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
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
    public boolean hasGuide() {
        return getGuide() != null;
    }

    @Deprecated
    public boolean hasRemarks() {
        return getRemarks() != null;
    }

    @Deprecated
    public boolean hasDateYearMonthDay() {
        return getDateYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasSituation() {
        return getSituation() != null;
    }

}
