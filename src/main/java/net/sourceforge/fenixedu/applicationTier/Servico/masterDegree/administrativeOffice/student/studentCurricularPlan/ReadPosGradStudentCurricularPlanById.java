package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Tânia Pousão 6/Out/2003
 */

public class ReadPosGradStudentCurricularPlanById {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Atomic
    public static Object run(String studentCurricularPlanId) {
        StudentCurricularPlan studentCurricularPlan = FenixFramework.getDomainObject(studentCurricularPlanId);
        return studentCurricularPlan == null ? null : InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan);
    }

}