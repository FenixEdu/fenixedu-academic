package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;

public class AddContentToPool extends FenixService {

    public void run(MetaDomainObjectPortal portal, Content content) {
	portal.addPool(content);
    }
}
