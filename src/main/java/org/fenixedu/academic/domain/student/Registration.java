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
package org.fenixedu.academic.domain.student;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.FenixEduAcademicConfiguration;
import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.IEnrolment;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOfficeType;
import org.fenixedu.academic.domain.candidacy.CandidacySituationType;
import org.fenixedu.academic.domain.candidacy.IngressionType;
import org.fenixedu.academic.domain.candidacy.PersonalInformationBean;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CycleCourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.groups.PermissionService;
import org.fenixedu.academic.domain.log.CurriculumLineLog;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.student.curriculum.ConclusionProcess;
import org.fenixedu.academic.domain.student.curriculum.Curriculum;
import org.fenixedu.academic.domain.student.curriculum.ICurriculum;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationState;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.Dismissal;
import org.fenixedu.academic.domain.studentCurriculum.ExternalEnrolment;
import org.fenixedu.academic.domain.studentCurriculum.StandaloneCurriculumGroup;
import org.fenixedu.academic.domain.treasury.TreasuryBridgeAPIFactory;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.signals.DomainObjectEvent;
import org.fenixedu.bennu.core.signals.Signal;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import pt.ist.fenixframework.Atomic;

public class Registration extends Registration_Base {

    public static final String REGISTRATION_CREATE_SIGNAL = "academic.registration.create";

    private static final Logger logger = LoggerFactory.getLogger(Registration.class);

    @Deprecated
    static private final java.util.function.Predicate<DegreeType> DEGREE_TYPES_TO_ENROL_BY_STUDENT =
            DegreeType.oneOf(DegreeType::isBolonhaDegree, DegreeType::isIntegratedMasterDegree, DegreeType::isBolonhaMasterDegree,
                    DegreeType::isAdvancedSpecializationDiploma);

    static final public Comparator<Registration> NUMBER_COMPARATOR = new Comparator<Registration>() {
        @Override
        public int compare(final Registration o1, final Registration o2) {
            return o1.getNumber().compareTo(o2.getNumber());
        }
    };

    static final public Comparator<Registration> COMPARATOR_BY_START_DATE = new Comparator<Registration>() {
        @Override
        public int compare(final Registration o1, final Registration o2) {
            final int comparationResult = o1.getStartDate().compareTo(o2.getStartDate());
            return comparationResult == 0 ? o1.getExternalId().compareTo(o2.getExternalId()) : comparationResult;
        }
    };

    static public final ComparatorChain COMPARATOR_BY_NUMBER_AND_ID = new ComparatorChain();
    static {
        COMPARATOR_BY_NUMBER_AND_ID.addComparator(NUMBER_COMPARATOR);
        COMPARATOR_BY_NUMBER_AND_ID.addComparator(DomainObjectUtil.COMPARATOR_BY_ID);
    }

    private Registration() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setRegistrationProtocol(RegistrationProtocol.getDefault());

        //Emit the Signal
        Signal.emit(REGISTRATION_CREATE_SIGNAL, new DomainObjectEvent<>(this));
    }

    private Registration(

            final Person person, final Integer registrationNumber, final Degree degree, final ExecutionYear executionYear) {
        this();

        if (executionYear == null) {
            throw new DomainException("error.creation.Registration.executionYearIsRequired");
        }

        final DateTime now = new DateTime();

        setStudent(person.getStudent() != null ? person.getStudent() : new Student(person, registrationNumber));
        setNumber(registrationNumber == null ? getStudent().getNumber() : registrationNumber);
        setStartDate(now.toYearMonthDay());
        setDegree(degree);
        setRegistrationYear(executionYear);
        RegistrationState.createRegistrationState(this, AccessControl.getPerson(), now, RegistrationStateType.REGISTERED,
                executionYear.getFirstExecutionPeriod());
    }

    @Deprecated
    public static Registration createRegistrationWithCustomStudentNumber(final Person person,
            final DegreeCurricularPlan degreeCurricularPlan, final StudentCandidacy studentCandidacy,
            final RegistrationProtocol protocol, final CycleType cycleType, final ExecutionYear executionYear,
            final Integer studentNumber) {

        final Degree degree = degreeCurricularPlan == null ? null : degreeCurricularPlan.getDegree();
        Registration registration = new Registration(person, studentNumber, degree, executionYear);
        registration.setRegistrationProtocol(protocol == null ? RegistrationProtocol.getDefault() : protocol);
        registration.createStudentCurricularPlan(degreeCurricularPlan, executionYear, cycleType);
        registration.setStudentCandidacyInformation(studentCandidacy);

        TreasuryBridgeAPIFactory.implementation().createCustomerIfMissing(registration.getStudent().getPerson());
        return registration;
    }

    public static Registration create(final Student student, final DegreeCurricularPlan degreeCurricularPlan,
            final ExecutionYear executionYear, final RegistrationProtocol protocol, final IngressionType ingressionType) {

        if (student == null) {
            throw new DomainException("error.Registration.student.cannot.be.null");
        }

        if (degreeCurricularPlan == null) {
            throw new DomainException("error.Registration.degreeCurricularPlan.cannot.be.null");
        }

        if (protocol == null) {
            throw new DomainException("error.Registration.registrationProtocol.cannot.be.null");
        }

        if (ingressionType == null) {
            throw new DomainException("error.Registration.ingressionType.cannot.be.null");
        }

        //TODO: remove entryGrade, entryPhase and ingressionType from StudentCandidacy
        final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
        if (executionDegree == null) {
            throw new DomainException("error.Registration.execution.degree.for.year.was.not.found");
        }

        final Registration result =
                new Registration(student.getPerson(), (Integer) null, degreeCurricularPlan.getDegree(), executionYear);
        result.setRegistrationProtocol(protocol);
        result.setIngressionType(ingressionType);

        result.createStudentCurricularPlan(degreeCurricularPlan, executionYear, (CycleType) null);

        final StudentCandidacy studentCandidacy = new StudentCandidacy(student.getPerson(), executionDegree);
        studentCandidacy.setState(CandidacySituationType.REGISTERED);
        studentCandidacy.setIngressionType(ingressionType);
        result.setStudentCandidacyInformation(studentCandidacy);

        //TODO: clean personal ingression data
        final PersonalIngressionData ingressionData = result.getStudent().getPersonalIngressionDataByExecutionYear(executionYear);
        if (ingressionData == null) {
            new PersonalIngressionData(result.getStudent(), executionYear/*, studentCandidacy.getPrecedentDegreeInformation()*/);
//        } else {
//            studentCandidacy.getPrecedentDegreeInformation().setPersonalIngressionData(ingressionData);
        }

        studentCandidacy.getPrecedentDegreeInformation().setRegistration(result);

        TreasuryBridgeAPIFactory.implementation().createCustomerIfMissing(result.getStudent().getPerson());

        return result;
    }

    @Deprecated
    public static Registration create(final Person person, final DegreeCurricularPlan degreeCurricularPlan,
            final StudentCandidacy studentCandidacy, final RegistrationProtocol protocol, final CycleType cycleType,
            final ExecutionYear executionYear) {
        Registration registration =
                importRegistration(person, degreeCurricularPlan, studentCandidacy, protocol, cycleType, executionYear);

        TreasuryBridgeAPIFactory.implementation().createCustomerIfMissing(registration.getStudent().getPerson());

        return registration;
    }

    @Deprecated
    public static Registration importRegistration(final Person person, final DegreeCurricularPlan degreeCurricularPlan,
            final StudentCandidacy studentCandidacy, final RegistrationProtocol protocol, final CycleType cycleType,
            final ExecutionYear executionYear) {
        final Registration registration = new Registration(person, null,
                degreeCurricularPlan != null ? degreeCurricularPlan.getDegree() : null, executionYear);
        registration.setRegistrationProtocol(protocol == null ? RegistrationProtocol.getDefault() : protocol);
        registration.createStudentCurricularPlan(degreeCurricularPlan, executionYear, cycleType);
        registration.setStudentCandidacyInformation(studentCandidacy);

        return registration;
    }

    /**
     * @deprecated use {@link StudentCandidacy#getPrecedentDegreeInformation()}
     */
    @Deprecated
    @Override
    public Set<PrecedentDegreeInformation> getPrecedentDegreesInformationsSet() {
        return super.getPrecedentDegreesInformationsSet();
    }

    /**
     * @deprecated use {@link StudentCandidacy#getPrecedentDegreeInformation()}
     */
    @Deprecated
    @Override
    public void addPrecedentDegreesInformations(PrecedentDegreeInformation precedentDegreesInformations) {
        super.addPrecedentDegreesInformations(precedentDegreesInformations);
    }

    private void setStudentCandidacyInformation(final StudentCandidacy studentCandidacy) {
        setStudentCandidacy(studentCandidacy);
        if (studentCandidacy != null) {
            super.setEntryPhase(studentCandidacy.getEntryPhase());
            super.setIngressionType(studentCandidacy.getIngressionType());
        }
    }

    public StudentCurricularPlan createStudentCurricularPlan(final DegreeCurricularPlan degreeCurricularPlan,
            final ExecutionYear executionYear) {

        return createStudentCurricularPlan(degreeCurricularPlan, executionYear, (CycleType) null);
    }

    private StudentCurricularPlan createStudentCurricularPlan(final DegreeCurricularPlan degreeCurricularPlan,
            final ExecutionYear executionYear, final CycleType cycleType) {

        final ExecutionInterval executionSInterval = executionYear.getFirstExecutionPeriod();
        final YearMonthDay startDay =
                executionSInterval.isCurrent() ? new YearMonthDay() : executionSInterval.getBeginDateYearMonthDay();

        return StudentCurricularPlan.createBolonhaStudentCurricularPlan(this, degreeCurricularPlan, startDay, executionSInterval,
                cycleType);
    }

    @Override
    final public void setNumber(final Integer number) {
        super.setNumber(number);
        if (number == null && getRegistrationNumber() != null) {
            getRegistrationNumber().delete();
        } else if (number != null) {
            if (getRegistrationNumber() != null) {
                getRegistrationNumber().setNumber(number);
            } else {
                new RegistrationNumber(this);
            }
        }
    }

    @Deprecated
    public void delete() {

        for (; !getRegistrationStatesSet().isEmpty(); getRegistrationStatesSet().iterator().next().delete()) {
            ;
        }
        for (; !getStudentCurricularPlansSet().isEmpty(); getStudentCurricularPlansSet().iterator().next().delete()) {
            ;
        }
        for (; !getAssociatedAttendsSet().isEmpty(); getAssociatedAttendsSet().iterator().next().delete()) {
            ;
        }
        for (; !getExternalEnrolmentsSet().isEmpty(); getExternalEnrolmentsSet().iterator().next().delete()) {
            ;
        }
        for (; !getRegistrationDataByExecutionYearSet().isEmpty(); getRegistrationDataByExecutionYearSet().iterator().next()
                .delete()) {
            ;
        }
        for (; !getRegistrationRegimesSet().isEmpty(); getRegistrationRegimesSet().iterator().next().delete()) {
            ;
        }
        for (; !getCurriculumLineLogsSet().isEmpty(); getCurriculumLineLogsSet().iterator().next().delete()) {
            ;
        }
        for (; !getRegistrationStateLogsSet().isEmpty(); getRegistrationStateLogsSet().iterator().next().delete()) {
            ;
        }
        for (; !getRegistrationStateLogSet().isEmpty(); getRegistrationStateLogSet().iterator().next().delete()) {
            ;
        }

        getPrecedentDegreesInformationsSet().forEach(pd -> pd.delete());

        if (getRegistrationNumber() != null) {
            getRegistrationNumber().delete();
        }
        if (getStudentCandidacy() != null) {
            getStudentCandidacy().delete();
        }

        setSourceRegistration(null);
        setRegistrationYear(null);
        setDegree(null);
        setStudent(null);
        super.setRegistrationProtocol(null);
        super.setIngressionType(null);
        setRootDomainObject(null);

        getDestinyRegistrationsSet().clear();
        getShiftsSet().clear();

        super.deleteDomainObject();
    }

    public StudentCurricularPlan getActiveStudentCurricularPlan() {
        return isActive() ? getLastStudentCurricularPlan() : null;
    }

    public StudentCurricularPlan getLastStudentCurricularPlan() {
        final Set<StudentCurricularPlan> studentCurricularPlans = getStudentCurricularPlansSet();

        if (studentCurricularPlans.isEmpty()) {
            return null;
        }
        return Collections.max(studentCurricularPlans, StudentCurricularPlan.STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_START_DATE);
    }

    public StudentCurricularPlan getFirstStudentCurricularPlan() {
        return !getStudentCurricularPlansSet().isEmpty() ? (StudentCurricularPlan) Collections.min(getStudentCurricularPlansSet(),
                StudentCurricularPlan.STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_START_DATE) : null;
    }

    public List<StudentCurricularPlan> getSortedStudentCurricularPlans() {
        final ArrayList<StudentCurricularPlan> sortedStudentCurricularPlans =
                new ArrayList<>(super.getStudentCurricularPlansSet());
        Collections.sort(sortedStudentCurricularPlans, StudentCurricularPlan.STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_START_DATE);
        return sortedStudentCurricularPlans;
    }

    final public List<StudentCurricularPlan> getStudentCurricularPlansExceptPast() {
        List<StudentCurricularPlan> result = new ArrayList<>();
        for (StudentCurricularPlan studentCurricularPlan : super.getStudentCurricularPlansSet()) {
            if (!studentCurricularPlan.isPast()) {
                result.add(studentCurricularPlan);
            }
        }
        return result;
    }

    public boolean attends(final ExecutionCourse executionCourse) {
        for (final Attends attends : getAssociatedAttendsSet()) {
            if (attends.isFor(executionCourse)) {
                return true;
            }
        }
        return false;
    }

    final public Stream<StudentCurricularPlan> getStudentCurricularPlanStream() {
        return getStudentCurricularPlansSet().stream()
                .sorted(StudentCurricularPlan.STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_START_DATE.reversed());
    }

    public static Boolean getEnrolmentsAllowStudentToChooseAffinityCycle() {
        return FenixEduAcademicConfiguration.getConfiguration().getEnrolmentsAllowStudentToChooseAffinityCycle();
    }

    public static Boolean getEnrolmentsAllowStudentToEnrolInAffinityCycle() {
        return FenixEduAcademicConfiguration.getConfiguration().getEnrolmentsAllowStudentToEnrolInAffinityCycle();
    }

    public static Boolean getEnrolmentsAllowStudentToCreateRegistrationForAffinityCycle() {
        return FenixEduAcademicConfiguration.getConfiguration().getEnrolmentsAllowStudentToCreateRegistrationForAffinityCycle();
    }

    final public ICurriculum getCurriculum() {
        return getCurriculum(new DateTime(), (ExecutionYear) null, (CycleType) null);
    }

    final public ICurriculum getCurriculum(final DateTime when) {
        return getCurriculum(when, (ExecutionYear) null, (CycleType) null);
    }

    final public ICurriculum getCurriculum(final ExecutionYear executionYear) {
        return getCurriculum(new DateTime(), executionYear, (CycleType) null);
    }

    final public ICurriculum getCurriculum(final CycleType cycleType) {
        return getCurriculum(new DateTime(), (ExecutionYear) null, cycleType);
    }

    final public ICurriculum getCurriculum(final ExecutionYear executionYear, final CycleType cycleType) {
        return getCurriculum(new DateTime(), executionYear, cycleType);
    }

    final public ICurriculum getCurriculum(final DateTime when, final ExecutionYear executionYear, final CycleType cycleType) {
        if (getStudentCurricularPlansSet().isEmpty()) {
            return Curriculum.createEmpty(executionYear);
        }

        if (getDegreeType().isBolonhaType()) {

            final StudentCurricularPlan studentCurricularPlan =
                    getStudentCurricularPlansSet().size() == 1 ? getLastStudentCurricularPlan() : getStudentCurricularPlan(
                            executionYear);
            if (studentCurricularPlan == null) {
                return Curriculum.createEmpty(executionYear);
            }

            if (cycleType == null) {
                return studentCurricularPlan.getCurriculum(when, executionYear);
            }

            final CycleCurriculumGroup cycleCurriculumGroup = studentCurricularPlan.getCycle(cycleType);
            if (cycleCurriculumGroup == null) {
                return Curriculum.createEmpty(executionYear);
            }

            return cycleCurriculumGroup.getCurriculum(when, executionYear);

        } else {
            final List<StudentCurricularPlan> sortedSCPs = getSortedStudentCurricularPlans();
            final ListIterator<StudentCurricularPlan> sortedSCPsIterator = sortedSCPs.listIterator(sortedSCPs.size());
            final StudentCurricularPlan lastStudentCurricularPlan = sortedSCPsIterator.previous();

            final ICurriculum curriculum;
            curriculum = lastStudentCurricularPlan.getCurriculum(when, executionYear);

            for (; sortedSCPsIterator.hasPrevious();) {
                final StudentCurricularPlan studentCurricularPlan = sortedSCPsIterator.previous();
                if (executionYear == null || studentCurricularPlan.getStartExecutionYear().isBeforeOrEquals(executionYear)) {
                    ((Curriculum) curriculum).add(studentCurricularPlan.getCurriculum(when, executionYear));
                }
            }

            return curriculum;

        }
    }

    public int getNumberOfCurriculumEntries() {
        return getCurriculum().getCurriculumEntries().size();
    }

    final public Grade getRawGrade() {
        return ProgramConclusion.getConclusionProcess(getLastStudentCurricularPlan()).map(ConclusionProcess::getRawGrade)
                .orElseGet(this::calculateRawGrade);
    }

    final public Grade calculateRawGrade() {
        return getCurriculum().getRawGrade();
    }

    final public BigDecimal getEctsCredits(final ExecutionYear executionYear, final CycleType cycleType) {
        return getCurriculum(executionYear, cycleType).getSumEctsCredits();
    }

    final public Grade getFinalGrade() {
        return ProgramConclusion.getConclusionProcess(getLastStudentCurricularPlan()).map(ConclusionProcess::getFinalGrade)
                .orElse(null);
    }

    final public Grade getFinalGrade(final ProgramConclusion programConclusion) {
        return programConclusion.groupFor(this).map(CurriculumGroup::getFinalGrade).orElse(null);
    }

    final public Collection<CurricularCourse> getCurricularCoursesApprovedByEnrolment() {
        final Collection<CurricularCourse> result = new HashSet<>();

        for (final Enrolment enrolment : getApprovedEnrolments()) {
            result.add(enrolment.getCurricularCourse());
        }

        return result;
    }

    final public Collection<Enrolment> getLatestCurricularCoursesEnrolments(final ExecutionYear executionYear) {
        return getStudentCurricularPlan(executionYear).getLatestCurricularCoursesEnrolments(executionYear);
    }

    final public boolean hasEnrolments(final Enrolment enrolment) {
        if (enrolment == null) {
            return false;
        }

        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.hasEnrolments(enrolment)) {
                return true;
            }
        }

        return false;
    }

    final public boolean hasAnyEnrolments() {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.hasAnyEnrolments()) {
                return true;
            }
        }

        return false;
    }

    final public boolean hasAnyCurriculumLines() {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.hasAnyCurriculumLines()) {
                return true;
            }
        }

        return false;
    }

    final public boolean hasAnyCurriculumLines(final ExecutionYear executionYear) {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.hasAnyCurriculumLines(executionYear)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasAnyCurriculumLines(final ExecutionInterval executionInterval) {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.hasAnyCurriculumLines(executionInterval)) {
                return true;
            }
        }

        return false;
    }

    final public Collection<Enrolment> getEnrolments(final ExecutionYear executionYear) {
        final StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(executionYear);
        return studentCurricularPlan != null ? studentCurricularPlan
                .getEnrolmentsByExecutionYear(executionYear) : Collections.EMPTY_LIST;
    }

    public Collection<Enrolment> getEnrolments(final ExecutionInterval executionInterval) {
        final StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(executionInterval.getExecutionYear());
        return studentCurricularPlan != null ? studentCurricularPlan
                .getEnrolmentsByExecutionPeriod(executionInterval) : Collections.EMPTY_LIST;
    }

    final public Collection<Enrolment> getApprovedEnrolments() {
        final Collection<Enrolment> result = new HashSet<>();

        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            result.addAll(studentCurricularPlan.getAprovedEnrolments());
        }

        return result;
    }

    final public Collection<ExternalEnrolment> getApprovedExternalEnrolments() {
        final Collection<ExternalEnrolment> result = new HashSet<>();
        for (final ExternalEnrolment externalEnrolment : getExternalEnrolmentsSet()) {
            if (externalEnrolment.isApproved()) {
                result.add(externalEnrolment);
            }
        }
        return result;
    }

    final public Collection<CurriculumLine> getExtraCurricularCurriculumLines() {
        final Collection<CurriculumLine> result = new HashSet<>();

        final Collection<StudentCurricularPlan> toInspect =
                isBolonha() ? Collections.singleton(getLastStudentCurricularPlan()) : getStudentCurricularPlansSet();
        for (final StudentCurricularPlan studentCurricularPlan : toInspect) {
            result.addAll(studentCurricularPlan.getExtraCurricularCurriculumLines());
        }

        return result;
    }

    final public Collection<CurriculumLine> getStandaloneCurriculumLines() {
        final Collection<CurriculumLine> result = new HashSet<>();

        final Collection<StudentCurricularPlan> toInspect =
                isBolonha() ? Collections.singleton(getLastStudentCurricularPlan()) : getStudentCurricularPlansSet();
        for (final StudentCurricularPlan studentCurricularPlan : toInspect) {
            result.addAll(studentCurricularPlan.getStandaloneCurriculumLines());
        }

        return result;
    }

    public void assertConclusionDate(final Collection<CurriculumModule> result) {
        for (final CurriculumLine curriculumLine : getApprovedCurriculumLines()) {
            if (curriculumLine.calculateConclusionDate() == null) {
                result.add(curriculumLine);
            }
        }
    }

    final public Collection<Enrolment> getPropaedeuticEnrolments() {
        final Collection<Enrolment> result = new HashSet<>();

        final Collection<StudentCurricularPlan> toInspect =
                isBolonha() ? Collections.singleton(getLastStudentCurricularPlan()) : getStudentCurricularPlansSet();
        for (final StudentCurricularPlan studentCurricularPlan : toInspect) {
            result.addAll(studentCurricularPlan.getPropaedeuticEnrolments());
        }

        return result;
    }

    final public Collection<CurriculumLine> getPropaedeuticCurriculumLines() {
        final Collection<CurriculumLine> result = new HashSet<>();

        final Collection<StudentCurricularPlan> toInspect =
                isBolonha() ? Collections.singleton(getLastStudentCurricularPlan()) : getStudentCurricularPlansSet();
        for (final StudentCurricularPlan studentCurricularPlan : toInspect) {
            result.addAll(studentCurricularPlan.getPropaedeuticCurriculumLines());
        }

        return result;
    }

    public YearMonthDay getLastExternalApprovedEnrolmentEvaluationDate() {

        if (getExternalEnrolmentsSet().isEmpty()) {
            return null;
        }

        ExternalEnrolment externalEnrolment =
                Collections.max(getExternalEnrolmentsSet(), ExternalEnrolment.COMPARATOR_BY_EXECUTION_PERIOD_AND_EVALUATION_DATE);

        return externalEnrolment.getApprovementDate() != null ? externalEnrolment
                .getApprovementDate() : externalEnrolment.getExecutionInterval() != null ? externalEnrolment
                        .getExecutionInterval().getEndDateYearMonthDay() : null;
    }

    final public Collection<CurriculumLine> getApprovedCurriculumLines() {
        if (isBolonha()) {
            return getLastStudentCurricularPlan().getApprovedCurriculumLines();
        } else {
            final Collection<CurriculumLine> result = new HashSet<>();

            for (final StudentCurricularPlan plan : getStudentCurricularPlansSet()) {
                result.addAll(plan.getApprovedCurriculumLines());
            }

            return result;
        }

    }

    final public boolean hasAnyApprovedCurriculumLines() {
        return getLastStudentCurricularPlan().hasAnyApprovedCurriculumLines();
    }

    final public Collection<IEnrolment> getApprovedIEnrolments() {
        final Collection<IEnrolment> result = new HashSet<>();

        for (final CurriculumLine curriculumLine : getApprovedCurriculumLines()) {
            if (curriculumLine.isEnrolment()) {
                result.add((Enrolment) curriculumLine);
            } else if (curriculumLine.isDismissal()) {
                result.addAll(((Dismissal) curriculumLine).getSourceIEnrolments());
            }
        }

        result.addAll(getExternalEnrolmentsSet());

        return result;
    }

    final public boolean hasAnyApprovedEnrolment() {
        return getLastStudentCurricularPlan().hasAnyApprovedEnrolment() || hasAnyExternalApprovedEnrolment();
    }

    final public boolean hasAnyApprovedEnrolments(final ExecutionYear executionYear) {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                if (enrolment.isApproved() && enrolment.getExecutionInterval().getExecutionYear() == executionYear) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasAnyEnroledEnrolments(final ExecutionYear year) {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                if (enrolment.isEnroled() && enrolment.isValid(year)) {
                    return true;
                }
            }
        }
        return false;
    }

    final public boolean hasAnyEnrolmentsIn(final ExecutionYear executionYear) {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                if (enrolment.getExecutionInterval().getExecutionYear() == executionYear) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasAnyEnrolmentsIn(final ExecutionInterval executionInterval) {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                if (enrolment.getExecutionInterval() == executionInterval) {
                    return true;
                }
            }
        }
        return false;
    }

    final public boolean hasAnyStandaloneEnrolmentsIn(final ExecutionYear executionYear) {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            StandaloneCurriculumGroup standaloneCurriculumGroup = studentCurricularPlan.getStandaloneCurriculumGroup();
            if (standaloneCurriculumGroup != null && standaloneCurriculumGroup.hasEnrolment(executionYear)) {
                return true;
            }
        }
        return false;
    }

    final public boolean hasAnyExternalApprovedEnrolment() {
        for (final ExternalEnrolment externalEnrolment : this.getExternalEnrolmentsSet()) {
            if (externalEnrolment.isApproved()) {
                return true;
            }
        }
        return false;
    }

    final public Double getDismissalsEctsCredits() {
        return getLastStudentCurricularPlan().getDismissalsEctsCredits();
    }

    final public boolean getHasExternalEnrolments() {
        return !getExternalEnrolmentsSet().isEmpty();
    }

    final public Stream<ExecutionYear> getEnrolmentsExecutionYearStream() {
        return getStudentCurricularPlansSet().stream().flatMap(scp -> scp.getEnrolmentStream()).map(e -> e.getExecutionYear())
                .distinct();
    }

    /**
     * @deprecated use getEnrolmentsExecutionYearStream instead
     */
    @Deprecated
    final public Collection<ExecutionYear> getEnrolmentsExecutionYears() {
        final Set<ExecutionYear> result = new HashSet<>();

        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                result.add(enrolment.getExecutionInterval().getExecutionYear());
            }
        }
        return result;
    }

    final public int getNumberOfYearsEnrolledUntil(final ExecutionYear executionYear) {
        return Math.toIntExact(getEnrolmentsExecutionYearStream().filter(y -> y.isBeforeOrEquals(executionYear)).count());
    }

    final public SortedSet<ExecutionYear> getSortedEnrolmentsExecutionYears() {
        final Supplier<TreeSet<ExecutionYear>> supplier = () -> new TreeSet<>(ExecutionYear.COMPARATOR_BY_YEAR);
        return getEnrolmentsExecutionYearStream().collect(Collectors.toCollection(supplier));
    }

    public Collection<ExecutionYear> getCurriculumLinesExecutionYears() {
        final Collection<ExecutionYear> result = new ArrayList<>();
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            for (final CurriculumLine curriculumLine : studentCurricularPlan.getAllCurriculumLines()) {
                if (curriculumLine.getExecutionInterval() != null) {
                    result.add(curriculumLine.getExecutionInterval().getExecutionYear());
                }
            }
        }
        return result;
    }

    public SortedSet<ExecutionYear> getSortedCurriculumLinesExecutionYears() {
        final SortedSet<ExecutionYear> result = new TreeSet<>(ExecutionYear.COMPARATOR_BY_YEAR);
        result.addAll(getCurriculumLinesExecutionYears());
        return result;
    }

    public ExecutionYear getFirstCurriculumLineExecutionYear() {
        final SortedSet<ExecutionYear> executionYears = getSortedCurriculumLinesExecutionYears();
        return executionYears.isEmpty() ? null : executionYears.first();
    }

    final public ExecutionYear getLastEnrolmentExecutionYear() {
        SortedSet<ExecutionYear> sorted = getSortedEnrolmentsExecutionYears();
        if (!sorted.isEmpty()) {
            return sorted.last();
        } else {
            return null;
        }
    }

    public ExecutionYear getLastApprovementExecutionYear() {
        if (isBolonha()) {
            return getLastStudentCurricularPlan().getLastApprovementExecutionYear();
        } else {
            ExecutionYear result = null;
            for (final StudentCurricularPlan plan : getStudentCurricularPlansSet()) {
                final ExecutionYear year = plan.getLastApprovementExecutionYear();
                if (year != null && (result == null || result.isBefore(year))) {
                    result = year;
                }
            }

            return result;
        }
    }

    final public Collection<ExecutionInterval> getEnrolmentsExecutionPeriods() {
        final Set<ExecutionInterval> result = new HashSet<>();
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                result.add(enrolment.getExecutionInterval());
            }
        }
        return result;
    }

    final public SortedSet<ExecutionInterval> getSortedEnrolmentsExecutionPeriods() {
        final SortedSet<ExecutionInterval> result = new TreeSet<>(ExecutionInterval.COMPARATOR_BY_BEGIN_DATE);
        result.addAll(getEnrolmentsExecutionPeriods());

        return result;
    }

    final public int countCompletedCoursesForActiveUndergraduateCurricularPlan() {
        return getActiveStudentCurricularPlan().getAprovedEnrolments().size();
    }

    final public List<StudentCurricularPlan> getStudentCurricularPlansByDegree(final Degree degree) {
        final List<StudentCurricularPlan> result = new ArrayList<>();
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.getDegree() == degree) {
                result.add(studentCurricularPlan);
            }
        }
        return result;
    }

    final public StudentCurricularPlan getPastStudentCurricularPlanByDegree(final Degree degree) {
        for (StudentCurricularPlan studentCurricularPlan : this.getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.getDegree() == degree && studentCurricularPlan.isPast()) {
                return studentCurricularPlan;
            }
        }
        return null;
    }

    public List<Attends> readAttendsInCurrentExecutionPeriod() {
        final List<Attends> attends = new ArrayList<>();
        for (final Attends attend : this.getAssociatedAttendsSet()) {
            if (attend.getExecutionCourse().getExecutionInterval().isCurrent()) {
                attends.add(attend);
            }
        }
        return attends;
    }

    public List<Attends> readAttendsByExecutionPeriod(final ExecutionInterval executionInterval) {
        final List<Attends> attends = new ArrayList<>();
        for (final Attends attend : this.getAssociatedAttendsSet()) {
            if (attend.isFor(executionInterval)) {
                attends.add(attend);
            }
        }
        return attends;
    }

    @Deprecated
    final public static Registration readByUsername(final String username) {
        final Person person = Person.readPersonByUsername(username);
        if (person != null) {
            for (final Registration registration : person.getStudentsSet()) {
                return registration;
            }
        }
        return null;
    }

    @Deprecated
    final public static Registration readStudentByNumberAndDegreeType(final Integer number, final DegreeType degreeType) {
        Registration nonActiveRegistration = null;
        for (Registration registration : Bennu.getInstance().getRegistrationsSet()) {
            if (registration.getNumber().intValue() == number.intValue() && registration.getDegreeType().equals(degreeType)) {
                if (registration.isActive()) {
                    return registration;
                }
                nonActiveRegistration = registration;
            }
        }
        return nonActiveRegistration;
    }

    final public static Registration readByNumberAndDegreeCurricularPlan(final Integer number,
            final DegreeCurricularPlan degreeCurricularPlan) {
        Registration nonActiveRegistration = null;
        for (Registration registration : Bennu.getInstance().getRegistrationsSet()) {
            if (registration.getNumber().intValue() == number.intValue()
                    && registration.getDegreeCurricularPlans().contains(degreeCurricularPlan)) {
                if (registration.isActive()) {
                    return registration;
                }
                nonActiveRegistration = registration;
            }
        }
        return nonActiveRegistration;
    }

    final public static Collection<Registration> readRegistrationsByNumberAndDegreeTypes(final Integer number,
            final DegreeType... degreeTypes) {
        List<Registration> result = new ArrayList<>();
        final List<DegreeType> degreeTypesList = Arrays.asList(degreeTypes);
        for (RegistrationNumber registrationNumber : Bennu.getInstance().getRegistrationNumbersSet()) {
            if (registrationNumber.getNumber().intValue() == number.intValue()) {
                final Registration registration = registrationNumber.getRegistration();
                if (degreeTypesList.contains(registration.getDegreeType())) {
                    result.add(registration);
                }
            }
        }
        return result;
    }

    final public static List<Registration> readByNumber(final Integer number) {
        final List<Registration> registrations = new ArrayList<>();
        for (RegistrationNumber registrationNumber : Bennu.getInstance().getRegistrationNumbersSet()) {
            if (registrationNumber.getNumber().intValue() == number.intValue()) {
                registrations.add(registrationNumber.getRegistration());
            }
        }
        return registrations;
    }

    final public static List<Registration> readByNumberAndDegreeType(final Integer number, final DegreeType degreeType) {
        final List<Registration> registrations = new ArrayList<>();
        for (RegistrationNumber registrationNumber : Bennu.getInstance().getRegistrationNumbersSet()) {
            if (registrationNumber.getNumber().intValue() == number.intValue()
                    && registrationNumber.getRegistration().getDegreeType() == degreeType) {
                registrations.add(registrationNumber.getRegistration());
            }
        }
        return registrations;
    }

    final public static List<Registration> readByNumberAndDegreeTypeAndAgreement(final Integer number,
            final DegreeType degreeType, final boolean normalAgreement) {
        final List<Registration> registrations = new ArrayList<>();
        for (RegistrationNumber registrationNumber : Bennu.getInstance().getRegistrationNumbersSet()) {
            if (registrationNumber.getNumber().intValue() == number.intValue()
                    && registrationNumber.getRegistration().getDegreeType() == degreeType && registrationNumber.getRegistration()
                            .getRegistrationProtocol() == RegistrationProtocol.getDefault() == normalAgreement) {
                registrations.add(registrationNumber.getRegistration());
            }
        }
        return registrations;
    }

    final public static List<Registration> readAllStudentsBetweenNumbers(final Integer fromNumber, final Integer toNumber) {
        int fromNumberInt = fromNumber.intValue();
        int toNumberInt = toNumber.intValue();

        final List<Registration> students = new ArrayList<>();
        for (final Registration registration : Bennu.getInstance().getRegistrationsSet()) {
            int studentNumberInt = registration.getNumber().intValue();
            if (studentNumberInt >= fromNumberInt && studentNumberInt <= toNumberInt) {
                students.add(registration);
            }
        }
        return students;
    }

    final public static List<Registration> readRegistrationsByDegreeType(final DegreeType degreeType) {
        final List<Registration> students = new ArrayList<>();
        for (final Registration registration : Bennu.getInstance().getRegistrationsSet()) {
            if (registration.getDegreeType().equals(degreeType)) {
                students.add(registration);
            }
        }
        return students;
    }

    final public Set<ExecutionCourse> getAttendingExecutionCoursesForCurrentExecutionPeriod() {
        final Set<ExecutionCourse> result = new HashSet<>();
        for (final Attends attends : getAssociatedAttendsSet()) {
            if (attends.getExecutionCourse().getExecutionInterval().isCurrent()) {
                result.add(attends.getExecutionCourse());
            }
        }
        return result;
    }

    final public Set<ExecutionCourse> getAttendingExecutionCoursesFor() {
        final Set<ExecutionCourse> result = new HashSet<>();
        for (final Attends attends : getAssociatedAttendsSet()) {
            result.add(attends.getExecutionCourse());
        }
        return result;
    }

    final public List<ExecutionCourse> getAttendingExecutionCoursesFor(final ExecutionInterval executionInterval) {
        final List<ExecutionCourse> result = new ArrayList<>();
        for (final Attends attends : getAssociatedAttendsSet()) {
            if (attends.isFor(executionInterval)) {
                result.add(attends.getExecutionCourse());
            }
        }
        return result;
    }

    final public List<ExecutionCourse> getAttendingExecutionCoursesFor(final ExecutionYear executionYear) {
        final List<ExecutionCourse> result = new ArrayList<>();
        for (final Attends attends : getAssociatedAttendsSet()) {
            if (attends.isFor(executionYear)) {
                result.add(attends.getExecutionCourse());
            }
        }

        return result;
    }

    final public List<Attends> getAttendsForExecutionPeriod(final ExecutionInterval executionInterval) {
        final List<Attends> result = new ArrayList<>();
        for (final Attends attends : getAssociatedAttendsSet()) {
            if (attends.isFor(executionInterval)) {
                result.add(attends);
            }
        }
        return result;
    }

    final public List<Shift> getShiftsForCurrentExecutionPeriod() {
        final List<Shift> result = new ArrayList<>();
        for (final Shift shift : getShiftsSet()) {
            if (shift.getExecutionCourse().getExecutionInterval().isCurrent()) {
                result.add(shift);
            }
        }
        return result;
    }

    final public List<Shift> getShiftsFor(final ExecutionInterval executionInterval) {
        final List<Shift> result = new ArrayList<>();
        for (final Shift shift : getShiftsSet()) {
            if (shift.getExecutionCourse().getExecutionInterval() == executionInterval) {
                result.add(shift);
            }
        }
        return result;
    }

    final public List<Shift> getShiftsFor(final ExecutionCourse executionCourse) {
        final List<Shift> result = new ArrayList<>();
        for (final Shift shift : getShiftsSet()) {
            if (shift.getExecutionCourse() == executionCourse) {
                result.add(shift);
            }
        }
        return result;
    }

    final public Shift getShiftFor(final ExecutionCourse executionCourse, final ShiftType shiftType) {
        for (final Shift shift : getShiftsSet()) {
            if (shift.getExecutionCourse() == executionCourse && shift.hasShiftType(shiftType)) {
                return shift;
            }
        }
        return null;
    }

    private int countNumberOfDistinctExecutionCoursesOfShiftsFor(final ExecutionInterval executionInterval) {
        final Set<ExecutionCourse> result = new HashSet<>();
        for (final Shift shift : getShiftsSet()) {
            if (shift.getExecutionCourse().getExecutionInterval() == executionInterval) {
                result.add(shift.getExecutionCourse());
            }
        }
        return result.size();
    }

    /**
     * @deprecated method is never used... delete it
     */
    @Deprecated
    final public Integer getNumberOfExecutionCoursesWithEnroledShiftsFor(final ExecutionInterval executionInterval) {
        return getAttendingExecutionCoursesFor(executionInterval).size()
                - countNumberOfDistinctExecutionCoursesOfShiftsFor(executionInterval);
    }

    final public Set<SchoolClass> getSchoolClassesToEnrol() {
        final Set<SchoolClass> result = new HashSet<>();
        for (final Attends attends : getAssociatedAttendsSet()) {
            final ExecutionCourse executionCourse = attends.getExecutionCourse();

            if (executionCourse.getExecutionInterval().isCurrent()) {
                result.addAll(getSchoolClassesToEnrolBy(executionCourse));
            }
        }
        return result;
    }

    final public Set<SchoolClass> getSchoolClassesToEnrolBy(final ExecutionCourse executionCourse) {
        StudentCurricularPlan scp = getActiveStudentCurricularPlan();
        Set<SchoolClass> schoolClasses =
                scp != null ? executionCourse.getSchoolClassesBy(scp.getDegreeCurricularPlan()) : new HashSet<SchoolClass>();
        return schoolClasses.isEmpty() ? executionCourse.getSchoolClasses() : schoolClasses;
    }

    public void addAttendsTo(final ExecutionCourse executionCourse) {

        checkIfReachedAttendsLimit();

        if (getStudent().readAttendByExecutionCourse(executionCourse) == null) {
            final Enrolment enrolment =
                    findEnrolment(getActiveStudentCurricularPlan(), executionCourse, executionCourse.getExecutionPeriod());
            if (enrolment != null) {
                enrolment.createAttends(this, executionCourse);
            } else {
                Attends attends = getAttendsForExecutionCourse(executionCourse);
                if (attends != null) {
                    attends.delete();
                }
                new Attends(this, executionCourse);
            }
        }
    }

    private Attends getAttendsForExecutionCourse(final ExecutionCourse executionCourse) {
        List<Attends> attendsInExecutionPeriod = getAttendsForExecutionPeriod(executionCourse.getExecutionPeriod());
        for (Attends attends : attendsInExecutionPeriod) {
            for (CurricularCourse curricularCourse : attends.getExecutionCourse().getAssociatedCurricularCoursesSet()) {
                if (executionCourse.getAssociatedCurricularCoursesSet().contains(curricularCourse)) {
                    return attends;
                }
            }
        }
        return null;
    }

    private Enrolment findEnrolment(final StudentCurricularPlan studentCurricularPlan, final ExecutionCourse executionCourse,
            final ExecutionInterval executionInterval) {
        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            final Enrolment enrolment =
                    studentCurricularPlan.getEnrolmentByCurricularCourseAndExecutionPeriod(curricularCourse, executionInterval);
            if (enrolment != null) {
                return enrolment;
            }
        }
        return null;
    }

    private static final int MAXIMUM_STUDENT_ATTENDS_PER_EXECUTION_PERIOD = 10;

    // ;

    private void checkIfReachedAttendsLimit() {
        final User userView = Authenticate.getUser();
        if (userView == null || !(AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.STUDENT_ENROLMENTS,
                getDegree(), userView.getPerson().getUser())
                || PermissionService.hasAccess("ACADEMIC_OFFICE_ENROLMENTS", getDegree(), userView.getPerson().getUser()))) {
            if (readAttendsInCurrentExecutionPeriod().size() >= MAXIMUM_STUDENT_ATTENDS_PER_EXECUTION_PERIOD) {
                throw new DomainException("error.student.reached.attends.limit",
                        String.valueOf(MAXIMUM_STUDENT_ATTENDS_PER_EXECUTION_PERIOD));
            }
        }
    }

    @Atomic
    final public void removeAttendFor(final ExecutionCourse executionCourse) {
        final Attends attend = readRegistrationAttendByExecutionCourse(executionCourse);
        if (attend != null) {
            checkIfHasEnrolmentFor(attend);
            checkIfHasShiftsFor(executionCourse);
            attend.delete();
        }
    }

    public void checkIfHasEnrolmentFor(final Attends attend) {
        if (attend.getEnrolment() != null) {
            throw new DomainException("errors.student.already.enroled");
        }
    }

    public void checkIfHasShiftsFor(final ExecutionCourse executionCourse) {
        if (!getShiftsFor(executionCourse).isEmpty()) {
            throw new DomainException("errors.student.already.enroled.in.shift");
        }
    }

    @Override
    final public Integer getNumber() {
        return super.getNumber() != null ? super.getNumber() : getStudent().getNumber();
    }

    final public Person getPerson() {
        return getStudent().getPerson();
    }

    final public String getName() {
        return getPerson().getName();
    }

    final public String getEmail() {
        return getPerson().getEmail();
    }

    final public Double getEntryGrade() {
        return getStudentCandidacy() != null ? getStudentCandidacy().getEntryGrade() : null;
    }

    final public void setEntryGrade(final Double entryGrade) {
        if (getStudentCandidacy() != null) {
            getStudentCandidacy().setEntryGrade(entryGrade);
        } else {
            throw new DomainException("error.registration.withou.student.candidacy");
        }
    }

    public ExecutionYear getIngressionYear() {
        return calculateIngressionYear();
    }

    public ExecutionYear calculateIngressionYear() {
        return inspectIngressionYear(this);
    }

    private ExecutionYear inspectIngressionYear(final Registration registration) {
        if (registration.getSourceRegistration() == null) {
            return registration.getStartExecutionYear();
        }

        return inspectIngressionYear(registration.getSourceRegistration());
    }

    public String getContigent() {
        return getStudentCandidacy() != null ? getStudentCandidacy().getContigent() : null;
    }

    public String getDegreeNameWithDegreeCurricularPlanName() {
        final StudentCurricularPlan toAsk = getStudentCurricularPlan(
                getStartExecutionYear()) == null ? getFirstStudentCurricularPlan() : getStudentCurricularPlan(
                        getStartExecutionYear());

        if (toAsk == null) {
            return StringUtils.EMPTY;
        }

        return toAsk.getPresentationName(getStartExecutionYear());
    }

    public String getDegreeNameWithDescription() {
        return getDegree().getPresentationName(getStartExecutionYear());
    }

    public String getDegreeName() {
        return getDegree().getNameFor(getStartExecutionYear()).getContent();
    }

    final public String getDegreeDescription(final ExecutionYear executionYear, ProgramConclusion programConclusion,
            final Locale locale) {
        final StringBuilder res = new StringBuilder();

        final Degree degree = getDegree();
        final DegreeType degreeType = degree.getDegreeType();
        if (programConclusion != null && !programConclusion.isTerminal()
                && !Strings.isNullOrEmpty(programConclusion.getName().getContent(locale))) {
            res.append(programConclusion.getName().getContent(locale));
            res.append(", ").append(BundleUtil.getString(Bundle.ACADEMIC, locale, "label.of.the.male")).append(" ");
        }

        // the degree type description is always given by the program conclusion of a course group matching available degree cycle types
        // if no cycle types available, choose any program conclusion
        if (programConclusion == null) {
            programConclusion = degreeType.getCycleTypes().stream()
                    .map(cycleType -> getLastStudentCurricularPlan().getCycleCourseGroup(cycleType)).filter(Objects::nonNull)
                    .map(CycleCourseGroup::getProgramConclusion).filter(Objects::nonNull).findAny()
                    .orElseGet(() -> ProgramConclusion.conclusionsFor(this).findAny().orElse(null));
        }

        if (!isEmptyDegree() && !degreeType.isEmpty()) {
            res.append(degreeType.getPrefix(locale));
            if (programConclusion != null && !Strings.isNullOrEmpty(programConclusion.getDescription().getContent(locale))) {
                res.append(programConclusion.getDescription().getContent(locale).toUpperCase());
                res.append(" ").append(BundleUtil.getString(Bundle.ACADEMIC, locale, "label.in")).append(" ");
            }
        }

        res.append(degree.getNameFor(executionYear).getContent(locale).toUpperCase());

        return res.toString();
    }

    public String getDegreeCurricularPlanName() {
        return getLastDegreeCurricularPlan().getName();
    }

    @Override
    final public Degree getDegree() {
        return super.getDegree() != null ? super.getDegree() : !getStudentCurricularPlansSet()
                .isEmpty() ? getLastStudentCurricularPlan().getDegree() : null;
    }

    final public DegreeType getDegreeType() {
        return getDegree() == null ? null : getDegree().getDegreeType();
    }

    final public boolean isBolonha() {
        DegreeType degreeType = getDegreeType();
        return degreeType != null && degreeType.isBolonhaType();
    }

    final public boolean isActiveForOffice(final Unit office) {
        return isActive() && isForOffice(office.getAdministrativeOffice());
    }

    @Deprecated
    public boolean isDegreeAdministrativeOffice() {
        return getDegree().getAdministrativeOffice().getAdministrativeOfficeType() == AdministrativeOfficeType.DEGREE;
    }

    final public boolean isForOffice(final AdministrativeOffice administrativeOffice) {
        return getDegree().getAdministrativeOffice().equals(administrativeOffice);
    }

    final public boolean isAllowedToManageRegistration() {
        final Degree degree = getDegree();
        final User user = Authenticate.getUser();
        Set<AcademicProgram> programsManageRegistration = AcademicAccessRule
                .getProgramsAccessibleToFunction(AcademicOperationType.MANAGE_REGISTRATIONS, user).collect(Collectors.toSet());
        programsManageRegistration.addAll(PermissionService.getDegrees("ACADEMIC_OFFICE_REGISTRATION_ACCESS", user));

        Set<AcademicProgram> programsViewFullStudentCurriculum =
                AcademicAccessRule.getProgramsAccessibleToFunction(AcademicOperationType.VIEW_FULL_STUDENT_CURRICULUM, user)
                        .collect(Collectors.toSet());
        programsViewFullStudentCurriculum.addAll(PermissionService.getDegrees("ACADEMIC_OFFICE_REGISTRATION_ACCESS", user));

        return programsManageRegistration.stream().anyMatch(ap -> ap == degree)
                || programsViewFullStudentCurriculum.stream().anyMatch(ap -> ap == degree);
    }

    public boolean isCurricularCourseApproved(final CurricularCourse curricularCourse) {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.isCurricularCourseApproved(curricularCourse)) {
                return true;
            }
        }
        return false;
    }

    public Set<RegistrationStateType> getRegistrationStatesTypes(final ExecutionYear executionYear) {
        final Set<RegistrationStateType> result = new HashSet<>();

        for (final RegistrationState registrationState : getRegistrationStates(executionYear)) {
            result.add(registrationState.getStateType());
        }

        return result;
    }

    public boolean isRegistered(final ExecutionInterval executionInterval) {
        return hasAnyActiveState(executionInterval) || hasAnyEnrolmentsIn(executionInterval);
    }

    public boolean isRegistered(final ExecutionYear executionYear) {
        return hasAnyActiveState(executionYear) || hasAnyEnrolmentsIn(executionYear);
    }

    public RegistrationState getActiveState() {
        return getRegistrationStatesSet().stream()
                .filter(s -> s.getExecutionInterval() != null
                        && (s.getExecutionInterval().getExecutionYear().getAcademicInterval().getStart().isBeforeNow()
                                || s.getExecutionInterval().isCurrent()))
                .max(RegistrationState.EXECUTION_INTERVAL_AND_DATE_COMPARATOR).orElseGet(() -> getLastState());
    }

    private RegistrationState getLastState() {
        return getRegistrationStatesSet().stream().max(RegistrationState.EXECUTION_INTERVAL_AND_DATE_COMPARATOR).orElse(null);
    }

    public RegistrationStateType getLastStateType() {
        final RegistrationState registrationState = getLastState();
        return registrationState == null ? null : registrationState.getStateType();
    }

    public RegistrationStateType getActiveStateType() {
        final RegistrationState activeState = getActiveState();
        return activeState != null ? activeState.getStateType() : RegistrationStateType.REGISTERED;
    }

    public boolean isActive() {
        return getActiveStateType().isActive();
    }

    private boolean hasAnyActiveState(final ExecutionInterval executionInterval) {
        return getRegistrationStates(executionInterval).stream().anyMatch(s -> s.isActive());
    }

    public boolean hasAnyActiveState(final ExecutionYear executionYear) {
        return getRegistrationStates(executionYear).stream().anyMatch(s -> s.isActive());
    }

    public boolean hasActiveLastState(final ExecutionInterval executionInterval) {
        final Set<RegistrationState> states = getRegistrationStates(executionInterval);
        return states.isEmpty() ? false : Collections.max(states, RegistrationState.EXECUTION_INTERVAL_AND_DATE_COMPARATOR)
                .isActive();
    }

    public boolean getInterruptedStudies() {
        return getActiveStateType() == RegistrationStateType.INTERRUPTED;
    }

    public boolean getFlunked() {
        return getActiveStateType() == RegistrationStateType.FLUNKED;
    }

    public boolean isSchoolPartConcluded() {
        return getActiveStateType() == RegistrationStateType.SCHOOLPARTCONCLUDED;
    }

    public boolean isConcluded() {
        return getActiveStateType() == RegistrationStateType.CONCLUDED;
    }

    public boolean isCanceled() {
        return getActiveStateType() == RegistrationStateType.CANCELED;
    }

    public RegistrationState getStateInDate(final DateTime dateTime) {

        List<RegistrationState> sortedRegistrationStates = new ArrayList<>(getRegistrationStatesSet());
        Collections.sort(sortedRegistrationStates, RegistrationState.DATE_COMPARATOR);

        for (ListIterator<RegistrationState> iterator =
                sortedRegistrationStates.listIterator(sortedRegistrationStates.size()); iterator.hasPrevious();) {

            RegistrationState registrationState = iterator.previous();
            if (!dateTime.isBefore(registrationState.getStateDate())) {
                return registrationState;
            }
        }

        return null;
    }

    /**
     * @deprecated use {@link #getStateInDate(DateTime)}
     */
    @Deprecated
    public RegistrationState getStateInDate(final LocalDate localDate) {
        final List<RegistrationState> sortedRegistrationStates = new ArrayList<>(getRegistrationStatesSet());
        Collections.sort(sortedRegistrationStates, RegistrationState.DATE_COMPARATOR);

        for (ListIterator<RegistrationState> iterator =
                sortedRegistrationStates.listIterator(sortedRegistrationStates.size()); iterator.hasPrevious();) {

            RegistrationState registrationState = iterator.previous();
            if (!localDate.isBefore(registrationState.getStateDate().toLocalDate())) {
                return registrationState;
            }
        }

        return null;
    }

    public Set<RegistrationState> getRegistrationStates(final ExecutionYear executionYear) {
        return executionYear.getExecutionPeriodsSet().stream().flatMap(es -> getRegistrationStates(es).stream())
                .collect(Collectors.toSet());
    }

    private Set<RegistrationState> getRegistrationStates(final ExecutionInterval executionInterval) {

        // group states by intervals
        final Map<ExecutionInterval, List<RegistrationState>> map =
                getRegistrationStatesSet().stream().collect(Collectors.groupingBy(RegistrationState::getExecutionInterval));

        ExecutionInterval interval = executionInterval;
        while (interval != null && !map.containsKey(interval)) {
            interval = interval.getPrevious(); // find previous interval with explicit states
        }

        if (interval == null) { // invalid interval
            return Collections.emptySet();

        } else if (interval == executionInterval) { // if found in provided interval, return all states
            return new HashSet<>(map.get(interval));

        } else { // otherwise return only the last one
            return map.get(interval).stream().max(RegistrationState.EXECUTION_INTERVAL_AND_DATE_COMPARATOR).map(Stream::of)
                    .orElseGet(Stream::empty).collect(Collectors.toSet());
        }

    }

    public RegistrationState getFirstRegistrationState() {
        return getRegistrationStatesSet().stream().min(RegistrationState.EXECUTION_INTERVAL_AND_DATE_COMPARATOR).orElse(null);
    }

    //TODO: change to execution interval
    //IMPORTANT: when executinInterval is executionYear (higher space) we must first ensure executionInterval is on same space (executionInterval.getExecutionYear) because we cannot compare intervals in different spaces

    public RegistrationState getLastRegistrationState(final ExecutionYear executionYear) {
        return getRegistrationStatesSet().stream().filter(s -> s.getExecutionYear().isBeforeOrEquals(executionYear))
                .max(RegistrationState.EXECUTION_INTERVAL_AND_DATE_COMPARATOR).orElse(null);
    }

    public boolean hasStateType(final ExecutionYear executionYear, final RegistrationStateType registrationStateType) {
        return getRegistrationStatesTypes(executionYear).contains(registrationStateType);
    }

    final public double getEctsCredits() {
        return calculateCredits();
    }

    public double calculateCredits() {
        return getTotalEctsCredits((ExecutionYear) null).doubleValue();
    }

    final public BigDecimal getTotalEctsCredits(final ExecutionYear executionYear) {
        return getCurriculum(executionYear).getSumEctsCredits();
    }

    public double getEnrolmentsEcts(final ExecutionYear executionYear) {
        return getLastStudentCurricularPlan().getEnrolmentsEctsCredits(executionYear);
    }

    final public int getCurricularYear() {
        return getCurricularYear(ExecutionYear.findCurrent(getDegree().getCalendar()));
    }

    final public int getCurricularYear(final ExecutionYear executionYear) {
        return getCurriculum(executionYear).getCurricularYear();
    }

    final public int getCurricularYear(final DateTime when, final ExecutionYear executionYear) {
        return getCurriculum(when, executionYear, (CycleType) null).getCurricularYear();
    }

    public boolean isRegistrationConclusionProcessed() {
        return getLastStudentCurricularPlan().isConclusionProcessed();
    }

    public boolean isQualifiedToRegistrationConclusionProcess() {
        return isActive() || isConcluded() || isSchoolPartConcluded();
    }

    private boolean isOldMasterDegree() {
        return getDegreeType().isPreBolonhaMasterDegree();
    }

    public YearMonthDay getConclusionDate() {
        return ProgramConclusion.getConclusionProcess(getLastStudentCurricularPlan())
                .map(ConclusionProcess::getConclusionYearMonthDay).orElse(null);
    }

    @Deprecated
    public YearMonthDay getConclusionDateForBolonha() {
        return getConclusionDate();
    }

    final public YearMonthDay getConclusionDate(final CycleType cycleType) {
        if (!getDegreeType().hasAnyCycleTypes()) {
            return getConclusionDate();
        }

        if (!hasConcludedCycle(cycleType)) {
            throw new DomainException("Registration.hasnt.finished.given.cycle");
        }

        final StudentCurricularPlan lastStudentCurricularPlan = getLastStudentCurricularPlan();
        if (lastStudentCurricularPlan == null) {
            throw new DomainException("Registration.has.no.student.curricular.plan");
        }

        return lastStudentCurricularPlan.getConclusionDate(cycleType);
    }

    public YearMonthDay calculateConclusionDate() {
        return getLastStudentCurricularPlan().getLastApprovementDate();
    }

    public YearMonthDay calculateConclusionDate(final CycleType cycleType) {
        if (!getDegreeType().hasAnyCycleTypes()) {
            return calculateConclusionDate();
        }

        if (!hasConcludedCycle(cycleType)) {
            throw new DomainException("Registration.hasnt.finished.given.cycle");
        }

        final StudentCurricularPlan lastStudentCurricularPlan = getLastStudentCurricularPlan();
        if (lastStudentCurricularPlan == null) {
            throw new DomainException("Registration.has.no.student.curricular.plan");
        }

        return lastStudentCurricularPlan.calculateConclusionDate(cycleType);
    }

    final public String getGraduateTitle(final ProgramConclusion programConclusion, final Locale locale) {
        if (programConclusion.isConclusionProcessed(this)) {
            return programConclusion.groupFor(this)
                    .map(cg -> cg.getDegreeModule().getGraduateTitle(cg.getConclusionYear(), locale)).orElse(null);
        }
        throw new DomainException("Registration.hasnt.concluded.requested.cycle");
    }

    final public boolean hasConcludedFirstCycle() {
        return hasConcludedCycle(CycleType.FIRST_CYCLE);
    }

    final public boolean hasConcludedSecondCycle() {
        return hasConcludedCycle(CycleType.SECOND_CYCLE);
    }

    final public boolean hasConcludedCycle(final CycleType cycleType) {
        return getLastStudentCurricularPlan().hasConcludedCycle(cycleType);
    }

    final public boolean hasConcludedCycle(final CycleType cycleType, final ExecutionYear executionYear) {
        return getLastStudentCurricularPlan().hasConcludedCycle(cycleType, executionYear);
    }

    public boolean hasConcluded() {
        final StudentCurricularPlan lastStudentCurricularPlan = getLastStudentCurricularPlan();
        return lastStudentCurricularPlan.isConcluded();
    }

    public boolean getHasConcluded() {
        return hasConcluded();
    }

    final public Collection<CycleType> getConcludedCycles() {
        if (!getDegreeType().hasAnyCycleTypes()) {
            return Collections.EMPTY_SET;
        }

        final Collection<CycleType> result = new TreeSet<>(CycleType.COMPARATOR_BY_LESS_WEIGHT);

        for (final CycleType cycleType : getDegreeType().getCycleTypes()) {
            if (hasConcludedCycle(cycleType)) {
                result.add(cycleType);
            }
        }

        return result;
    }

    final public Collection<CycleCurriculumGroup> getConclusionProcessedCycles(final ExecutionYear executionYear) {
        final Collection<CycleCurriculumGroup> result = new HashSet<>();

        for (final CycleCurriculumGroup group : getLastStudentCurricularPlan().getInternalCycleCurriculumGrops()) {
            if (group.isConclusionProcessed() && group.getConclusionYear() == executionYear) {
                result.add(group);
            }
        }

        return result;
    }

    final public Collection<CycleType> getConcludedCycles(final ExecutionYear executionYear) {
        if (!getDegreeType().hasAnyCycleTypes()) {
            return Collections.emptySet();
        }

        final Collection<CycleType> result = new TreeSet<>(CycleType.COMPARATOR_BY_LESS_WEIGHT);

        for (final CycleType cycleType : getDegreeType().getCycleTypes()) {
            if (hasConcludedCycle(cycleType, executionYear)) {
                result.add(cycleType);
            }
        }

        return result;
    }

    final public CycleType getCycleType(final ExecutionYear executionYear) {
        if (!isBolonha() || isEmptyDegree() || getDegreeType().isEmpty()) {
            return null;
        }

        final SortedSet<CycleType> concludedCycles = new TreeSet<>(getConcludedCycles(executionYear));

        if (concludedCycles.isEmpty()) {
            CycleCurriculumGroup cycleGroup = getLastStudentCurricularPlan().getFirstOrderedCycleCurriculumGroup();
            return cycleGroup != null ? cycleGroup.getCycleType() : null;
        } else {
            CycleType result = null;
            for (CycleType cycleType : concludedCycles) {
                final CycleCurriculumGroup group = getLastStudentCurricularPlan().getCycle(cycleType);
                if (group.hasEnrolment(executionYear)) {
                    result = cycleType;
                }
            }

            if (result != null) {
                return result;
            }

            final CycleType last = concludedCycles.last();
            return last.hasNext() && getDegreeType().hasCycleTypes(last.getNext()) ? last.getNext() : last;
        }
    }

    private boolean isEmptyDegree() {
        return getLastStudentCurricularPlan() != null ? getLastStudentCurricularPlan().isEmpty() : true;
    }

    final public CycleType getLastConcludedCycleType() {
        final SortedSet<CycleType> concludedCycles = new TreeSet<>(getConcludedCycles());
        return concludedCycles.isEmpty() ? null : concludedCycles.last();
    }

    public boolean canRepeatConclusionProcess(final Person person) {
        return AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.REPEAT_CONCLUSION_PROCESS, getDegree(),
                person.getUser())
                || PermissionService.hasAccess("ACADEMIC_OFFICE_CONCLUSION_REPEAT", getDegree(), person.getUser());
    }

    public void conclude(final CurriculumGroup curriculumGroup) {
        if (curriculumGroup == null
                || getStudentCurricularPlansSet().stream().noneMatch(scp -> scp.hasCurriculumModule(curriculumGroup))) {
            throw new DomainException("error.Registration.invalid.cycleCurriculumGroup");
        }

        curriculumGroup.conclude();

        ProgramConclusion conclusion = curriculumGroup.getDegreeModule().getProgramConclusion();

        if (conclusion != null && conclusion.getTargetState() != null
                && !conclusion.getTargetState().equals(getActiveStateType())) {
            final ExecutionYear conclusionYear = curriculumGroup.getConclusionYear();
            RegistrationState.createRegistrationState(this, AccessControl.getPerson(), new DateTime(),
                    conclusion.getTargetState(), conclusionYear != null ? conclusionYear.getLastExecutionPeriod() : null);
        }

    }

    public boolean hasApprovement(final ExecutionYear executionYear) {
        int curricularYearInTheBegin = getCurricularYear(executionYear);
        int curricularYearAtTheEnd = getCurricularYear(executionYear.getNextExecutionYear());

        if (curricularYearInTheBegin > curricularYearAtTheEnd) {
            throw new DomainException("Registration.curricular.year.has.decreased");
        }

        return curricularYearAtTheEnd > curricularYearInTheBegin;
    }

    public boolean isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree() {
        return getDegreeType().isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree();
    }

    public boolean isMasterDegreeOrBolonhaMasterDegree() {
        final DegreeType degreeType = getDegreeType();
        return degreeType.isPreBolonhaMasterDegree() || degreeType.isBolonhaMasterDegree();
    }

    public boolean isDEA() {
        return getDegreeType().isAdvancedSpecializationDiploma();
    }

    private RegistrationDataByExecutionYear getRegistrationDataByExecutionYear(final ExecutionYear year) {
        for (RegistrationDataByExecutionYear registrationData : getRegistrationDataByExecutionYearSet()) {
            if (registrationData.getExecutionYear().equals(year)) {
                return registrationData;
            }
        }
        return null;
    }

    public boolean isFirstTime(final ExecutionYear executionYear) {
        return getRegistrationYear() == executionYear;
    }

    public boolean isFirstTime() {
        return isFirstTime(ExecutionYear.findCurrent(getDegree().getCalendar()));
    }

    public StudentCurricularPlan getStudentCurricularPlan(final ExecutionYear executionYear) {
        return executionYear == null ? getStudentCurricularPlan(new YearMonthDay()) : getStudentCurricularPlan(
                executionYear.getEndDateYearMonthDay());
    }

    public StudentCurricularPlan getStudentCurricularPlan(final ExecutionInterval executionInterval) {
        return executionInterval == null ? getStudentCurricularPlan(new YearMonthDay()) : getStudentCurricularPlan(
                executionInterval.getEndDateYearMonthDay());
    }

    private StudentCurricularPlan getStudentCurricularPlan(final YearMonthDay date) {
        StudentCurricularPlan result = null;
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            final YearMonthDay startDate = studentCurricularPlan.getStartDateYearMonthDay();
            if (!startDate.isAfter(date) && (result == null || startDate.isAfter(result.getStartDateYearMonthDay()))) {
                result = studentCurricularPlan;
            }
        }
        return result;
    }

    public StudentCurricularPlan getStudentCurricularPlan(final DegreeCurricularPlan degreeCurricularPlan) {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.getDegreeCurricularPlan().equals(degreeCurricularPlan)) {
                return studentCurricularPlan;
            }
        }
        return null;
    }

    public StudentCurricularPlan getStudentCurricularPlan(final CycleType cycleType) {
        if (cycleType == null) {
            return getLastStudentCurricularPlan();
        }
        return getStudentCurricularPlansSet().stream().filter(scp -> scp.getRoot().getCycleCurriculumGroup(cycleType) != null)
                .max(StudentCurricularPlan.STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_START_DATE)
                .orElse(getLastStudentCurricularPlan());
    }

    public Set<DegreeCurricularPlan> getDegreeCurricularPlans() {
        Set<DegreeCurricularPlan> result = new HashSet<>();
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            result.add(studentCurricularPlan.getDegreeCurricularPlan());
        }
        return result;
    }

    @Override
    public YearMonthDay getStartDate() {

        if (super.getStartDate() != null) {
            return super.getStartDate();
        }

        if (getRegistrationYear() != null) {
            return getRegistrationYear().getBeginDateYearMonthDay();
        }

        return null;
    }

    /**
     * @deprecated Replaced by {@link #getRegistrationYear()}
     */
    @Deprecated
    final public ExecutionYear getStartExecutionYear() {
        return getRegistrationYear();
    }

    final public boolean hasStudentCurricularPlanInExecutionPeriod(final ExecutionInterval executionInterval) {
        return getStudentCurricularPlan(executionInterval) != null;
    }

    final public DegreeCurricularPlan getActiveDegreeCurricularPlan() {
        return getActiveStudentCurricularPlan() != null ? getActiveStudentCurricularPlan().getDegreeCurricularPlan() : null;
    }

    final public DegreeCurricularPlan getLastDegreeCurricularPlan() {
        return getLastStudentCurricularPlan() != null ? getLastStudentCurricularPlan().getDegreeCurricularPlan() : null;
    }

    public Degree getLastDegree() {
        return getLastDegreeCurricularPlan().getDegree();
    }

    final public boolean isInactive() {
        return getActiveStateType().isInactive();
    }

    public Space getCampus() {
        return getLastStudentCurricularPlan().getLastCampus();
    }

    public Space getCampus(final ExecutionYear executionYear) {
        final StudentCurricularPlan scp = getStudentCurricularPlan(executionYear);
        return scp == null ? getLastStudentCurricularPlan().getCampus(executionYear) : scp.getCampus(executionYear);
    }

    final public String getIstUniversity() {
        return getCampus().getName();
    }

    @Override
    final public void setStudentCandidacy(final StudentCandidacy studentCandidacy) {
        if (getStudentCandidacy() != null) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.student.Registration.studentCandidacy.cannot.be.modified");
        }

        super.setStudentCandidacy(studentCandidacy);
    }

    final public void removeStudentCandidacy() {
        super.setStudentCandidacy(null);
    }

    final public Attends readAttendByExecutionCourse(final ExecutionCourse executionCourse) {
        return getStudent().readAttendByExecutionCourse(executionCourse);
    }

    final public Attends readRegistrationAttendByExecutionCourse(final ExecutionCourse executionCourse) {
        for (final Attends attend : this.getAssociatedAttendsSet()) {
            if (attend.isFor(executionCourse)) {
                return attend;
            }
        }
        return null;
    }

    @Override
    public void setRegistrationProtocol(RegistrationProtocol registrationProtocol) {
        if (registrationProtocol == null) {
            registrationProtocol = RegistrationProtocol.getDefault();
        }
        super.setRegistrationProtocol(registrationProtocol);
    }

    final public Enrolment getDissertationEnrolment() {
        return getDissertationEnrolment(null);
    }

    final public Enrolment getDissertationEnrolment(final DegreeCurricularPlan degreeCurricularPlan) {
        for (StudentCurricularPlan scp : getStudentCurricularPlansSet()) {
            if (degreeCurricularPlan != null && scp.getDegreeCurricularPlan() != degreeCurricularPlan) {
                continue;
            }

            Enrolment enrolment = scp.getLatestDissertationEnrolment();
            if (enrolment != null) {
                return enrolment;
            }
        }

        return null;
    }

    public Set<Enrolment> getDissertationEnrolments(final DegreeCurricularPlan degreeCurricularPlan) {
        final Set<Enrolment> enrolments = new HashSet<>();
        for (StudentCurricularPlan scp : getStudentCurricularPlansSet()) {
            if (degreeCurricularPlan != null && scp.getDegreeCurricularPlan() != degreeCurricularPlan) {
                continue;
            }
            enrolments.addAll(scp.getDissertationEnrolments());
        }
        return enrolments;
    }

    final public ExternalEnrolment findExternalEnrolment(final Unit university, final ExecutionInterval period,
            final String code) {
        for (final ExternalEnrolment externalEnrolment : this.getExternalEnrolmentsSet()) {
            if (externalEnrolment.getExecutionInterval() == period
                    && externalEnrolment.getExternalCurricularCourse().getCode().equals(code)
                    && externalEnrolment.getExternalCurricularCourse().getUnit() == university) {
                return externalEnrolment;
            }
        }
        return null;
    }

    final public SortedSet<ExternalEnrolment> getSortedExternalEnrolments() {
        final SortedSet<ExternalEnrolment> result = new TreeSet<>(ExternalEnrolment.COMPARATOR_BY_NAME);
        result.addAll(getExternalEnrolmentsSet());
        return result;
    }

    public void editStartDates(final ExecutionYear registrationYear, final YearMonthDay startDate,
            final YearMonthDay homologationDate, final YearMonthDay studiesStartDate) {

        setStartDate(startDate);

        // registration year
        if (registrationYear == null) {
            throw new DomainException("error.Registration.invalid.execution.year");
        }
        if (getStartDate().isAfter(registrationYear.getEndLocalDate())) {
            throw new DomainException("error.Registration.startDate.after.registrationYear");
        }
        setRegistrationYear(registrationYear);

        // edit RegistrationState execution interval and date
        final RegistrationState firstRegistrationState = getFirstRegistrationState();
        firstRegistrationState.setStateDate(startDate);
        firstRegistrationState.setExecutionInterval(registrationYear.getFirstExecutionPeriod());
        if (firstRegistrationState != getFirstRegistrationState()) {
            throw new DomainException("error.Registration.startDate.changes.first.registration.state");
        }

        // edit Scp start year
        final StudentCurricularPlan first = getFirstStudentCurricularPlan();
        first.editStart(registrationYear);
        if (first != getFirstStudentCurricularPlan()) {
            throw new DomainException("error.Registration.startDate.changes.first.scp");
        }

        setHomologationDate(homologationDate);
        setStudiesStartDate(studiesStartDate);
    }

    @Override
    public void setStartDate(final YearMonthDay startDate) {
        if (startDate == null) {
            throw new DomainException("error.Registration.null.startDate");
        }
        super.setStartDate(startDate);
    }

    public void setHomologationDate(final LocalDate homologationDate) {
        setHomologationDate(new YearMonthDay(homologationDate));
    }

    public void setStudiesStartDate(final LocalDate studiesStartDate) {
        setStudiesStartDate(new YearMonthDay(studiesStartDate));
    }

    public Collection<CurriculumLineLog> getCurriculumLineLogs(final ExecutionInterval executionInterval) {
        final Collection<CurriculumLineLog> res = new HashSet<>();
        for (final CurriculumLineLog curriculumLineLog : getCurriculumLineLogsSet()) {
            if (curriculumLineLog.isFor(executionInterval)) {
                res.add(curriculumLineLog);
            }
        }
        return res;
    }

    public boolean hasStartedBetween(final ExecutionYear firstExecutionYear, final ExecutionYear finalExecutionYear) {
        return getStartExecutionYear().isAfterOrEquals(firstExecutionYear)
                && getStartExecutionYear().isBeforeOrEquals(finalExecutionYear);
    }

    public boolean hasRegistrationRegime(final ExecutionYear executionYear, final RegistrationRegimeType type) {
        for (final RegistrationRegime regime : getRegistrationRegimesSet()) {
            if (regime.isFor(executionYear) && regime.hasRegime(type)) {
                return true;
            }
        }
        return false;
    }

    public RegistrationRegimeType getRegimeType(final ExecutionYear executionYear) {
        for (final RegistrationRegime regime : getRegistrationRegimesSet()) {
            if (regime.isFor(executionYear)) {
                return regime.getRegimeType();
            }
        }
        // if not specified, use the default regime
        return RegistrationRegimeType.defaultType();
    }

    public boolean isPartialRegime(final ExecutionYear executionYear) {
        return getRegimeType(executionYear) == RegistrationRegimeType.PARTIAL_TIME;
    }

    public boolean isFullRegime(final ExecutionYear executionYear) {
        return getRegimeType(executionYear) == RegistrationRegimeType.FULL_TIME;
    }

    public void changeShifts(final Attends attend, final Registration newRegistration) {
        for (final Shift shift : getShiftsSet()) {
            if (attend.isFor(shift)) {
                shift.removeStudents(this);
                shift.addStudents(newRegistration);
            }
        }
    }

//    public boolean hasMissingPersonalInformationForAcademicService(final ExecutionYear executionYear) {
//        if (getPrecedentDegreeInformation(executionYear) != null
//                && !getPersonalInformationBean(executionYear).isEditableByAcademicService()) {
//            return false;
//        }
//
//        return true;
//    }

    @Atomic
    public void createReingression(final ExecutionYear executionYear, final LocalDate reingressionDate) {
        RegistrationDataByExecutionYear dataByYear =
                RegistrationDataByExecutionYear.getOrCreateRegistrationDataByYear(this, executionYear);
        dataByYear.createReingression(reingressionDate);
    }

    public boolean hasReingression(final ExecutionYear executionYear) {
        RegistrationDataByExecutionYear data = getRegistrationDataByExecutionYear(executionYear);
        if (data != null) {
            return data.isReingression();
        }

        return false;
    }

    public Set<RegistrationDataByExecutionYear> getReingressions() {
        Set<RegistrationDataByExecutionYear> reingressions = new HashSet<>();
        for (RegistrationDataByExecutionYear year : getRegistrationDataByExecutionYearSet()) {
            if (year.isReingression()) {
                reingressions.add(year);
            }
        }
        return reingressions;
    }

    @Atomic
    public void deleteReingression(final ExecutionYear executionYear) {
        RegistrationDataByExecutionYear dataByExecutionYear = getRegistrationDataByExecutionYear(executionYear);

        if (dataByExecutionYear == null || dataByExecutionYear.getExecutionYear() != executionYear) {
            throw new DomainException("error.Registration.reingression.not.marked.in.execution.year");
        }

        dataByExecutionYear.deleteReingression();
    }

//    public PersonalInformationBean getPersonalInformationBean(final ExecutionYear executionYear) {
//        PrecedentDegreeInformation precedentInformation = getPrecedentDegreeInformation(executionYear);
//
//        if (precedentInformation == null) {
//            precedentInformation = getLatestPrecedentDegreeInformation();
//        }
//        if (precedentInformation == null) {
//            return new PersonalInformationBean(this);
//        }
//
//        return new PersonalInformationBean(precedentInformation);
//    }

//    public PrecedentDegreeInformation getPrecedentDegreeInformation(final ExecutionYear executionYear) {
//        for (PrecedentDegreeInformation precedentDegreeInfo : getPrecedentDegreesInformationsSet()) {
//            if (precedentDegreeInfo.getPersonalIngressionData().getExecutionYear().equals(executionYear)) {
//                return precedentDegreeInfo;
//            }
//        }
//        return null;
//    }

//    public PrecedentDegreeInformation getLatestPrecedentDegreeInformation() {
//        TreeSet<PrecedentDegreeInformation> degreeInformations =
//                new TreeSet<>(Collections.reverseOrder(PrecedentDegreeInformation.COMPARATOR_BY_EXECUTION_YEAR));
//        ExecutionYear currentExecutionYear = ExecutionYear.findCurrent(getDegree().getCalendar());
//        for (PrecedentDegreeInformation pdi : getPrecedentDegreesInformationsSet()) {
//            if (!pdi.getExecutionYear().isAfter(currentExecutionYear)) {
//                degreeInformations.add(pdi);
//            }
//        }
//
//        if (degreeInformations.isEmpty()) {
//            return null;
//        }
//        return degreeInformations.iterator().next();
//    }

    public int getNumberEnroledCurricularCoursesInCurrentYear() {
        final StudentCurricularPlan lastStudentCurricularPlan = getLastStudentCurricularPlan();
        if (lastStudentCurricularPlan == null) {
            return 0;
        }
        long result = lastStudentCurricularPlan.getEnrolmentsSet().stream()
                .filter(e -> e.getExecutionInterval().getExecutionYear().isCurrent()).count();
        return Math.toIntExact(result);
    }

    public List<CycleCurriculumGroup> getInternalCycleCurriculumGrops() {
        return getLastStudentCurricularPlan().getInternalCycleCurriculumGrops();
    }

    public Collection<CurriculumGroup> getAllCurriculumGroups() {
        Collection<CurriculumGroup> result = new TreeSet<>(CurriculumGroup.COMPARATOR_BY_NAME_AND_ID);
        for (final StudentCurricularPlan plan : getStudentCurricularPlansSet()) {
            result.addAll(plan.getAllCurriculumGroups());
        }
        return result;
    }

    public Collection<CurriculumGroup> getAllCurriculumGroupsWithoutNoCourseGroupCurriculumGroups() {
        Collection<CurriculumGroup> result = new TreeSet<>(CurriculumGroup.COMPARATOR_BY_NAME_AND_ID);
        for (final StudentCurricularPlan plan : getStudentCurricularPlansSet()) {
            result.addAll(plan.getAllCurriculumGroupsWithoutNoCourseGroupCurriculumGroups());
        }
        return result;
    }

    public void updateEnrolmentDate(final ExecutionYear executionYear) {

        final RegistrationDataByExecutionYear registrationData =
                RegistrationDataByExecutionYear.getOrCreateRegistrationDataByYear(this, executionYear);
        final Collection<Enrolment> executionYearEnrolments = getEnrolments(executionYear);

        if (executionYearEnrolments.isEmpty()) {
            registrationData.setEnrolmentDate(null);

        } else if (registrationData.getEnrolmentDate() == null) {

            final Enrolment firstEnrolment = Collections.min(executionYearEnrolments, new Comparator<Enrolment>() {
                @Override
                public int compare(final Enrolment left, final Enrolment right) {
                    return left.getCreationDateDateTime().compareTo(right.getCreationDateDateTime());
                }
            });

            registrationData.edit(firstEnrolment.getCreationDateDateTime().toLocalDate());
        }
    }

}
