package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.CurriculumModuleEnroledWrapperConverter;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.util.LanguageUtils;

public class SpecialSeasonBolonhaStudentEnrolmentBean extends BolonhaStudentEnrollmentBean {
    
    public SpecialSeasonBolonhaStudentEnrolmentBean(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionPeriod executionPeriod) {
	super(studentCurricularPlan, executionPeriod, 
		new SpecialSeasonStudentCurriculumGroupBean(studentCurricularPlan.getRoot(), executionPeriod), CurricularRuleLevel.SPECIAL_SEASON_ENROLMENT);
    }
    
    @Override
    public Converter getDegreeModulesToEvaluateConverter() {
        return new CurriculumModuleEnroledWrapperConverter();
    }

    public String getFuncionalityTitle() {
	final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.AcademicAdminOffice", LanguageUtils.getLocale());
	return resourceBundle.getString("label.special.season.enrolment");
    }
    
}
