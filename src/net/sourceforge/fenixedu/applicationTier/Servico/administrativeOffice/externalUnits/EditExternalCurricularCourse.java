package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.EditExternalCurricularCourseBean;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class EditExternalCurricularCourse extends FenixService {

    @Checked("RolePredicates.ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static void run(final EditExternalCurricularCourseBean bean) {
	final ExternalCurricularCourse externalCurricularCourse = bean.getExternalCurricularCourse();
	externalCurricularCourse.edit(bean.getName(), bean.getCode());
    }
}