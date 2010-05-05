package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;

import org.joda.time.LocalDate;

public class ErasmusIndividualCandidacy extends ErasmusIndividualCandidacy_Base {

    public ErasmusIndividualCandidacy() {
	super();
    }

    ErasmusIndividualCandidacy(final ErasmusIndividualCandidacyProcess process, final ErasmusIndividualCandidacyProcessBean bean) {
	this();

	if ("Raul Gonzalez".equals(bean.getPersonBean().getName())) {
	    bean.getPersonBean().setPerson(Person.readPersonByUsername("ist90427"));
	} else if ("Javier Garcia".equals(bean.getPersonBean().getName())) {
	    bean.getPersonBean().setPerson(Person.readPersonByUsername("ist90428"));
	}

	bean.getPersonBean().setCreateLoginIdentificationAndUserIfNecessary(false);

	Person person = init(bean, process);

	createEramusStudentData(bean);

	associateCurricularCourses(bean.getSelectedCurricularCourses());
    }

    private void associateCurricularCourses(Set<CurricularCourse> selectedCurricularCourses) {
	for (CurricularCourse curricularCourse : selectedCurricularCourses) {
	    addCurricularCourses(curricularCourse);
	}
    }

    private void createEramusStudentData(ErasmusIndividualCandidacyProcessBean bean) {
	setErasmusStudentData(new ErasmusStudentData(this, bean.getErasmusStudentDataBean(), bean.calculateErasmusVacancy()));
    }

    @Override
    protected void createDebt(final Person person) {

    }

    @Override
    protected void checkParameters(final Person person, final IndividualCandidacyProcess process,
	    final IndividualCandidacyProcessBean bean) {
	ErasmusIndividualCandidacyProcess erasmusIndividualCandidacyProcess = (ErasmusIndividualCandidacyProcess) process;
	ErasmusIndividualCandidacyProcessBean secondCandidacyProcessBean = (ErasmusIndividualCandidacyProcessBean) bean;
	LocalDate candidacyDate = bean.getCandidacyDate();

	checkParameters(person, erasmusIndividualCandidacyProcess, candidacyDate, null);
    }

    private void checkParameters(final Person person, final ErasmusIndividualCandidacyProcess process,
	    final LocalDate candidacyDate, Object dummy) {

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
    }

    void editDegreeAndCoursesInformation(ErasmusIndividualCandidacyProcessBean bean) {
	Set<CurricularCourse> setOne = new HashSet<CurricularCourse>(this.getCurricularCourses());
	setOne.addAll(bean.getSelectedCurricularCourses());

	getErasmusStudentData().setSelectedVacancy(bean.calculateErasmusVacancy());

	for (CurricularCourse curricularCourse : setOne) {
	    if (hasCurricularCourses(curricularCourse) && !bean.getSelectedCurricularCourses().contains(curricularCourse)) {
		removeCurricularCourses(curricularCourse);
	    } else if (!hasCurricularCourses(curricularCourse) && bean.getSelectedCurricularCourses().contains(curricularCourse)) {
		addCurricularCourses(curricularCourse);
	    }
	}
    }

    public Degree getSelectedDegree() {
	return getErasmusStudentData().getSelectedVacancy().getDegree();
    }

    protected boolean hasSelectedDegree() {
	return getSelectedDegree() != null;
    }

    @Override
    public String getDescription() {
	return getCandidacyProcess().getDisplayName() + (hasSelectedDegree() ? ": " + getSelectedDegree().getNameI18N() : "");
    }

    @Override
    public ErasmusIndividualCandidacyProcess getCandidacyProcess() {
	return (ErasmusIndividualCandidacyProcess) super.getCandidacyProcess();
    }

}
