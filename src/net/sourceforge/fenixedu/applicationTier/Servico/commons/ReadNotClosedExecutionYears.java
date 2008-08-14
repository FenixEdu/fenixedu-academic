package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadNotClosedExecutionYears extends Service {

    public List<InfoExecutionYear> run() {
	final List<InfoExecutionYear> result = new ArrayList<InfoExecutionYear>();
	for (final ExecutionYear executionYear : ExecutionYear.readNotClosedExecutionYears()) {
	    result.add(InfoExecutionYear.newInfoFromDomain(executionYear));
	}
	return result;
    }
}
