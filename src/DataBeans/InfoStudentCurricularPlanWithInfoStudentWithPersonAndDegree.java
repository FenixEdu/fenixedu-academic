/*
 * Created on 23/Jun/2004
 *
 */
package DataBeans;

import Dominio.IStudentCurricularPlan;

/**
 * @author Tânia Pousão
 * 23/Jun/2004
 */
public class InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree
		extends
			InfoStudentCurricularPlan {
	public void copyFromDomain(IStudentCurricularPlan studentCurricularPlan) {
		super.copyFromDomain(studentCurricularPlan);
		if(studentCurricularPlan != null) {
			setInfoDegreeCurricularPlan(InfoDegreeCurricularPlanWithDegree.newInfoFromDomain(studentCurricularPlan.getDegreeCurricularPlan()));
			setInfoStudent(InfoStudentWithInfoPerson.newInfoFromDomain(studentCurricularPlan.getStudent()));
		}
	}
	
	public static InfoStudentCurricularPlan newInfoFromDomain (IStudentCurricularPlan studentCurricularPlan) {
		InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree infoStudentCurricularPlan = null;
		if(studentCurricularPlan != null) {
			infoStudentCurricularPlan = new InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree();
			infoStudentCurricularPlan.copyFromDomain(studentCurricularPlan);
		}		
		return infoStudentCurricularPlan;
	}
}
