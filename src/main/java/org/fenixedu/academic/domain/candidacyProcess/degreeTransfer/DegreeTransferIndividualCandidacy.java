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
package org.fenixedu.academic.domain.candidacyProcess.degreeTransfer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.List;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.events.candidacy.DegreeTransferIndividualCandidacyEvent;
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

public class DegreeTransferIndividualCandidacy extends DegreeTransferIndividualCandidacy_Base {

    private DegreeTransferIndividualCandidacy() {
        super();
    }

    DegreeTransferIndividualCandidacy(final DegreeTransferIndividualCandidacyProcess process,
            final DegreeTransferIndividualCandidacyProcessBean bean) {
        this();

        Person person = init(bean, process);
        super.setSelectedDegree(bean.getSelectedDegree());

        DegreeTransferIndividualCandidacySeriesGrade newSCICSeriesGrade = new DegreeTransferIndividualCandidacySeriesGrade();
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
    protected void checkParameters(final Person person, final IndividualCandidacyProcess process,
            final IndividualCandidacyProcessBean bean) {
        DegreeTransferIndividualCandidacyProcessBean transferProcessBean = (DegreeTransferIndividualCandidacyProcessBean) bean;
        DegreeTransferIndividualCandidacyProcess transferProcess = (DegreeTransferIndividualCandidacyProcess) process;
        LocalDate candidacyDate = bean.getCandidacyDate();
        Degree selectedDegree = transferProcessBean.getSelectedDegree();
        PrecedentDegreeInformationBean precedentDegreeInformation = transferProcessBean.getPrecedentDegreeInformation();

        checkParameters(person, transferProcess, candidacyDate, selectedDegree, precedentDegreeInformation);
    }

    private void checkParameters(final Person person, final DegreeTransferIndividualCandidacyProcess process,
            final LocalDate candidacyDate, final Degree selectedDegree,
            final PrecedentDegreeInformationBean precedentDegreeInformation) {

        checkParameters(person, process, candidacyDate);

        if (selectedDegree == null) {
            throw new DomainException("error.DegreeTransferIndividualCandidacy.invalid.degree");
        }

        /*
         * 31/03/2009 - The candidacy may be submited externally hence may not
         * be associated to a person
         * 
         * 
         * if (personHasDegree(person, selectedDegree)) { throw new
         * DomainException
         * ("error.DegreeTransferIndividualCandidacy.existing.degree",
         * selectedDegree.getNameFor(
         * getCandidacyExecutionInterval()).getContent()); }
         */

        if (precedentDegreeInformation == null) {
            throw new DomainException("error.DegreeTransferIndividualCandidacy.invalid.precedentDegreeInformation");
        }
    }

    @Override
    protected void createDebt(final Person person) {
        new DegreeTransferIndividualCandidacyEvent(this, person);
    }

    @Override
    public DegreeTransferIndividualCandidacyProcess getCandidacyProcess() {
        return (DegreeTransferIndividualCandidacyProcess) super.getCandidacyProcess();
    }

    @Override
    protected ExecutionYear getCandidacyExecutionInterval() {
        return (ExecutionYear) super.getCandidacyExecutionInterval();
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
        return getPersonalDetails().getPerson() != null && getPersonalDetails().getPerson().getStudent() != null
                && getPersonalDetails().getPerson().getStudent().hasRegistrationFor(degreeCurricularPlan);
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

                ExecutionYear candidacyExecutionInterval = getCandidacyExecutionInterval();
                ExecutionYear previousExecutionYear = candidacyExecutionInterval.getPreviousExecutionYear();

                if (previousRegistration.hasAnyEnrolmentsIn(candidacyExecutionInterval)) {
                    throw new DomainException(
                            "error.DegreeTransferIndividualCandidacy.cannot.create.abandon.state.due.enrolments",
                            previousRegistration.getDegreeCurricularPlanName(), candidacyExecutionInterval.getQualifiedName());
                }

                RegistrationState.createRegistrationState(previousRegistration, AccessControl.getPerson(), previousExecutionYear
                        .getEndDateYearMonthDay().toDateTimeAtMidnight(), RegistrationStateType.INTERNAL_ABANDON);
            }

        }
    }

    void editCandidacyInformation(final DegreeTransferIndividualCandidacyProcessBean bean) {
        checkParameters(bean.getCandidacyDate(), bean.getSelectedDegree(), bean.getPrecedentDegreeInformation());

        setCandidacyDate(bean.getCandidacyDate());
        setSelectedDegree(bean.getSelectedDegree());

        PrecedentDegreeInformationForIndividualCandidacyFactory.edit(bean);
    }

    private void checkParameters(final LocalDate candidacyDate, final Degree selectedDegree,
            PrecedentDegreeInformationBean precedentDegreeInformation) {

        checkParameters(getPersonalDetails().getPerson(), getCandidacyProcess(), candidacyDate);

        if (selectedDegree == null) {
            throw new DomainException("error.DegreeTransferIndividualCandidacy.invalid.degree");
        }

        if (personHasDegree(getPersonalDetails().getPerson(), selectedDegree)) {
            throw new DomainException("error.DegreeTransferIndividualCandidacy.existing.degree", selectedDegree.getNameFor(
                    getCandidacyExecutionInterval()).getContent());
        }

        if (precedentDegreeInformation == null) {
            throw new DomainException("error.DegreeTransferIndividualCandidacy.invalid.precedentDegreeInformation");
        }
    }

    void editCandidacyResult(final DegreeTransferIndividualCandidacyResultBean bean) {

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

    private void checkParameters(final DegreeTransferIndividualCandidacyResultBean bean) {
        if (isAccepted() && bean.getState() != IndividualCandidacyState.ACCEPTED && getRegistration() != null) {
            throw new DomainException("error.DegreeTransferIndividualCandidacy.cannot.change.state.from.accepted.candidacies");
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

    }

    @Override
    public String getDescription() {
        return getCandidacyProcess().getDisplayName()
                + (getSelectedDegree() != null ? ": " + getSelectedDegree().getNameI18N() : "");
    }

    @Override
    public boolean isDegreeTransfer() {
        return true;
    }

    public DegreeTransferIndividualCandidacySeriesGrade getDegreeTransferIndividualCandidacySeriesGradeForDegree(Degree degree) {
        for (IndividualCandidacySeriesGrade seriesGrade : getIndividualCandidacySeriesGradeSet()) {
            if (seriesGrade.getDegree() == degree) {
                return (DegreeTransferIndividualCandidacySeriesGrade) seriesGrade;
            }
        }
        return null;
    }

    private DegreeTransferIndividualCandidacySeriesGrade getDegreeTransferIndividualCandidacySeriesGrade() {
        if (getIndividualCandidacySeriesGradeSet().size() == 0) {
            return null;
        } else {
            return getDegreeTransferIndividualCandidacySeriesGradeForDegree(getSelectedDegree());
        }
    }

    @Override
    public BigDecimal getAffinity() {
        if (getDegreeTransferIndividualCandidacySeriesGrade() != null) {
            return getDegreeTransferIndividualCandidacySeriesGrade().getAffinity();
        } else {
            return null;
        }
    }

    @Override
    public Integer getDegreeNature() {
        if (getDegreeTransferIndividualCandidacySeriesGrade() != null) {
            return getDegreeTransferIndividualCandidacySeriesGrade().getDegreeNature();
        } else {
            return null;
        }
    }

    @Override
    public BigDecimal getApprovedEctsRate() {
        if (getDegreeTransferIndividualCandidacySeriesGrade() != null) {
            return getDegreeTransferIndividualCandidacySeriesGrade().getApprovedEctsRate();
        } else {
            return null;
        }
    }

    @Override
    public BigDecimal getGradeRate() {
        if (getDegreeTransferIndividualCandidacySeriesGrade() != null) {
            return getDegreeTransferIndividualCandidacySeriesGrade().getGradeRate();
        } else {
            return null;
        }
    }

    @Override
    public BigDecimal getSeriesCandidacyGrade() {
        if (getDegreeTransferIndividualCandidacySeriesGrade() != null) {
            return getDegreeTransferIndividualCandidacySeriesGrade().getSeriesCandidacyGrade();
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
        getDegreeTransferIndividualCandidacySeriesGrade().setDegree(selectedDegree);
        super.setSelectedDegree(selectedDegree);
    }

    @Override
    public void setAffinity(BigDecimal value) {
        getDegreeTransferIndividualCandidacySeriesGrade().setAffinity(value);
    }

    @Override
    public void setDegreeNature(Integer value) {
        getDegreeTransferIndividualCandidacySeriesGrade().setDegreeNature(value);
    }

    @Override
    public void setApprovedEctsRate(BigDecimal value) {
        getDegreeTransferIndividualCandidacySeriesGrade().setApprovedEctsRate(value);
    }

    @Override
    public void setGradeRate(BigDecimal value) {
        getDegreeTransferIndividualCandidacySeriesGrade().setGradeRate(value);
    }

    @Override
    public void setSeriesCandidacyGrade(BigDecimal value) {
        getDegreeTransferIndividualCandidacySeriesGrade().setSeriesCandidacyGrade(value);
    }

}
