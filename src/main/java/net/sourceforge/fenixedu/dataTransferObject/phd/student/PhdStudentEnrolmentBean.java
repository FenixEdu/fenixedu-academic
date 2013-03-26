package net.sourceforge.fenixedu.dataTransferObject.phd.student;

import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.BolonhaStudentEnrollmentBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;

public class PhdStudentEnrolmentBean extends BolonhaStudentEnrollmentBean {

    static private final long serialVersionUID = 1L;

    public PhdStudentEnrolmentBean(StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester,
            int[] curricularYears, CurricularRuleLevel curricularRuleLevel) {

        super(studentCurricularPlan, executionSemester, new PhdStudentCurriculumGroupBean(studentCurricularPlan.getRoot(),
                executionSemester, curricularYears), curricularRuleLevel);
    }

}
