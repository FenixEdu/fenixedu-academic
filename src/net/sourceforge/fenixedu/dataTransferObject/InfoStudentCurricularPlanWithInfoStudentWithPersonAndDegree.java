/*
 * Created on 23/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

/**
 * @author Tânia Pousão 23/Jun/2004
 */
public class InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree extends
        InfoStudentCurricularPlan {
    public void copyFromDomain(StudentCurricularPlan studentCurricularPlan) {
        super.copyFromDomain(studentCurricularPlan);
        if (studentCurricularPlan != null) {
            setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan
                    .newInfoFromDomain(studentCurricularPlan.getDegreeCurricularPlan()));
            setInfoStudent(InfoStudentWithInfoPerson.newInfoFromDomain(studentCurricularPlan
                    .getStudent()));
        }
    }

    public static InfoStudentCurricularPlan newInfoFromDomain(
            StudentCurricularPlan studentCurricularPlan) {
        InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree infoStudentCurricularPlan = null;
        if (studentCurricularPlan != null) {
            infoStudentCurricularPlan = new InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree();
            infoStudentCurricularPlan.copyFromDomain(studentCurricularPlan);
        }
        return infoStudentCurricularPlan;
    }
}