package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.ist.utl.fenix.utils.Pair;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class TeacherFromSiteContainingSectionsAuthorization extends AuthorizationByRoleFilter {

    @Override
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        super.execute(request, response);
        
        List<Pair<Section, Section>> sections = (List<Pair<Section, Section>>) request.getServiceParameters().getParameter(1);
        for (Pair<Section, Section> pair : sections) {
            checkSection(getRemoteUser(request), pair.getKey());
            checkSection(getRemoteUser(request), pair.getValue());
        }
    }

    private void checkSection(IUserView remoteUser, Section section) throws NotAuthorizedFilterException {
        if (section == null) {
            return;
        }
        
        Person person = remoteUser.getPerson();
        
        if (person == null) {
            throw new NotAuthorizedFilterException();
        }
        
        Teacher teacher = person.getTeacher();
        
        if (teacher == null) {
            throw new NotAuthorizedFilterException();
        }
        
        ExecutionCourseSite site = (ExecutionCourseSite) section.getSite();
        if (! teacher.hasProfessorshipForExecutionCourse(site.getExecutionCourse())) {
            throw new NotAuthorizedFilterException();
        }
    }

}
