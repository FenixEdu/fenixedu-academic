package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class BolonhaStudentEnrollmentBean implements Serializable {

    private DomainReference<StudentCurricularPlan> studentCurricularPlan;

    private DomainReference<ExecutionPeriod> executionPeriod;

    private StudentCurriculumGroupBean rootStudentCurriculumGroupBean;

    private List<DegreeModuleToEnrol> degreeModulesToEnrol;

    private List<DomainReference<CurriculumModule>> curriculumModulesToRemove;

    private DegreeModuleToEnrol optionalDegreeModuleToEnrol;

    private BolonhaStudentEnrollmentBean(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionPeriod executionPeriod,
	    final StudentCurriculumGroupBean rootStudentCurriculumGroupBean) {
	super();

	setStudentCurricularPlan(studentCurricularPlan);
	setExecutionPeriod(executionPeriod);
	setRootStudentCurriculumGroupBean(rootStudentCurriculumGroupBean);

	setDegreeModulesToEnrol(new ArrayList<DegreeModuleToEnrol>());
	setCurriculumModulesToRemove(new ArrayList<CurriculumModule>());

    }

    public static BolonhaStudentEnrollmentBean create(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionPeriod executionPeriod, final int[] curricularYears) {

	return new BolonhaStudentEnrollmentBean(studentCurricularPlan, executionPeriod,
		StudentCurriculumGroupBean.create(studentCurricularPlan.getRoot(), executionPeriod,
			curricularYears));
    }

    public static BolonhaStudentEnrollmentBean create(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionPeriod executionPeriod) {
	return create(studentCurricularPlan, executionPeriod, null);
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
	return (this.studentCurricularPlan != null) ? this.studentCurricularPlan.getObject() : null;
    }

    private void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
	this.studentCurricularPlan = (studentCurricularPlan != null) ? new DomainReference<StudentCurricularPlan>(
		studentCurricularPlan)
		: null;
    }

    public ExecutionPeriod getExecutionPeriod() {
	return (this.executionPeriod != null) ? this.executionPeriod.getObject() : null;
    }

    private void setExecutionPeriod(ExecutionPeriod executionPeriod) {
	this.executionPeriod = (executionPeriod != null) ? new DomainReference<ExecutionPeriod>(
		executionPeriod) : null;
    }

    public StudentCurriculumGroupBean getRootStudentCurriculumGroupBean() {
	return rootStudentCurriculumGroupBean;
    }

    public void setRootStudentCurriculumGroupBean(StudentCurriculumGroupBean studentCurriculumGroupBean) {
	this.rootStudentCurriculumGroupBean = studentCurriculumGroupBean;
    }

    public List<DegreeModuleToEnrol> getDegreeModulesToEnrol() {
	return degreeModulesToEnrol;
    }

    public void setDegreeModulesToEnrol(List<DegreeModuleToEnrol> degreeModulesToEnrol) {
	this.degreeModulesToEnrol = degreeModulesToEnrol;
    }

    public DegreeModuleToEnrol getOptionalDegreeModuleToEnrol() {
	return optionalDegreeModuleToEnrol;
    }

    public void setOptionalDegreeModuleToEnrol(DegreeModuleToEnrol optionalDegreeModuleToEnrol) {
	this.optionalDegreeModuleToEnrol = optionalDegreeModuleToEnrol;
    }

    public List<CurriculumModule> getCurriculumModulesToRemove() {
	final List<CurriculumModule> result = new ArrayList<CurriculumModule>();

	for (final DomainReference<CurriculumModule> domainReference : this.curriculumModulesToRemove) {
	    result.add(domainReference.getObject());
	}

	return result;
    }

    public void setCurriculumModulesToRemove(List<CurriculumModule> curriculumModules) {
	this.curriculumModulesToRemove = new ArrayList<DomainReference<CurriculumModule>>();

	for (final CurriculumModule curriculumModule : curriculumModules) {
	    this.curriculumModulesToRemove.add(new DomainReference<CurriculumModule>(curriculumModule));
	}
    }

}
