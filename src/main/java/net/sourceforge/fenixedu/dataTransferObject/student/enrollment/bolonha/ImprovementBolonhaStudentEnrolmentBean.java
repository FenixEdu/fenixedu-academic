package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.CurriculumModuleEnroledWrapperConverter;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import java.util.Locale;

public class ImprovementBolonhaStudentEnrolmentBean extends BolonhaStudentEnrollmentBean {

    static private final long serialVersionUID = 3655858704185977193L;

    public ImprovementBolonhaStudentEnrolmentBean(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionSemester executionSemester) {
        super(studentCurricularPlan, executionSemester, createBean(studentCurricularPlan, executionSemester),
                CurricularRuleLevel.IMPROVEMENT_ENROLMENT);
    }

    private static ImprovementStudentCurriculumGroupBean createBean(StudentCurricularPlan scp, ExecutionSemester semester) {
        if (scp.isEmptyDegree()) {
            return new EmptyDegreeImprovementStudentCurriculumGroupBean(scp.getRoot(), semester);
        }
        return new ImprovementStudentCurriculumGroupBean(scp.getRoot(), semester);
    }

    @Override
    public Converter getDegreeModulesToEvaluateConverter() {
        return new CurriculumModuleEnroledWrapperConverter();
    }

    @Override
    public String getFuncionalityTitle() {
        final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.AcademicAdminOffice", I18N.getLocale());
        return resourceBundle.getString("label.improvement.enrolment");
    }

}
