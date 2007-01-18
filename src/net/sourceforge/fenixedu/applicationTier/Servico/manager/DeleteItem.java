package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Site;

/**
 * @author Fernanda Quitério
 * 
 */
public class DeleteItem extends Service {

    public Boolean run(Site site, final Item item) {
	if (item != null) {
	    item.delete();
	}
        return Boolean.TRUE;
    }

}

