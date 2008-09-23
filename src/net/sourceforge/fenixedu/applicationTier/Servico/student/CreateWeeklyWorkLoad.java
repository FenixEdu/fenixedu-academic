package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateWeeklyWorkLoad extends FenixService {

    public void run(final Integer attendsID, final Integer contact, final Integer autonomousStudy, final Integer other) {
	final Attends attends = rootDomainObject.readAttendsByOID(attendsID);
	attends.createWeeklyWorkLoad(contact, autonomousStudy, other);
    }

}
