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
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDepartment;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author jpvl
 */
public class CreditsAuthorizationFilter extends Filtro {

    // the singleton of this class
    public CreditsAuthorizationFilter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView requester = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);

        Collection roles = requester.getRoles();
        boolean authorizedRequester = false;
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        // ATTENTION: ifs order matters...
        if (AuthorizationUtils.containsRole(roles, RoleType.CREDITS_MANAGER)) {
            authorizedRequester = true;
        } else if (AuthorizationUtils.containsRole(roles, RoleType.DEPARTMENT_CREDITS_MANAGER)) {
            ITeacher teacherToEdit = readTeacher(arguments[0], sp);

            IPessoaPersistente personDAO = sp.getIPessoaPersistente();
            IPerson requesterPerson = personDAO.lerPessoaPorUsername(requester.getUtilizador());

            List departmentsWithAccessGranted = requesterPerson.getManageableDepartmentCredits();
            IPersistentDepartment departmentDAO = sp.getIDepartamentoPersistente();
            IDepartment department = departmentDAO.readByTeacher(teacherToEdit);
            authorizedRequester = departmentsWithAccessGranted.contains(department);

        } else if (AuthorizationUtils.containsRole(roles, RoleType.TEACHER)) {
            ITeacher teacherToEdit = readTeacher(arguments[0], sp);
            authorizedRequester = teacherToEdit.getPerson().getUsername().equals(
                    requester.getUtilizador());

        }

        if (!authorizedRequester) {
            throw new NotAuthorizedFilterException(" -----------> User = " + requester.getUtilizador()
                    + "ACCESS NOT GRANTED!");
        }

    }

    /**
     * @param object
     * @return @throws
     *         ExcepcaoPersistencia
     */
    private ITeacher readTeacher(Object object, ISuportePersistente sp) throws ExcepcaoPersistencia {
        Integer teacherOID = null;
        if (object instanceof InfoTeacher) {
            teacherOID = ((InfoTeacher) object).getIdInternal();
        } else if (object instanceof Integer) {
            teacherOID = (Integer) object;
        }
        return (ITeacher) sp.getIPersistentTeacher().readByOID(Teacher.class, teacherOID);
    }
}