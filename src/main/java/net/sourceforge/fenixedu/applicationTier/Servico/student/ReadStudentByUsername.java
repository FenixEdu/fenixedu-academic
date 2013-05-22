package net.sourceforge.fenixedu.applicationTier.Servico.student;

/**
 * 
 * @author tfc130
 */
import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.MasterDegreeAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.StudentAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;

public class ReadStudentByUsername extends FenixService {

    protected Object run(String username) {
        final Registration registration = Registration.readByUsername(username);
        return registration == null ? null : InfoStudent.newInfoFromDomain(registration);
    }

    // Service Invokers migrated from Berserk

    private static final ReadStudentByUsername serviceInstance = new ReadStudentByUsername();

    @Service
    public static Object runReadStudentByUsername(String username) throws NotAuthorizedException {
        try {
            StudentAuthorizationFilter.instance.execute();
            return serviceInstance.run(username);
        } catch (NotAuthorizedException ex1) {
            try {
                MasterDegreeAdministrativeOfficeAuthorizationFilter.instance.execute();
                return serviceInstance.run(username);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}