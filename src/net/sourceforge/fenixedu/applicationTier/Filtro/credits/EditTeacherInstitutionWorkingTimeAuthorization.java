/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import net.sourceforge.fenixedu.dataTransferObject.teacher.workTime.InfoTeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author jpvl
 */
public class EditTeacherInstitutionWorkingTimeAuthorization extends
        AbstractTeacherDepartmentAuthorization {
    public final static EditTeacherInstitutionWorkingTimeAuthorization filter = new EditTeacherInstitutionWorkingTimeAuthorization();

    public static EditTeacherInstitutionWorkingTimeAuthorization getInstance() {
        return filter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.credits.AbstractTeacherDepartmentAuthorization#getTeacherId(java.lang.Object[])
     */
    protected Integer getTeacherId(Object[] arguments) {
        InfoTeacherInstitutionWorkTime infoTeacherInstitutionWorkTime = (InfoTeacherInstitutionWorkTime) arguments[1];

        Teacher teacher;
        try {
            teacher = (Teacher) persistentObject.readByOID(Teacher.class, infoTeacherInstitutionWorkTime
                    .getInfoTeacher().getIdInternal());
        } catch (ExcepcaoPersistencia e) {
            return null;
        }
        return teacher != null ? teacher.getIdInternal() : null;
    }

}