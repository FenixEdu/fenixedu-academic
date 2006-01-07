/*
 * Created on Jul 5, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

/**
 * @author João Mota
 *  
 */
public class InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranch extends
        InfoStudentCurricularPlanWithInfoStudentAndInfoBranch {

    public void copyFromDomain(StudentCurricularPlan studentCurricularPlan) {
        super.copyFromDomain(studentCurricularPlan);
        if (studentCurricularPlan != null) {

            setInfoSecundaryBranch(InfoBranch.newInfoFromDomain(studentCurricularPlan
                    .getSecundaryBranch()));
        }
    }

    public static InfoStudentCurricularPlan newInfoFromDomain(
            StudentCurricularPlan studentCurricularPlan) {
        InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranch infoStudentCurricularPlan = null;
        if (studentCurricularPlan != null) {
            infoStudentCurricularPlan = new InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranch();
            infoStudentCurricularPlan.copyFromDomain(studentCurricularPlan);
        }
        return infoStudentCurricularPlan;
    }
}