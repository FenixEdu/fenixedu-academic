package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.EditExternalCurricularCourseBean;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;

public class EditExternalCurricularCourse extends Service {

    public void run(final EditExternalCurricularCourseBean bean) {
	final ExternalCurricularCourse externalCurricularCourse = bean.getExternalCurricularCourse();
	externalCurricularCourse.edit(bean.getName(), bean.getCode());
    }
}
