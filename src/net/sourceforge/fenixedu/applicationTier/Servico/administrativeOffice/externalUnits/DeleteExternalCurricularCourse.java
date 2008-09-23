package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;

public class DeleteExternalCurricularCourse extends FenixService {

    public void run(final ExternalCurricularCourse externalCurricularCourse) {
	externalCurricularCourse.delete();
    }
}
