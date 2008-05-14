package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CopyTSDProcessPhaseService;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;

public class CopyTSDProcess extends Service {
    public TSDProcess run(List<Integer> executionPeriodIdList, Integer tsdProcessId, Integer personId, String name) {
	Person creator = (Person) rootDomainObject.readPartyByOID(personId);
	List<ExecutionSemester> executionPeriodList = getExecutionPeriods(executionPeriodIdList);
	TSDProcess tsdProcessCopied = rootDomainObject.readTSDProcessByOID(tsdProcessId);

	CopyTSDProcessPhaseService service = CopyTSDProcessPhaseService.getInstance();

	return service.copyTSDProcess(tsdProcessCopied, executionPeriodList, name, creator);

    }

    private List<ExecutionSemester> getExecutionPeriods(List<Integer> executionPeriodIdList) {
	List<ExecutionSemester> executionPeriodList = new ArrayList<ExecutionSemester>();

	for (Integer executionPeriodId : executionPeriodIdList) {
	    executionPeriodList.add(rootDomainObject.readExecutionSemesterByOID(executionPeriodId));
	}
	return executionPeriodList;
    }
}
