package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.vigilancy.Convoke;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ConvokesAttended extends Service {

    public void run(Integer convokeOID, Boolean bool) throws ExcepcaoPersistencia {

        Convoke convoke = (Convoke) RootDomainObject.readDomainObjectByOID(Convoke.class, convokeOID);
        convoke.setAttendedToConvoke(bool);

    }

}