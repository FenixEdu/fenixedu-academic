package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultUnitAssociationCreationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation;

public class CreateResultUnitAssociation extends Service {
    public ResultUnitAssociation run(ResultUnitAssociationCreationBean bean)
	    throws FenixServiceException {
	final Result result = bean.getResult();
	ResultUnitAssociation association = null;
	Unit unit = bean.getUnit();

	try {
	    if (unit == null) {
		unit = Unit.createNewExternalInstitution(bean.getUnitName());
	    }
	    association = new ResultUnitAssociation(result, unit, bean.getRole());
	} catch (DomainException e) {
	    // If a new unit was created for this purpose and the
                // association was not created, unit will be deleted.
	    if (unit != null && bean.getUnit() == null) {
		unit.delete();
	    }
	    throw e;
	}
	return association;
    }
}
