package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CopyTSDProcessPhaseService;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class CopyTSDProcess {
    protected TSDProcess run(List<String> executionPeriodIdList, String tsdProcessId, String personId, String name) {
        Person creator = (Person) AbstractDomainObject.fromExternalId(personId);
        List<ExecutionSemester> executionPeriodList = getExecutionPeriods(executionPeriodIdList);
        TSDProcess tsdProcessCopied = AbstractDomainObject.fromExternalId(tsdProcessId);

        CopyTSDProcessPhaseService service = CopyTSDProcessPhaseService.getInstance();

        return service.copyTSDProcess(tsdProcessCopied, executionPeriodList, name, creator);

    }

    private List<ExecutionSemester> getExecutionPeriods(List<String> executionPeriodIdList) {
        List<ExecutionSemester> executionPeriodList = new ArrayList<ExecutionSemester>();

        for (String executionPeriodId : executionPeriodIdList) {
            executionPeriodList.add(AbstractDomainObject.<ExecutionSemester> fromExternalId(executionPeriodId));
        }
        return executionPeriodList;
    }

    // Service Invokers migrated from Berserk

    private static final CopyTSDProcess serviceInstance = new CopyTSDProcess();

    @Service
    public static TSDProcess runCopyTSDProcess(List<String> executionPeriodIdList, String tsdProcessId, String personId,
            String name) throws NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            return serviceInstance.run(executionPeriodIdList, tsdProcessId, personId, name);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                return serviceInstance.run(executionPeriodIdList, tsdProcessId, personId, name);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    return serviceInstance.run(executionPeriodIdList, tsdProcessId, personId, name);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}