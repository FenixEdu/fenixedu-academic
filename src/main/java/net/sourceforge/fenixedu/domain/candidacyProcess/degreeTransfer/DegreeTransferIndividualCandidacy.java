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
package net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer;

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
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.DegreeTransferIndividualCandidacyEvent;
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
        return getPersonalDetails().hasPerson() && getPersonalDetails().getPerson().hasStudent()
                && getPersonalDetails().getPerson().getStudent().hasRegistrationFor(degreeCurricularPlan);
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

                ExecutionYear candidacyExecutionInterval = getCandidacyExecutionInterval();
                ExecutionYear previousExecutionYear = candidacyExecutionInterval.getPreviousExecutionYear();

                if (previousRegistration.hasAnyEnrolmentsIn(candidacyExecutionInterval)) {
                    throw new DomainException(
                            "error.DegreeTransferIndividualCandidacy.cannot.create.abandon.state.due.enrolments",
                            previousRegistration.getDegreeCurricularPlanName(), candidacyExecutionInterval.getQualifiedName());
                }

                RegistrationStateCreator.createState(previousRegistration, AccessControl.getPerson(), previousExecutionYear
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
        if (isAccepted() && bean.getState() != IndividualCandidacyState.ACCEPTED && hasRegistration()) {
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

        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.CANDIDATE, "label.process.id"), getCandidacyProcess().getProcessCode());
        PrecedentDegreeInformation precedentDegreeInformation = getCandidacyProcess().getPrecedentDegreeInformation();
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.SecondCycleIndividualCandidacy.previous.degree"),
                precedentDegreeInformation.getPrecedentDegreeDesignation());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.SecondCycleIndividualCandidacy.institution"),
                precedentDegreeInformation.getPrecedentInstitution().getName());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.numberOfEnroledCurricularCourses"),
                precedentDegreeInformation.getNumberOfEnroledCurricularCourses());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.numberOfApprovedCurricularCourses"),
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
        formatter.format("%s: %f\n", BundleUtil.getString(Bundle.ACADEMIC, "label.DegreeChangeIndividualCandidacy.approvedEctsRate"),
                getApprovedEctsRate() != null ? getApprovedEctsRate() : BigDecimal.ZERO);
        formatter.format("%s: %f\n", BundleUtil.getString(Bundle.ACADEMIC, "label.DegreeChangeIndividualCandidacy.gradeRate"),
                getGradeRate() != null ? getGradeRate() : BigDecimal.ZERO);
        formatter.format("%s: %f\n", BundleUtil.getString(Bundle.ACADEMIC, "label.SecondCycleIndividualCandidacy.seriesCandidacyGrade"),
                getSeriesCandidacyGrade() != null ? getSeriesCandidacyGrade() : BigDecimal.ZERO);

    }

    @Override
    public String getDescription() {
        return getCandidacyProcess().getDisplayName() + (hasSelectedDegree() ? ": " + getSelectedDegree().getNameI18N() : "");
    }

    @Override
    public boolean isDegreeTransfer() {
        return true;
    }

    public DegreeTransferIndividualCandidacySeriesGrade getDegreeTransferIndividualCandidacySeriesGradeForDegree(Degree degree) {
        for (IndividualCandidacySeriesGrade seriesGrade : getIndividualCandidacySeriesGrade()) {
            if (seriesGrade.getDegree() == degree) {
                return (DegreeTransferIndividualCandidacySeriesGrade) seriesGrade;
            }
        }
        return null;
    }

    private DegreeTransferIndividualCandidacySeriesGrade getDegreeTransferIndividualCandidacySeriesGrade() {
        if (getIndividualCandidacySeriesGrade().size() == 0) {
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
