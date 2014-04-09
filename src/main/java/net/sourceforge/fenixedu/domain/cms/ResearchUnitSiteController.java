package net.sourceforge.fenixedu.domain.cms;

import net.sourceforge.fenixedu.domain.ResearchUnitSite;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;

public class ResearchUnitSiteController extends UnitClassTemplateController {

    public ResearchUnitSiteController() {
        super(new Class<?>[] { ResearchUnit.class });
    }

    @Override
    public Class<? extends Site> getControlledClass() {
        return ResearchUnitSite.class;
    }

}
