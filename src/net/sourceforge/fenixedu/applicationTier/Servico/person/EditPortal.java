package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.contents.Portal;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class EditPortal extends Service {

    public void run(Portal portal, MultiLanguageString name, String prefix) {
	portal.setName(name);
	portal.setPrefix(prefix);
    }
    
}
