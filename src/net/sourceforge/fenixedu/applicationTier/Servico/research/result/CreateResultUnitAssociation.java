package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultUnitAssociationCreationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.util.researcher.ResearchResultMetaDataManager;

public class CreateResultUnitAssociation extends Service {
	public void run(ResultUnitAssociationCreationBean bean) {
		final ResearchResult result = bean.getResult();
		Unit unit = bean.getUnit();

		if (unit == null) {
			throw new DomainException("error.label.invalidNameForInternalUnit");
		}

		result.addUnitAssociation(unit, bean.getRole());
		ResearchResultMetaDataManager.updateMetaDataInStorageFor(result);

	}
}
