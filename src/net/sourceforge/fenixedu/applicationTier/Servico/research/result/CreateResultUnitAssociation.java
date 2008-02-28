package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultUnitAssociationCreationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation.ResultUnitAssociationRole;
import net.sourceforge.fenixedu.util.researcher.ResearchResultMetaDataManager;

public class CreateResultUnitAssociation extends Service {

    public void run(ResultUnitAssociationCreationBean bean) {
	
	if(bean.isSuggestion()) {
	    run(bean.getResult(),bean.getSuggestedUnits());
	}else {
	    run(bean.getResult(),bean.getUnit());
	}
	ResearchResultMetaDataManager.updateMetaDataInStorageFor(bean.getResult());
    }

    private void run(ResearchResult result, Unit unit) {
	if (unit == null) {
	    throw new DomainException("error.label.invalidNameForInternalUnit");
	}
	result.addUnitAssociation(unit, ResultUnitAssociationRole.getDefaultRole());
    }
    
    private void run(ResearchResult result, List<Unit> suggestedUnits) {
	for (Unit unit : suggestedUnits) {
	    result.addUnitAssociation(unit, ResultUnitAssociationRole.getDefaultRole());
	}
    }
}
