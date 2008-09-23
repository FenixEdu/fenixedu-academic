package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExecutionPeriodByOID extends FenixService {

    public InfoExecutionPeriod run(final Integer executionPeriodID) {
	final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodID);

	return InfoExecutionPeriod.newInfoFromDomain(executionSemester);
    }

}