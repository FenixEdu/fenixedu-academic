package net.sourceforge.fenixedu.domain.enrolment;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;

public class EnrolmentContext {

    private StudentCurricularPlan studentCurricularPlan;

    private ExecutionSemester executionSemester;

    private Set<IDegreeModuleToEvaluate> degreeModulesToEvaluate;

    private List<CurriculumModule> curriculumModulesToRemove;

    private CurricularRuleLevel curricularRuleLevel;

    private Person responsiblePerson;

    public EnrolmentContext(final Person responsiblePerson, final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionSemester executionSemester, final Set<IDegreeModuleToEvaluate> degreeModulesToEnrol,
	    final List<CurriculumModule> curriculumModulesToRemove, final CurricularRuleLevel curricularRuleLevel) {

	this.responsiblePerson = responsiblePerson;
	this.studentCurricularPlan = studentCurricularPlan;

	this.degreeModulesToEvaluate = new HashSet<IDegreeModuleToEvaluate>();
	for (final IDegreeModuleToEvaluate moduleToEnrol : degreeModulesToEnrol) {
	    if (curriculumModulesToRemove.contains(moduleToEnrol.getCurriculumGroup())) {
		throw new DomainException(
			"error.StudentCurricularPlan.cannot.remove.enrollment.on.curriculum.group.because.other.enrollments.depend.on.it",
			moduleToEnrol.getCurriculumGroup().getName().getContent());
	    }

	    this.addDegreeModuleToEvaluate(moduleToEnrol);
	}

	this.executionSemester = executionSemester;
	this.curriculumModulesToRemove = curriculumModulesToRemove;
	this.curricularRuleLevel = curricularRuleLevel;
    }

    public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate() {
	return degreeModulesToEvaluate;
    }

    public Set<IDegreeModuleToEvaluate> getAllChildDegreeModulesToEvaluateFor(final DegreeModule degreeModule) {
	final Set<IDegreeModuleToEvaluate> result = new HashSet<IDegreeModuleToEvaluate>();
	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : this.degreeModulesToEvaluate) {
	    if (degreeModule.hasDegreeModule(degreeModuleToEvaluate.getDegreeModule())) {
		result.add(degreeModuleToEvaluate);
	    }
	}

	return result;
    }

    public void addDegreeModuleToEvaluate(final IDegreeModuleToEvaluate degreeModuleToEvaluate) {
	getDegreeModulesToEvaluate().add(degreeModuleToEvaluate);
    }

    public ExecutionSemester getExecutionPeriod() {
	return executionSemester;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
	this.executionSemester = executionSemester;
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
	return studentCurricularPlan;
    }

    public Registration getRegistration() {
	return studentCurricularPlan.getRegistration();
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
	this.studentCurricularPlan = studentCurricularPlan;
    }

    public List<CurriculumModule> getToRemove() {
	return curriculumModulesToRemove;
    }

    public CurricularRuleLevel getCurricularRuleLevel() {
	return curricularRuleLevel;
    }

    public void setCurricularRuleLevel(CurricularRuleLevel curricularRuleLevel) {
	this.curricularRuleLevel = curricularRuleLevel;
    }

    public Person getResponsiblePerson() {
	return responsiblePerson;
    }

    public void setResponsiblePerson(Person responsiblePerson) {
	this.responsiblePerson = responsiblePerson;
    }

    public boolean hasResponsiblePerson() {
	return getResponsiblePerson() != null;
    }

    public boolean isImprovement() {
	return getCurricularRuleLevel() == CurricularRuleLevel.IMPROVEMENT_ENROLMENT;
    }

    public boolean isSpecialSeason() {
	return getCurricularRuleLevel() == CurricularRuleLevel.SPECIAL_SEASON_ENROLMENT;
    }

    public boolean isExtra() {
	return getCurricularRuleLevel() == CurricularRuleLevel.EXTRA_ENROLMENT;
    }

    public boolean isPropaeudeutics() {
	return getCurricularRuleLevel() == CurricularRuleLevel.PROPAEUDEUTICS_ENROLMENT;
    }

    public boolean isStandalone() {
	return getCurricularRuleLevel() == CurricularRuleLevel.STANDALONE_ENROLMENT;
    }

    @SuppressWarnings("unchecked")
    static public EnrolmentContext createForVerifyWithRules(final Person person,
	    final StudentCurricularPlan studentCurricularPlan, final ExecutionSemester executionSemester) {
	return createForVerifyWithRules(person, studentCurricularPlan, executionSemester, Collections.EMPTY_SET);
    }

    @SuppressWarnings("unchecked")
    static public EnrolmentContext createForVerifyWithRules(final Person person,
	    final StudentCurricularPlan studentCurricularPlan, final ExecutionSemester executionSemester,
	    final Set<IDegreeModuleToEvaluate> degreeModulesToEvaluate) {
	return new EnrolmentContext(person, studentCurricularPlan, executionSemester, degreeModulesToEvaluate,
		Collections.EMPTY_LIST, CurricularRuleLevel.ENROLMENT_WITH_RULES);
    }

    @SuppressWarnings("unchecked")
    static public EnrolmentContext createForNoCourseGroupCurriculumGroupEnrolment(final Person person,
	    final StudentCurricularPlan studentCurricularPlan, final CurricularCourse curricularCourse,
	    final ExecutionSemester executionSemester, final NoCourseGroupCurriculumGroupType groupType) {

	final IDegreeModuleToEvaluate moduleToEvaluate = new ExternalCurricularCourseToEnrol(
		readOrCreateNoCourseGroupCurriculumGroup(studentCurricularPlan, groupType), curricularCourse, executionSemester);

	return new EnrolmentContext(person, studentCurricularPlan, executionSemester, Collections.singleton(moduleToEvaluate),
		Collections.EMPTY_LIST, groupType.getCurricularRuleLevel());
    }

    static private NoCourseGroupCurriculumGroup readOrCreateNoCourseGroupCurriculumGroup(
	    final StudentCurricularPlan studentCurricularPlan, final NoCourseGroupCurriculumGroupType groupType) {
	NoCourseGroupCurriculumGroup group = studentCurricularPlan.getNoCourseGroupCurriculumGroup(groupType);
	if (group == null) {
	    group = studentCurricularPlan.createNoCourseGroupCurriculumGroup(groupType);
	}
	return group;
    }

}
