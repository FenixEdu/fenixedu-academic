/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.teacher;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.framework.DomainObjectAuthorizationFilter;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.OldPublication;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class OldPublicationTeacherAuthorizationFilter extends DomainObjectAuthorizationFilter {

    protected boolean verifyCondition(IUserView id, Integer objectId) {
        try {
            Teacher teacher = Teacher.readTeacherByUsername(id.getUtilizador());
            OldPublication oldPublication = rootDomainObject.readOldPublicationByOID(objectId);
            return oldPublication.getTeacher().equals(teacher);
        } catch (Exception e) {
            return false;
        }
    }

    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }
    
}
