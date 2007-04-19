package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ResearchUnitSite;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;

public class CreateResearchUnitSite extends Service {

    public void run(ResearchUnit unit) {
	new ResearchUnitSite(unit);
    }
}
