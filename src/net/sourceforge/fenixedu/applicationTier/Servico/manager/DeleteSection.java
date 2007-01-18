package net.sourceforge.fenixedu.applicationTier.Servico.manager;

/**
 * 
 * @author lmac1
 */
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;

public class DeleteSection extends Service {

    public Boolean run(Site site, final Section section) {
        if (section != null) {
            section.delete();
            return Boolean.TRUE;
        }
        else {
            return Boolean.FALSE;
        }
    }

}