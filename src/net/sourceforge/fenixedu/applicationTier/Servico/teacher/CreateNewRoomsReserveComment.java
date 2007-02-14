package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.teacher.RoomsReserveBean;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest;

import org.joda.time.DateTime;

public class CreateNewRoomsReserveComment extends Service {

    public void run(RoomsReserveBean bean, boolean reOpenRequest, boolean resolveRequest) {
	
	if (bean != null && bean.getReserveRequest() != null) {
	    
	    DateTime now = new DateTime();
	    PunctualRoomsOccupationRequest reserveRequest = bean.getReserveRequest();	    	   
	    
	    if (reOpenRequest) {
		reserveRequest.createNewTeacherCommentAndOpenRequest(bean.getDescription(), bean.getRequestor(), now);
	    
	    } else if (resolveRequest) {
		reserveRequest.createNewEmployeeCommentAndCloseRequest(bean.getDescription(), bean.getRequestor(), now);
	    
	    } else {
		reserveRequest.createNewTeacherOrEmployeeComment(bean.getDescription(), bean.getRequestor(), now);
	    }
	}
    }
}
