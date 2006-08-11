/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.gesdis;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseReport;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class EditCourseInformationAuthorizationFilter extends AuthorizationByRoleFilter {

    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);

        try {
            if (((id != null && id.getRoleTypes() != null && !id.hasRoleType(getRoleType())))
                    || (id == null) || (id.getRoleTypes() == null)
                    || (!isResponsibleFor(id, (InfoCourseReport) arguments[1]))) {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedException(e.getMessage());
        }
    }

    private boolean isResponsibleFor(IUserView id, InfoCourseReport infoCourseReport) {
        final Person person = id.getPerson();
        final Teacher teacher = person == null ? null : person.getTeacher();

        InfoExecutionCourse infoExecutionCourse = infoCourseReport.getInfoExecutionCourse();
        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(infoExecutionCourse
                .getIdInternal());

        List<Professorship> responsiblesFor = executionCourse.responsibleFors();

        for (Professorship professorship : responsiblesFor) {
            if (professorship.getTeacher().equals(teacher))
                return true;
        }
        return false;
    }
}