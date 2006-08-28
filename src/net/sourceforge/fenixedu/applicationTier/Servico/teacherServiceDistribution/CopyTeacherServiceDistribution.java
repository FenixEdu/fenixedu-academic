package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;

public class CopyTeacherServiceDistribution extends Service {
	public TeacherServiceDistribution run(List<Integer> executionPeriodIdList, Integer teacherServiceDistributionId, Integer personId, String name) {
		Person creator = (Person) rootDomainObject.readPartyByOID(personId);
		
		List<ExecutionPeriod> executionPeriodList = getExecutionPeriods(executionPeriodIdList);
		
		TeacherServiceDistribution teacherServiceDistributionCopied = rootDomainObject.readTeacherServiceDistributionByOID(teacherServiceDistributionId);	
		
		return TeacherServiceDistribution.copyTeacherServiceDistribution(teacherServiceDistributionCopied, executionPeriodList, name, creator);
		
	}

	private List<ExecutionPeriod> getExecutionPeriods(List<Integer> executionPeriodIdList) {
		List<ExecutionPeriod> executionPeriodList = new ArrayList<ExecutionPeriod>();

		for (Integer executionPeriodId : executionPeriodIdList) {
			executionPeriodList.add(rootDomainObject
					.readExecutionPeriodByOID(executionPeriodId));
		}
		return executionPeriodList;
	}
}
