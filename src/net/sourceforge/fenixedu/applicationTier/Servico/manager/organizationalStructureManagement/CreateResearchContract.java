package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchContract;
import net.sourceforge.fenixedu.presentationTier.Action.webSiteManager.ResearchContractBean;

public class CreateResearchContract extends Service {

	public void run(ResearchContractBean bean) {
		new ResearchContract(bean.getPerson(), bean.getBegin(), bean.getEnd(), bean.getUnit(), bean.getFunctionType());
	}
}
