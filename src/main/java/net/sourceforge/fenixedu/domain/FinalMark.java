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

public class FinalMark extends FinalMark_Base {

    public FinalMark() {
        super();
        setGradeListVersion(0);
    }

    public FinalMark(final Attends attends, final FinalEvaluation finalEvaluation, final String markValue) {
        this();
        setAttend(attends);
        setEvaluation(finalEvaluation);
        setMark(markValue);
    }

    public void setFinalEvaluation(FinalEvaluation finalEvaluation) {
        setEvaluation(finalEvaluation);
    }

    public FinalEvaluation getFinalEvaluation() {
        return (FinalEvaluation) getEvaluation();
    }

    @Deprecated
    public java.util.Date getSubmitDate() {
        org.joda.time.YearMonthDay ymd = getSubmitDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setSubmitDate(java.util.Date date) {
        if (date == null) {
            setSubmitDateYearMonthDay(null);
        } else {
            setSubmitDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getWhenSubmited() {
        org.joda.time.YearMonthDay ymd = getWhenSubmitedYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setWhenSubmited(java.util.Date date) {
        if (date == null) {
            setWhenSubmitedYearMonthDay(null);
        } else {
            setWhenSubmitedYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public boolean hasSubmitDateYearMonthDay() {
        return getSubmitDateYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasSubmitedMark() {
        return getSubmitedMark() != null;
    }

    @Deprecated
    public boolean hasWhenSubmitedYearMonthDay() {
        return getWhenSubmitedYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasGradeListVersion() {
        return getGradeListVersion() != null;
    }

}
