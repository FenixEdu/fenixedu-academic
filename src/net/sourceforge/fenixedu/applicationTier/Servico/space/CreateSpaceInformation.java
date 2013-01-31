package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import pt.ist.fenixWebFramework.services.Service;

public class CreateSpaceInformation extends FenixService {

	@Service
	public static void run(final Integer spaceInformationID) {
		final SpaceInformation spaceInformation = rootDomainObject.readSpaceInformationByOID(spaceInformationID);
		// spaceInformation.createNewSpaceInformation();
	}

}