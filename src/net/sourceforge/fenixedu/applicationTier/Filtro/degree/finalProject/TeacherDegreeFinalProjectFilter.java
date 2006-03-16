/*
 * Created on Nov 12, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.degree.finalProject;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author jpvl
 */
public class TeacherDegreeFinalProjectFilter extends AuthorizationByRoleFilter {

    public TeacherDegreeFinalProjectFilter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.DEPARTMENT_CREDITS_MANAGER;
    }

    /**
     * @param integer
     */
    private void verifyTeacherPermission(IUserView requester, Integer teacherNumber)
            throws FenixServiceException {

        Teacher teacher = Teacher.readByNumber(teacherNumber);
        if (teacher == null) {
            throw new NonExistingServiceException("Teacher doesn't exists");
        }
        
        Person requesterPerson = Person.readPersonByUsername(requester.getUtilizador());
        if (requesterPerson == null) {
            throw new NotAuthorizedException("No person with that userView");
        }

        List departmentsWithAccessGranted = requesterPerson.getManageableDepartmentCredits();

        Department department = teacher.getCurrentWorkingDepartment();

        if (department == null) {
            throw new NotAuthorizedException("Teacher number " + teacher.getTeacherNumber()
                    + " doesn't have department!");
        }

        if (!departmentsWithAccessGranted.contains(department)) {
            throw new NotAuthorizedException("Not authorized to run the service!");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws FilterException,
            Exception {
        super.execute(request, response);
        verifyTeacherPermission(getRemoteUser(request), (Integer) getServiceCallArguments(request)[0]);
    }

}