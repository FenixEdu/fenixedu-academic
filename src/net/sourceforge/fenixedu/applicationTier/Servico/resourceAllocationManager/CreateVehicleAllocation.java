package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.VehicleAllocationBean;
import net.sourceforge.fenixedu.domain.resource.VehicleAllocation;

public class CreateVehicleAllocation extends Service {

    public void run(VehicleAllocationBean bean) {
	if(bean != null) {	    	  
	    new VehicleAllocation(bean.getBeginDateTime(), bean.getEndDateTime(), bean.getVehicle(), 
		    bean.getRequestor(), bean.getReason(), bean.getDistance(), bean.getAmountCharged());
	}
    }
}
