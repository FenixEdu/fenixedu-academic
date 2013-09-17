package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class CreateTSDProcess {
    protected TSDProcess run(List<String> executionPeriodIdList, String departmentId, String creatorId, String name) {
        Department department = FenixFramework.getDomainObject(departmentId);

        List<ExecutionSemester> executionPeriodList = getExecutionPeriods(executionPeriodIdList);

        ResourceBundle rb = ResourceBundle.getBundle("resources.DepartmentMemberResources", Language.getLocale());

        Person creator = (Person) FenixFramework.getDomainObject(creatorId);

        TSDProcess tsdProcess =
                new TSDProcess(department, executionPeriodList, creator, name,
                        rb.getString("label.teacherServiceDistribution.initialPhase"));

        return tsdProcess;
    }

    private List<ExecutionSemester> getExecutionPeriods(List<String> executionPeriodIdList) {
        List<ExecutionSemester> executionPeriodList = new ArrayList<ExecutionSemester>();

        for (String executionPeriodId : executionPeriodIdList) {
            executionPeriodList.add(FenixFramework.<ExecutionSemester> getDomainObject(executionPeriodId));
        }
        return executionPeriodList;
    }

    // Service Invokers migrated from Berserk

    private static final CreateTSDProcess serviceInstance = new CreateTSDProcess();

    @Atomic
    public static TSDProcess runCreateTSDProcess(List<String> executionPeriodIdList, String departmentId, String creatorId,
            String name) throws NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            return serviceInstance.run(executionPeriodIdList, departmentId, creatorId, name);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                return serviceInstance.run(executionPeriodIdList, departmentId, creatorId, name);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    return serviceInstance.run(executionPeriodIdList, departmentId, creatorId, name);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}