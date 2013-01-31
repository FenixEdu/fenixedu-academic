package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchContract;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteResearchContract extends FenixService {

	@Service
	public static void run(ResearchContract contract) {
		contract.delete();
	}
}