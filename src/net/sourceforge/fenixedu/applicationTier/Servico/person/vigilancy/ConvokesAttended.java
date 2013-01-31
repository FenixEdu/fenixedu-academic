package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import pt.ist.fenixWebFramework.services.Service;

public class ConvokesAttended extends FenixService {

	@Service
	public static void run(Vigilancy convoke, Boolean bool) {
		convoke.setAttendedToConvoke(bool);
	}

}