package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;

public class ReadStudentsByPerson extends FenixService {

    public List run(InfoPerson infoPerson) {
        final List<InfoStudent> result = new ArrayList<InfoStudent>();

        Person person = (Person) rootDomainObject.readPartyByOID(infoPerson.getIdInternal());
        for (final Registration registration : person.getStudents()) {
            result.add(InfoStudent.newInfoFromDomain(registration));
        }

        return result;
    }

}
