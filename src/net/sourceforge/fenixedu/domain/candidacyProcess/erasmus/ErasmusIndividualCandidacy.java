package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.LocalDate;

public class ErasmusIndividualCandidacy extends ErasmusIndividualCandidacy_Base {

    public ErasmusIndividualCandidacy() {
	super();
    }

    ErasmusIndividualCandidacy(final ErasmusIndividualCandidacyProcess process, final ErasmusIndividualCandidacyProcessBean bean) {
	this();

	Person person = init(bean, process);

	setSelectedDegree(bean.getSelectedDegree());

	createEramusStudentData(bean);

	associateCurricularCourses(bean.getSelectedCurricularCourses());

    }

    private void associateCurricularCourses(Set<CurricularCourse> selectedCurricularCourses) {
	for (CurricularCourse curricularCourse : selectedCurricularCourses) {
	    addCurricularCourses(curricularCourse);
	}
    }

    private void createEramusStudentData(ErasmusIndividualCandidacyProcessBean bean) {
	setErasmusStudentData(new ErasmusStudentData(this, bean.getErasmusStudentDataBean()));

    }

    @Override
    protected void createDebt(final Person person) {

    }

    @Override
    protected void checkParameters(final Person person, final IndividualCandidacyProcess process,
	    final IndividualCandidacyProcessBean bean) {
	ErasmusIndividualCandidacyProcess secondCycleIndividualCandidacyProcess = (ErasmusIndividualCandidacyProcess) process;
	ErasmusIndividualCandidacyProcessBean secondCandidacyProcessBean = (ErasmusIndividualCandidacyProcessBean) bean;
	LocalDate candidacyDate = bean.getCandidacyDate();
	Degree selectedDegree = secondCandidacyProcessBean.getSelectedDegree();

	checkParameters(person, secondCycleIndividualCandidacyProcess, candidacyDate, selectedDegree);
    }

    private void checkParameters(final Person person, final ErasmusIndividualCandidacyProcess process,
	    final LocalDate candidacyDate, final Degree degree) {

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

	if (degree == null) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.degree");
	}
    }

    void editDegreeAndCoursesInformation(ErasmusIndividualCandidacyProcessBean bean) {
	this.setSelectedDegree(bean.getSelectedDegree());

	Set<CurricularCourse> setOne = new HashSet<CurricularCourse>(this.getCurricularCourses());
	setOne.addAll(bean.getSelectedCurricularCourses());

	for (CurricularCourse curricularCourse : this.getCurricularCourses()) {
	    if (hasCurricularCourses(curricularCourse) && !bean.getSelectedCurricularCourses().contains(curricularCourse)) {
		removeCurricularCourses(curricularCourse);
	    } else if (!hasCurricularCourses(curricularCourse) && bean.getSelectedCurricularCourses().contains(curricularCourse)) {
		addCurricularCourses(curricularCourse);
	    }
	}
    }

}
