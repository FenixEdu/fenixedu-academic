package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.services.Service;

public class ReadNotClosedExecutionPeriods extends FenixService {

	@Service
	public static List<InfoExecutionPeriod> run() throws FenixServiceException {
		final List<InfoExecutionPeriod> result = new ArrayList<InfoExecutionPeriod>();
		for (final ExecutionSemester executionSemester : ExecutionSemester.readNotClosedExecutionPeriods()) {
			result.add(InfoExecutionPeriod.newInfoFromDomain(executionSemester));
		}
		return result;
	}
}