package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.vigilancy.Convoke;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ConfirmConvokeByOID extends Service {

    public void run(Integer idInternal) throws ExcepcaoPersistencia {

        Convoke convoke = (Convoke) RootDomainObject.readDomainObjectByOID(Convoke.class, idInternal);
        convoke.setConfirmed(true);

    }

}
