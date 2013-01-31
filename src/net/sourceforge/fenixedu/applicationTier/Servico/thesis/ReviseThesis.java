package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import pt.ist.fenixWebFramework.services.Service;

public class ReviseThesis extends FenixService {

	@Service
	public static void run(Thesis thesis) {
		thesis.allowRevision();
	}

}