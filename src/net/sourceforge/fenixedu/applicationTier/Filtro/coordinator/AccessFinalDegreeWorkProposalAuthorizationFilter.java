/*
 * Created on 2004/03/12
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.coordinator;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.framework.DomainObjectAuthorizationFilter;
import net.sourceforge.fenixedu.domain.ICoordinator;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

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

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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