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
package net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.DegreeChangeIndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacySeriesGrade;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;
import net.sourceforge.fenixedu.domain.candidacyProcess.PrecedentDegreeInformationForIndividualCandidacyFactory;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.RegistrationStateCreator;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.Bundle;

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
        getIndividualCandidacySeriesGrade().add(newSCICSeriesGrade);

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
        if (isAccepted() && bean.getState() != IndividualCandidacyState.ACCEPTED && hasRegistration()) {
            throw new DomainException("error.DegreeChangeIndividualCandidacy.cannot.change.state.from.accepted.candidacies");
        }
    }

    @Override
    public Registration createRegistration(final DegreeCurricularPlan degreeCurricularPlan, final CycleType cycleType,
            final Ingression ingression) {

        if (hasRegistration()) {
            throw new DomainException("error.IndividualCandidacy.person.with.registration",
                    degreeCurricularPlan.getPresentationName());
        }

        if (hasRegistration(degreeCurricularPlan)) {
            final Registration registration = getMostRecentRegistration(degreeCurricularPlan);
            setRegistration(registration);

            if (!registration.isActive()) {
                RegistrationStateCreator.createState(registration, AccessControl.getPerson(), new DateTime(),
                        RegistrationStateType.REGISTERED);
            }

            createInternalAbandonStateInPreviousRegistration();

            return registration;
        }

        getPersonalDetails().ensurePersonInternalization();
        return createRegistration(getPersonalDetails().getPerson(), degreeCurricularPlan, cycleType, ingression);
    }

    private boolean hasRegistration(DegreeCurricularPlan degreeCurricularPlan) {
        return getPersonalDetails().hasStudent() && getPersonalDetails().getStudent().hasRegistrationFor(degreeCurricularPlan);
    }

    private Registration getMostRecentRegistration(final DegreeCurricularPlan degreeCurricularPlan) {
        return getStudent().getMostRecentRegistration(degreeCurricularPlan);
    }

    @Override
    protected Registration createRegistration(Person person, DegreeCurricularPlan degreeCurricularPlan, CycleType cycleType,
            Ingression ingression) {
        final Registration registration = super.createRegistration(person, degreeCurricularPlan, cycleType, ingression);
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

                RegistrationStateCreator.createState(previousRegistration, AccessControl.getPerson(), now,
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
        return getCandidacyProcess().getDisplayName() + (hasSelectedDegree() ? ": " + getSelectedDegree().getNameI18N() : "");
    }

    @Override
    public boolean isDegreeChange() {
        return true;
    }

    public DegreeChangeIndividualCandidacySeriesGrade getDegreeChangeIndividualCandidacySeriesGradeForDegree(Degree degree) {
        for (IndividualCandidacySeriesGrade seriesGrade : getIndividualCandidacySeriesGrade()) {
            if (seriesGrade.getDegree() == degree) {
                return (DegreeChangeIndividualCandidacySeriesGrade) seriesGrade;
            }
        }
        return null;
    }

    private DegreeChangeIndividualCandidacySeriesGrade getDegreeChangeIndividualCandidacySeriesGrade() {
        if (getIndividualCandidacySeriesGrade().size() == 0) {
            return null;
        } else {
            return (DegreeChangeIndividualCandidacySeriesGrade) getIndividualCandidacySeriesGrade().iterator().next();
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

    @Deprecated
    public boolean hasApprovedEctsRate() {
        return getApprovedEctsRate() != null;
    }

    @Deprecated
    public boolean hasDegreeNature() {
        return getDegreeNature() != null;
    }

    @Deprecated
    public boolean hasSeriesCandidacyGrade() {
        return getSeriesCandidacyGrade() != null;
    }

    @Deprecated
    public boolean hasGradeRate() {
        return getGradeRate() != null;
    }

    @Deprecated
    public boolean hasSelectedDegree() {
        return getSelectedDegree() != null;
    }

    @Deprecated
    public boolean hasAffinity() {
        return getAffinity() != null;
    }

}
