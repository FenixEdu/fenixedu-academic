/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Filtro.credits;

import Dominio.teacher.workTime.ITeacherInstitutionWorkTime;
import Dominio.teacher.workTime.TeacherInstitutionWorkTime;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;

/**
 * @author jpvl
 */
public class ReadDeleteTeacherInstitutionWorkingTimeAuthorization
        extends
            AbstractTeacherDepartmentAuthorization
{
    public final static ReadDeleteTeacherInstitutionWorkingTimeAuthorization filter = new ReadDeleteTeacherInstitutionWorkingTimeAuthorization();

    public static ReadDeleteTeacherInstitutionWorkingTimeAuthorization getInstance()
    {
        return filter;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Filtro.credits.AbstractTeacherDepartmentAuthorization#getTeacherId(java.lang.Object[])
	 */
    protected Integer getTeacherId(Object[] arguments, ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        Integer teacherInstitutionWorkingTime = (Integer) arguments[0];
        IPersistentTeacherInstitutionWorkingTime teacherInstitutionWorkingTimeDAO = sp
                .getIPersistentTeacherInstitutionWorkingTime();

        ITeacherInstitutionWorkTime teacherInstitutionWorkTime = (ITeacherInstitutionWorkTime) teacherInstitutionWorkingTimeDAO
                .readByOID(TeacherInstitutionWorkTime.class, teacherInstitutionWorkingTime, false);
        return teacherInstitutionWorkTime != null ? teacherInstitutionWorkTime.getTeacher()
                .getIdInternal() : null;
    }

}