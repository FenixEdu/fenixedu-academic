package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.contents.Portal;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class EditPortal extends Service {

    public void run(Portal portal, MultiLanguageString name, String prefix) {
	portal.setName(name);
	portal.setPrefix(prefix);
    }
    
}
