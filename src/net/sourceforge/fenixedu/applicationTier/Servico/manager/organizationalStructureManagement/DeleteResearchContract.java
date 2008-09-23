package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchContract;

public class DeleteResearchContract extends FenixService {

    public void run(ResearchContract contract) {
	contract.delete();
    }
}
