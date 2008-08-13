/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent;

/**
 * @author jpvl
 */
public class ReadDeleteTeacherDegreeFinalProjectStudentAuthorization extends AbstractTeacherDepartmentAuthorization {
    public final static ReadDeleteTeacherDegreeFinalProjectStudentAuthorization filter = new ReadDeleteTeacherDegreeFinalProjectStudentAuthorization();

    public static ReadDeleteTeacherDegreeFinalProjectStudentAuthorization getInstance() {
	return filter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ServidorAplicacao.Filtro.credits.AbstractTeacherDepartmentAuthorization
     * #getTeacherId(java.lang.Object[])
     */
    protected Integer getTeacherId(Object[] arguments) {
	Integer teacherDegreeFinalProjectStudentId = (Integer) arguments[0];

	TeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent = rootDomainObject
		.readTeacherDegreeFinalProjectStudentByOID(teacherDegreeFinalProjectStudentId);
	return teacherDegreeFinalProjectStudent != null ? teacherDegreeFinalProjectStudent.getTeacher().getIdInternal() : null;
    }

}