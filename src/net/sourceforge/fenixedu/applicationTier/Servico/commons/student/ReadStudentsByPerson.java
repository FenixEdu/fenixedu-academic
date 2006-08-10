package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentWithInfoPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadStudentsByPerson extends Service {

    public List run(InfoPerson infoPerson) throws ExcepcaoPersistencia {
        final List<InfoStudent> result = new ArrayList<InfoStudent>();
        
        Person person = (Person) rootDomainObject.readPartyByOID(infoPerson.getIdInternal());
        for (final Registration student : person.getStudents()) {
            result.add(InfoStudentWithInfoPerson.newInfoFromDomain(student));
        }
        
        return result;
    }
    
}
