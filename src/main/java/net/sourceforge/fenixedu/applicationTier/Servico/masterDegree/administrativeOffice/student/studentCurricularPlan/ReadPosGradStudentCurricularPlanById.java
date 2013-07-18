package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan;


import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Tânia Pousão 6/Out/2003
 */

public class ReadPosGradStudentCurricularPlanById {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static Object run(Integer studentCurricularPlanId) {
        StudentCurricularPlan studentCurricularPlan = RootDomainObject.getInstance().readStudentCurricularPlanByOID(studentCurricularPlanId);
        return studentCurricularPlan == null ? null : InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan);
    }

}