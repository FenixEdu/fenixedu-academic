package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Professorship;
import pt.ist.fenixWebFramework.services.Service;

public class ReadDomainProfessorshipByOID extends FenixService {

    @Service
    public static Professorship run(Integer professorshipID) {
        return rootDomainObject.readProfessorshipByOID(professorshipID);
    }

}