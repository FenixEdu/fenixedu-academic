package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

/**
 * @author Fernanda Quitério 23/Jul/2004
 */
public class InfoStudentCurricularPlanWithInfoStudentAndDegree extends InfoStudentCurricularPlan {
    public void copyFromDomain(StudentCurricularPlan studentCurricularPlan) {
        super.copyFromDomain(studentCurricularPlan);
        if (studentCurricularPlan != null) {
            setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan
                    .newInfoFromDomain(studentCurricularPlan.getDegreeCurricularPlan()));
            setInfoStudent(InfoStudentWithInfoPerson.newInfoFromDomain(studentCurricularPlan.getStudent()));
        }
    }

    public static InfoStudentCurricularPlan newInfoFromDomain(
            StudentCurricularPlan studentCurricularPlan) {
        InfoStudentCurricularPlanWithInfoStudentAndDegree infoStudentCurricularPlan = null;
        if (studentCurricularPlan != null) {
            infoStudentCurricularPlan = new InfoStudentCurricularPlanWithInfoStudentAndDegree();
            infoStudentCurricularPlan.copyFromDomain(studentCurricularPlan);
        }
        return infoStudentCurricularPlan;
    }
}