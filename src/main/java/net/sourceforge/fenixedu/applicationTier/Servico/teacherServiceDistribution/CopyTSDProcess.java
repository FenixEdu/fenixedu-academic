package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CopyTSDProcessPhaseService;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;
import pt.ist.fenixWebFramework.services.Service;

public class CopyTSDProcess {
    protected TSDProcess run(List<Integer> executionPeriodIdList, Integer tsdProcessId, Integer personId, String name) {
        Person creator = (Person) RootDomainObject.getInstance().readPartyByOID(personId);
        List<ExecutionSemester> executionPeriodList = getExecutionPeriods(executionPeriodIdList);
        TSDProcess tsdProcessCopied = RootDomainObject.getInstance().readTSDProcessByOID(tsdProcessId);

        CopyTSDProcessPhaseService service = CopyTSDProcessPhaseService.getInstance();

        return service.copyTSDProcess(tsdProcessCopied, executionPeriodList, name, creator);

    }

    private List<ExecutionSemester> getExecutionPeriods(List<Integer> executionPeriodIdList) {
        List<ExecutionSemester> executionPeriodList = new ArrayList<ExecutionSemester>();

        for (Integer executionPeriodId : executionPeriodIdList) {
            executionPeriodList.add(RootDomainObject.getInstance().readExecutionSemesterByOID(executionPeriodId));
        }
        return executionPeriodList;
    }

    // Service Invokers migrated from Berserk

    private static final CopyTSDProcess serviceInstance = new CopyTSDProcess();

    @Service
    public static TSDProcess runCopyTSDProcess(List<Integer> executionPeriodIdList, Integer tsdProcessId, Integer personId,
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