package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteSpaceInformation extends FenixService {

	@Service
	public static void run(final SpaceInformation spaceInformation) {
		if (spaceInformation != null) {
			spaceInformation.delete();
		}
	}

}