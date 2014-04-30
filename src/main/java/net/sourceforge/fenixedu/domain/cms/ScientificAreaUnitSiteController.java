package net.sourceforge.fenixedu.domain.cms;

import net.sourceforge.fenixedu.domain.ScientificAreaSite;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;

public class ScientificAreaUnitSiteController extends UnitClassTemplateController {

    public ScientificAreaUnitSiteController() {
        super(new Class<?>[] { DepartmentUnit.class, ScientificAreaUnit.class });
    }

    @Override
    public Class<? extends Site> getControlledClass() {
        return ScientificAreaSite.class;
    }
}
