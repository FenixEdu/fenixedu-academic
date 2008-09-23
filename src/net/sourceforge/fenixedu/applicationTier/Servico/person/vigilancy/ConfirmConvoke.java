package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.vigilancy.OtherCourseVigilancy;

public class ConfirmConvoke extends FenixService {

    public void run(OtherCourseVigilancy convoke) {
	convoke.setConfirmed(true);
    }

}
