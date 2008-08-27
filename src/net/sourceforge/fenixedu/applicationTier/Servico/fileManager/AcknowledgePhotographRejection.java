package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.PhotoState;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

/**
 * @author Pedro Santos (pmrsa)
 */
public class AcknowledgePhotographRejection extends Service {
    public void run() {
	Photograph photo = AccessControl.getPerson().getPersonalPhotoEvenIfRejected();
	if (photo != null && photo.getState() == PhotoState.REJECTED) {
	    photo.setRejectedAcknowledged(Boolean.TRUE);
	}
    }
}
