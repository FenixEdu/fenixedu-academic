/*
 * Created on 2004/03/12
 *  
 */
package ServidorAplicacao.Filtro.coordinator;

import java.util.List;

import Dominio.ICoordinator;
import Dominio.IExecutionDegree;
import Dominio.ITeacher;
import Dominio.finalDegreeWork.IProposal;
import Dominio.finalDegreeWork.Proposal;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.framework.DomainObjectAuthorizationFilter;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentFinalDegreeWork;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author Luis Cruz
 *  
 */
public class AccessFinalDegreeWorkProposalAuthorizationFilter extends DomainObjectAuthorizationFilter {
    /**
     *  
     */
    public AccessFinalDegreeWorkProposalAuthorizationFilter() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.framework.DomainObjectAuthorizationFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.framework.DomainObjectAuthorizationFilter#verifyCondition(ServidorAplicacao.IUserView,
     *      java.lang.Integer)
     */
    protected boolean verifyCondition(IUserView id, Integer objectId) {
        try {
            if (objectId == null) {
                return false;
            }

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            IPersistentFinalDegreeWork persistentFinalDegreeWork = sp.getIPersistentFinalDegreeWork();

            IProposal proposal = (IProposal) persistentFinalDegreeWork.readByOID(Proposal.class,
                    objectId);
            if (proposal == null) {
                return false;
            }
            IExecutionDegree executionDegree = proposal.getExecutionDegree();
            ITeacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());

            List coordinators = executionDegree.getCoordinatorsList();
            if (coordinators != null && teacher != null) {
                for (int i = 0; i < coordinators.size(); i++) {
                    ICoordinator coordinator = (ICoordinator) coordinators.get(i);
                    if (coordinator != null && teacher.equals(coordinator.getTeacher())) {
                        return true;
                    }
                }
            }

            if (teacher != null && (teacher.equals(proposal.getOrientator()))
                    || (teacher.equals(proposal.getCoorientator()))) {
                return true;
            }

            return false;
        } catch (ExcepcaoPersistencia e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}