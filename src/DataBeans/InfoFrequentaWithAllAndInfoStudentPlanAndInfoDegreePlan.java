/*
 * Created on 18/Jun/2004
 *
 */
package DataBeans;

import Dominio.IFrequenta;

/**
 * @author Tânia Pousão
 * 18/Jun/2004
 */
public class InfoFrequentaWithAllAndInfoStudentPlanAndInfoDegreePlan extends InfoFrequenta {
	
	public void copyFromDomain(IFrequenta frequenta) {
		super.copyFromDomain(frequenta);
		if(frequenta != null) {
			setAluno(InfoStudentWithInfoPerson.newInfoFromDomain(frequenta.getAluno()));
			setDisciplinaExecucao(InfoExecutionCourse.newInfoFromDomain(frequenta.getDisciplinaExecucao()));
			setInfoEnrolment(InfoEnrolmentWithInfoStudentPlanAndInfoDegreePlan.newInfoFromDomain(frequenta.getEnrolment()));
		}
	}
	
	public static InfoFrequenta newInfoFromDomain(IFrequenta frequenta) {
		InfoFrequentaWithAllAndInfoStudentPlanAndInfoDegreePlan infoFrequenta = null;
		if(frequenta != null) {
			infoFrequenta = new InfoFrequentaWithAllAndInfoStudentPlanAndInfoDegreePlan();
			infoFrequenta.copyFromDomain(frequenta);
		}		
		
		return infoFrequenta;
	}
}
