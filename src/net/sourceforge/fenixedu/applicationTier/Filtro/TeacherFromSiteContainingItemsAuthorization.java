package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class TeacherFromSiteContainingItemsAuthorization extends AuthorizationByRoleFilter {

    @Override
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        super.execute(request, response);
        
        List<Item> items = (List<Item>) request.getServiceParameters().getParameter(0);
        for (Item item : items) {
            checkItem(getRemoteUser(request), item);
        }
    }

    private void checkItem(IUserView remoteUser, Item item) throws NotAuthorizedFilterException {
        if (item == null) {
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
        
        ExecutionCourseSite site = (ExecutionCourseSite) item.getSection().getSite();
        if (teacher.isResponsibleFor(site.getExecutionCourse()) == null) {
            throw new NotAuthorizedFilterException();
        }
    }

}
