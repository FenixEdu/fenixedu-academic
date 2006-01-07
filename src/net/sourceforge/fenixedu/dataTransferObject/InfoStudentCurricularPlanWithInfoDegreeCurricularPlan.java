/*
 * Created on 18/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

/**
 * @author Tânia Pousão 18/Jun/2004
 */
public class InfoStudentCurricularPlanWithInfoDegreeCurricularPlan extends InfoStudentCurricularPlan {

    public void copyFromDomain(StudentCurricularPlan studentCurricularPlan) {
        super.copyFromDomain(studentCurricularPlan);
        if (studentCurricularPlan != null) {
            setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan.newInfoFromDomain(studentCurricularPlan
                    .getDegreeCurricularPlan()));
        }
    }

    public static InfoStudentCurricularPlan newInfoFromDomain(
            StudentCurricularPlan studentCurricularPlan) {
        InfoStudentCurricularPlanWithInfoDegreeCurricularPlan infoStudentCurricularPlan = null;
        if (studentCurricularPlan != null) {
            infoStudentCurricularPlan = new InfoStudentCurricularPlanWithInfoDegreeCurricularPlan();
            infoStudentCurricularPlan.copyFromDomain(studentCurricularPlan);
        }
        return infoStudentCurricularPlan;
    }
}