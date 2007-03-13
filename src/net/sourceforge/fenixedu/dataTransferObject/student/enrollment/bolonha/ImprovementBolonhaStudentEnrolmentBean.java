package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.CurriculumModuleEnroledWrapperConverter;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.util.LanguageUtils;

public class ImprovementBolonhaStudentEnrolmentBean extends BolonhaStudentEnrollmentBean {
    
    public ImprovementBolonhaStudentEnrolmentBean(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionPeriod executionPeriod) {
	super(studentCurricularPlan, executionPeriod, 
		new ImprovementStudentCurriculumGroupBean(studentCurricularPlan.getRoot(), executionPeriod), CurricularRuleLevel.IMPROVEMENT_ENROLMENT);
    }
    
    @Override
    public Converter getDegreeModulesToEvaluateConverter() {
        return new CurriculumModuleEnroledWrapperConverter();
    }

    public String getFuncionalityTitle() {
	final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.AcademicAdminOffice", LanguageUtils.getLocale());
	return resourceBundle.getString("label.improvement.enrolment");
    }
    
}
