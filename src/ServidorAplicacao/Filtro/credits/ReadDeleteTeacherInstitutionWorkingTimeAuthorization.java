/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Filtro.credits;

import Dominio.teacher.workTime.ITeacherInstitutionWorkTime;
import Dominio.teacher.workTime.TeacherInstitutionWorkTime;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
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
    protected Integer getTeacherId(Object[] arguments, ISuportePersistente sp)
            throws FenixServiceException
    {
        Integer teacherInstitutionWorkingTime = (Integer) arguments[0];
        IPersistentTeacherInstitutionWorkingTime teacherInstitutionWorkingTimeDAO = sp
                .getIPersistentTeacherInstitutionWorkingTime();

        ITeacherInstitutionWorkTime teacherInstitutionWorkTime = (ITeacherInstitutionWorkTime) teacherInstitutionWorkingTimeDAO
                .readByOId(new TeacherInstitutionWorkTime(teacherInstitutionWorkingTime), false);
        return teacherInstitutionWorkTime != null ? teacherInstitutionWorkTime.getTeacher()
                .getIdInternal() : null;
    }

}