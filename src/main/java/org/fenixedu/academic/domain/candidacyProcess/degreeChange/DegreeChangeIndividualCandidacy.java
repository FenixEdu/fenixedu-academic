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
package org.fenixedu.academic.domain.candidacyProcess.degreeChange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.List;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.events.candidacy.DegreeChangeIndividualCandidacyEvent;
import org.fenixedu.academic.domain.candidacy.IngressionType;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcessBean;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacySeriesGrade;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyState;
import org.fenixedu.academic.domain.candidacyProcess.PrecedentDegreeInformationForIndividualCandidacyFactory;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.PrecedentDegreeInformation;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationState;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.academic.dto.candidacy.PrecedentDegreeInformationBean;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class DegreeChangeIndividualCandidacy extends DegreeChangeIndividualCandidacy_Base {

    private DegreeChangeIndividualCandidacy() {
        super();
    }

    DegreeChangeIndividualCandidacy(final DegreeChangeIndividualCandidacyProcess process,
            final DegreeChangeIndividualCandidacyProcessBean bean) {
        this();

        Person person = init(bean, process);

        super.setSelectedDegree(bean.getSelectedDegree());
        createFormationEntries(bean.getFormationConcludedBeanList(), bean.getFormationNonConcludedBeanList());
        DegreeChangeIndividualCandidacySeriesGrade newSCICSeriesGrade = new DegreeChangeIndividualCandidacySeriesGrade();
        newSCICSeriesGrade.setDegree(bean.getSelectedDegree());
        getIndividualCandidacySeriesGradeSet().add(newSCICSeriesGrade);

        /*
         * 06/04/2009 - The candidacy may not be associated with a person. In
         * this case we will not create an Event
         */
        if (bean.getInternalPersonCandidacy()) {
            createDebt(person);
        }
    }

    @Override
    protected void checkParameters(Person person, IndividualCandidacyProcess process, IndividualCandidacyProcessBean bean) {
        DegreeChangeIndividualCandidacyProcess degreeChangeIndividualCandidacyProcess =
                (DegreeChangeIndividualCandidacyProcess) process;
        DegreeChangeIndividualCandidacyProcessBean degreeChangeProcessBean = (DegreeChangeIndividualCandidacyProcessBean) bean;
        LocalDate candidacyDate = bean.getCandidacyDate();
        Degree selectedDegree = degreeChangeProcessBean.getSelectedDegree();
        PrecedentDegreeInformationBean precedentDegreeInformation = degreeChangeProcessBean.getPrecedentDegreeInformation();

        checkParameters(person, degreeChangeIndividualCandidacyProcess, candidacyDate, selectedDegree, precedentDegreeInformation);
    }

    private void checkParameters(final Person person, final DegreeChangeIndividualCandidacyProcess process,
            final LocalDate candidacyDate, final Degree selectedDegree,
            final PrecedentDegreeInformationBean precedentDegreeInformation) {

        checkParameters(person, process, candidacyDate);

        if (selectedDegree == null) {
            throw new DomainException("error.DegreeChangeIndividualCandidacy.invalid.degree");
        }

        /*
         * 31/03/2009 - The candidacy may be submited externally hence may not
         * be associated to a person
         * 
         * 
         * if (personHasDegree(person, selectedDegree)) { throw new
         * DomainException
         * ("error.DegreeChangeIndividualCandidacy.existing.degree",
         * selectedDegree.getNameFor(
         * getCandidacyExecutionInterval()).getContent()); }
         */

        if (precedentDegreeInformation == null) {
            throw new DomainException("error.DegreeChangeIndividualCandidacy.invalid.precedentDegreeInformation");
        }
    }

    @Override
    protected void createDebt(final Person person) {
        new DegreeChangeIndividualCandidacyEvent(this, person);
    }

    @Override
    public DegreeChangeIndividualCandidacyProcess getCandidacyProcess() {
        return (DegreeChangeIndividualCandidacyProcess) super.getCandidacyProcess();
    }

    @Override
    protected ExecutionYear getCandidacyExecutionInterval() {
        return (ExecutionYear) super.getCandidacyExecutionInterval();
    }

    void editCandidacyInformation(final DegreeChangeIndividualCandidacyProcessBean bean) {
        checkParameters(bean.getCandidacyDate(), bean.getSelectedDegree(), bean.getPrecedentDegreeInformation());

        setCandidacyDate(bean.getCandidacyDate());
        setSelectedDegree(bean.getSelectedDegree());

        PrecedentDegreeInformationForIndividualCandidacyFactory.edit(bean);
    }

    private void checkParameters(final LocalDate candidacyDate, final Degree selectedDegree,
            PrecedentDegreeInformationBean precedentDegreeInformation) {

        checkParameters(getPersonalDetails().getPerson(), getCandidacyProcess(), candidacyDate);

        if (selectedDegree == null) {
            throw new DomainException("error.DegreeChangeIndividualCandidacy.invalid.degree");
        }

        if (personHasDegree(getPersonalDetails().getPerson(), selectedDegree)) {
            throw new DomainException("error.DegreeChangeIndividualCandidacy.existing.degree", selectedDegree.getNameFor(
                    getCandidacyExecutionInterval()).getContent());
        }

        if (precedentDegreeInformation == null) {
            throw new DomainException("error.DegreeChangeIndividualCandidacy.invalid.precedentDegreeInformation");
        }
    }

    void editCandidacyResult(DegreeChangeIndividualCandidacyResultBean bean) {

        checkParameters(bean);

        setAffinity(bean.getAffinity());
        setDegreeNature(bean.getDegreeNature());
        setApprovedEctsRate(bean.getApprovedEctsRate());
        setGradeRate(bean.getGradeRate());
        setSeriesCandidacyGrade(bean.getSeriesCandidacyGrade());

        if (isCandidacyResultStateValid(bean.getState())) {
            setState(bean.getState());
        } else if (bean.getState() == null) {
            setState(IndividualCandidacyState.STAND_BY);
        }
    }

    private void checkParameters(final DegreeChangeIndividualCandidacyResultBean bean) {
        if (isAccepted() && bean.getState() != IndividualCandidacyState.ACCEPTED && getRegistration() != null) {
            throw new DomainException("error.DegreeChangeIndividualCandidacy.cannot.change.state.from.accepted.candidacies");
        }
    }

    @Override
    public Registration createRegistration(final DegreeCurricularPlan degreeCurricularPlan, final CycleType cycleType,
            final IngressionType ingressionType) {

        if (getRegistration() != null) {
            throw new DomainException("error.IndividualCandidacy.person.with.registration",
                    degreeCurricularPlan.getPresentationName());
        }

        if (hasRegistration(degreeCurricularPlan)) {
            final Registration registration = getMostRecentRegistration(degreeCurricularPlan);
            setRegistration(registration);

            if (!registration.isActive()) {
                RegistrationState.createRegistrationState(registration, AccessControl.getPerson(), new DateTime(),
                        RegistrationStateType.REGISTERED);
            }

            createInternalAbandonStateInPreviousRegistration();

            return registration;
        }

        getPersonalDetails().ensurePersonInternalization();
        return createRegistration(getPersonalDetails().getPerson(), degreeCurricularPlan, cycleType, ingressionType);
    }

    private boolean hasRegistration(DegreeCurricularPlan degreeCurricularPlan) {
        return getPersonalDetails().hasStudent() && getPersonalDetails().getStudent().hasRegistrationFor(degreeCurricularPlan);
    }

    private Registration getMostRecentRegistration(final DegreeCurricularPlan degreeCurricularPlan) {
        return getStudent().getMostRecentRegistration(degreeCurricularPlan);
    }

    @Override
    protected Registration createRegistration(Person person, DegreeCurricularPlan degreeCurricularPlan, CycleType cycleType,
            IngressionType ingressionType) {
        final Registration registration = super.createRegistration(person, degreeCurricularPlan, cycleType, ingressionType);
        registration.setRegistrationYear(getCandidacyExecutionInterval());
        createInternalAbandonStateInPreviousRegistration();
        return registration;
    }

    private void createInternalAbandonStateInPreviousRegistration() {
        if (getRefactoredPrecedentDegreeInformation().isCandidacyInternal()) {
            final PrecedentDegreeInformation information = getRefactoredPrecedentDegreeInformation();

            Registration previousRegistration = information.getStudentCurricularPlan().getRegistration();
            if (previousRegistration.isActive()) {

                final DateTime now = new DateTime();
                final ExecutionYear executionYear = ExecutionYear.readByDateTime(now);

                if (previousRegistration.hasAnyEnrolmentsIn(executionYear)) {
                    throw new DomainException("error.DegreeChangeIndividualCandidacy.cannot.create.abandon.state.due.enrolments",
                            previousRegistration.getDegreeCurricularPlanName(), executionYear.getQualifiedName());
                }

                RegistrationState.createRegistrationState(previousRegistration, AccessControl.getPerson(), now,
                        RegistrationStateType.INTERNAL_ABANDON);
            }
        }
    }

    void editSelectedDegree(final Degree selectedDegree) {
        setSelectedDegree(selectedDegree);
    }

    @Override
    public void exportValues(StringBuilder result) {
        super.exportValues(result);

        Formatter formatter = new Formatter(result);

        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.CANDIDATE, "label.process.id"), getCandidacyProcess()
                .getProcessCode());
        PrecedentDegreeInformation precedentDegreeInformation = getCandidacyProcess().getPrecedentDegreeInformation();
        formatter.format("%s: %s\n",
                BundleUtil.getString(Bundle.ACADEMIC, "label.SecondCycleIndividualCandidacy.previous.degree"),
                precedentDegreeInformation.getPrecedentDegreeDesignation());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.SecondCycleIndividualCandidacy.institution"),
                precedentDegreeInformation.getPrecedentInstitution().getName());
        formatter.format("%s: %s\n",
                BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.numberOfEnroledCurricularCourses"),
                precedentDegreeInformation.getNumberOfEnroledCurricularCourses());
        formatter.format("%s: %s\n",
                BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.numberOfApprovedCurricularCourses"),
                precedentDegreeInformation.getNumberOfApprovedCurricularCourses());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.gradeSum"),
                precedentDegreeInformation.getGradeSum());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.approvedEcts"),
                precedentDegreeInformation.getApprovedEcts());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.enroledEcts"),
                precedentDegreeInformation.getEnroledEcts());

        formatter.format("\n");
        formatter.format("%s: %f\n", BundleUtil.getString(Bundle.ACADEMIC, "label.SecondCycleIndividualCandidacy.affinity"),
                getAffinity() != null ? getAffinity() : BigDecimal.ZERO);
        formatter.format("%s: %d\n", BundleUtil.getString(Bundle.ACADEMIC, "label.SecondCycleIndividualCandidacy.degreeNature"),
                getDegreeNature() != null ? getDegreeNature() : 0);
        formatter.format("%s: %f\n",
                BundleUtil.getString(Bundle.ACADEMIC, "label.DegreeChangeIndividualCandidacy.approvedEctsRate"),
                getApprovedEctsRate() != null ? getApprovedEctsRate() : BigDecimal.ZERO);
        formatter.format("%s: %f\n", BundleUtil.getString(Bundle.ACADEMIC, "label.DegreeChangeIndividualCandidacy.gradeRate"),
                getGradeRate() != null ? getGradeRate() : BigDecimal.ZERO);
        formatter.format("%s: %f\n",
                BundleUtil.getString(Bundle.ACADEMIC, "label.SecondCycleIndividualCandidacy.seriesCandidacyGrade"),
                getSeriesCandidacyGrade() != null ? getSeriesCandidacyGrade() : BigDecimal.ZERO);

        formatter.close();
    }

    @Override
    public String getDescription() {
        return getCandidacyProcess().getDisplayName()
                + (getSelectedDegree() != null ? ": " + getSelectedDegree().getNameI18N() : "");
    }

    @Override
    public boolean isDegreeChange() {
        return true;
    }

    public DegreeChangeIndividualCandidacySeriesGrade getDegreeChangeIndividualCandidacySeriesGradeForDegree(Degree degree) {
        for (IndividualCandidacySeriesGrade seriesGrade : getIndividualCandidacySeriesGradeSet()) {
            if (seriesGrade.getDegree() == degree) {
                return (DegreeChangeIndividualCandidacySeriesGrade) seriesGrade;
            }
        }
        return null;
    }

    private DegreeChangeIndividualCandidacySeriesGrade getDegreeChangeIndividualCandidacySeriesGrade() {
        if (getIndividualCandidacySeriesGradeSet().size() == 0) {
            return null;
        } else {
            return (DegreeChangeIndividualCandidacySeriesGrade) getIndividualCandidacySeriesGradeSet().iterator().next();
        }
    }

    @Override
    public BigDecimal getAffinity() {
        if (getDegreeChangeIndividualCandidacySeriesGrade() != null) {
            return getDegreeChangeIndividualCandidacySeriesGrade().getAffinity();
        } else {
            return null;
        }
    }

    @Override
    public Integer getDegreeNature() {
        if (getDegreeChangeIndividualCandidacySeriesGrade() != null) {
            return getDegreeChangeIndividualCandidacySeriesGrade().getDegreeNature();
        } else {
            return null;
        }
    }

    @Override
    public BigDecimal getApprovedEctsRate() {
        if (getDegreeChangeIndividualCandidacySeriesGrade() != null) {
            return getDegreeChangeIndividualCandidacySeriesGrade().getApprovedEctsRate();
        } else {
            return null;
        }
    }

    @Override
    public BigDecimal getSeriesCandidacyGrade() {
        if (getDegreeChangeIndividualCandidacySeriesGrade() != null) {
            return getDegreeChangeIndividualCandidacySeriesGrade().getSeriesCandidacyGrade();
        } else {
            return null;
        }
    }

    @Override
    public Collection<Degree> getAllDegrees() {
        List<Degree> result = new ArrayList<Degree>();
        result.add(getSelectedDegree());
        return result;
    }

    @Override
    public void setSelectedDegree(Degree selectedDegree) {
        getDegreeChangeIndividualCandidacySeriesGrade().setDegree(selectedDegree);
        super.setSelectedDegree(selectedDegree);
    }

    @Override
    public void setAffinity(BigDecimal value) {
        getDegreeChangeIndividualCandidacySeriesGrade().setAffinity(value);
    }

    @Override
    public void setDegreeNature(Integer value) {
        getDegreeChangeIndividualCandidacySeriesGrade().setDegreeNature(value);
    }

    @Override
    public void setApprovedEctsRate(BigDecimal value) {
        getDegreeChangeIndividualCandidacySeriesGrade().setApprovedEctsRate(value);
    }

    @Override
    public void setGradeRate(BigDecimal value) {
        getDegreeChangeIndividualCandidacySeriesGrade().setGradeRate(value);
    }

    @Override
    public void setSeriesCandidacyGrade(BigDecimal value) {
        getDegreeChangeIndividualCandidacySeriesGrade().setSeriesCandidacyGrade(value);
    }

}
