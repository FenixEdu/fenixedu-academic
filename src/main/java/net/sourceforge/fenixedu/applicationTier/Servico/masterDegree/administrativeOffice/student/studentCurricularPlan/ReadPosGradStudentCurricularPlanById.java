package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Tânia Pousão 6/Out/2003
 */

public class ReadPosGradStudentCurricularPlanById {

    @Atomic
    public static Object run(String studentCurricularPlanId) {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);
        StudentCurricularPlan studentCurricularPlan = FenixFramework.getDomainObject(studentCurricularPlanId);
        return studentCurricularPlan == null ? null : InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan);
    }

}