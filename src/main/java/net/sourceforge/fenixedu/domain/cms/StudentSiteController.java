package net.sourceforge.fenixedu.domain.cms;

import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.StudentsSite;

public class StudentSiteController extends UnitAcronymSiteTemplateController {

    public StudentSiteController() {
        super("ACD");
    }

    @Override
    public Class<? extends Site> getControlledClass() {
        return StudentsSite.class;
    }

}
