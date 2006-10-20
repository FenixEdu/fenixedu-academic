package net.sourceforge.fenixedu.applicationTier.Filtro.student;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/*
 * 
 * @author Fernanda Quitério 10/Fev/2004
 *  
 */
public class StudentInformationOwnerAuthorizationFilter extends Filtro {

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);

        if (id == null || id.getRoleTypes() == null || !id.hasRoleType(getRoleType())
                || !curriculumOwner(id, request.getServiceParameters().parametersArray())) {
            throw new NotAuthorizedFilterException();
        }
    }

    private boolean curriculumOwner(IUserView id, Object[] arguments) {
        final StudentCurricularPlan studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID((Integer) arguments[1]);
        return studentCurricularPlan != null && studentCurricularPlan.getRegistration().getPerson() == id.getPerson();
    }

    protected RoleType getRoleType() {
        return RoleType.STUDENT;
    }
}