/*
 * Created on 2004/04/19
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroupProposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 *  
 */
public class RemoveProposalFromFinalDegreeWorkStudentGroup implements IService {

    public RemoveProposalFromFinalDegreeWorkStudentGroup() {
        super();
    }

    public boolean run(Integer groupOID, Integer groupProposalOID) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                .getIPersistentFinalDegreeWork();

        IGroup group = (IGroup) persistentFinalDegreeWork.readByOID(Group.class, groupOID);
        IGroupProposal groupProposal = (IGroupProposal) CollectionUtils.find(group.getGroupProposals(),
                new PREDICATE_FIND_BY_ID(groupProposalOID));
        if (groupProposal != null) {
            for (int i = 0; i < group.getGroupProposals().size(); i++) {
                IGroupProposal otherGroupProposal = group.getGroupProposals().get(i);
                if (!groupProposal.equals(otherGroupProposal)
                        && groupProposal.getOrderOfPreference().intValue() < otherGroupProposal
                                .getOrderOfPreference().intValue()) {
                    persistentFinalDegreeWork.simpleLockWrite(otherGroupProposal);
                    otherGroupProposal.setOrderOfPreference(new Integer(otherGroupProposal
                            .getOrderOfPreference().intValue() - 1));
                }
            }
            persistentFinalDegreeWork.simpleLockWrite(group);
            group.getGroupProposals().remove(groupProposal);
            return true;
        }
        return false;

    }

    private class PREDICATE_FIND_BY_ID implements Predicate {

        Integer groupProposalID;

        public boolean evaluate(Object arg0) {
            IGroupProposal groupProposal = (IGroupProposal) arg0;
            return groupProposalID.equals(groupProposal.getIdInternal());
        }

        public PREDICATE_FIND_BY_ID(Integer groupProposalID) {
            super();
            this.groupProposalID = groupProposalID;
        }
    }

}