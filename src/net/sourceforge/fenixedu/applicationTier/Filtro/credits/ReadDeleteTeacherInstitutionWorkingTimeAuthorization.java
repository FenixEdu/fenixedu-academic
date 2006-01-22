/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import net.sourceforge.fenixedu.domain.teacher.workTime.TeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author jpvl
 */
public class ReadDeleteTeacherInstitutionWorkingTimeAuthorization extends
        AbstractTeacherDepartmentAuthorization {
    public final static ReadDeleteTeacherInstitutionWorkingTimeAuthorization filter = new ReadDeleteTeacherInstitutionWorkingTimeAuthorization();

    public static ReadDeleteTeacherInstitutionWorkingTimeAuthorization getInstance() {
        return filter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.credits.AbstractTeacherDepartmentAuthorization#getTeacherId(java.lang.Object[])
     */
    protected Integer getTeacherId(Object[] arguments)
            throws ExcepcaoPersistencia {
        Integer teacherInstitutionWorkingTime = (Integer) arguments[0];
        TeacherInstitutionWorkTime teacherInstitutionWorkTime = (TeacherInstitutionWorkTime) persistentObject
                .readByOID(TeacherInstitutionWorkTime.class, teacherInstitutionWorkingTime);
        return teacherInstitutionWorkTime != null ? teacherInstitutionWorkTime.getTeacher()
                .getIdInternal() : null;
    }

}