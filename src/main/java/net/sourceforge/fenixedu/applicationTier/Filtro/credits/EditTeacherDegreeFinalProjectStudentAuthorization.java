/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import net.sourceforge.fenixedu.dataTransferObject.degree.finalProject.InfoTeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;

/**
 * @author jpvl
 */
public class EditTeacherDegreeFinalProjectStudentAuthorization extends
        AbstractTeacherDepartmentAuthorization<InfoTeacherDegreeFinalProjectStudent> {

    public static final EditTeacherDegreeFinalProjectStudentAuthorization instance =
            new EditTeacherDegreeFinalProjectStudentAuthorization();
    public final static EditTeacherDegreeFinalProjectStudentAuthorization filter =
            new EditTeacherDegreeFinalProjectStudentAuthorization();

    public static EditTeacherDegreeFinalProjectStudentAuthorization getInstance() {
        return filter;
    }

    @Override
    protected Integer getTeacherId(InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent) {
        Teacher teacher =
                RootDomainObject.getInstance().readTeacherByOID(
                        infoTeacherDegreeFinalProjectStudent.getInfoTeacher().getIdInternal());
        return teacher != null ? teacher.getIdInternal() : null;
    }

}
