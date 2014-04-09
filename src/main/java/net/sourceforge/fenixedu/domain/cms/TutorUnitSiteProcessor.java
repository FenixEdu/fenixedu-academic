package net.sourceforge.fenixedu.domain.cms;

import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.TutorSite;

public class TutorUnitSiteProcessor extends UnitAcronymSiteTemplateController {

    public TutorUnitSiteProcessor() {
        super("Tutor");
    }

    @Override
    public Class<? extends Site> getControlledClass() {
        return TutorSite.class;
    }

}
