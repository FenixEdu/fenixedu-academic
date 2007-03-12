package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.CurriculumModuleEnroledWrapperConverter;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class ImprovementBolonhaStudentEnrolmentBean extends BolonhaStudentEnrollmentBean {
    
    public ImprovementBolonhaStudentEnrolmentBean(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionPeriod executionPeriod) {
	super(studentCurricularPlan, executionPeriod, 
		new ImprovementStudentCurriculumGroupBean(studentCurricularPlan.getRoot(), executionPeriod));
    }
    
    @Override
    public Converter getDegreeModulesToEvaluateConverter() {
        return new CurriculumModuleEnroledWrapperConverter();
    }
}
