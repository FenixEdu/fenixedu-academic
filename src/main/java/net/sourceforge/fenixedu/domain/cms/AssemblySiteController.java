package net.sourceforge.fenixedu.domain.cms;

import net.sourceforge.fenixedu.domain.AssemblySite;
import net.sourceforge.fenixedu.domain.Site;

public class AssemblySiteController extends UnitAcronymSiteTemplateController {

    public AssemblySiteController() {
        super("aestatutaria");
    }

    @Override
    public Class<? extends Site> getControlledClass() {
        return AssemblySite.class;
    }
}
