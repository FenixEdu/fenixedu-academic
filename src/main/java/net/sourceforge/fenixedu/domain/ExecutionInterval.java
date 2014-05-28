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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeMapping;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;
import net.sourceforge.fenixedu.domain.period.DegreeCandidacyForGraduatedPersonCandidacyPeriod;
import net.sourceforge.fenixedu.domain.period.DegreeChangeCandidacyPeriod;
import net.sourceforge.fenixedu.domain.period.DegreeTransferCandidacyPeriod;
import net.sourceforge.fenixedu.domain.period.MobilityApplicationPeriod;
import net.sourceforge.fenixedu.domain.period.Over23CandidacyPeriod;
import net.sourceforge.fenixedu.domain.period.SecondCycleCandidacyPeriod;
import net.sourceforge.fenixedu.domain.period.StandaloneCandidacyPeriod;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.util.PeriodState;

import org.fenixedu.bennu.core.domain.Bennu;

abstract public class ExecutionInterval extends ExecutionInterval_Base {

    protected ExecutionInterval() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setState(PeriodState.NOT_OPEN);
    }

    @Override
    public void setAcademicInterval(AcademicInterval academicInterval) {
        if (academicInterval == null) {
            throw new DomainException("error.executionInterval.empty.executionInterval");
        }
        super.setAcademicInterval(academicInterval);
    }

    @Override
    public void setState(PeriodState state) {
        if (state == null) {
            throw new DomainException("error.executionInterval.empty.state");
        }
        super.setState(state);
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkDateInterval() {
        return getBeginDateYearMonthDay() != null && getEndDateYearMonthDay() != null
                && getBeginDateYearMonthDay().isBefore(getEndDateYearMonthDay());
    }

    public List<? extends CandidacyPeriod> getCandidacyPeriods(final Class<? extends CandidacyPeriod> clazz) {
        final List<CandidacyPeriod> result = new ArrayList<CandidacyPeriod>();
        for (final CandidacyPeriod candidacyPeriod : getCandidacyPeriods()) {
            if (candidacyPeriod.getClass().equals(clazz)) {
                result.add(candidacyPeriod);
            }
        }
        return result;
    }

    public boolean hasCandidacyPeriods(final Class<? extends CandidacyPeriod> clazz) {
        for (final CandidacyPeriod candidacyPeriod : getCandidacyPeriods()) {
            if (candidacyPeriod.getClass().equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    public Over23CandidacyPeriod getOver23CandidacyPeriod() {
        final List<Over23CandidacyPeriod> candidacyPeriods =
                (List<Over23CandidacyPeriod>) getCandidacyPeriods(Over23CandidacyPeriod.class);
        return candidacyPeriods.isEmpty() ? null : candidacyPeriods.iterator().next();
    }

    public boolean hasOver23CandidacyPeriod() {
        return hasCandidacyPeriods(Over23CandidacyPeriod.class);
    }

    public List<SecondCycleCandidacyPeriod> getSecondCycleCandidacyPeriods() {
        return (List<SecondCycleCandidacyPeriod>) getCandidacyPeriods(SecondCycleCandidacyPeriod.class);
    }

    public List<MobilityApplicationPeriod> getMobilityApplicationPeriods() {
        return (List<MobilityApplicationPeriod>) getCandidacyPeriods(MobilityApplicationPeriod.class);
    }

    public boolean hasAnySecondCycleCandidacyPeriod() {
        return hasCandidacyPeriods(SecondCycleCandidacyPeriod.class);
    }

    public DegreeCandidacyForGraduatedPersonCandidacyPeriod getDegreeCandidacyForGraduatedPersonCandidacyPeriod() {
        final List<DegreeCandidacyForGraduatedPersonCandidacyPeriod> candidacyPeriods =
                (List<DegreeCandidacyForGraduatedPersonCandidacyPeriod>) getCandidacyPeriods(DegreeCandidacyForGraduatedPersonCandidacyPeriod.class);
        return candidacyPeriods.isEmpty() ? null : candidacyPeriods.iterator().next();
    }

    public boolean hasDegreeCandidacyForGraduatedPersonCandidacyPeriod() {
        return hasCandidacyPeriods(DegreeCandidacyForGraduatedPersonCandidacyPeriod.class);
    }

    public DegreeChangeCandidacyPeriod getDegreeChangeCandidacyPeriod() {
        final List<DegreeChangeCandidacyPeriod> candidacyPeriods =
                (List<DegreeChangeCandidacyPeriod>) getCandidacyPeriods(DegreeChangeCandidacyPeriod.class);
        return candidacyPeriods.isEmpty() ? null : candidacyPeriods.iterator().next();
    }

    public boolean hasDegreeChangeCandidacyPeriod() {
        return hasCandidacyPeriods(DegreeChangeCandidacyPeriod.class);
    }

    public DegreeTransferCandidacyPeriod getDegreeTransferCandidacyPeriod() {
        final List<DegreeTransferCandidacyPeriod> candidacyPeriods =
                (List<DegreeTransferCandidacyPeriod>) getCandidacyPeriods(DegreeTransferCandidacyPeriod.class);
        return candidacyPeriods.isEmpty() ? null : candidacyPeriods.iterator().next();
    }

    public boolean hasDegreeTransferCandidacyPeriod() {
        return hasCandidacyPeriods(DegreeTransferCandidacyPeriod.class);
    }

    public List<StandaloneCandidacyPeriod> getStandaloneCandidacyPeriods() {
        return (List<StandaloneCandidacyPeriod>) getCandidacyPeriods(StandaloneCandidacyPeriod.class);
    }

    public boolean hasAnyStandaloneCandidacyPeriod() {
        return hasCandidacyPeriods(StandaloneCandidacyPeriod.class);
    }

    public PaymentCode findNewCodeInPaymentCodeMapping(final PaymentCode oldCode) {
        for (final PaymentCodeMapping mapping : getPaymentCodeMappingsSet()) {
            if (mapping.hasOldPaymentCode(oldCode)) {
                return mapping.getNewPaymentCode();
            }
        }
        return null;
    }

    abstract public String getQualifiedName();

    abstract public boolean isCurrent();

    // static information

    static public List<ExecutionInterval> readExecutionIntervalsWithCandidacyPeriod(final Class<? extends CandidacyPeriod> clazz) {
        final List<ExecutionInterval> result = new ArrayList<ExecutionInterval>();
        for (final ExecutionInterval executionInterval : Bennu.getInstance().getExecutionIntervalsSet()) {
            if (executionInterval.hasCandidacyPeriods(clazz)) {
                result.add(executionInterval);
            }
        }
        return result;
    }

    public static ExecutionInterval getExecutionInterval(String qualifiedName) {
        for (ExecutionInterval interval : Bennu.getInstance().getExecutionIntervalsSet()) {
            if (interval.getQualifiedName().equals(qualifiedName)) {
                return interval;
            }
        }
        return null;
    }

    public static ExecutionInterval getExecutionInterval(AcademicInterval academicInterval) {
        for (ExecutionInterval interval : Bennu.getInstance().getExecutionIntervalsSet()) {
            if (interval.getAcademicInterval().equals(academicInterval)) {
                return interval;
            }
        }
        return null;
    }

    @Deprecated
    public java.util.Date getBeginDate() {
        org.joda.time.YearMonthDay ymd = getBeginDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setBeginDate(java.util.Date date) {
        if (date == null) {
            setBeginDateYearMonthDay(null);
        } else {
            setBeginDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
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
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction> getPersonFunction() {
        return getPersonFunctionSet();
    }

    @Deprecated
    public boolean hasAnyPersonFunction() {
        return !getPersonFunctionSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.PaymentCodeMapping> getPaymentCodeMappings() {
        return getPaymentCodeMappingsSet();
    }

    @Deprecated
    public boolean hasAnyPaymentCodeMappings() {
        return !getPaymentCodeMappingsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.period.CandidacyPeriod> getCandidacyPeriods() {
        return getCandidacyPeriodsSet();
    }

    @Deprecated
    public boolean hasAnyCandidacyPeriods() {
        return !getCandidacyPeriodsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CoordinatorExecutionDegreeCoursesReport> getExecutionDegreeCoursesReports() {
        return getExecutionDegreeCoursesReportsSet();
    }

    @Deprecated
    public boolean hasAnyExecutionDegreeCoursesReports() {
        return !getExecutionDegreeCoursesReportsSet().isEmpty();
    }

    @Deprecated
    public boolean hasBeginDateYearMonthDay() {
        return getBeginDateYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasAcademicInterval() {
        return getAcademicInterval() != null;
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
    public boolean hasEndDateYearMonthDay() {
        return getEndDateYearMonthDay() != null;
    }

}
