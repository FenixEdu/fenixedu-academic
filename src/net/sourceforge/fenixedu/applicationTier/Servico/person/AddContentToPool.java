package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import pt.ist.fenixWebFramework.services.Service;

public class AddContentToPool extends FenixService {

	@Service
	public static void run(MetaDomainObjectPortal portal, Content content) {
		portal.addPool(content);
	}
}