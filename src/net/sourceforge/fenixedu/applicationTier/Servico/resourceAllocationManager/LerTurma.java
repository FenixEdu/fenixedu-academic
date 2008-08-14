package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

/**
 * Servi�o LerTurma
 * 
 * @author tfc130
 * @version
 */

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class LerTurma extends Service {

    public InfoClass run(String className, InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod) {
	final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(infoExecutionDegree.getIdInternal());
	final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(infoExecutionPeriod
		.getIdInternal());

	final SchoolClass turma = executionDegree.findSchoolClassesByExecutionPeriodAndName(executionSemester, className);

	if (turma != null) {
	    return InfoClass.newInfoFromDomain(turma);
	}

	return null;
    }

}
