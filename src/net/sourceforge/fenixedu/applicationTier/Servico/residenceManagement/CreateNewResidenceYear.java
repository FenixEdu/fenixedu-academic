package net.sourceforge.fenixedu.applicationTier.Servico.residenceManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResidenceManagementUnit;
import net.sourceforge.fenixedu.domain.residence.ResidenceYear;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class CreateNewResidenceYear extends Service {

    public void run() {
	new ResidenceYear((ResidenceManagementUnit) AccessControl.getPerson().getEmployee().getCurrentWorkingPlace());
    }

}
