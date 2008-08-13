package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadDomainProfessorshipByOID extends Service {

    public Professorship run(Integer professorshipID) {
        return rootDomainObject.readProfessorshipByOID(professorshipID);
    }

}
