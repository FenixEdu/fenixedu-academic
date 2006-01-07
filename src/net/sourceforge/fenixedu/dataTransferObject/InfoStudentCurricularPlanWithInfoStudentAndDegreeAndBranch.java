/*
 * Created on Nov 3, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

/**
 * @author farsola
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InfoStudentCurricularPlanWithInfoStudentAndDegreeAndBranch extends
        InfoStudentCurricularPlanWithInfoStudentAndDegree
{
    public void copyFromDomain(StudentCurricularPlan studentCurricularPlan) {
        super.copyFromDomain(studentCurricularPlan);
        if (studentCurricularPlan != null)
        {
            setInfoBranch(InfoBranch.newInfoFromDomain(studentCurricularPlan.getBranch()));
        }
    }

    public static InfoStudentCurricularPlan newInfoFromDomain(
            StudentCurricularPlan studentCurricularPlan) {
        InfoStudentCurricularPlanWithInfoStudentAndDegreeAndBranch infoStudentCurricularPlan = null;
        if (studentCurricularPlan != null) {
            infoStudentCurricularPlan = new InfoStudentCurricularPlanWithInfoStudentAndDegreeAndBranch();
            infoStudentCurricularPlan.copyFromDomain(studentCurricularPlan);
        }
        return infoStudentCurricularPlan;
    }

}
