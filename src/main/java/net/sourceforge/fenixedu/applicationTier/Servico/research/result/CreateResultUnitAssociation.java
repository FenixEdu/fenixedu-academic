package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultUnitAssociationCreationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation.ResultUnitAssociationRole;
import net.sourceforge.fenixedu.util.researcher.ResearchResultMetaDataManager;
import pt.ist.fenixWebFramework.services.Service;

public class CreateResultUnitAssociation {

    @Service
    public static void run(ResultUnitAssociationCreationBean bean) {

        if (bean.isSuggestion()) {
            run(bean.getResult(), bean.getSuggestedUnits());
        } else {
            run(bean.getResult(), bean.getUnit());
        }
        ResearchResultMetaDataManager.updateMetaDataInStorageFor(bean.getResult());
    }

    private static void run(ResearchResult result, Unit unit) {
        if (unit == null) {
            throw new DomainException("error.label.invalidNameForInternalUnit");
        }
        result.addUnitAssociation(unit, ResultUnitAssociationRole.getDefaultRole());
    }

    private static void run(ResearchResult result, List<Unit> suggestedUnits) {
        for (Unit unit : suggestedUnits) {
            result.addUnitAssociation(unit, ResultUnitAssociationRole.getDefaultRole());
        }
    }
}