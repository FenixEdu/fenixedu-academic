/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import net.sourceforge.fenixedu.dataTransferObject.degree.finalProject.InfoTeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author jpvl
 */
public class EditTeacherDegreeFinalProjectStudentAuthorization extends
        AbstractTeacherDepartmentAuthorization {
    public final static EditTeacherDegreeFinalProjectStudentAuthorization filter = new EditTeacherDegreeFinalProjectStudentAuthorization();

    public static EditTeacherDegreeFinalProjectStudentAuthorization getInstance() {
        return filter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.credits.AbstractTeacherDepartmentAuthorization#getTeacherId(java.lang.Object[])
     */
    protected Integer getTeacherId(Object[] arguments, ISuportePersistente sp) {
        InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent = (InfoTeacherDegreeFinalProjectStudent) arguments[1];
        IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();

        ITeacher teacher;
        try {
            teacher = (ITeacher) teacherDAO.readByOID(Teacher.class,
                    infoTeacherDegreeFinalProjectStudent.getInfoTeacher().getIdInternal());
        } catch (ExcepcaoPersistencia e) {
            return null;
        }
        return teacher != null ? teacher.getIdInternal() : null;
    }

}