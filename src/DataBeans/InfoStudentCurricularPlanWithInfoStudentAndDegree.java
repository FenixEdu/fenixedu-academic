package DataBeans;

import Dominio.IStudentCurricularPlan;

/**
 * @author Fernanda Quitério 23/Jul/2004
 */
public class InfoStudentCurricularPlanWithInfoStudentAndDegree extends InfoStudentCurricularPlan {
    public void copyFromDomain(IStudentCurricularPlan studentCurricularPlan) {
        super.copyFromDomain(studentCurricularPlan);
        if (studentCurricularPlan != null) {
            setInfoDegreeCurricularPlan(InfoDegreeCurricularPlanWithDegree
                    .newInfoFromDomain(studentCurricularPlan.getDegreeCurricularPlan()));
            setInfoStudent(InfoStudentWithInfoPerson.newInfoFromDomain(studentCurricularPlan.getStudent()));
        }
    }

    public static InfoStudentCurricularPlan newInfoFromDomain(
            IStudentCurricularPlan studentCurricularPlan) {
        InfoStudentCurricularPlanWithInfoStudentAndDegree infoStudentCurricularPlan = null;
        if (studentCurricularPlan != null) {
            infoStudentCurricularPlan = new InfoStudentCurricularPlanWithInfoStudentAndDegree();
            infoStudentCurricularPlan.copyFromDomain(studentCurricularPlan);
        }
        return infoStudentCurricularPlan;
    }
}