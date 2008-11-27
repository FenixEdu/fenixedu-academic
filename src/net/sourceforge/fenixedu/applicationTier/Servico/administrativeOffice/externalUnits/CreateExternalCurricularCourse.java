package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.CreateExternalCurricularCourseBean;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateExternalCurricularCourse extends FenixService {

    @Checked("RolePredicates.ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static ExternalCurricularCourse run(final CreateExternalCurricularCourseBean bean) throws FenixServiceException {

	final ExternalCurricularCourse externalCurricularCourse = new ExternalCurricularCourse(bean.getParentUnit(), bean
		.getName(), bean.getCode());

	return externalCurricularCourse;
    }
}