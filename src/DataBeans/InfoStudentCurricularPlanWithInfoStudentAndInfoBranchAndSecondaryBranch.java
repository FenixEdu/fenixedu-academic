/*
 * Created on Jul 5, 2004
 *
 */
package DataBeans;

import Dominio.IStudentCurricularPlan;

/**
 * @author João Mota
 *  
 */
public class InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranch
        extends InfoStudentCurricularPlanWithInfoStudentAndInfoBranch {

    public void copyFromDomain(IStudentCurricularPlan studentCurricularPlan) {
        super.copyFromDomain(studentCurricularPlan);
        if (studentCurricularPlan != null) {

            setInfoSecundaryBranch(InfoBranch
                    .newInfoFromDomain(studentCurricularPlan
                            .getSecundaryBranch()));
        }
    }

    public static InfoStudentCurricularPlan newInfoFromDomain(
            IStudentCurricularPlan studentCurricularPlan) {
        InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranch infoStudentCurricularPlan = null;
        if (studentCurricularPlan != null) {
            infoStudentCurricularPlan = new InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranch();
            infoStudentCurricularPlan.copyFromDomain(studentCurricularPlan);
        }
        return infoStudentCurricularPlan;
    }
}