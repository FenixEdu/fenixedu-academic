/*
 * Created on 2/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.ICoordinator;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCoordinator;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Tânia Pousão
 *  
 */
public class CoordinatorAndLEECAuthorizationFilter extends AuthorizationByRoleFilter {

    // the singleton of this class
    public final static CoordinatorAuthorizationFilter instance = new CoordinatorAuthorizationFilter();

    /**
     * The singleton access method of this class.
     * 
     * @return Returns the instance of this class responsible for the
     *         authorization access to services.
     */
    public static Filtro getInstance() {
        return instance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] argumentos = getServiceCallArguments(request);

        if ((id == null) || (id.getRoles() == null)
                || !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())
                || !coordinatorLEEC(id, argumentos)) {
            throw new NotAuthorizedFilterException();
        }
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean coordinatorLEEC(IUserView id, Object[] argumentos) {
        if (argumentos == null) {
            return false;
        }
        if(argumentos[0] == null)return false;	
        
        ISuportePersistente sp;
        String degreeCode = null;
        try {
        	sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());
            
           	IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();
            ICoordinator coordinator = persistentCoordinator
                    .readCoordinatorByTeacherAndExecutionDegreeId(teacher, (Integer) argumentos[0]);
            if (coordinator != null && coordinator.getExecutionDegree() != null
                    && coordinator.getExecutionDegree().getCurricularPlan() != null
                    && coordinator.getExecutionDegree().getCurricularPlan().getDegree() != null) {
                degreeCode = coordinator.getExecutionDegree().getCurricularPlan().getDegree().getSigla();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (degreeCode == null) {
            return false;
        }

        //curso de LEEC
        return new String("LEEC").equals(degreeCode);
    }
}