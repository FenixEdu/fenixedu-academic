/*
 * Created on 23/Jun/2004
 *
 */
package DataBeans;

import Dominio.IStudentCurricularPlan;

/**
 * @author Tânia Pousão 23/Jun/2004
 */
public class InfoStudentCurricularPlanWithInfoStudentAndInfoBranch extends
        InfoStudentCurricularPlanWithInfoStudent {
    public void copyFromDomain(IStudentCurricularPlan studentCurricularPlan) {
        super.copyFromDomain(studentCurricularPlan);
        if (studentCurricularPlan != null) {

            setInfoBranch(InfoBranch.newInfoFromDomain(studentCurricularPlan
                    .getBranch()));
        }
    }

    public static InfoStudentCurricularPlan newInfoFromDomain(
            IStudentCurricularPlan studentCurricularPlan) {
        InfoStudentCurricularPlanWithInfoStudentAndInfoBranch infoStudentCurricularPlan = null;
        if (studentCurricularPlan != null) {
            infoStudentCurricularPlan = new InfoStudentCurricularPlanWithInfoStudentAndInfoBranch();
            infoStudentCurricularPlan.copyFromDomain(studentCurricularPlan);
        }
        return infoStudentCurricularPlan;
    }
}