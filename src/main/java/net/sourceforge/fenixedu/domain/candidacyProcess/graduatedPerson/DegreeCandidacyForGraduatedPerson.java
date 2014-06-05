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
package net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson;

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
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.DegreeCandidacyForGraduatedPersonEvent;
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
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.LocalDate;

public class DegreeCandidacyForGraduatedPerson extends DegreeCandidacyForGraduatedPerson_Base {

    private DegreeCandidacyForGraduatedPerson() {
        super();
    }

    DegreeCandidacyForGraduatedPerson(final DegreeCandidacyForGraduatedPersonIndividualProcess process,
            final DegreeCandidacyForGraduatedPersonIndividualProcessBean bean) {
        this();

        Person person = init(bean, process);

        createFormationEntries(bean.getFormationConcludedBeanList(), bean.getFormationNonConcludedBeanList());
        DegreeCandidacyForGraduatedPersonSeriesGade newSCICSeriesGrade = new DegreeCandidacyForGraduatedPersonSeriesGade();
        newSCICSeriesGrade.setDegree(bean.getSelectedDegree());
        getIndividualCandidacySeriesGrade().add(newSCICSeriesGrade);
        setSelectedDegree(bean.getSelectedDegree());

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
        DegreeCandidacyForGraduatedPersonIndividualProcess degreeCandidacyProcess =
                (DegreeCandidacyForGraduatedPersonIndividualProcess) process;
        DegreeCandidacyForGraduatedPersonIndividualProcessBean degreeCandidacyBean =
                (DegreeCandidacyForGraduatedPersonIndividualProcessBean) bean;

        LocalDate candidacyDate = degreeCandidacyBean.getCandidacyDate();
        Degree selectedDegree = degreeCandidacyBean.getSelectedDegree();
        PrecedentDegreeInformationBean precedentDegreeInformation = degreeCandidacyBean.getPrecedentDegreeInformation();

        checkParameters(person, degreeCandidacyProcess, candidacyDate, selectedDegree, precedentDegreeInformation);
    }

    private void checkParameters(final Person person, final DegreeCandidacyForGraduatedPersonIndividualProcess process,
            final LocalDate candidacyDate, final Degree selectedDegree,
            final PrecedentDegreeInformationBean precedentDegreeInformation) {

        checkParameters(person, process, candidacyDate);

        /*
         * 31/03/2009 - The candidacy will not be associated with a Person if it
         * is submited externally (not in administrative office)
         * 
         * if (person == null) { throw new
         * DomainException("error.IndividualCandidacy.invalid.person"); }
         * 
         * if(person.hasValidDegreeCandidacyForGraduatedPerson(process.
         * getCandidacyExecutionInterval())) { throw newDomainException(
         * "error.DegreeCandidacyForGraduatedPerson.person.already.has.candidacy"
         * , process .getCandidacyExecutionInterval().getName()); }
         */

        if (selectedDegree == null) {
            throw new DomainException("error.DegreeCandidacyForGraduatedPerson.invalid.degree");
        }

        if (precedentDegreeInformation == null) {
            throw new DomainException("error.DegreeCandidacyForGraduatedPerson.invalid.precedentDegreeInformation");
        }
    }

    @Override
    protected void createDebt(final Person person) {
        new DegreeCandidacyForGraduatedPersonEvent(this, person);
    }

    @Override
    public DegreeCandidacyForGraduatedPersonIndividualProcess getCandidacyProcess() {
        return (DegreeCandidacyForGraduatedPersonIndividualProcess) super.getCandidacyProcess();
    }

    public void editCandidacyInformation(final DegreeCandidacyForGraduatedPersonIndividualProcessBean bean) {
        checkParameters(bean.getCandidacyDate(), bean.getSelectedDegree(), bean.getPrecedentDegreeInformation());

        setCandidacyDate(bean.getCandidacyDate());
        setSelectedDegree(bean.getSelectedDegree());

        PrecedentDegreeInformationForIndividualCandidacyFactory.edit(bean);
    }

    private void checkParameters(final LocalDate candidacyDate, final Degree selectedDegree,
            PrecedentDegreeInformationBean precedentDegreeInformation) {

        checkParameters(getPersonalDetails().getPerson(), getCandidacyProcess(), candidacyDate);
        if (selectedDegree == null) {
            throw new DomainException("error.DegreeCandidacyForGraduatedPerson.invalid.degree");
        }

        if (hasRegistration() && getRegistration().getDegree() != selectedDegree) {
            throw new DomainException("error.DegreeCandidacyForGraduatedPerson.cannot.change.degree");
        }

        if (precedentDegreeInformation == null) {
            throw new DomainException("error.DegreeCandidacyForGraduatedPerson.invalid.precedentDegreeInformation");
        }
    }

    void editCandidacyResult(final DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean bean) {
        checkParameters(bean);

        setAffinity(bean.getAffinity());
        setDegreeNature(bean.getDegreeNature());
        setCandidacyGrade(bean.getGrade());

        if (isCandidacyResultStateValid(bean.getState())) {
            setState(bean.getState());
        }
    }

    private void checkParameters(final DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean bean) {
        if (isAccepted() && bean.getState() != IndividualCandidacyState.ACCEPTED && hasRegistration()) {
            throw new DomainException("error.DegreeCandidacyForGraduatedPerson.cannot.change.state.from.accepted.candidacies");
        }
    }

    void editSelectedDegree(final Degree selectedDegree) {
        setSelectedDegree(selectedDegree);
    }

    @Override
    protected ExecutionYear getCandidacyExecutionInterval() {
        return (ExecutionYear) super.getCandidacyExecutionInterval();
    }

    @Override
    protected Registration createRegistration(Person person, DegreeCurricularPlan degreeCurricularPlan, CycleType cycleType,
            Ingression ingression) {
        final Registration registration = super.createRegistration(person, degreeCurricularPlan, cycleType, ingression);
        registration.setRegistrationYear(getCandidacyExecutionInterval());
        return registration;
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
        formatter.format("%s: %f\n", BundleUtil.getString(Bundle.ACADEMIC, "label.SecondCycleIndividualCandidacy.affinity"),
                getAffinity() != null ? getAffinity() : BigDecimal.ZERO);
        formatter.format("%s: %d\n", BundleUtil.getString(Bundle.ACADEMIC, "label.SecondCycleIndividualCandidacy.degreeNature"),
                getDegreeNature() != null ? getDegreeNature() : 0);
        formatter.format("%s: %f\n", BundleUtil.getString(Bundle.ACADEMIC, "label.SecondCycleIndividualCandidacy.candidacyGrade"),
                getCandidacyGrade() != null ? getCandidacyGrade() : BigDecimal.ZERO);
        formatter.close();
    }

    @Override
    public String getDescription() {
        return getCandidacyProcess().getDisplayName() + (hasSelectedDegree() ? ": " + getSelectedDegree().getNameI18N() : "");
    }

    @Override
    public boolean isDegreeCandidacyForGraduatedPerson() {
        return true;
    }

    public DegreeCandidacyForGraduatedPersonSeriesGade getDegreeCandidacyForGraduatedPersonSeriesGadeForDegree(Degree degree) {
        for (IndividualCandidacySeriesGrade seriesGrade : getIndividualCandidacySeriesGrade()) {
            if (seriesGrade.getDegree() == degree) {
                return (DegreeCandidacyForGraduatedPersonSeriesGade) seriesGrade;
            }
        }
        return null;
    }

    private DegreeCandidacyForGraduatedPersonSeriesGade getDegreeCandidacyForGraduatedPersonSeriesGade() {
        if (getIndividualCandidacySeriesGrade().size() == 0) {
            return null;
        } else {
            return (DegreeCandidacyForGraduatedPersonSeriesGade) getIndividualCandidacySeriesGrade().iterator().next();
        }
    }

    @Override
    public BigDecimal getAffinity() {
        if (getDegreeCandidacyForGraduatedPersonSeriesGade() != null) {
            return getDegreeCandidacyForGraduatedPersonSeriesGade().getAffinity();
        } else {
            return null;
        }
    }

    @Override
    public Integer getDegreeNature() {
        if (getDegreeCandidacyForGraduatedPersonSeriesGade() != null) {
            return getDegreeCandidacyForGraduatedPersonSeriesGade().getDegreeNature();
        } else {
            return null;
        }
    }

    @Override
    public BigDecimal getCandidacyGrade() {
        if (getDegreeCandidacyForGraduatedPersonSeriesGade() != null) {
            return getDegreeCandidacyForGraduatedPersonSeriesGade().getCandidacyGrade();
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
        getDegreeCandidacyForGraduatedPersonSeriesGade().setDegree(selectedDegree);
        super.setSelectedDegree(selectedDegree);
    }

    @Override
    public void setAffinity(BigDecimal value) {
        getDegreeCandidacyForGraduatedPersonSeriesGade().setAffinity(value);
    }

    @Override
    public void setDegreeNature(Integer value) {
        getDegreeCandidacyForGraduatedPersonSeriesGade().setDegreeNature(value);
    }

    @Override
    public void setCandidacyGrade(BigDecimal value) {
        getDegreeCandidacyForGraduatedPersonSeriesGade().setCandidacyGrade(value);
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
    public boolean hasSelectedDegree() {
        return getSelectedDegree() != null;
    }

    @Deprecated
    public boolean hasAffinity() {
        return getAffinity() != null;
    }

}
