/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Filtro.credits;

import DataBeans.teacher.workTime.InfoTeacherInstitutionWorkTime;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;

/**
 * @author jpvl
 */
public class EditTeacherInstitutionWorkingTimeAuthorization extends AbstractTeacherDepartmentAuthorization
{
	public final static EditTeacherInstitutionWorkingTimeAuthorization filter = new EditTeacherInstitutionWorkingTimeAuthorization();

	public static EditTeacherInstitutionWorkingTimeAuthorization getInstance()
	{
		return filter;
	}

    
    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Filtro.credits.AbstractTeacherDepartmentAuthorization#getTeacherId(java.lang.Object[])
	 */
    protected Integer getTeacherId(Object[] arguments, ISuportePersistente sp)
    {
        InfoTeacherInstitutionWorkTime infoTeacherInstitutionWorkTime = (InfoTeacherInstitutionWorkTime) arguments[1];
        IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
        
        ITeacher teacher = (ITeacher) teacherDAO.readByOId(new Teacher(
                infoTeacherInstitutionWorkTime.getInfoTeacher().getIdInternal()),
                false);
        
        return teacher != null ? teacher.getIdInternal() : null;
    }

}