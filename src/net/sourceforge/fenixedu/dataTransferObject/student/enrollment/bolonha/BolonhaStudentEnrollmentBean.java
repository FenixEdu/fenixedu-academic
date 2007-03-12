package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DegreeModuleToEnrolKeyConverter;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class BolonhaStudentEnrollmentBean implements Serializable {

    private DomainReference<StudentCurricularPlan> studentCurricularPlan;

    private DomainReference<ExecutionPeriod> executionPeriod;

    private StudentCurriculumGroupBean rootStudentCurriculumGroupBean;

    private List<IDegreeModuleToEvaluate> degreeModulesToEvaluate;

    private List<DomainReference<CurriculumModule>> curriculumModulesToRemove;

    private IDegreeModuleToEvaluate optionalDegreeModuleToEnrol;

    public BolonhaStudentEnrollmentBean(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionPeriod executionPeriod,
	    final int[] curricularYears) {
	this(studentCurricularPlan, executionPeriod, 
		new StudentCurriculumGroupBean(studentCurricularPlan.getRoot(), executionPeriod, curricularYears));
    }
    
    protected BolonhaStudentEnrollmentBean(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionPeriod executionPeriod,
	    final StudentCurriculumGroupBean rootStudentCurriculumGroupBean) {
	super();
	setStudentCurricularPlan(studentCurricularPlan);
	setExecutionPeriod(executionPeriod);
	setRootStudentCurriculumGroupBean(rootStudentCurriculumGroupBean);

	setDegreeModulesToEvaluate(new ArrayList<IDegreeModuleToEvaluate>());
	setCurriculumModulesToRemove(new ArrayList<CurriculumModule>());
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

    public List<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate() {
	return degreeModulesToEvaluate;
    }

    public void setDegreeModulesToEvaluate(List<IDegreeModuleToEvaluate> degreeModulesToEnrol) {
	this.degreeModulesToEvaluate = degreeModulesToEnrol;
    }

    public IDegreeModuleToEvaluate getOptionalDegreeModuleToEnrol() {
	return optionalDegreeModuleToEnrol;
    }

    public void setOptionalDegreeModuleToEnrol(IDegreeModuleToEvaluate optionalDegreeModuleToEnrol) {
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
    
    public Converter getDegreeModulesToEvaluateConverter() {
	return new DegreeModuleToEnrolKeyConverter();
    }

}
