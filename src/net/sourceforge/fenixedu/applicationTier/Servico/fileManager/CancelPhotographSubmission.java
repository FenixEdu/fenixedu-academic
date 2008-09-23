package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.PhotoState;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

/**
 * @author Pedro Santos (pmrsa)
 */
public class CancelPhotographSubmission extends FenixService {
    public void run() {
	Photograph photo = AccessControl.getPerson().getPersonalPhotoEvenIfRejected();
	if (photo != null && photo.getState() == PhotoState.PENDING) {
	    photo.setState(PhotoState.REJECTED);
	    photo.setRejectedAcknowledged(Boolean.TRUE);
	}
    }
}
