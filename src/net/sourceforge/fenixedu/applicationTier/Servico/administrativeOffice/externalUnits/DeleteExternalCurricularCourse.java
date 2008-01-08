package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;

public class DeleteExternalCurricularCourse extends Service {

    public void run(final ExternalCurricularCourse externalCurricularCourse) {
	externalCurricularCourse.delete();
    }
}
