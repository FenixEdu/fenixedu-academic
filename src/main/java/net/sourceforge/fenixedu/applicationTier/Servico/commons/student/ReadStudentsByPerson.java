package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.MasterDegreeAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.StudentAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;

public class ReadStudentsByPerson {

    protected List run(InfoPerson infoPerson) {
        final List<InfoStudent> result = new ArrayList<InfoStudent>();

        Person person = (Person) RootDomainObject.getInstance().readPartyByOID(infoPerson.getIdInternal());
        for (final Registration registration : person.getStudents()) {
            result.add(InfoStudent.newInfoFromDomain(registration));
        }

        return result;
    }

    // Service Invokers migrated from Berserk

    private static final ReadStudentsByPerson serviceInstance = new ReadStudentsByPerson();

    @Service
    public static List runReadStudentsByPerson(InfoPerson infoPerson) throws NotAuthorizedException {
        try {
            StudentAuthorizationFilter.instance.execute();
            return serviceInstance.run(infoPerson);
        } catch (NotAuthorizedException ex1) {
            try {
                MasterDegreeAdministrativeOfficeAuthorizationFilter.instance.execute();
                return serviceInstance.run(infoPerson);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}