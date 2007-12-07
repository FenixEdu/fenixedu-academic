package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;

public class AddContentToPool extends Service {

    public void run(MetaDomainObjectPortal portal, Content content) {
	portal.addPool(content);
    }
}
