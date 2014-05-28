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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.events.AnnualEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.DfaGratuityEvent;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.candidacy.degree.ShiftDistribution;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYearCE;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

/**
 * Created on 11/Fev/2003
 * 
 * @author Jo�o Mota ciapl Dominio
 * 
 */
public class ExecutionYear extends ExecutionYear_Base implements Comparable<ExecutionYear> {

    static {
        getRelationExecutionPeriodExecutionYear().addListener(new ExecutionPeriodExecutionYearListener());
    }

    static final public Comparator<ExecutionYear> COMPARATOR_BY_YEAR = new Comparator<ExecutionYear>() {
        @Override
        public int compare(ExecutionYear o1, ExecutionYear o2) {
            return o1.getYear().compareTo(o2.getYear());
        }
    };

    static final public Comparator<ExecutionYear> REVERSE_COMPARATOR_BY_YEAR = new Comparator<ExecutionYear>() {
        @Override
        public int compare(ExecutionYear o1, ExecutionYear o2) {
            return -COMPARATOR_BY_YEAR.compare(o1, o2);
        }
    };

    private ExecutionYear() {
        super();
        setRootDomainObjectForExecutionYear(Bennu.getInstance());
    }

    public ExecutionYear(AcademicInterval academicInterval, String year) {
        this();
        setAcademicInterval(academicInterval);
        setBeginDateYearMonthDay(academicInterval.getBeginYearMonthDayWithoutChronology());
        setEndDateYearMonthDay(academicInterval.getEndYearMonthDayWithoutChronology());
        setYear(year);
    }

    public String getYear() {
        return getName();
    }

    public void setYear(String year) {
        if (year == null || StringUtils.isEmpty(year.trim())) {
            throw new DomainException("error.ExecutionYear.empty.year");
        }
        super.setName(year);
    }

    @Override
    public String getQualifiedName() {
        return getName();
    }

    public Collection<ExecutionDegree> getExecutionDegreesByType(final DegreeType degreeType) {
        return CollectionUtils.select(getExecutionDegrees(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                ExecutionDegree executionDegree = (ExecutionDegree) arg0;
                return executionDegree.getDegreeCurricularPlan().getDegreeType() == degreeType;
            }
        });
    }

    public ExecutionYear getNextExecutionYear() {
        AcademicYearCE year = getAcademicInterval().plusYear(1);
        return getExecutionYear(year);
    }

    public ExecutionYear getPreviousExecutionYear() {
        AcademicYearCE year = getAcademicInterval().minusYear(1);
        return getExecutionYear(year);
    }

    public ExecutionYear getPreviousExecutionYear(final Integer previousCivilYears) {
        if (previousCivilYears >= 0) {
            AcademicYearCE year = getAcademicInterval().minusYear(previousCivilYears);
            return getExecutionYear(year);
        }
        return null;
    }

    public boolean hasPreviousExecutionYear() {
        return getPreviousExecutionYear() != null;
    }

    public boolean hasNextExecutionYear() {
        return getNextExecutionYear() != null;
    }

    @Override
    public int compareTo(ExecutionYear object) {
        if (object == null) {
            return 1;
        }
        return getAcademicInterval().getStartDateTimeWithoutChronology().compareTo(
                object.getAcademicInterval().getStartDateTimeWithoutChronology());
    }

    public boolean isAfter(final ExecutionYear executionYear) {
        return this.compareTo(executionYear) > 0;
    }

    public boolean isAfterOrEquals(final ExecutionYear executionYear) {
        return this.compareTo(executionYear) >= 0;
    }

    public boolean isBefore(final ExecutionYear executionYear) {
        return this.compareTo(executionYear) < 0;
    }

    public boolean isBeforeOrEquals(final ExecutionYear executionYear) {
        return this.compareTo(executionYear) <= 0;
    }

    public boolean isInclusivelyBetween(final ExecutionYear y1, final ExecutionYear y2) {
        return (isAfterOrEquals(y1) && isBeforeOrEquals(y2));
    }

    public boolean isExclusivelyBetween(final ExecutionYear y1, final ExecutionYear y2) {
        return (isAfter(y1) && isBefore(y2));
    }

    public Collection<ExecutionDegree> getExecutionDegreesSortedByDegreeName() {
        final List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>(getExecutionDegrees());
        Collections.sort(executionDegrees, ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);
        return executionDegrees;
    }

    public ExecutionSemester getExecutionSemesterFor(final Integer semester) {
        for (final ExecutionSemester executionSemester : getExecutionPeriods()) {
            if (executionSemester.isFor(semester)) {
                return executionSemester;
            }
        }
        return null;
    }

    public ExecutionSemester getFirstExecutionPeriod() {
        return Collections.min(this.getExecutionPeriods(), ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR);
    }

    public ExecutionSemester getLastExecutionPeriod() {
        return Collections.max(this.getExecutionPeriods(), ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR);
    }

    public List<ExecutionSemester> readNotClosedPublicExecutionPeriods() {
        final List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();
        for (final ExecutionSemester executionSemester : getExecutionPeriodsSet()) {
            if (!executionSemester.isClosed() && !executionSemester.isNotOpen()) {
                result.add(executionSemester);
            }
        }
        return result;
    }

    public ExecutionSemester readExecutionPeriodByName(final String name) {
        for (final ExecutionSemester executionSemester : getExecutionPeriodsSet()) {
            if (executionSemester.getName().equals(name)) {
                return executionSemester;
            }
        }
        return null;
    }

    public String getNextYearsYearString() {
        final int yearPart1 = Integer.parseInt(getYear().substring(0, 4)) + 1;
        final int yearPart2 = Integer.parseInt(getYear().substring(5, 9)) + 1;
        return Integer.toString(yearPart1) + getYear().charAt(4) + Integer.toString(yearPart2);
    }

    public DegreeInfo getDegreeInfo(final Degree degree) {
        for (final DegreeInfo degreeInfo : getDegreeInfos()) {
            if (degreeInfo.getDegree() == degree) {
                return degreeInfo;
            }
        }
        return null;
    }

    public boolean containsDate(final DateTime dateTime) {
        final DateMidnight begin = getBeginDateYearMonthDay().toDateMidnight();
        final DateMidnight end = getEndDateYearMonthDay().plusDays(1).toDateMidnight();
        return new Interval(begin, end).contains(dateTime);
    }

    public boolean overlapsInterval(final Interval interval) {
        final DateMidnight begin = getBeginDateYearMonthDay().toDateMidnight();
        final DateMidnight end = getEndDateYearMonthDay().plusDays(1).toDateMidnight();
        return new Interval(begin, end).overlaps(interval);
    }

    public boolean containsDate(final LocalDate date) {
        return !getBeginDateYearMonthDay().isAfter(date) && !getEndDateYearMonthDay().isBefore(date);
    }

    public List<ExecutionDegree> getExecutionDegreesFor(final DegreeType... degreeTypes) {
        final List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
        final List<DegreeType> degreeTypesList = Arrays.asList(degreeTypes);
        for (final ExecutionDegree executionDegree : getExecutionDegreesSet()) {
            if (degreeTypesList.contains(executionDegree.getDegreeCurricularPlan().getDegree().getDegreeType())) {
                result.add(executionDegree);
            }
        }
        return result;
    }

    @Override
    public boolean isCurrent() {
        return this.getState().equals(PeriodState.CURRENT);
    }

    public boolean isOpen() {
        return getState().equals(PeriodState.OPEN);
    }

    public boolean isClosed() {
        return getState().equals(PeriodState.CLOSED);
    }

    private boolean isNotOpen() {
        return getState().equals(PeriodState.NOT_OPEN);
    }

    public boolean isFor(final String year) {
        return getYear().equals(year);
    }

    public ShiftDistribution createShiftDistribution() {
        return new ShiftDistribution(this);
    }

    public void delete() {
        if (!getExecutionDegreesSet().isEmpty()) {
            throw new Error("cannot.delete.execution.year.because.execution.degrees.exist");
        }
        if (!getStudentGroupSet().isEmpty()) {
            throw new DomainException("error.executionYear.cannotDeleteExecutionYearUsedInAccessControl");
        }
        if (!getTeacherGroupSet().isEmpty()) {
            throw new DomainException("error.executionYear.cannotDeleteExecutionYearUsedInAccessControl");
        }
        if (!getStudentsConcludedInExecutionYearGroupSet().isEmpty()) {
            throw new DomainException("error.executionYear.cannotDeleteExecutionYearUsedInAccessControl");
        }
        for (; hasAnyExecutionPeriods(); getExecutionPeriodsSet().iterator().next().delete()) {
            ;
        }

        setRootDomainObject(null);
        setRootDomainObjectForExecutionYear(null);
        deleteDomainObject();
    }

    public boolean belongsToCivilYear(int civilYear) {
        return (getBeginCivilYear() == civilYear || getEndCivilYear() == civilYear);
    }

    public int getBeginCivilYear() {
        return getBeginDateYearMonthDay().getYear();
    }

    public int getEndCivilYear() {
        return getEndDateYearMonthDay().getYear();
    }

    public boolean belongsToCivilYearInterval(int beginCivilYear, int endCivilYear) {
        for (int year = beginCivilYear; year <= endCivilYear; year++) {
            if (belongsToCivilYear(year)) {
                return true;
            }
        }
        return false;
    }

    public boolean isBeforeCivilYear(int civilYear) {
        return getEndCivilYear() < civilYear;
    }

    public boolean isAfterCivilYear(int civilYear) {
        return getBeginCivilYear() > civilYear;
    }

    public int getDistanceInCivilYears(final ExecutionYear executionYear) {
        if (executionYear == null || executionYear == this) {
            return 0;
        }

        return Math.abs(getBeginCivilYear() - executionYear.getBeginCivilYear());
    }

    public Collection<DegreeCurricularPlan> getDegreeCurricularPlans() {
        final Collection<DegreeCurricularPlan> result = new HashSet<DegreeCurricularPlan>();
        for (final ExecutionDegree executionDegree : getExecutionDegreesSet()) {
            result.add(executionDegree.getDegreeCurricularPlan());
        }
        return result;
    }

    private Set<AccountingTransaction> getPaymentsFor(final Class<? extends AnnualEvent> eventClass) {
        final Set<AccountingTransaction> result = new HashSet<AccountingTransaction>();
        for (final AnnualEvent each : getAnnualEvents()) {
            if (eventClass.equals(each.getClass()) && !each.isCancelled()) {
                result.addAll(each.getNonAdjustingTransactions());
            }
        }

        return result;
    }

    public Set<AccountingTransaction> getDFAGratuityPayments() {
        return getPaymentsFor(DfaGratuityEvent.class);
    }

    public List<StudentCandidacy> getStudentCandidacies() {
        final List<StudentCandidacy> result = new ArrayList<StudentCandidacy>();
        for (final ExecutionDegree executionDegree : getExecutionDegreesSet()) {
            result.addAll(executionDegree.getStudentCandidacies());
        }
        return result;
    }

    private static class ExecutionPeriodExecutionYearListener extends RelationAdapter<ExecutionYear, ExecutionSemester> {
        @Override
        public void beforeAdd(ExecutionYear executionYear, ExecutionSemester executionSemester) {
            if (executionYear != null && executionSemester != null && executionYear.getExecutionPeriodsSet().size() == 2) {
                throw new DomainException("error.ExecutionYear.exceeded.number.of.executionPeriods", executionYear.getYear());
            }
        }
    }

    public ExecutionSemester getExecutionSemester(final YearMonthDay date) {
        for (final ExecutionSemester semester : getExecutionPeriods()) {
            if (semester.containsDay(date)) {
                return semester;
            }
        }
        return null;
    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------

    public static ExecutionYear getExecutionYear(AcademicYearCE entry) {
        if (entry != null) {
            entry = (AcademicYearCE) entry.getOriginalTemplateEntry();
            for (final ExecutionYear executionYear : Bennu.getInstance().getExecutionYearsSet()) {
                if (executionYear.getAcademicInterval().getAcademicCalendarEntry().equals(entry)) {
                    return executionYear;
                }
            }
        }
        return null;
    }

    static public ExecutionYear readCurrentExecutionYear() {
        ExecutionSemester semester = ExecutionSemester.readActualExecutionSemester();
        if (semester != null) {
            return semester.getExecutionYear();
        } else {
            return null;
        }
    }

    static transient private ExecutionYear startExecutionYearForOptionalCurricularCoursesWithLessTenEnrolments = null;

    static private ExecutionYear readFromProperties(ExecutionYear executionYear, String yearString) {
        if (executionYear == null || executionYear.getRootDomainObject() != Bennu.getInstance()) {
            if (yearString == null || yearString.length() == 0) {
                executionYear = null;
            } else {
                executionYear = readExecutionYearByName(yearString);
            }
        }

        return executionYear;
    }

    public static ExecutionYear readStartExecutionYearForOptionalCurricularCoursesWithLessTenEnrolments() {
        startExecutionYearForOptionalCurricularCoursesWithLessTenEnrolments =
                readFromProperties(startExecutionYearForOptionalCurricularCoursesWithLessTenEnrolments, FenixConfigurationManager
                        .getConfiguration().getStartExecutionYearForAllOptionalCurricularCoursesWithLessTenEnrolments());
        return startExecutionYearForOptionalCurricularCoursesWithLessTenEnrolments;
    }

    static public List<ExecutionYear> readOpenExecutionYears() {
        final List<ExecutionYear> result = new ArrayList<ExecutionYear>();
        for (final ExecutionYear executionYear : Bennu.getInstance().getExecutionYearsSet()) {
            if (executionYear.isOpen()) {
                result.add(executionYear);
            }
        }
        return result;
    }

    static public List<ExecutionYear> readNotClosedExecutionYears() {
        final List<ExecutionYear> result = new ArrayList<ExecutionYear>();
        for (final ExecutionYear executionYear : Bennu.getInstance().getExecutionYearsSet()) {
            if (!executionYear.isClosed()) {
                result.add(executionYear);
            }
        }
        return result;
    }

    public static List<ExecutionYear> readNotOpenExecutionYears() {
        final List<ExecutionYear> result = new ArrayList<ExecutionYear>();
        for (final ExecutionYear executionYear : Bennu.getInstance().getExecutionYearsSet()) {
            if (executionYear.isNotOpen()) {
                result.add(executionYear);
            }
        }
        return result;
    }

    static public List<ExecutionYear> readExecutionYears(ExecutionYear startYear, ExecutionYear endYear) {
        final List<ExecutionYear> result = new ArrayList<ExecutionYear>();
        result.add(startYear);

        ExecutionYear year = startYear.getNextExecutionYear();
        while (year != null && year.isBeforeOrEquals(endYear)) {
            result.add(year);
            year = year.getNextExecutionYear();
        }
        return result;
    }

    static public ExecutionYear readExecutionYearByName(final String year) {
        for (final ExecutionYear executionYear : Bennu.getInstance().getExecutionYearsSet()) {
            if (executionYear.isFor(year)) {
                return executionYear;
            }
        }
        return null;
    }

    static public ExecutionYear readFirstBolonhaExecutionYear() {
        return ExecutionSemester.readFirstBolonhaExecutionPeriod().getExecutionYear();
    }

    public static class ExecutionYearSearchCache {
        private final Map<Integer, Set<ExecutionYear>> map = new HashMap<Integer, Set<ExecutionYear>>();

        private Set<ExecutionYear> updateIfNeeded(final Integer year) {
            Set<ExecutionYear> result = map.get(year);

            // for a given civil year, a maximum of two ExecutionYear can be indexed => must update cache if only one ExecutionYear is cached 
            if (result == null || result.size() < 2) {
                for (final ExecutionYear executionYear : Bennu.getInstance().getExecutionYearsSet()) {
                    add(executionYear);
                }
                result = map.get(year);
            }

            return result;
        }

        public ExecutionYear findByDateTime(final DateTime dateTime) {
            final Integer year = Integer.valueOf(dateTime.getYear());
            final Set<ExecutionYear> executionYears = updateIfNeeded(year);
            if (executionYears != null) {
                for (final ExecutionYear executionYear : executionYears) {
                    if (executionYear.containsDate(dateTime)) {
                        return executionYear;
                    }
                }
            }
            return null;
        }

        public ExecutionYear findByPartial(final Partial partial) {
            final Integer year = Integer.valueOf(partial.get(DateTimeFieldType.year()));
            final Set<ExecutionYear> executionYears = updateIfNeeded(year);
            if (executionYears != null) {
                for (final ExecutionYear executionYear : executionYears) {
                    if (executionYear.getBeginDateYearMonthDay().getYear() == year) {
                        return executionYear;
                    }
                }
            }
            return null;
        }

        private void add(final ExecutionYear executionYear) {
            final Integer year1 = executionYear.getBeginDateYearMonthDay().getYear();
            final Integer year2 = executionYear.getEndDateYearMonthDay().getYear();
            add(executionYear, year1);
            add(executionYear, year2);
        }

        private void add(final ExecutionYear executionYear, final Integer year) {
            Set<ExecutionYear> executionYears = map.get(year);
            if (executionYears == null) {
                executionYears = new HashSet<ExecutionYear>();
                map.put(year, executionYears);
            }
            executionYears.add(executionYear);
        }
    }

    private static final ExecutionYearSearchCache executionYearSearchCache = new ExecutionYearSearchCache();

    static public ExecutionYear readByDateTime(final DateTime dateTime) {
        return executionYearSearchCache.findByDateTime(dateTime);
    }

    static public ExecutionYear readByDateTime(final LocalDate localDate) {
        return executionYearSearchCache.findByDateTime(localDate.toDateTimeAtCurrentTime());
    }

    static public ExecutionYear readByPartial(final Partial partial) {
        return executionYearSearchCache.findByPartial(partial);
    }

    public static ExecutionYear readBy(final YearMonthDay begin, YearMonthDay end) {
        for (final ExecutionYear executionYear : Bennu.getInstance().getExecutionYearsSet()) {
            if (executionYear.getBeginDateYearMonthDay().isEqual(begin) && executionYear.getEndDateYearMonthDay().isEqual(end)) {
                return executionYear;
            }
        }
        return null;
    }

    static public ExecutionYear getExecutionYearByDate(final YearMonthDay date) {
        return readByDateTime(date.toDateTimeAtMidnight());
    }

    static public List<ExecutionYear> readExecutionYearsByCivilYear(int civilYear) {
        final List<ExecutionYear> result = new ArrayList<ExecutionYear>();

        for (final ExecutionYear executionYear : executionYearSearchCache.map.get(civilYear)) {
            if (executionYear.belongsToCivilYear(civilYear)) {
                result.add(executionYear);
            }
        }

        return result;

    }

    static public ExecutionYear readFirstExecutionYear() {
        for (final ExecutionYear executionYear : Bennu.getInstance().getExecutionYearsSet()) {
            if (!executionYear.hasPreviousExecutionYear()) {
                return executionYear;
            }
        }
        return null;
    }

    static public ExecutionYear readLastExecutionYear() {
        for (final ExecutionYear executionYear : Bennu.getInstance().getExecutionYearsSet()) {
            if (!executionYear.hasNextExecutionYear()) {
                return executionYear;
            }
        }
        return null;
    }

    public static ExecutionYear readByAcademicInterval(AcademicInterval academicInterval) {
        for (final ExecutionYear executionYear : Bennu.getInstance().getExecutionYearsSet()) {
            if (executionYear.getAcademicInterval().equals(academicInterval)) {
                return executionYear;
            }
        }
        return null;
    }

    public ExecutionDegree getExecutionDegreeByAcronym(String acronym) {
        for (ExecutionDegree executionDegree : getExecutionDegrees()) {
            if (executionDegree.getDegree().getSigla().equals(acronym)) {
                return executionDegree;
            }
        }
        return null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.curriculum.ConclusionProcess> getConclusionProcessesConcluded() {
        return getConclusionProcessesConcludedSet();
    }

    @Deprecated
    public boolean hasAnyConclusionProcessesConcluded() {
        return !getConclusionProcessesConcludedSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.events.export.SIBSOutgoingPaymentFile> getSIBSOutgoingPaymentFiles() {
        return getSIBSOutgoingPaymentFilesSet();
    }

    @Deprecated
    public boolean hasAnySIBSOutgoingPaymentFiles() {
        return !getSIBSOutgoingPaymentFilesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.elections.DelegateElection> getDelegateElections() {
        return getDelegateElectionsSet();
    }

    @Deprecated
    public boolean hasAnyDelegateElections() {
        return !getDelegateElectionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.Formation> getCandidacyFormations() {
        return getCandidacyFormationsSet();
    }

    @Deprecated
    public boolean hasAnyCandidacyFormations() {
        return !getCandidacyFormationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.reports.GepReportFile> getGepReportFile() {
        return getGepReportFileSet();
    }

    @Deprecated
    public boolean hasAnyGepReportFile() {
        return !getGepReportFileSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction> getInsuranceTransactions() {
        return getInsuranceTransactionsSet();
    }

    @Deprecated
    public boolean hasAnyInsuranceTransactions() {
        return !getInsuranceTransactionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.events.AnnualEvent> getAnnualEvents() {
        return getAnnualEventsSet();
    }

    @Deprecated
    public boolean hasAnyAnnualEvents() {
        return !getAnnualEventsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation> getTeacherPersonalExpectations() {
        return getTeacherPersonalExpectationsSet();
    }

    @Deprecated
    public boolean hasAnyTeacherPersonalExpectations() {
        return !getTeacherPersonalExpectationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup> getVigilantGroups() {
        return getVigilantGroupsSet();
    }

    @Deprecated
    public boolean hasAnyVigilantGroups() {
        return !getVigilantGroupsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.events.dfa.DfaRegistrationEvent> getDfaRegistrationEvents() {
        return getDfaRegistrationEventsSet();
    }

    @Deprecated
    public boolean hasAnyDfaRegistrationEvents() {
        return !getDfaRegistrationEventsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExecutionSemester> getExecutionPeriods() {
        return getExecutionPeriodsSet();
    }

    @Deprecated
    public boolean hasAnyExecutionPeriods() {
        return !getExecutionPeriodsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.paymentCodes.MasterDegreeInsurancePaymentCode> getMasterDegreeInsurancePaymentCodes() {
        return getMasterDegreeInsurancePaymentCodesSet();
    }

    @Deprecated
    public boolean hasAnyMasterDegreeInsurancePaymentCodes() {
        return !getMasterDegreeInsurancePaymentCodesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.RegistrationDataByExecutionYear> getRegistrationDataByExecutionYear() {
        return getRegistrationDataByExecutionYearSet();
    }

    @Deprecated
    public boolean hasAnyRegistrationDataByExecutionYear() {
        return !getRegistrationDataByExecutionYearSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.curriculum.ConclusionProcessVersion> getConclusionProcessVersionsConcluded() {
        return getConclusionProcessVersionsConcludedSet();
    }

    @Deprecated
    public boolean hasAnyConclusionProcessVersionsConcluded() {
        return !getConclusionProcessVersionsConcludedSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroupInformation> getCycleCourseGroupInformation() {
        return getCycleCourseGroupInformationSet();
    }

    @Deprecated
    public boolean hasAnyCycleCourseGroupInformation() {
        return !getCycleCourseGroupInformationSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.DegreeLog> getDegreeLogs() {
        return getDegreeLogsSet();
    }

    @Deprecated
    public boolean hasAnyDegreeLogs() {
        return !getDegreeLogsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExpectationEvaluationGroup> getExpectationEvaluationGroups() {
        return getExpectationEvaluationGroupsSet();
    }

    @Deprecated
    public boolean hasAnyExpectationEvaluationGroups() {
        return !getExpectationEvaluationGroupsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.importation.DgesBaseProcess> getDgesBaseProcess() {
        return getDgesBaseProcessSet();
    }

    @Deprecated
    public boolean hasAnyDgesBaseProcess() {
        return !getDgesBaseProcessSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.DegreeInfo> getDegreeInfos() {
        return getDegreeInfosSet();
    }

    @Deprecated
    public boolean hasAnyDegreeInfos() {
        return !getDegreeInfosSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.curriculum.ConclusionProcessVersion> getConclusionProcessVersionsBegined() {
        return getConclusionProcessVersionsBeginedSet();
    }

    @Deprecated
    public boolean hasAnyConclusionProcessVersionsBegined() {
        return !getConclusionProcessVersionsBeginedSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.TeacherPersonalExpectationPeriod> getTeacherPersonalExpectationPeriods() {
        return getTeacherPersonalExpectationPeriodsSet();
    }

    @Deprecated
    public boolean hasAnyTeacherPersonalExpectationPeriods() {
        return !getTeacherPersonalExpectationPeriodsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.Registration> getStudents() {
        return getStudentsSet();
    }

    @Deprecated
    public boolean hasAnyStudents() {
        return !getStudentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator> getExamCoordinators() {
        return getExamCoordinatorsSet();
    }

    @Deprecated
    public boolean hasAnyExamCoordinators() {
        return !getExamCoordinatorsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.alumni.AlumniReportFile> getAlumniReportFiles() {
        return getAlumniReportFilesSet();
    }

    @Deprecated
    public boolean hasAnyAlumniReportFiles() {
        return !getAlumniReportFilesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.report.GratuityReportQueueJob> getGratuityReportQueueJobs() {
        return getGratuityReportQueueJobsSet();
    }

    @Deprecated
    public boolean hasAnyGratuityReportQueueJobs() {
        return !getGratuityReportQueueJobsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.PublicRelationsStudentListQueueJob> getPublicRelationsStudentListQueueJob() {
        return getPublicRelationsStudentListQueueJobSet();
    }

    @Deprecated
    public boolean hasAnyPublicRelationsStudentListQueueJob() {
        return !getPublicRelationsStudentListQueueJobSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacy.MeasurementTest> getMeasurementTests() {
        return getMeasurementTestsSet();
    }

    @Deprecated
    public boolean hasAnyMeasurementTests() {
        return !getMeasurementTestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJob> getEventReportQueueJob() {
        return getEventReportQueueJobSet();
    }

    @Deprecated
    public boolean hasAnyEventReportQueueJob() {
        return !getEventReportQueueJobSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.RegistrationRegime> getRegistrationRegimes() {
        return getRegistrationRegimesSet();
    }

    @Deprecated
    public boolean hasAnyRegistrationRegimes() {
        return !getRegistrationRegimesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess> getPhdIndividualProgramProcesses() {
        return getPhdIndividualProgramProcessesSet();
    }

    @Deprecated
    public boolean hasAnyPhdIndividualProgramProcesses() {
        return !getPhdIndividualProgramProcessesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.DepartmentCreditsPool> getDepartmentCreditsPools() {
        return getDepartmentCreditsPoolsSet();
    }

    @Deprecated
    public boolean hasAnyDepartmentCreditsPools() {
        return !getDepartmentCreditsPoolsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.PersonalIngressionData> getPersonalIngressionsData() {
        return getPersonalIngressionsDataSet();
    }

    @Deprecated
    public boolean hasAnyPersonalIngressionsData() {
        return !getPersonalIngressionsDataSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest> getAcademicServiceRequests() {
        return getAcademicServiceRequestsSet();
    }

    @Deprecated
    public boolean hasAnyAcademicServiceRequests() {
        return !getAcademicServiceRequestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.StudentDataByExecutionYear> getStudentDataByExecutionYear() {
        return getStudentDataByExecutionYearSet();
    }

    @Deprecated
    public boolean hasAnyStudentDataByExecutionYear() {
        return !getStudentDataByExecutionYearSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExecutionDegree> getExecutionDegrees() {
        return getExecutionDegreesSet();
    }

    @Deprecated
    public boolean hasAnyExecutionDegrees() {
        return !getExecutionDegreesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.postingRules.PartialRegistrationRegimeRequestPR> getPartialRegistrationRegimeRequestPostingRules() {
        return getPartialRegistrationRegimeRequestPostingRulesSet();
    }

    @Deprecated
    public boolean hasAnyPartialRegistrationRegimeRequestPostingRules() {
        return !getPartialRegistrationRegimeRequestPostingRulesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.PaymentPlan> getPaymentPlans() {
        return getPaymentPlansSet();
    }

    @Deprecated
    public boolean hasAnyPaymentPlans() {
        return !getPaymentPlansSet().isEmpty();
    }

    @Deprecated
    public boolean hasShiftDistribution() {
        return getShiftDistribution() != null;
    }

    @Deprecated
    public boolean hasAnnualCreditsState() {
        return getAnnualCreditsState() != null;
    }

    @Deprecated
    public boolean hasInsuranceValue() {
        return getInsuranceValue() != null;
    }

    @Deprecated
    public boolean hasBennuForExecutionYear() {
        return getRootDomainObjectForExecutionYear() != null;
    }

}
