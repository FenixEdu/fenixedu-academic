package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class CreateTSDProcess extends Service {
    public TSDProcess run(List<Integer> executionPeriodIdList, Integer departmentId, Integer creatorId, String name) {
	Department department = rootDomainObject.readDepartmentByOID(departmentId);

	List<ExecutionSemester> executionPeriodList = getExecutionPeriods(executionPeriodIdList);

	ResourceBundle rb = ResourceBundle.getBundle("resources.DepartmentMemberResources", Language.getLocale());

	Person creator = (Person) rootDomainObject.readPartyByOID(creatorId);

	TSDProcess tsdProcess = new TSDProcess(department, executionPeriodList, creator, name, rb
		.getString("label.teacherServiceDistribution.initialPhase"));

	return tsdProcess;
    }

    private List<ExecutionSemester> getExecutionPeriods(List<Integer> executionPeriodIdList) {
	List<ExecutionSemester> executionPeriodList = new ArrayList<ExecutionSemester>();

	for (Integer executionPeriodId : executionPeriodIdList) {
	    executionPeriodList.add(rootDomainObject.readExecutionSemesterByOID(executionPeriodId));
	}
	return executionPeriodList;
    }
}
