/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Filtro.credits;

import DataBeans.degree.finalProject.InfoTeacherDegreeFinalProjectStudent;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;

/**
 * @author jpvl
 */
public class EditTeacherDegreeFinalProjectStudentAuthorization
        extends
            AbstractTeacherDepartmentAuthorization
{
    public final static EditTeacherDegreeFinalProjectStudentAuthorization filter = new EditTeacherDegreeFinalProjectStudentAuthorization();

    public static EditTeacherDegreeFinalProjectStudentAuthorization getInstance()
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
        InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent = (InfoTeacherDegreeFinalProjectStudent) arguments[1];
        IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();

        ITeacher teacher = (ITeacher) teacherDAO.readByOId(new Teacher(
                infoTeacherDegreeFinalProjectStudent.getInfoTeacher().getIdInternal()), false);

        return teacher != null ? teacher.getIdInternal() : null;
    }

}