package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;

public class ConvokesAttended extends FenixService {

    public void run(Vigilancy convoke, Boolean bool) {
	convoke.setAttendedToConvoke(bool);
    }

}