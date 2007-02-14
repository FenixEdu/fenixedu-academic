package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest;

public class MarkPunctualRoomsOccupationCommentsAsRead extends Service {

    public void run(PunctualRoomsOccupationRequest request, boolean forTeacher) {
	if(request != null) {
	    if(forTeacher) {
		request.setTeacherReadComments(request.getCommentsCount());
	    } else {
		request.setEmployeeReadComments(request.getCommentsCount());
	    }
	}
    }   
}
