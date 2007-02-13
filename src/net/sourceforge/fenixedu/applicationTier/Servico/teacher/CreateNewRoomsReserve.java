package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.teacher.RoomsReserveBean;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest;

public class CreateNewRoomsReserve extends Service {

    public PunctualRoomsOccupationRequest run(RoomsReserveBean bean) {
	if(bean != null) {
	    return new PunctualRoomsOccupationRequest(bean.getRequestor(), bean.getSubject(), bean.getDescription());
	}
	return null;
    }
}
