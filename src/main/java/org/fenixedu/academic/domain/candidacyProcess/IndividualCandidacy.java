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
package org.fenixedu.academic.domain.candidacyProcess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.EntryPhase;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.candidacy.DegreeCandidacy;
import org.fenixedu.academic.domain.candidacy.IMDCandidacy;
import org.fenixedu.academic.domain.candidacy.IngressionType;
import org.fenixedu.academic.domain.candidacy.MDCandidacy;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.PersonalIngressionData;
import org.fenixedu.academic.domain.student.PrecedentDegreeInformation;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.person.ChoosePersonBean;
import org.fenixedu.academic.dto.person.PersonBean;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.signals.Signal;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

abstract public class IndividualCandidacy extends IndividualCandidacy_Base {

    public final static String ACCEPTED = "candidacy.accepted";
    
    protected IndividualCandidacy() {
        super();
        super.setWhenCreated(new DateTime());
        setRootDomainObject(Bennu.getInstance());

    }

    protected Person init(final IndividualCandidacyProcessBean bean, final IndividualCandidacyProcess process) {
        /*
         * 31/03/2009 - Now the person may be created inside init() method
         * 
         * 06/04/2009 - All subclasses share the code below. So the
         * checkParameters() is now abstract
         */

        /*
         * 08/05/2009 - Now all candidacies are external (even made in academic
         * administrative office)
         */
        /*
         * 06/07/2009 - Due to payments the applications will be created with an
         * associated person. This person might be created or associated with an
         * existing.
         */
        Person person = null;
        if (bean.getInternalPersonCandidacy().booleanValue()) {
            person = bean.getOrCreatePersonFromBean();
        }

        checkParameters(person, process, bean);
        bean.getPersonBean().setPerson(person);

        IndividualCandidacyPersonalDetails.createDetails(this, bean);
        setCandidacyProcess(process);
        setCandidacyDate(bean.getCandidacyDate());
        setState(IndividualCandidacyState.STAND_BY);
        editObservations(bean);
        setUtlStudent(bean.getUtlStudent());

        createPrecedentDegreeInformation(bean);

        return person;
    }

    /**
     * 06/04/2009 All subclasses of IndividualCandidacy call a checkParameters()
     * in their constructor. The arguments of checkParameters varies from
     * subclass to subclass but they come from Person,
     * IndividualCandidacyProcess and IndividualCandidacyProcessBean
     * 
     * @param person
     * @param process
     * @param bean
     */
    protected abstract void checkParameters(final Person person, final IndividualCandidacyProcess process,
            final IndividualCandidacyProcessBean bean);

    protected void checkParameters(final Person person, final IndividualCandidacyProcess process, final LocalDate candidacyDate) {
        /*
         * 31/03/2009 - The candidacy will not be associated with a Person if it
         * is submited externally (not in administrative office)
         * 
         * 
         * if (person == null) { throw new
         * DomainException("error.IndividualCandidacy.invalid.person"); }
         */

        if (process == null) {
            throw new DomainException("error.IndividualCandidacy.invalid.process");
        }
        if (candidacyDate == null || !process.hasOpenCandidacyPeriod(candidacyDate.toDateTimeAtCurrentTime())) {
            throw new DomainException("error.IndividualCandidacy.invalid.candidacyDate", process.getCandidacyStart().toString(
                    "dd/MM/yyyy"), process.getCandidacyEnd().toString("dd/MM/yyyy"));
        }
    }

    @Override
    public void setWhenCreated(DateTime whenCreated) {
        throw new DomainException("error.IndividualCandidacy.cannot.modify.when.created");
    }

    public boolean hasAnyPayment() {
        return getEvent() != null && getEvent().hasAnyPayments();
    }

    public void editPersonalCandidacyInformation(final PersonBean personBean) {
        getPersonalDetails().edit(personBean);
    }

    public void editPersonalCandidacyInformationPublic(final PersonBean personBean) {
        getPersonalDetails().editPublic(personBean);
    }

    public void cancel(final Person person) {
        checkRulesToCancel();
        setState(IndividualCandidacyState.CANCELLED);
        setResponsible(person.getUsername());
        if (getEvent() != null) {
            getEvent().cancel(person, "IndividualCandidacy.canceled");
        }
    }

    public void reject(final Person person) {
        setState(IndividualCandidacyState.REJECTED);
        setResponsible(person.getUsername());
    }

    public void revertToStandBy(final Person person) {
        setState(IndividualCandidacyState.STAND_BY);
        setResponsible(person.getUsername());
    }

    protected void checkRulesToCancel() {
        if (getEvent() != null && hasAnyPayment()) {
            throw new DomainException("error.IndividualCandidacy.cannot.cancel.candidacy.with.payments");
        }
    }

    public Person getResponsiblePerson() {
        return Person.readPersonByUsername(getResponsible());
    }

    public boolean isInStandBy() {
        return getState() == IndividualCandidacyState.STAND_BY;
    }

    public boolean isAccepted() {
        return getState() == IndividualCandidacyState.ACCEPTED;
    }

    public boolean isNotAccepted() {
        return getState() == IndividualCandidacyState.NOT_ACCEPTED;
    }

    public boolean isCancelled() {
        return getState() == IndividualCandidacyState.CANCELLED;
    }

    public boolean isRejected() {
        return getState() == IndividualCandidacyState.REJECTED;
    }

    public boolean isDebtPayed() {
        return getEvent() == null || (getEvent() != null && getEvent().isClosed());
    }

    public boolean isFor(final ExecutionInterval executionInterval) {
        return getCandidacyProcess() != null && getCandidacyProcess().isFor(executionInterval);
    }

    @Override
    public void setState(IndividualCandidacyState state) {
        super.setState(state);
        if (state == IndividualCandidacyState.ACCEPTED) {
           Signal.emit(ACCEPTED, this); 
        }
    }
    
    protected boolean isCandidacyResultStateValid(final IndividualCandidacyState state) {
        return state == IndividualCandidacyState.ACCEPTED || state == IndividualCandidacyState.REJECTED;
    }

    protected void createPrecedentDegreeInformation(final IndividualCandidacyProcessBean processBean) {
        PrecedentDegreeInformationForIndividualCandidacyFactory.create(this, processBean);
    }

    public Registration createRegistration(final DegreeCurricularPlan degreeCurricularPlan, final CycleType cycleType,
            final IngressionType ingressionType) {

        if (getRegistration() != null) {
            throw new DomainException("error.IndividualCandidacy.person.with.registration",
                    degreeCurricularPlan.getPresentationName());
        }

        if (hasActiveRegistration(degreeCurricularPlan)) {
            final Registration registration = getStudent().getActiveRegistrationFor(degreeCurricularPlan);
            setRegistration(registration);
            return registration;
        }

        getPersonalDetails().ensurePersonInternalization();
        return createRegistration(getPersonalDetails().getPerson(), degreeCurricularPlan, cycleType, ingressionType);
    }

    protected Registration createRegistration(final Person person, final DegreeCurricularPlan degreeCurricularPlan,
            final CycleType cycleType, final IngressionType ingressionType) {

        final Registration registration = new Registration(person, degreeCurricularPlan, cycleType);
        registration.setEntryPhase(EntryPhase.FIRST_PHASE);
        registration.setIngressionType(ingressionType);
        registration.editStartDates(getStartDate(), registration.getHomologationDate(), registration.getStudiesStartDate());

        createRaidesInformation(registration);

        setRegistration(registration);

        return registration;
    }

    protected void createRaidesInformation(Registration registration) {
        Degree degree = registration.getDegree();
        ExecutionYear startExecutionYear = registration.getStartExecutionYear();
        ExecutionDegree executionDegree =
                ExecutionDegree.getAllByDegreeAndExecutionYear(degree, startExecutionYear.getName()).iterator().next();
        StudentCandidacy studentCandidacy = null;

        if (registration.getDegree().getDegreeType().isIntegratedMasterDegree()) {
            studentCandidacy = new IMDCandidacy(registration.getPerson(), executionDegree);
        } else if (registration.getDegree().getDegreeType().isDegree()) {
            studentCandidacy = new DegreeCandidacy(registration.getPerson(), executionDegree);
        } else if (registration.getDegree().getDegreeType().isMasterDegree()) {
            studentCandidacy = new MDCandidacy(registration.getPerson(), executionDegree);
        }

        studentCandidacy.getPrecedentDegreeInformation().delete();
        PrecedentDegreeInformation refactoredPrecedentDegreeInformation = getRefactoredPrecedentDegreeInformation();
        refactoredPrecedentDegreeInformation.setRegistration(registration);
        studentCandidacy.setPrecedentDegreeInformation(refactoredPrecedentDegreeInformation);
        studentCandidacy.setRegistration(registration);

        PersonalIngressionData personalIngressionDataByExecutionYear =
                registration.getStudent().getPersonalIngressionDataByExecutionYear(startExecutionYear);
        if (personalIngressionDataByExecutionYear != null) {
            personalIngressionDataByExecutionYear.addPrecedentDegreesInformations(refactoredPrecedentDegreeInformation);
        } else {
            new PersonalIngressionData(registration.getStudent(), startExecutionYear, refactoredPrecedentDegreeInformation);
        }
    }

    protected boolean hasActiveRegistration(final DegreeCurricularPlan degreeCurricularPlan) {
        return getPersonalDetails().hasStudent()
                && getPersonalDetails().getStudent().hasActiveRegistrationFor(degreeCurricularPlan);
    }

    protected YearMonthDay getStartDate() {
        final ExecutionInterval interval = getCandidacyExecutionInterval();
        final YearMonthDay today = new YearMonthDay();
        return interval.isCurrent() && interval.getAcademicInterval().contains(today.toDateMidnight()) ? today : interval
                .getBeginDateYearMonthDay();
    }

    public Student getStudent() {
        return getPersonalDetails().getStudent();
    }

    public boolean hasStudent() {
        return getStudent() != null;
    }

    protected ExecutionInterval getCandidacyExecutionInterval() {
        return getCandidacyProcess() != null ? getCandidacyProcess().getCandidacyExecutionInterval() : null;
    }

    abstract public Collection<Degree> getAllDegrees();

    protected boolean personHasDegree(final Person person, final Degree selectedDegree) {

        if (person.getStudent() == null) {
            return false;
        }

        List<Registration> registrationsFor = getNotCanceledRegistrationsFor(person, selectedDegree);
        if (registrationsFor.isEmpty()) {
            return false;
        }

        if ((registrationsFor.size() == 1) && registrationsFor.iterator().next() == getRegistration()) {
            return false;
        }

        return true;
    }

    private List<Registration> getNotCanceledRegistrationsFor(final Person person, final Degree selectedDegree) {
        return person.getStudent().getRegistrationsFor(selectedDegree).stream()
                .filter(registration -> !registration.isCanceled())
                .collect(Collectors.toList());
    }

    protected boolean personHasOneOfDegrees(final Person person, final Set<Degree> selectedDegrees) {
        if (person.getStudent() == null) {
            return false;
        }

        return selectedDegrees.stream().anyMatch(degree -> person.getStudent().hasActiveRegistrationFor(degree));
    }

    public void editObservations(final IndividualCandidacyProcessBean bean) {
        this.setObservations(bean.getObservations());
    }

    public Boolean isCandidacyInternal() {
        return this.getPersonalDetails() instanceof IndividualCandidacyInternalPersonDetails;
    }

    protected abstract void createDebt(final Person person);

    public void bindPerson(ChoosePersonBean bean) {
        if (this.isCandidacyInternal()) {
            throw new DomainException("error.bind.candidacy.internal");
        }

        Person selectedPerson = bean.getPerson();
        if (selectedPerson != null) {
            selectedPerson.edit(this.getPersonalDetails());
            this.setPersonalDetails(new IndividualCandidacyInternalPersonDetails(this, selectedPerson));
        } else {
            selectedPerson = new Person(this.getPersonalDetails(), false);
            this.setPersonalDetails(new IndividualCandidacyInternalPersonDetails(this, selectedPerson));
        }

        createDebt(this.getPersonalDetails().getPerson());
    }

    protected void createFormationEntries(List<FormationBean> formationConcludedBeanList,
            List<FormationBean> formationNonConcludedBeanList) {
        for (FormationBean formation : formationConcludedBeanList) {
            this.addFormations(new Formation(this, formation));
        }

        for (FormationBean formation : formationNonConcludedBeanList) {
            this.addFormations(new Formation(this, formation));
        }
    }

    public List<Formation> getConcludedFormationList() {
        return getFormationsSet().stream().filter(Formation::getConcluded).collect(Collectors.toList());
    }

    public List<Formation> getNonConcludedFormationList() {
        return getFormationsSet().stream().filter(formation -> !formation.getConcluded()).collect(Collectors.toList());
    }

    public void editFormationEntries(List<FormationBean> formationConcludedBeanList,
            List<FormationBean> formationNonConcludedBeanList) {
        List<Formation> formationsToBeRemovedList = new ArrayList<Formation>();
        for (final Formation formation : this.getFormationsSet()) {
            if (formation.getConcluded()) {
                editFormationEntry(formationConcludedBeanList, formationsToBeRemovedList, formation);
            }
        }

        for (final Formation formation : this.getFormationsSet()) {
            if (!formation.getConcluded()) {
                editFormationEntry(formationNonConcludedBeanList, formationsToBeRemovedList, formation);
            }
        }

        for (Formation formation : formationsToBeRemovedList) {
            this.getFormationsSet().remove(formation);
            formation.delete();
        }

        for (FormationBean bean : formationConcludedBeanList) {
            if (bean.getFormation() == null) {
                this.addFormations(new Formation(this, bean));
            }
        }

        for (FormationBean bean : formationNonConcludedBeanList) {
            if (bean.getFormation() == null) {
                this.addFormations(new Formation(this, bean));
            }
        }
    }

    private void editFormationEntry(List<FormationBean> formationConcludedBeanList, List<Formation> formationsToBeRemovedList,
            final Formation formation) {
        FormationBean bean = (FormationBean) CollectionUtils.find(formationConcludedBeanList, arg0 -> {
            FormationBean bean1 = (FormationBean) arg0;
            return bean1.getFormation() == formation;
        });

        if (bean == null) {
            formationsToBeRemovedList.add(formation);
        } else {
            formation.edit(bean);
        }
    }

    public void exportValues(final StringBuilder result) {
        Formatter formatter = new Formatter(result);

        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.IndividualCandidacy.candidacy"),
                getCandidacyExecutionInterval().getName());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.IndividualCandidacy.state"), getState()
                .getLocalizedName());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.IndividualCandidacy.whenCreated"),
                getWhenCreated().toString("yyy-MM-dd"));
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.IndividualCandidacy.candidacyDate"),
                getCandidacyDate().toString());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.IndividualCandidacy.responsible"),
                StringUtils.isEmpty(getResponsible()) ? StringUtils.EMPTY : getResponsible());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.IndividualCandidacy.notes"),
                StringUtils.isEmpty(getNotes()) ? StringUtils.EMPTY : getNotes());

        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.IndividualCandidacy.observations"),
                StringUtils.isEmpty(getObservations()) ? StringUtils.EMPTY : getObservations());

        for (final Formation formation : getFormationsSet()) {
            formation.exportValues(result);
        }

        formatter.close();
    }

    abstract public String getDescription();

    public boolean isDegreeCandidacyForGraduatedPerson() {
        return false;
    }

    public boolean isDegreeChange() {
        return false;
    }

    public boolean isDegreeTransfer() {
        return false;
    }

    public boolean isErasmus() {
        return false;
    }

    public boolean isOver23() {
        return false;
    }

    public boolean isSecondCycle() {
        return false;
    }

    public boolean isStandalone() {
        return false;
    }

    void editPrecedentDegreeInformation(IndividualCandidacyProcessBean bean) {
        PrecedentDegreeInformationForIndividualCandidacyFactory.edit(bean);
    }

}
