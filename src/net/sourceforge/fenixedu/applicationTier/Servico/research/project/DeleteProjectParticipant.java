package net.sourceforge.fenixedu.applicationTier.Servico.research.project;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.project.ProjectParticipation;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteProjectParticipant extends FenixService {

	@Checked("ResultPredicates.author")
	@Service
	public static void run(Integer participationId) throws FenixServiceException {
		ProjectParticipation participation = rootDomainObject.readProjectParticipationByOID(participationId);
		if (participation == null) {
			throw new FenixServiceException();
		}
		participation.delete();
	}
}