/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import net.sourceforge.fenixedu.dataTransferObject.teacher.workTime.InfoTeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.domain.Teacher;

/**
 * @author jpvl
 */
public class EditTeacherInstitutionWorkingTimeAuthorization extends AbstractTeacherDepartmentAuthorization {
    public final static EditTeacherInstitutionWorkingTimeAuthorization filter = new EditTeacherInstitutionWorkingTimeAuthorization();

    public static EditTeacherInstitutionWorkingTimeAuthorization getInstance() {
	return filter;
    }

    protected Integer getTeacherId(Object[] arguments) {
	InfoTeacherInstitutionWorkTime infoTeacherInstitutionWorkTime = (InfoTeacherInstitutionWorkTime) arguments[1];

	Teacher teacher = rootDomainObject.readTeacherByOID(infoTeacherInstitutionWorkTime.getInfoTeacher().getIdInternal());
	return teacher != null ? teacher.getIdInternal() : null;
    }

}
