/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Filtro.credits;

import Dominio.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import Dominio.degree.finalProject.TeacherDegreeFinalProjectStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;

/**
 * @author jpvl
 */
public class ReadDeleteTeacherDegreeFinalProjectStudentAuthorization
        extends
            AbstractTeacherDepartmentAuthorization
{
    public final static ReadDeleteTeacherDegreeFinalProjectStudentAuthorization filter = new ReadDeleteTeacherDegreeFinalProjectStudentAuthorization();

    public static ReadDeleteTeacherDegreeFinalProjectStudentAuthorization getInstance()
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
        Integer teacherDegreeFinalProjectStudentId = (Integer) arguments[0];
        IPersistentTeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudentDAO = sp
                .getIPersistentTeacherDegreeFinalProjectStudent();

        ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent = (ITeacherDegreeFinalProjectStudent) teacherDegreeFinalProjectStudentDAO
                .readByOId(new TeacherDegreeFinalProjectStudent(teacherDegreeFinalProjectStudentId),
                        false);
        return teacherDegreeFinalProjectStudent != null ? teacherDegreeFinalProjectStudent.getTeacher()
                .getIdInternal() : null;
    }

}