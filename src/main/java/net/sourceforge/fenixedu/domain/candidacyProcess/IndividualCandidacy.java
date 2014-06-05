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
package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.DegreeCandidacy;
import net.sourceforge.fenixedu.domain.candidacy.IMDCandidacy;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacy.MDCandidacy;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.PersonalIngressionData;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.DateFormatUtil;

abstract public class IndividualCandidacy extends IndividualCandidacy_Base {

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
                    DateFormatUtil.DEFAULT_DATE_FORMAT), process.getCandidacyEnd().toString(DateFormatUtil.DEFAULT_DATE_FORMAT));
        }
    }

    @Override
    public void setWhenCreated(DateTime whenCreated) {
        throw new DomainException("error.IndividualCandidacy.cannot.modify.when.created");
    }

    public boolean hasAnyPayment() {
        return hasEvent() && getEvent().hasAnyPayments();
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
        if (hasEvent()) {
            getEvent().cancel("IndividualCandidacy.canceled");
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
        if (hasEvent() && hasAnyPayment()) {
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
        return !hasEvent() || (hasEvent() && getEvent().isClosed());
    }

    public boolean isFor(final ExecutionInterval executionInterval) {
        return hasCandidacyProcess() && getCandidacyProcess().isFor(executionInterval);
    }

    protected boolean isCandidacyResultStateValid(final IndividualCandidacyState state) {
        return state == IndividualCandidacyState.ACCEPTED || state == IndividualCandidacyState.REJECTED;
    }

    protected void createPrecedentDegreeInformation(final IndividualCandidacyProcessBean processBean) {
        PrecedentDegreeInformationForIndividualCandidacyFactory.create(this, processBean);
    }

    public Registration createRegistration(final DegreeCurricularPlan degreeCurricularPlan, final CycleType cycleType,
            final Ingression ingression) {

        if (hasRegistration()) {
            throw new DomainException("error.IndividualCandidacy.person.with.registration",
                    degreeCurricularPlan.getPresentationName());
        }

        if (hasActiveRegistration(degreeCurricularPlan)) {
            final Registration registration = getStudent().getActiveRegistrationFor(degreeCurricularPlan);
            setRegistration(registration);
            return registration;
        }

        getPersonalDetails().ensurePersonInternalization();
        return createRegistration(getPersonalDetails().getPerson(), degreeCurricularPlan, cycleType, ingression);
    }

    protected Registration createRegistration(final Person person, final DegreeCurricularPlan degreeCurricularPlan,
            final CycleType cycleType, final Ingression ingression) {

        final Registration registration = new Registration(person, degreeCurricularPlan, cycleType);
        registration.setEntryPhase(EntryPhase.FIRST_PHASE);
        registration.setIngression(ingression);
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
        return interval.isCurrent() ? new YearMonthDay() : interval.getBeginDateYearMonthDay();
    }

    public Student getStudent() {
        return getPersonalDetails().getStudent();
    }

    public boolean hasStudent() {
        return getStudent() != null;
    }

    protected ExecutionInterval getCandidacyExecutionInterval() {
        return hasCandidacyProcess() ? getCandidacyProcess().getCandidacyExecutionInterval() : null;
    }

    abstract public Collection<Degree> getAllDegrees();

    protected boolean personHasDegree(final Person person, final Degree selectedDegree) {

        if (!person.hasStudent()) {
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
        List<Registration> registrationsFor = person.getStudent().getRegistrationsFor(selectedDegree);
        List<Registration> notCanceledRegistrationsForDegree = new ArrayList<Registration>();

        for (Registration registration : registrationsFor) {
            if (registration.isCanceled()) {
                continue;
            }

            notCanceledRegistrationsForDegree.add(registration);
        }

        return notCanceledRegistrationsForDegree;
    }

    protected boolean personHasOneOfDegrees(final Person person, final Set<Degree> selectedDegrees) {
        if (!person.hasStudent()) {
            return false;
        }

        for (Degree degree : selectedDegrees) {
            if (person.getStudent().hasActiveRegistrationFor(degree)) {
                return true;
            }
        }

        return false;
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
            if (!selectedPerson.hasRole(RoleType.EMPLOYEE)) {
                selectedPerson.edit(this.getPersonalDetails());
            }
            this.setPersonalDetails(new IndividualCandidacyInternalPersonDetails(this, selectedPerson));
        } else {
            selectedPerson = new Person(this.getPersonalDetails());
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
        return new ArrayList<Formation>(CollectionUtils.select(getFormations(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return ((Formation) arg0).getConcluded();
            }

        }));
    }

    public List<Formation> getNonConcludedFormationList() {
        return new ArrayList<Formation>(CollectionUtils.select(getFormations(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return !((Formation) arg0).getConcluded();
            }

        }));
    }

    public Boolean isCandidacyBelongsToStudent() {
        return this.isCandidacyInternal() && this.getPersonalDetails().getPerson().hasRole(RoleType.STUDENT);
    }

    public Boolean isCandidacyBelongsToEmployee() {
        return this.isCandidacyInternal() && this.getPersonalDetails().getPerson().hasRole(RoleType.EMPLOYEE);
    }

    public void editFormationEntries(List<FormationBean> formationConcludedBeanList,
            List<FormationBean> formationNonConcludedBeanList) {
        List<Formation> formationsToBeRemovedList = new ArrayList<Formation>();
        for (final Formation formation : this.getFormations()) {
            if (formation.getConcluded()) {
                editFormationEntry(formationConcludedBeanList, formationsToBeRemovedList, formation);
            }
        }

        for (final Formation formation : this.getFormations()) {
            if (!formation.getConcluded()) {
                editFormationEntry(formationNonConcludedBeanList, formationsToBeRemovedList, formation);
            }
        }

        for (Formation formation : formationsToBeRemovedList) {
            this.getFormations().remove(formation);
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
        FormationBean bean = (FormationBean) CollectionUtils.find(formationConcludedBeanList, new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                FormationBean bean = (FormationBean) arg0;
                return bean.getFormation() == formation;
            }
        });

        if (bean == null) {
            formationsToBeRemovedList.add(formation);
        } else {
            formation.edit(bean);
        }
    }

    public void exportValues(final StringBuilder result) {
        Formatter formatter = new Formatter(result);

        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.IndividualCandidacy.candidacy"), getCandidacyExecutionInterval()
                .getName());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.IndividualCandidacy.state"), getState().getLocalizedName());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.IndividualCandidacy.whenCreated"),
                getWhenCreated().toString("yyy-MM-dd"));
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.IndividualCandidacy.candidacyDate"), getCandidacyDate().toString());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.IndividualCandidacy.responsible"),
                StringUtils.isEmpty(getResponsible()) ? StringUtils.EMPTY : getResponsible());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.IndividualCandidacy.notes"),
                StringUtils.isEmpty(getNotes()) ? StringUtils.EMPTY : getNotes());

        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.ACADEMIC, "label.IndividualCandidacy.observations"),
                StringUtils.isEmpty(getObservations()) ? StringUtils.EMPTY : getObservations());

        for (final Formation formation : getFormations()) {
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

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.Formation> getFormations() {
        return getFormationsSet();
    }

    @Deprecated
    public boolean hasAnyFormations() {
        return !getFormationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile> getDocuments() {
        return getDocumentsSet();
    }

    @Deprecated
    public boolean hasAnyDocuments() {
        return !getDocumentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacySeriesGrade> getIndividualCandidacySeriesGrade() {
        return getIndividualCandidacySeriesGradeSet();
    }

    @Deprecated
    public boolean hasAnyIndividualCandidacySeriesGrade() {
        return !getIndividualCandidacySeriesGradeSet().isEmpty();
    }

    @Deprecated
    public boolean hasRegistration() {
        return getRegistration() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasHighSchoolType() {
        return getHighSchoolType() != null;
    }

    @Deprecated
    public boolean hasMotherSchoolLevel() {
        return getMotherSchoolLevel() != null;
    }

    @Deprecated
    public boolean hasGrantOwnerType() {
        return getGrantOwnerType() != null;
    }

    @Deprecated
    public boolean hasDistrictSubdivisionOfResidence() {
        return getDistrictSubdivisionOfResidence() != null;
    }

    @Deprecated
    public boolean hasCandidacyDate() {
        return getCandidacyDate() != null;
    }

    @Deprecated
    public boolean hasFatherProfessionType() {
        return getFatherProfessionType() != null;
    }

    @Deprecated
    public boolean hasProfessionalCondition() {
        return getProfessionalCondition() != null;
    }

    @Deprecated
    public boolean hasMotherProfessionType() {
        return getMotherProfessionType() != null;
    }

    @Deprecated
    public boolean hasCandidacyProcess() {
        return getCandidacyProcess() != null;
    }

    @Deprecated
    public boolean hasGrantOwnerProvider() {
        return getGrantOwnerProvider() != null;
    }

    @Deprecated
    public boolean hasUtlStudent() {
        return getUtlStudent() != null;
    }

    @Deprecated
    public boolean hasMaritalStatus() {
        return getMaritalStatus() != null;
    }

    @Deprecated
    public boolean hasFatherProfessionalCondition() {
        return getFatherProfessionalCondition() != null;
    }

    @Deprecated
    public boolean hasSpouseSchoolLevel() {
        return getSpouseSchoolLevel() != null;
    }

    @Deprecated
    public boolean hasSpouseProfessionType() {
        return getSpouseProfessionType() != null;
    }

    @Deprecated
    public boolean hasResponsible() {
        return getResponsible() != null;
    }

    @Deprecated
    public boolean hasFatherSchoolLevel() {
        return getFatherSchoolLevel() != null;
    }

    @Deprecated
    public boolean hasSchoolTimeDistrictSubDivisionOfResidence() {
        return getSchoolTimeDistrictSubDivisionOfResidence() != null;
    }

    @Deprecated
    public boolean hasCountryOfResidence() {
        return getCountryOfResidence() != null;
    }

    @Deprecated
    public boolean hasEvent() {
        return getEvent() != null;
    }

    @Deprecated
    public boolean hasWhenCreated() {
        return getWhenCreated() != null;
    }

    @Deprecated
    public boolean hasFormerStudentNumber() {
        return getFormerStudentNumber() != null;
    }

    @Deprecated
    public boolean hasDislocatedFromPermanentResidence() {
        return getDislocatedFromPermanentResidence() != null;
    }

    @Deprecated
    public boolean hasObservations() {
        return getObservations() != null;
    }

    @Deprecated
    public boolean hasSpouseProfessionalCondition() {
        return getSpouseProfessionalCondition() != null;
    }

    @Deprecated
    public boolean hasNotes() {
        return getNotes() != null;
    }

    @Deprecated
    public boolean hasRefactoredPrecedentDegreeInformation() {
        return getRefactoredPrecedentDegreeInformation() != null;
    }

    @Deprecated
    public boolean hasNumberOfFlunksOnHighSchool() {
        return getNumberOfFlunksOnHighSchool() != null;
    }

    @Deprecated
    public boolean hasState() {
        return getState() != null;
    }

    @Deprecated
    public boolean hasPrecedentDegreeInformation() {
        return getPrecedentDegreeInformation() != null;
    }

    @Deprecated
    public boolean hasPersonalDetails() {
        return getPersonalDetails() != null;
    }

    @Deprecated
    public boolean hasMotherProfessionalCondition() {
        return getMotherProfessionalCondition() != null;
    }

    @Deprecated
    public boolean hasProfessionType() {
        return getProfessionType() != null;
    }

    @Deprecated
    public boolean hasNumberOfCandidaciesToHigherSchool() {
        return getNumberOfCandidaciesToHigherSchool() != null;
    }

}
