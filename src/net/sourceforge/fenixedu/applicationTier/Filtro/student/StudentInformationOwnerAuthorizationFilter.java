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

    public StudentInformationOwnerAuthorizationFilter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);

        if (id == null || id.getRoleTypes() == null || !id.hasRoleType(getRoleType())
                || !curriculumOwner(id, request.getServiceParameters().parametersArray())) {
            throw new NotAuthorizedFilterException();
        }
    }

    /**
     * @param id
     * @param objects
     * @return
     */
    private boolean curriculumOwner(IUserView id, Object[] arguments) {
        StudentCurricularPlan studentCurricularPlan;
        try {
            studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID((Integer) arguments[1]);
            if (studentCurricularPlan == null) {
                return false;
            }
            if (!studentCurricularPlan.getStudent().getPerson().getUsername().equals(id.getUtilizador())) {
                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    protected RoleType getRoleType() {
        return RoleType.STUDENT;
    }
}