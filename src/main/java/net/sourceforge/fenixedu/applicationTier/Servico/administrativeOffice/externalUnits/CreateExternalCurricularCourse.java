package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.CreateExternalCurricularCourseBean;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import pt.ist.fenixframework.Atomic;

public class CreateExternalCurricularCourse {

    @Atomic
    public static ExternalCurricularCourse run(final CreateExternalCurricularCourseBean bean) throws FenixServiceException {

        final ExternalCurricularCourse externalCurricularCourse =
                new ExternalCurricularCourse(bean.getParentUnit(), bean.getName(), bean.getCode());

        return externalCurricularCourse;
    }
}