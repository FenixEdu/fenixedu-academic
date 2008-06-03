package net.sourceforge.fenixedu.applicationTier.Servico.site;

import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreateVirtualFunction extends ManageVirtualFunction {

    public Function run(UnitSite site, Unit unit, MultiLanguageString name) {
	checkUnit(site, unit);
	return Function.createVirtualFunction(unit, name);
    }
}
