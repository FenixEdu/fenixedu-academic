package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.vigilancy.VigilancyWithCredits;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ConvokesAttended extends Service {

    public void run(Integer convokeOID, Boolean bool) throws ExcepcaoPersistencia {

        VigilancyWithCredits convoke = (VigilancyWithCredits) RootDomainObject.readDomainObjectByOID(VigilancyWithCredits.class, convokeOID);
        convoke.setAttendedToConvoke(bool);

    }

}