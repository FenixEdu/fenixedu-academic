package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;

public class CreateTeacherServiceDistribution extends Service {
	public TeacherServiceDistribution run(List<Integer> executionPeriodIdList,
			Integer departmentId, Integer creatorId, String name) {
		Department department = rootDomainObject
				.readDepartmentByOID(departmentId);

		List<ExecutionPeriod> executionPeriodList = getExecutionPeriods(executionPeriodIdList);

		ResourceBundle rb = ResourceBundle
				.getBundle("resources.DepartmentMemberResources");

		Person creator = (Person) rootDomainObject.readPartyByOID(creatorId);

		TeacherServiceDistribution teacherServiceDistribution = new TeacherServiceDistribution(
				department, executionPeriodList, creator, name,
				rb.getString("label.teacherServiceDistribution.initialPhase"));

		return teacherServiceDistribution;
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
