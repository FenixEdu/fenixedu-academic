/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.gesdis;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.framework.DomainObjectAuthorizationFilter;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ReadCourseInformationAuthorizationFilter extends DomainObjectAuthorizationFilter {

    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    protected boolean verifyCondition(IUserView id, Integer executionCourseID) {
        try {
            Teacher teacher = Teacher.readTeacherByUsername(id.getUtilizador());

            ExecutionCourse executionCourse = (ExecutionCourse) persistentObject.readByOID(
                    ExecutionCourse.class, executionCourseID);

            List<Professorship> responsiblesFor = executionCourse.responsibleFors();

            for (Professorship responsibleFor : responsiblesFor) {
                if (responsibleFor.getTeacher().equals(teacher))
                    return true;
            }

            return false;

        } catch (ExcepcaoPersistencia e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}