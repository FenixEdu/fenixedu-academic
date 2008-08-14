package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DegreeModuleToEnrolKeyConverter;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class BolonhaStudentEnrollmentBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5614162187691303580L;

    private DomainReference<StudentCurricularPlan> studentCurricularPlan;

    private DomainReference<ExecutionSemester> executionSemester;

    private StudentCurriculumGroupBean rootStudentCurriculumGroupBean;

    private List<IDegreeModuleToEvaluate> degreeModulesToEvaluate;

    private List<DomainReference<CurriculumModule>> curriculumModulesToRemove;

    private IDegreeModuleToEvaluate optionalDegreeModuleToEnrol;

    private CurricularRuleLevel curricularRuleLevel;

    private CycleType cycleTypeToEnrol;

    public BolonhaStudentEnrollmentBean(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionSemester executionSemester, final int[] curricularYears, CurricularRuleLevel curricularRuleLevel) {
	this(studentCurricularPlan, executionSemester, new StudentCurriculumGroupBean(studentCurricularPlan.getRoot(),
		executionSemester, curricularYears), curricularRuleLevel);
    }

    protected BolonhaStudentEnrollmentBean(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionSemester executionSemester, final StudentCurriculumGroupBean rootStudentCurriculumGroupBean,
	    CurricularRuleLevel curricularRuleLevel) {
	super();
	setStudentCurricularPlan(studentCurricularPlan);
	setExecutionPeriod(executionSemester);
	setRootStudentCurriculumGroupBean(rootStudentCurriculumGroupBean);

	setDegreeModulesToEvaluate(new ArrayList<IDegreeModuleToEvaluate>());
	setCurriculumModulesToRemove(new ArrayList<CurriculumModule>());
	setCurricularRuleLevel(curricularRuleLevel);
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
	return (this.studentCurricularPlan != null) ? this.studentCurricularPlan.getObject() : null;
    }

    private void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
	this.studentCurricularPlan = (studentCurricularPlan != null) ? new DomainReference<StudentCurricularPlan>(
		studentCurricularPlan) : null;
    }

    public ExecutionSemester getExecutionPeriod() {
	return (this.executionSemester != null) ? this.executionSemester.getObject() : null;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
	this.executionSemester = (executionSemester != null) ? new DomainReference<ExecutionSemester>(executionSemester) : null;
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

    public CurricularRuleLevel getCurricularRuleLevel() {
	return curricularRuleLevel;
    }

    public void setCurricularRuleLevel(CurricularRuleLevel curricularRuleLevel) {
	this.curricularRuleLevel = curricularRuleLevel;
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

    public String getFuncionalityTitle() {
	final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.AcademicAdminOffice", Language.getLocale());

	final StringBuilder result = new StringBuilder();
	result.append(resourceBundle.getString("label.student.enrollment.courses")).append(" ");

	switch (curricularRuleLevel) {

	case ENROLMENT_WITH_RULES:
	    result.append("(").append(resourceBundle.getString("label.student.enrollment.withRules")).append(")");
	    break;

	case ENROLMENT_NO_RULES:
	    result.append("(").append(resourceBundle.getString("label.student.enrollment.withoutRules")).append(")");
	    break;
	}

	return result.toString();
    }

    public CycleType getCycleTypeToEnrol() {
	return cycleTypeToEnrol;
    }

    public void setCycleTypeToEnrol(CycleType cycleTypeToEnrol) {
	this.cycleTypeToEnrol = cycleTypeToEnrol;
    }

}
