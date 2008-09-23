package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadPreviousExecutionPeriod extends FenixService {

    public InfoExecutionPeriod run(final Integer oid) {
	final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(oid);
	return (executionSemester != null) ? InfoExecutionPeriod
		.newInfoFromDomain(executionSemester.getPreviousExecutionPeriod()) : null;
    }

}
