package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.teacher.RoomsReserveBean;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationComment;

public class CreateNewRoomsReserveComment extends Service {

    public void run(RoomsReserveBean bean, Boolean reOpenRequest, Boolean resolveRequest) {
	if(bean != null) {	    
	    new PunctualRoomsOccupationComment(bean.getReserveRequest(), null, 
		    bean.getDescription(), bean.getRequestor(), new DateTime(), reOpenRequest, resolveRequest);	    
	}
    }
}
