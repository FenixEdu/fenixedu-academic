package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchContract;

public class DeleteResearchContract extends Service {

	public void run(ResearchContract contract) {
		contract.delete();
	}
}
