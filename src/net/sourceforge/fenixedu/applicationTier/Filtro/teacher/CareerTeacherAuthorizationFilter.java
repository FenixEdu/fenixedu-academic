/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.teacher;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.framework.DomainObjectAuthorizationFilter;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.Career;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class CareerTeacherAuthorizationFilter extends DomainObjectAuthorizationFilter {

    protected boolean verifyCondition(IUserView id, Integer objectId) {
        final Person person = id.getPerson();
        final Teacher teacher = person == null ? null : person.getTeacher();
        final Career career = rootDomainObject.readCareerByOID(objectId);
        return teacher != null && career.getTeacher() == teacher;
    }

    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

}
