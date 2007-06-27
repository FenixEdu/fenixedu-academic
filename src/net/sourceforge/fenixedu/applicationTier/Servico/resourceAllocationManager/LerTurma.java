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
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class LerTurma extends Service {

    public InfoClass run(String className, InfoExecutionDegree infoExecutionDegree,
            InfoExecutionPeriod infoExecutionPeriod) throws ExcepcaoPersistencia {
    	final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(infoExecutionDegree.getIdInternal());
    	final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(infoExecutionPeriod.getIdInternal());

        final SchoolClass turma = executionDegree.findSchoolClassesByExecutionPeriodAndName(executionPeriod, className);

        if (turma != null) {
            return InfoClass.newInfoFromDomain(turma);
        }

        return null;
    }

}
