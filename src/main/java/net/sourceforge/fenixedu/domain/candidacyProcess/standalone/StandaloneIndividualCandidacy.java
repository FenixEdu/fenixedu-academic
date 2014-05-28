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
package net.sourceforge.fenixedu.domain.candidacyProcess.standalone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentStandaloneEnrolmentBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EmptyDegree;
import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.StandaloneIndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;
import net.sourceforge.fenixedu.domain.curricularRules.MaximumNumberOfEctsInStandaloneCurriculumGroup;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

public class StandaloneIndividualCandidacy extends StandaloneIndividualCandidacy_Base {

    private StandaloneIndividualCandidacy() {
        super();
    }

    StandaloneIndividualCandidacy(final StandaloneIndividualCandidacyProcess process,
            final StandaloneIndividualCandidacyProcessBean bean) {

        this();

        Person person = init(bean, process);
        addSelectedCurricularCourses(bean.getCurricularCourses(), bean.getCandidacyExecutionInterval());

        /*
         * 06/04/2009 - The candidacy may not be associated with a person. In
         * this case we will not create an Event
         */
        if (bean.getInternalPersonCandidacy()) {
            createDebt(person);
        }

    }

    @Override
    protected void checkParameters(final Person person, final IndividualCandidacyProcess process,
            final IndividualCandidacyProcessBean bean) {
        if (hasValidStandaloneIndividualCandidacy(bean, process.getCandidacyExecutionInterval())) {
            throw new DomainException("error.StandaloneIndividualCandidacy.person.already.has.candidacy", process
                    .getCandidacyExecutionInterval().getName());
        }

        LocalDate candidacyDate = bean.getCandidacyDate();
        checkParameters(person, process, candidacyDate);
    }

    @Override
    protected void checkParameters(final Person person, final IndividualCandidacyProcess process, final LocalDate candidacyDate) {
        super.checkParameters(person, process, candidacyDate);
    }

    private <T extends CandidacyProcess> boolean hasValidIndividualCandidacy(final Class<T> clazz,
            final ExecutionInterval executionInterval, final IndividualCandidacyProcessBean bean) {
        T candidacyProcess = CandidacyProcess.getCandidacyProcessByExecutionInterval(clazz, executionInterval);
        IndividualCandidacyProcess individualCandidacyProcess =
                candidacyProcess.getChildProcessByDocumentId(bean.getPersonBean().getIdDocumentType(), bean.getPersonBean()
                        .getDocumentIdNumber());
        return individualCandidacyProcess != null && !individualCandidacyProcess.isCandidacyCancelled();
    }

    public boolean hasValidStandaloneIndividualCandidacy(final IndividualCandidacyProcessBean bean,
            final ExecutionInterval executionInterval) {
        return hasValidIndividualCandidacy(StandaloneCandidacyProcess.class, executionInterval, bean);
    }

    private void addSelectedCurricularCourses(final List<CurricularCourse> curricularCourses,
            final ExecutionSemester executionSemester) {
        checkEctsCredits(curricularCourses, executionSemester);
        getCurricularCourses().addAll(curricularCourses);
    }

    private void checkEctsCredits(List<CurricularCourse> curricularCourses, ExecutionSemester executionSemester) {
        double total = 0.0d;
        for (final CurricularCourse curricularCourse : curricularCourses) {
            total += curricularCourse.getEctsCredits(executionSemester);
        }

        if (!MaximumNumberOfEctsInStandaloneCurriculumGroup.allowEctsCheckingDefaultValue(total)) {
            throw new DomainException("error.StandaloneIndividualCandidacy.ects.credits.above.maximum",
                    String.valueOf(MaximumNumberOfEctsInStandaloneCurriculumGroup.MAXIMUM_DEFAULT_VALUE));
        }
    }

    @Override
    protected void createDebt(final Person person) {
        new StandaloneIndividualCandidacyEvent(this, person);
    }

    public void editCandidacyInformation(final LocalDate candidacyDate, final List<CurricularCourse> curricularCourses) {
        super.checkParameters(getPersonalDetails().getPerson(), getCandidacyProcess(), candidacyDate);
        setCandidacyDate(candidacyDate);
        getCurricularCourses().clear();
        addSelectedCurricularCourses(curricularCourses, getCandidacyExecutionInterval());
    }

    public void editCandidacyResult(final StandaloneIndividualCandidacyResultBean bean) {
        checkParameters(bean);

        if (isCandidacyResultStateValid(bean.getState())) {
            setState(bean.getState());
        }
    }

    private void checkParameters(final StandaloneIndividualCandidacyResultBean bean) {
        if (isAccepted() && bean.getState() != IndividualCandidacyState.ACCEPTED && hasRegistration()) {
            throw new DomainException("error.StandaloneIndividualCandidacy.cannot.change.state.from.accepted.candidacies");
        }
    }

    @Override
    public Registration createRegistration(final DegreeCurricularPlan degreeCurricularPlan, final CycleType cycleType,
            final Ingression ingression) {

        if (hasRegistration()) {
            throw new DomainException("error.IndividualCandidacy.person.with.registration",
                    degreeCurricularPlan.getPresentationName());
        }

        if (!degreeCurricularPlan.isEmpty()) {
            throw new DomainException("error.StandaloneIndividualCandidacy.dcp.must.be.empty");
        }

        if (hasActiveRegistration(degreeCurricularPlan)) {
            final Registration registration = getStudent().getActiveRegistrationFor(degreeCurricularPlan);
            setRegistration(registration);
            enrolInCurricularCourses(registration);
            return registration;
        }

        getPersonalDetails().ensurePersonInternalization();
        return createRegistration(getPersonalDetails().getPerson(), degreeCurricularPlan, cycleType, ingression);
    }

    @Override
    protected Registration createRegistration(final Person person, final DegreeCurricularPlan degreeCurricularPlan,
            final CycleType cycleType, final Ingression ingression) {

        final Registration registration = new Registration(person, degreeCurricularPlan);
        registration.setEntryPhase(EntryPhase.FIRST_PHASE);
        registration.setIngression(ingression);
        registration.setRegistrationYear(getCandidacyExecutionInterval().getExecutionYear());
        registration.editStartDates(getStartDate(), registration.getHomologationDate(), registration.getStudiesStartDate());

        setRegistration(registration);

//	person.addPersonRoleByRoleType(RoleType.PERSON);
//	person.addPersonRoleByRoleType(RoleType.STUDENT);

        enrolInCurricularCourses(registration);

        return registration;
    }

    @Override
    protected YearMonthDay getStartDate() {
        final ExecutionInterval interval = getCandidacyExecutionInterval().getExecutionYear();
        return interval.isCurrent() ? new YearMonthDay() : interval.getBeginDateYearMonthDay();
    }

    private void enrolInCurricularCourses(final Registration registration) {
        final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();

        for (final CurricularCourse curricularCourse : getCurricularCoursesSet()) {
            if (!studentCurricularPlan.isEnroledInExecutionPeriod(curricularCourse, getCandidacyExecutionInterval())) {

                studentCurricularPlan.createNoCourseGroupCurriculumGroupEnrolment(createStudentStandaloneEnrolmentBean(
                        studentCurricularPlan, curricularCourse));

            }
        }
    }

    private StudentStandaloneEnrolmentBean createStudentStandaloneEnrolmentBean(final StudentCurricularPlan scp,
            final CurricularCourse curricularCourse) {

        final StudentStandaloneEnrolmentBean bean = new StudentStandaloneEnrolmentBean(scp, getCandidacyExecutionInterval());

        bean.setSelectedCurricularCourse(curricularCourse);
        bean.setCurricularRuleLevel(CurricularRuleLevel.STANDALONE_ENROLMENT);

        return bean;
    }

    @Override
    protected ExecutionSemester getCandidacyExecutionInterval() {
        return (ExecutionSemester) super.getCandidacyExecutionInterval();
    }

    @Override
    public String getDescription() {
        return getCandidacyProcess().getDisplayName() + ": " + Degree.readEmptyDegree().getNameI18N();
    }

    @Override
    public Collection<Degree> getAllDegrees() {
        List<Degree> result = new ArrayList<Degree>();
        result.add(EmptyDegree.getInstance());
        return result;
    }

    @Override
    public boolean isStandalone() {
        return true;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CurricularCourse> getCurricularCourses() {
        return getCurricularCoursesSet();
    }

    @Deprecated
    public boolean hasAnyCurricularCourses() {
        return !getCurricularCoursesSet().isEmpty();
    }

}
