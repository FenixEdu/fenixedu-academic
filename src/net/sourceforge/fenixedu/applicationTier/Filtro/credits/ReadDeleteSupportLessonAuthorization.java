/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import net.sourceforge.fenixedu.domain.SupportLesson;

/**
 * @author jpvl
 */
public class ReadDeleteSupportLessonAuthorization extends AbstractTeacherDepartmentAuthorization {
    public final static ReadDeleteSupportLessonAuthorization filter = new ReadDeleteSupportLessonAuthorization();

    public static ReadDeleteSupportLessonAuthorization getInstance() {
	return filter;
    }

    protected Integer getTeacherId(Object[] arguments) {
	Integer supportLessonId = (Integer) arguments[0];

	SupportLesson supportLesson = rootDomainObject.readSupportLessonByOID(supportLessonId);
	return supportLesson != null ? supportLesson.getProfessorship().getTeacher().getIdInternal() : null;
    }

}
