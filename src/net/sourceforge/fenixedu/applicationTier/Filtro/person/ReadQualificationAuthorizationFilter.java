/*
 * Created on 21/Nov/2003
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.person;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationUtils;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IQualification;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.grant.owner.IGrantOwner;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentQualification;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOwner;
import net.sourceforge.fenixedu.util.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Barbosa
 * @author Pica
 */

public class ReadQualificationAuthorizationFilter extends Filtro {

    public ReadQualificationAuthorizationFilter() {
    }

    //Role Type of teacher
    protected RoleType getRoleTypeTeacher() {
        return RoleType.TEACHER;
    }

    //Role Type of Grant Owner Manager
    protected RoleType getRoleTypeGrantOwnerManager() {
        return RoleType.GRANT_OWNER_MANAGER;
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);
        try {
            boolean isNew = ((arguments[0] == null) || ((Integer) arguments[0]).equals(new Integer(0)));

            //Verify if needed fields are null
            if ((id == null) || (id.getRoles() == null)) {
                throw new NotAuthorizedException();
            }

            Integer objectId = (Integer) arguments[0];

            //Verify if:
            // 1: The user ir a Grant Owner Manager and the qualification
            // belongs to a Grant Owner
            // 2: The user ir a Teacher and the qualification is his own
            if (!isNew) {
                boolean valid = false;

                if ((AuthorizationUtils.containsRole(id.getRoles(), getRoleTypeGrantOwnerManager()))
                        && isGrantOwner(objectId)) {
                    valid = true;
                }

                if (AuthorizationUtils.containsRole(id.getRoles(), getRoleTypeTeacher())
                        && isOwnQualification(id.getUtilizador(), objectId)) {
                    valid = true;
                }

                if (!valid)
                    throw new NotAuthorizedException();
            } else {
                if (!AuthorizationUtils.containsRole(id.getRoles(), getRoleTypeGrantOwnerManager())
                        && !AuthorizationUtils.containsRole(id.getRoles(), getRoleTypeTeacher()))
                    throw new NotAuthorizedException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedException();
        }
    }

    /**
     * Verifies if the qualification user is a grant owner
     * 
     * @param arguments
     * @return true or false
     */
    private boolean isGrantOwner(Integer objectId) {
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentQualification persistentQualification = persistentSuport
                    .getIPersistentQualification();
            IQualification qualification = (IQualification) persistentQualification.readByOID(
                    Qualification.class, objectId);

            IPersistentGrantOwner persistentGrantOwner = persistentSuport.getIPersistentGrantOwner();
            //Try to read the grant owner from the database
            IGrantOwner grantOwner = persistentGrantOwner.readGrantOwnerByPerson(qualification
                    .getPerson().getIdInternal());

            return grantOwner != null;
        } catch (ExcepcaoPersistencia e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifies if the qualification to be changed is owned by the user that is
     * running the service
     * 
     * @param arguments
     * @return true or false
     */
    private boolean isOwnQualification(String username, Integer objectId) {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            IPerson person = persistentPerson.lerPessoaPorUsername(username);

            IPersistentQualification persistentQualification = sp.getIPersistentQualification();
            IQualification qualification = (IQualification) persistentQualification.readByOID(
                    Qualification.class, objectId);

            return qualification.getPerson().equals(person);
        } catch (ExcepcaoPersistencia e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}