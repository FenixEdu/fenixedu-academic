package net.sourceforge.fenixedu.applicationTier.Servico.student.administrativeOfficeServices;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.NoCourseGroupEnrolmentBean;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateExtraEnrolment {

    @Checked("RolePredicates.ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE_AND_GRI")
    @Service
    public static RuleResult run(final NoCourseGroupEnrolmentBean bean) {
	final StudentCurricularPlan studentCurricularPlan = bean.getStudentCurricularPlan();
	return studentCurricularPlan.createNoCourseGroupCurriculumGroupEnrolment(bean);
    }

}