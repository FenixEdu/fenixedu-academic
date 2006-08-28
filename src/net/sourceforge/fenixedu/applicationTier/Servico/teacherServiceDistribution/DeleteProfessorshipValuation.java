package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ProfessorshipValuation;

public class DeleteProfessorshipValuation extends Service {
	public void run(Integer professorshipValuationId) {
		ProfessorshipValuation professorshipValuation = rootDomainObject.readProfessorshipValuationByOID(professorshipValuationId);
		
		professorshipValuation.delete();
	}
}
