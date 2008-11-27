package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.teacher.RoomsReserveBean;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest;

public class CreateNewRoomsReserve extends FenixService {

    @Checked("RolePredicates.TEACHER_PREDICATE")
    @Service
    public static PunctualRoomsOccupationRequest run(RoomsReserveBean bean) {
	if (bean != null) {
	    return new PunctualRoomsOccupationRequest(bean.getRequestor(), bean.getSubject(), bean.getDescription());
	}
	return null;
    }
}