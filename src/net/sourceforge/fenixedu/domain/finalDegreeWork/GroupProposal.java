package net.sourceforge.fenixedu.domain.finalDegreeWork;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.beanutils.BeanComparator;

public class GroupProposal extends GroupProposal_Base {

    public static final Comparator<GroupProposal> COMPARATOR_BY_PREFERENCE_ORDER = new BeanComparator("orderOfPreference");

    public GroupProposal() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	deleteAttributions();
	removeFinalDegreeDegreeWorkGroup();
	removeFinalDegreeWorkProposal();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public void deleteAttributions() {
	final Proposal proposal = getFinalDegreeWorkProposal();
	if (proposal.getGroupAttributed() == getFinalDegreeDegreeWorkGroup()) {
	    proposal.removeGroupAttributed();
	}
	if (proposal.getGroupAttributedByTeacher() == getFinalDegreeDegreeWorkGroup()) {
	    proposal.removeGroupAttributedByTeacher();
	}
	final FinalDegreeWorkGroup finalDegreeWorkGroup = getFinalDegreeDegreeWorkGroup();
	for (final GroupStudent groupStudent : finalDegreeWorkGroup.getGroupStudentsSet()) {
	    if (groupStudent.getFinalDegreeWorkProposalConfirmation() == proposal) {
		groupStudent.removeFinalDegreeWorkProposalConfirmation();
	    }
	}
    }

}