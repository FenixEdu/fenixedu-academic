/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import net.sourceforge.fenixedu.domain.Teacher;

/**
 * @author jpvl
 */
public class DepartmentInsertProfessorshipAuthorization extends AbstractTeacherDepartmentAuthorization<String> {

    public static final DepartmentInsertProfessorshipAuthorization instance = new DepartmentInsertProfessorshipAuthorization();

    @Override
    protected Integer getTeacherId(String istId) {
        Teacher teacher = Teacher.readByIstId(istId);
        return (teacher == null) ? null : teacher.getExternalId();
    }

}