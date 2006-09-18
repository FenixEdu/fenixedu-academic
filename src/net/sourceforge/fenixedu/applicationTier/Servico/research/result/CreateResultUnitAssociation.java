package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultUnitAssociationCreationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.Result;

public class CreateResultUnitAssociation extends Service {
    public void run(ResultUnitAssociationCreationBean bean) {
	final Result result = bean.getResult();
	Unit unit = bean.getUnit();

	try {
	    if (unit == null) {
		unit = Unit.createNewExternalInstitution(bean.getUnitName());
	    }
	    result.addUnitAssociation(unit, bean.getRole());
	} catch (DomainException e) {
	    // If a new unit was created for this purpose and the
            // association was not created, unit will be deleted.
	    if (unit != null && bean.getUnit() == null) {
		unit.delete();
	    }
	    throw e;
	}
    }
}
