package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;


import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

public class ReadDomainProfessorshipByOID {

    @Service
    public static Professorship run(Integer professorshipID) {
        return RootDomainObject.getInstance().readProfessorshipByOID(professorshipID);
    }

}