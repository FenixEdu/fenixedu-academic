package net.sourceforge.fenixedu.domain.finalDegreeWork;

import java.util.Comparator;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.bennu.core.domain.Bennu;

public class GroupProposal extends GroupProposal_Base {

    public static final Comparator<GroupProposal> COMPARATOR_BY_PREFERENCE_ORDER = new BeanComparator("orderOfPreference");

    public GroupProposal() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        deleteAttributions();
        setFinalDegreeDegreeWorkGroup(null);
        setFinalDegreeWorkProposal(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public void deleteAttributions() {
        final Proposal proposal = getFinalDegreeWorkProposal();
        if (proposal != null && proposal.getGroupAttributed() == getFinalDegreeDegreeWorkGroup()) {
            proposal.setGroupAttributed(null);
        }
        if (proposal != null && proposal.getGroupAttributedByTeacher() == getFinalDegreeDegreeWorkGroup()) {
            proposal.setGroupAttributedByTeacher(null);
        }
        final FinalDegreeWorkGroup finalDegreeWorkGroup = getFinalDegreeDegreeWorkGroup();
        if (finalDegreeWorkGroup != null) {
            for (final GroupStudent groupStudent : finalDegreeWorkGroup.getGroupStudentsSet()) {
                if (groupStudent.getFinalDegreeWorkProposalConfirmation() == proposal) {
                    groupStudent.setFinalDegreeWorkProposalConfirmation(null);
                }
            }
        }
    }

    @Deprecated
    public boolean hasFinalDegreeWorkProposal() {
        return getFinalDegreeWorkProposal() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasFinalDegreeDegreeWorkGroup() {
        return getFinalDegreeDegreeWorkGroup() != null;
    }

    @Deprecated
    public boolean hasOrderOfPreference() {
        return getOrderOfPreference() != null;
    }

}
