/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import net.sourceforge.fenixedu.dataTransferObject.teacher.workTime.InfoTeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

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
    protected Integer getTeacherId(Object[] arguments, ISuportePersistente sp) {
        InfoTeacherInstitutionWorkTime infoTeacherInstitutionWorkTime = (InfoTeacherInstitutionWorkTime) arguments[1];
        IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();

        ITeacher teacher;
        try {
            teacher = (ITeacher) teacherDAO.readByOID(Teacher.class, infoTeacherInstitutionWorkTime
                    .getInfoTeacher().getIdInternal());
        } catch (ExcepcaoPersistencia e) {
            return null;
        }
        return teacher != null ? teacher.getIdInternal() : null;
    }

}