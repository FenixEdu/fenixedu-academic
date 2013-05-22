package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteTSDProcess extends FenixService {
    protected void run(Integer tsdProcessId) {
        TSDProcess tsdProcess = rootDomainObject.readTSDProcessByOID(tsdProcessId);

        tsdProcess.delete();
    }

    // Service Invokers migrated from Berserk

    private static final DeleteTSDProcess serviceInstance = new DeleteTSDProcess();

    @Service
    public static void runDeleteTSDProcess(Integer tsdProcessId) throws NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            serviceInstance.run(tsdProcessId);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                serviceInstance.run(tsdProcessId);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    serviceInstance.run(tsdProcessId);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}