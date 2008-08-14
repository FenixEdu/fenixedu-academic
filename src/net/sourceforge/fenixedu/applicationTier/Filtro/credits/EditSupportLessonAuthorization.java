/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.InfoSupportLesson;
import net.sourceforge.fenixedu.domain.Professorship;

/**
 * @author jpvl
 */
public class EditSupportLessonAuthorization extends AbstractTeacherDepartmentAuthorization {
    public final static EditSupportLessonAuthorization filter = new EditSupportLessonAuthorization();

    public static EditSupportLessonAuthorization getInstance() {
	return filter;
    }

    protected Integer getTeacherId(Object[] arguments) {
	InfoSupportLesson supportLesson = (InfoSupportLesson) arguments[1];

	Professorship professorship = rootDomainObject.readProfessorshipByOID(supportLesson.getInfoProfessorship()
		.getIdInternal());
	return professorship != null ? professorship.getTeacher().getIdInternal() : null;
    }

}
