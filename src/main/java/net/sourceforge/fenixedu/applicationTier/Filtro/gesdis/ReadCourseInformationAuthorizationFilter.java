/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.gesdis;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.framework.DomainObjectAuthorizationFilter;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ReadCourseInformationAuthorizationFilter extends DomainObjectAuthorizationFilter {

    public static final ReadCourseInformationAuthorizationFilter instance = new ReadCourseInformationAuthorizationFilter();

    @Override
    protected RoleType getRoleType() {
        return RoleType.PERSON;
    }

    @Override
    protected boolean verifyCondition(IUserView id, String executionCourseID) {
        final Person person = id.getPerson();

        for (final Professorship professorship : person.getProfessorshipsSet()) {
            final ExecutionCourse executionCourse = professorship.getExecutionCourse();
            if (executionCourse.getExternalId().equals(executionCourseID)) {
                return true;
            }
        }
        return false;
    }

}