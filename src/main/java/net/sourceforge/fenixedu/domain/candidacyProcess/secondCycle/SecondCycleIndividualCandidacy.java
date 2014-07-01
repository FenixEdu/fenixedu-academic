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
package net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeState;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.IndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.SecondCycleIndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacySeriesGrade;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.LocalDate;

public class SecondCycleIndividualCandidacy extends SecondCycleIndividualCandidacy_Base {

    private SecondCycleIndividualCandidacy() {
        super();
    }

    SecondCycleIndividualCandidacy(final SecondCycleIndividualCandidacyProcess process,
            final SecondCycleIndividualCandidacyProcessBean bean) {
        this();

        Person person = init(bean, process);

        getSelectedDegrees().addAll(bean.getSelectedDegreeList());
        for (Degree degree : bean.getSelectedDegreeList()) {
            SecondCycleIndividualCandidacySeriesGrade newSCICSeriesGrade = new SecondCycleIndividualCandidacySeriesGrade();
            newSCICSeriesGrade.setDegree(degree);
            getIndividualCandidacySeriesGrade().add(newSCICSeriesGrade);
        }

        setProfessionalStatus(bean.getProfessionalStatus());
        setOtherEducation(bean.getOtherEducation());

        createFormationEntries(bean.getFormationConcludedBeanList(), bean.getFormationNonConcludedBeanList());

        editFormerIstStudentNumber(bean);

        /*
         * 06/04/2009 - The candidacy may not be associated with a person. In
         * this case we will not create an Event
         */
        if (bean.getInternalPersonCandidacy()) {
            createDebt(person);
        }

        if (getSelectedDegrees().isEmpty()) {
            throw new DomainException("This shouldnt happen");
        }
    }

    @Override
    protected void checkParameters(final Person person, final IndividualCandidacyProcess process,
            final IndividualCandidacyProcessBean bean) {
        SecondCycleIndividualCandidacyProcess secondCycleIndividualCandidacyProcess =
                (SecondCycleIndividualCandidacyProcess) process;
        SecondCycleIndividualCandidacyProcessBean secondCandidacyProcessBean = (SecondCycleIndividualCandidacyProcessBean) bean;
        LocalDate candidacyDate = bean.getCandidacyDate();
        PrecedentDegreeInformationBean precedentDegreeInformationBean =
                secondCandidacyProcessBean.getPrecedentDegreeInformation();

        checkParameters(person, secondCycleIndividualCandidacyProcess, candidacyDate,
                secondCandidacyProcessBean.getSelectedDegreeList(), precedentDegreeInformationBean);

    }

    private void checkParameters(final Person person, final SecondCycleIndividualCandidacyProcess process,
            final LocalDate candidacyDate, final Set<Degree> degrees,
            final PrecedentDegreeInformationBean precedentDegreeInformation) {

        checkParameters(person, process, candidacyDate);

        /*
         * 31/03/2009 - The candidacy may be submited externally hence may not
         * be associated to a person
         * 
         * 
         * if(person.hasValidSecondCycleIndividualCandidacy(process.
         * getCandidacyExecutionInterval())) { throw newDomainException(
         * "error.SecondCycleIndividualCandidacy.person.already.has.candidacy",
         * process .getCandidacyExecutionInterval().getName()); }
         */

        if (degrees.isEmpty()) {
            throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.degrees.selection");
        }

        if (precedentDegreeInformation == null) {
            throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.precedentDegreeInformation");
        }
    }

    @Override
    protected void createDebt(final Person person) {
        new SecondCycleIndividualCandidacyEvent(this, person);
    }

    @Override
    public SecondCycleIndividualCandidacyProcess getCandidacyProcess() {
        return (SecondCycleIndividualCandidacyProcess) super.getCandidacyProcess();
    }

    void editCandidacyInformation(final LocalDate candidacyDate, final Set<Degree> selectedDegrees,
            final PrecedentDegreeInformationBean precedentDegreeInformation, final String professionalStatus,
            final String otherEducation) {

        checkParameters(candidacyDate, selectedDegrees, precedentDegreeInformation);
        setCandidacyDate(candidacyDate);

        putSelectedDegrees(selectedDegrees);

        setProfessionalStatus(professionalStatus);
        setOtherEducation(otherEducation);
    }

    private void putSelectedDegrees(final Set<Degree> selectedDegreeList) {
        for (Degree degree : getSelectedDegrees()) {
            if (selectedDegreeList.contains(degree)) {
                if (getSecondCycleIndividualCandidacySeriesGradeForDegree(degree) == null) {
                    SecondCycleIndividualCandidacySeriesGrade newSCICSeriesGrade =
                            new SecondCycleIndividualCandidacySeriesGrade();
                    newSCICSeriesGrade.setDegree(degree);
                    getIndividualCandidacySeriesGrade().add(newSCICSeriesGrade);
                }
            } else {
                SecondCycleIndividualCandidacySeriesGrade seriesGrade =
                        getSecondCycleIndividualCandidacySeriesGradeForDegree(degree);
                if (seriesGrade == null) {
                    throw new RuntimeException("should.exist.series.grade.to.delete");
                } else {
                    if (!seriesGrade.isClean()) {
                        throw new RuntimeException("cannot.delete.series.grade.with.grades");
                    } else {
                        seriesGrade.delete();
                    }
                }
            }
        }

        while (!getSelectedDegrees().isEmpty()) {
            getSelectedDegrees().remove(getSelectedDegrees().iterator().next());
        }

        getSelectedDegrees().addAll(selectedDegreeList);

        IndividualCandidacyEvent individualCandidacyEvent = getEvent();
        if (individualCandidacyEvent != null && individualCandidacyEvent.getAmountToPay().isPositive() && getEvent().isClosed()) {
            individualCandidacyEvent.open();

            Collection<AccountingEventPaymentCode> paymentCodes = individualCandidacyEvent.getAllPaymentCodes();

            for (AccountingEventPaymentCode accountingEventPaymentCode : paymentCodes) {
                accountingEventPaymentCode.setState(PaymentCodeState.NEW);
            }

        }

        if (getSelectedDegrees().isEmpty()) {
            throw new DomainException("this shouldnt happen");
        }
    }

    void editSelectedDegrees(final Set<Degree> selectedDegreeList) {
        for (Degree degree : getSelectedDegrees()) {
            if (selectedDegreeList.contains(degree)) {
                if (getSecondCycleIndividualCandidacySeriesGradeForDegree(degree) == null) {
                    SecondCycleIndividualCandidacySeriesGrade newSCICSeriesGrade =
                            new SecondCycleIndividualCandidacySeriesGrade();
                    newSCICSeriesGrade.setDegree(degree);
                    getIndividualCandidacySeriesGrade().add(newSCICSeriesGrade);
                }
            } else {
                SecondCycleIndividualCandidacySeriesGrade seriesGrade =
                        getSecondCycleIndividualCandidacySeriesGradeForDegree(degree);
                if (seriesGrade == null) {
                    throw new RuntimeException("should.exist.series.grade.to.delete");
                } else {
                    if (!seriesGrade.isClean()) {
                        throw new RuntimeException("cannot.delete.series.grade.with.grades");
                    } else {
                        seriesGrade.delete();
                    }
                }
            }
        }

        while (!getSelectedDegrees().isEmpty()) {
            getSelectedDegrees().remove(getSelectedDegrees().iterator().next());
        }

        getSelectedDegrees().addAll(selectedDegreeList);

        IndividualCandidacyEvent individualCandidacyEvent = getEvent();
        if (individualCandidacyEvent != null && individualCandidacyEvent.getAmountToPay().isPositive() && getEvent().isClosed()) {
            individualCandidacyEvent.open();

            Collection<AccountingEventPaymentCode> paymentCodes = individualCandidacyEvent.getAllPaymentCodes();

            for (AccountingEventPaymentCode accountingEventPaymentCode : paymentCodes) {
                accountingEventPaymentCode.setState(PaymentCodeState.NEW);
            }

        }

        if (getSelectedDegrees().isEmpty()) {
            throw new DomainException("this shouldnt happen");
        }
    }

    private void checkParameters(final LocalDate candidacyDate, final Set<Degree> selectedDegrees,
            final PrecedentDegreeInformationBean precedentDegreeInformation) {

        checkParameters(getPersonalDetails().getPerson(), getCandidacyProcess(), candidacyDate);

        if (selectedDegrees == null || selectedDegrees.isEmpty()) {
            throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.degree");
        }

        if (isCandidacyInternal()) {
            if (personHasOneOfDegrees(getPersonalDetails().getPerson(), selectedDegrees)) {
                throw new DomainException("error.SecondCycleIndividualCandidacy.existing.degree");
            }
        }

        if (precedentDegreeInformation == null) {
            throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.precedentDegreeInformation");
        }
    }

    void editCandidacyResult(final SecondCycleIndividualCandidacyResultBean bean) {

        setProfessionalExperience(bean.getProfessionalExperience());
        setAffinity(bean.getAffinity());
        setDegreeNature(bean.getDegreeNature());
        setCandidacyGrade(bean.getGrade());
        setInterviewGrade(bean.getInterviewGrade());
        setSeriesCandidacyGrade(bean.getSeriesGrade());
        setNotes(bean.getNotes());

        if (bean.getState() == null) {
            setState(IndividualCandidacyState.STAND_BY);
            setRegistration(null);
        } else {
            setState(bean.getState());
        }
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
            final Registration registration = getRegistration(degreeCurricularPlan);
            setRegistration(registration);
            return registration;
        }

        getPersonalDetails().ensurePersonInternalization();
        return createRegistration(getPersonalDetails().getPerson(), degreeCurricularPlan, cycleType, ingression);
    }

    private boolean hasRegistration(final DegreeCurricularPlan degreeCurricularPlan) {
        return hasStudent() && getStudent().hasRegistrationFor(degreeCurricularPlan);
    }

    private Registration getRegistration(final DegreeCurricularPlan degreeCurricularPlan) {
        final List<Registration> registrations = getStudent().getRegistrationsFor(degreeCurricularPlan);
        Collections.sort(registrations, Registration.COMPARATOR_BY_START_DATE);

        Registration result = null;
        for (final Registration registration : registrations) {
            if (result == null || registration.hasAnyActiveState(getCandidacyExecutionInterval())) {
                result = registration;
            }
        }
        return result;
    }

    @Override
    protected Registration createRegistration(final Person person, final DegreeCurricularPlan degreeCurricularPlan,
            final CycleType cycleType, final Ingression ingression) {
        final Registration registration = super.createRegistration(person, degreeCurricularPlan, cycleType, ingression);
        registration.setRegistrationYear(getCandidacyExecutionInterval());
        return registration;
    }

    void editFormerIstStudentNumber(SecondCycleIndividualCandidacyProcessBean bean) {
        this.setFormerStudentNumber(bean.getIstStudentNumber());
    }

    @Override
    public void exportValues(StringBuilder result) {
        super.exportValues(result);

        Formatter formatter = new Formatter(result);

        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.CANDIDATE, "label.process.id"), getCandidacyProcess().getProcessCode());
        PrecedentDegreeInformation precedentDegreeInformation = getCandidacyProcess().getPrecedentDegreeInformation();
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.SecondCycleIndividualCandidacy.previous.degree"),
                precedentDegreeInformation.getDegreeDesignation());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.conclusionDate"), precedentDegreeInformation.getConclusionDate());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.SecondCycleIndividualCandidacy.institution"),
                precedentDegreeInformation.getInstitution().getName());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.conclusionGrade"), precedentDegreeInformation.getConclusionGrade());
        formatter.format("\n");
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.SecondCycleIndividualCandidacy.professionalStatus"),
                StringUtils.isEmpty(getProfessionalStatus()) ? StringUtils.EMPTY : getProfessionalStatus());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.SecondCycleIndividualCandidacy.otherEducation"),
                StringUtils.isEmpty(getOtherEducation()) ? StringUtils.EMPTY : getOtherEducation());
        formatter.format("%s: %d\n", BundleUtil.getString(Bundle.ACADEMIC, "label.SecondCycleIndividualCandidacy.professionalExperience"),
                getProfessionalExperience() != null ? getProfessionalExperience() : 0);
        formatter.format("%s: %f\n", BundleUtil.getString(Bundle.ACADEMIC, "label.SecondCycleIndividualCandidacy.affinity"),
                getAffinity() != null ? getAffinity() : BigDecimal.ZERO);
        formatter.format("%s: %d\n", BundleUtil.getString(Bundle.ACADEMIC, "label.SecondCycleIndividualCandidacy.degreeNature"),
                getDegreeNature() != null ? getDegreeNature() : 0);
        formatter.format("%s: %f\n", BundleUtil.getString(Bundle.ACADEMIC, "label.SecondCycleIndividualCandidacy.candidacyGrade"),
                getCandidacyGrade() != null ? getCandidacyGrade() : BigDecimal.ZERO);
        formatter
                .format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.SecondCycleIndividualCandidacy.interviewGrade"), getInterviewGrade());
        formatter.format("%s: %f\n", BundleUtil.getString(Bundle.ACADEMIC, "label.SecondCycleIndividualCandidacy.seriesCandidacyGrade"),
                getSeriesCandidacyGrade() != null ? getSeriesCandidacyGrade() : BigDecimal.ZERO);

        formatter.close();
    }

    @Override
    public String getDescription() {
        return getCandidacyProcess().getDisplayName() + (hasSelectedDegree() ? ": " + getSelectedDegree().getNameI18N() : "");
    }

    @Override
    public Collection<Degree> getAllDegrees() {
        List<Degree> result = new ArrayList<Degree>();
        result.addAll(getSelectedDegrees());
        return result;
    }

    public boolean hasSelectedDegree() {
        throw new DomainException("error.second.cycle.individual.candidacy.relation.with.degree.obsolete");
    }

    @Override
    public void setSelectedDegree(Degree selectedDegree) {
        throw new DomainException("error.second.cycle.individual.candidacy.relation.with.degree.obsolete");
    }

    public void removeSelectedDegree() {
        throw new DomainException("error.second.cycle.individual.candidacy.relation.with.degree.obsolete");
    }

    @Override
    public boolean isSecondCycle() {
        return true;
    }

    public boolean isEventClosedButWithDebt() {
        IndividualCandidacyEvent event = getEvent();

        return !event.getNonAdjustingTransactions().isEmpty() && event.getAmountToPay().isPositive();
    }

    public SecondCycleIndividualCandidacySeriesGrade getSecondCycleIndividualCandidacySeriesGradeForDegree(Degree degree) {
        for (IndividualCandidacySeriesGrade seriesGrade : getIndividualCandidacySeriesGrade()) {
            if (seriesGrade.getDegree() == degree) {
                return (SecondCycleIndividualCandidacySeriesGrade) seriesGrade;
            }
        }
        return null;
    }

    public SecondCycleIndividualCandidacySeriesGrade getSecondCycleIndividualCandidacySeriesGrade() {
        if (getIndividualCandidacySeriesGradeSet().size() == 0) {
            return null;
        } else {
            if (getIndividualCandidacySeriesGradeSet().size() == 1) {
                return getSecondCycleIndividualCandidacySeriesGradeForDegree(getSelectedDegrees().iterator().next());
            } else {
                return getSecondCycleIndividualCandidacySeriesGradeForDegree(getSelectedDegree());
            }
        }
    }

    @Override
    public BigDecimal getAffinity() {
        if (getSecondCycleIndividualCandidacySeriesGrade() != null) {
            return getSecondCycleIndividualCandidacySeriesGrade().getAffinity();
        } else {
            return null;
        }
    }

    @Override
    public Integer getProfessionalExperience() {
        if (getSecondCycleIndividualCandidacySeriesGrade() != null) {
            return getSecondCycleIndividualCandidacySeriesGrade().getProfessionalExperience();
        } else {
            return null;
        }
    }

    @Override
    public BigDecimal getCandidacyGrade() {
        if (getSecondCycleIndividualCandidacySeriesGrade() != null) {
            return getSecondCycleIndividualCandidacySeriesGrade().getCandidacyGrade();
        } else {
            return null;
        }
    }

    public BigDecimal getSerieseCandidacyGrade() {
        if (getSecondCycleIndividualCandidacySeriesGrade() != null) {
            return getSecondCycleIndividualCandidacySeriesGrade().getCandidacyGrade();
        } else {
            return null;
        }
    }

    @Override
    public String getInterviewGrade() {
        if (getSecondCycleIndividualCandidacySeriesGrade() != null) {
            return getSecondCycleIndividualCandidacySeriesGrade().getInterviewGrade();
        } else {
            return null;
        }
    }

    @Override
    public Integer getDegreeNature() {
        if (getSecondCycleIndividualCandidacySeriesGrade() != null) {
            return getSecondCycleIndividualCandidacySeriesGrade().getDegreeNature();
        } else {
            return null;
        }
    }

    @Override
    public void setProfessionalExperience(Integer value) {
        getSecondCycleIndividualCandidacySeriesGrade().setProfessionalExperience(value);
    }

    @Override
    public void setCandidacyGrade(BigDecimal value) {
        getSecondCycleIndividualCandidacySeriesGrade().setCandidacyGrade(value);
    }

    @Override
    public void setAffinity(BigDecimal value) {
        getSecondCycleIndividualCandidacySeriesGrade().setAffinity(value);
    }

    @Override
    public void setInterviewGrade(String value) {
        getSecondCycleIndividualCandidacySeriesGrade().setInterviewGrade(value);
    }

    @Override
    public void setSeriesCandidacyGrade(BigDecimal value) {
        getSecondCycleIndividualCandidacySeriesGrade().setSeriesCandidacyGrade(value);
    }

    @Override
    public void setDegreeNature(Integer value) {
        getSecondCycleIndividualCandidacySeriesGrade().setDegreeNature(value);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Degree> getSelectedDegrees() {
        return getSelectedDegreesSet();
    }

    @Deprecated
    public boolean hasAnySelectedDegrees() {
        return !getSelectedDegreesSet().isEmpty();
    }

    @Deprecated
    public boolean hasProfessionalStatus() {
        return getProfessionalStatus() != null;
    }

    @Deprecated
    public boolean hasProfessionalExperience() {
        return getProfessionalExperience() != null;
    }

    @Deprecated
    public boolean hasCandidacyGrade() {
        return getCandidacyGrade() != null;
    }

    @Deprecated
    public boolean hasDegreeNature() {
        return getDegreeNature() != null;
    }

    @Deprecated
    public boolean hasOtherEducation() {
        return getOtherEducation() != null;
    }

    @Deprecated
    public boolean hasSeriesCandidacyGrade() {
        return getSeriesCandidacyGrade() != null;
    }

    @Deprecated
    public boolean hasInterviewGrade() {
        return getInterviewGrade() != null;
    }

    @Deprecated
    public boolean hasAffinity() {
        return getAffinity() != null;
    }

}
