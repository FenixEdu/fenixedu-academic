/*
 * Created on 23/Jun/2004
 *
 */
package DataBeans;

import Dominio.IStudentCurricularPlan;

/**
 * @author Tânia Pousão 23/Jun/2004
 */
public class InfoStudentCurricularPlanWithInfoStudent extends
        InfoStudentCurricularPlan {
    public void copyFromDomain(IStudentCurricularPlan studentCurricularPlan) {
        super.copyFromDomain(studentCurricularPlan);
        if (studentCurricularPlan != null) {

            setInfoStudent(InfoStudentWithInfoPerson
                    .newInfoFromDomain(studentCurricularPlan.getStudent()));
        }
    }

    public static InfoStudentCurricularPlan newInfoFromDomain(
            IStudentCurricularPlan studentCurricularPlan) {
        InfoStudentCurricularPlanWithInfoStudent infoStudentCurricularPlan = null;
        if (studentCurricularPlan != null) {
            infoStudentCurricularPlan = new InfoStudentCurricularPlanWithInfoStudent();
            infoStudentCurricularPlan.copyFromDomain(studentCurricularPlan);
        }
        return infoStudentCurricularPlan;
    }
}