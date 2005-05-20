/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.gesdis;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.framework.DomainObjectAuthorizationFilter;
import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentResponsibleFor;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

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
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());
            IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();
            List<IResponsibleFor> responsiblesFor = persistentResponsibleFor
                    .readByExecutionCourse(executionCourseID);

            for (IResponsibleFor responsibleFor : responsiblesFor) {
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