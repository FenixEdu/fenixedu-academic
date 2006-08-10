/*
 * Created on 13/Mar/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author jpvl
 */
public class CreditsAuthorizationFilter extends Filtro {

    // the singleton of this class
    public CreditsAuthorizationFilter() {
    }

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView requester = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);

        boolean authorizedRequester = false;
        // ATTENTION: ifs order matters...
        if (requester.hasRoleType(RoleType.CREDITS_MANAGER)) {
            authorizedRequester = true;
        } else if (requester.hasRoleType(RoleType.DEPARTMENT_CREDITS_MANAGER)) {
            Teacher teacherToEdit = readTeacher(arguments[0]);
            Person requesterPerson = Person.readPersonByUsername(requester.getUtilizador());
            List departmentsWithAccessGranted = requesterPerson.getManageableDepartmentCredits();            
            Department department = teacherToEdit.getCurrentWorkingDepartment();
            authorizedRequester = departmentsWithAccessGranted.contains(department);
        } else if (requester.hasRoleType(RoleType.TEACHER)) {
            Teacher teacherToEdit = readTeacher(arguments[0]);
            authorizedRequester = teacherToEdit.getPerson().getUsername().equals(
                    requester.getUtilizador());
        }

        if (!authorizedRequester) {
            throw new NotAuthorizedFilterException(" -----------> User = " + requester.getUtilizador()
                    + "ACCESS NOT GRANTED!");
        }

    }

    private Teacher readTeacher(Object object) throws ExcepcaoPersistencia {
        Integer teacherOID = null;
        if (object instanceof InfoTeacher) {
            teacherOID = ((InfoTeacher) object).getIdInternal();
        } else if (object instanceof Integer) {
            teacherOID = (Integer) object;
        }
        return rootDomainObject.readTeacherByOID(teacherOID);
    }
}