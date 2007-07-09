package net.sourceforge.fenixedu.domain.finalDegreeWork;

import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.utl.ist.fenix.tools.util.CollectionUtils;

public class FinalDegreeWorkGroup extends FinalDegreeWorkGroup_Base {

	public static final Comparator<FinalDegreeWorkGroup> COMPARATOR_BY_STUDENT_NUMBERS = new Comparator<FinalDegreeWorkGroup>() {
		public int compare(final FinalDegreeWorkGroup group1, final FinalDegreeWorkGroup group2) {
			final GroupStudent groupStudent1 = Collections.min(group1.getGroupStudentsSet(), GroupStudent.COMPARATOR_BY_STUDENT_NUMBER);
			final GroupStudent groupStudent2 = Collections.min(group2.getGroupStudentsSet(), GroupStudent.COMPARATOR_BY_STUDENT_NUMBER);
			return groupStudent1.getRegistration().getNumber().compareTo(groupStudent2.getRegistration().getNumber());
		}
	};

	public FinalDegreeWorkGroup() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public SortedSet<net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal> getGroupProposalsSortedByPreferenceOrder() {
		return CollectionUtils.constructSortedSet(getGroupProposalsSet(), net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal.COMPARATOR_BY_PREFERENCE_ORDER);
	}

	public boolean isConfirmedByStudents(final Proposal proposal) {
		for (GroupStudent groupStudent : getGroupStudentsSet()) {
			if (groupStudent.getFinalDegreeWorkProposalConfirmation() != proposal) {
				return false;
			}
		}

		return true;
	}

}
