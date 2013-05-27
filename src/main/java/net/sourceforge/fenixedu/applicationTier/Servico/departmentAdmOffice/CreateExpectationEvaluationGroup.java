package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;


import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ExpectationEvaluationGroup;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateExpectationEvaluationGroup {

    @Checked("RolePredicates.DEPARTMENT_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static void run(Teacher appraiser, Teacher evaluated, ExecutionYear executionYear) {
        new ExpectationEvaluationGroup(appraiser, evaluated, executionYear);
    }
}