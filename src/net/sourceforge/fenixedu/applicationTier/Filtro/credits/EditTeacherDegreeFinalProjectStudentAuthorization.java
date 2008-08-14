/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import net.sourceforge.fenixedu.dataTransferObject.degree.finalProject.InfoTeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.domain.Teacher;

/**
 * @author jpvl
 */
public class EditTeacherDegreeFinalProjectStudentAuthorization extends AbstractTeacherDepartmentAuthorization {
    public final static EditTeacherDegreeFinalProjectStudentAuthorization filter = new EditTeacherDegreeFinalProjectStudentAuthorization();

    public static EditTeacherDegreeFinalProjectStudentAuthorization getInstance() {
	return filter;
    }

    protected Integer getTeacherId(Object[] arguments) {
	InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent = (InfoTeacherDegreeFinalProjectStudent) arguments[1];

	Teacher teacher = rootDomainObject
		.readTeacherByOID(infoTeacherDegreeFinalProjectStudent.getInfoTeacher().getIdInternal());
	return teacher != null ? teacher.getIdInternal() : null;
    }

}
