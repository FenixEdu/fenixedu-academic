/*
 * Created on 2004/04/19
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Luis Cruz
 *  
 */
public class RemoveProposalFromFinalDegreeWorkStudentGroup extends Service {

    public RemoveProposalFromFinalDegreeWorkStudentGroup() {
        super();
    }

    public boolean run(Integer groupOID, Integer groupProposalOID) throws ExcepcaoPersistencia {
        IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                .getIPersistentFinalDegreeWork();

        Group group = (Group) persistentFinalDegreeWork.readByOID(Group.class, groupOID);
        GroupProposal groupProposal = (GroupProposal) CollectionUtils.find(group.getGroupProposals(),
                new PREDICATE_FIND_BY_ID(groupProposalOID));
        if (groupProposal != null) {
            for (int i = 0; i < group.getGroupProposals().size(); i++) {
                GroupProposal otherGroupProposal = group.getGroupProposals().get(i);
                if (!groupProposal.equals(otherGroupProposal)
                        && groupProposal.getOrderOfPreference().intValue() < otherGroupProposal
                                .getOrderOfPreference().intValue()) {
                    otherGroupProposal.setOrderOfPreference(new Integer(otherGroupProposal
                            .getOrderOfPreference().intValue() - 1));
                }
            }
            group.getGroupProposals().remove(groupProposal);
            return true;
        }
        return false;

    }

    private class PREDICATE_FIND_BY_ID implements Predicate {

        Integer groupProposalID;

        public boolean evaluate(Object arg0) {
            GroupProposal groupProposal = (GroupProposal) arg0;
            return groupProposalID.equals(groupProposal.getIdInternal());
        }

        public PREDICATE_FIND_BY_ID(Integer groupProposalID) {
            super();
            this.groupProposalID = groupProposalID;
        }
    }

}