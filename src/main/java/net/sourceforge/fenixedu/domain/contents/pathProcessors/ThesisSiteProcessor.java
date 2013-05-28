package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ThesisSiteProcessor extends AbstractPathProcessor {

    @Override
    public Content processPath(String path) {
        String[] parts = path.split("/");
        Thesis thesis = AbstractDomainObject.fromExternalId(Integer.parseInt(parts[0]));
        return thesis != null ? thesis.getSite() : null;
    }

    @Override
    public boolean keepPortalInContentsPath() {
        return false;
    }
}
