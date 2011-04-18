package net.sourceforge.fenixedu.domain.finalDegreeWork;

import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.collections.Predicate;

import pt.utl.ist.fenix.tools.util.CollectionUtils;

public class FinalDegreeWorkGroup extends FinalDegreeWorkGroup_Base {
    
    public static class IsValidGroupPredicate implements Predicate {

	public boolean evaluate(Object arg0) {
	    if (arg0 instanceof FinalDegreeWorkGroup) {
		return ((FinalDegreeWorkGroup)arg0).isValid();
	    }
	    return false;
	}
	
    }
    
    public static class AttributionStatusPredicate implements Predicate {

	final CandidacyAttributionType attributionType;

	public AttributionStatusPredicate(CandidacyAttributionType attributionType) {
	    super();
	    this.attributionType = attributionType;
	}

	public boolean evaluate(Object object) {
	    if (object instanceof FinalDegreeWorkGroup) {
		FinalDegreeWorkGroup group = (FinalDegreeWorkGroup) object;
		if (CandidacyAttributionType.TOTAL.equals(attributionType)
			|| group.getCandidacyAttributionStatus().equals(attributionType)) {
		    return true;
		}
	    }
	    return false;
	}

    }

    public final static Predicate WITHOUT_DISSERTATION_PREDICATE = new Predicate() {

	public boolean evaluate(Object object) {
	    if (object instanceof FinalDegreeWorkGroup) {
		FinalDegreeWorkGroup group = (FinalDegreeWorkGroup) object;
		if (group.isAttributed()) {
		    Registration registration = group.getGroupStudentsIterator().next().getRegistration();
		    ExecutionYear nextExecutionYear = group.getExecutionDegree().getExecutionYear().getNextExecutionYear();
		    return registration.getStudentCurricularPlan(nextExecutionYear).getDissertationEnrolments().isEmpty();
		}
		return false;
	    }
	    return false;
	}
    };
    public static final Comparator<FinalDegreeWorkGroup> COMPARATOR_BY_STUDENT_NUMBERS = new Comparator<FinalDegreeWorkGroup>() {
	public int compare(final FinalDegreeWorkGroup group1, final FinalDegreeWorkGroup group2) {
	    final GroupStudent groupStudent1 = Collections.min(group1.getGroupStudentsSet(),
		    GroupStudent.COMPARATOR_BY_STUDENT_NUMBER);
	    final GroupStudent groupStudent2 = Collections.min(group2.getGroupStudentsSet(),
		    GroupStudent.COMPARATOR_BY_STUDENT_NUMBER);
	    return groupStudent1.getRegistration().getNumber().compareTo(groupStudent2.getRegistration().getNumber());
	}
    };

    public FinalDegreeWorkGroup() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public SortedSet<net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal> getGroupProposalsSortedByPreferenceOrder() {
	return CollectionUtils.constructSortedSet(getGroupProposalsSet(),
		net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal.COMPARATOR_BY_PREFERENCE_ORDER);
    }

    public boolean isConfirmedByStudents(final Proposal proposal) {
	for (GroupStudent groupStudent : getGroupStudentsSet()) {
	    if (groupStudent.getFinalDegreeWorkProposalConfirmation() != proposal) {
		return false;
	    }
	}

	return true;
    }

    public void delete() {
	removeExecutionDegree();
	for (final GroupProposal groupProposal : getGroupProposalsSet()) {
	    groupProposal.delete();
	}
	for (final GroupStudent groupStudent : getGroupStudentsSet()) {
	    groupStudent.delete();
	}
	removeProposalAttributed();
	removeProposalAttributedByTeacher();
	removeRootDomainObject();
	deleteDomainObject();
    }

    public CandidacyAttributionType getCandidacyAttributionStatus() {
	if (hasProposalAttributed()) {
	    return CandidacyAttributionType.ATTRIBUTED_BY_CORDINATOR;
	}
	Proposal proposalAttributedByTeacher = getProposalAttributedByTeacher();
	if (proposalAttributedByTeacher != null) {
	    for (GroupStudent groupStudent : getGroupStudents()) {
		if (groupStudent.getFinalDegreeWorkProposalConfirmation() != proposalAttributedByTeacher) {
		    return CandidacyAttributionType.ATTRIBUTED_NOT_CONFIRMED;
		}
	    }
	    return CandidacyAttributionType.ATTRIBUTED;
	}
	return CandidacyAttributionType.NOT_ATTRIBUTED;
    }

    public boolean getAttributed() {
	return getCandidacyAttributionStatus().isFinalAttribution();
    }

    public boolean isAttributed() {
	return getAttributed();
    }

    public Proposal getAttributedProposal() {
	if (hasProposalAttributed()) {
	    return getProposalAttributed();
	}
	Proposal proposalAttributedByTeacher = getProposalAttributedByTeacher();
	if (proposalAttributedByTeacher != null) {
	    for (GroupStudent groupStudent : getGroupStudents()) {
		if (groupStudent.getFinalDegreeWorkProposalConfirmation() != proposalAttributedByTeacher) {
		    return null;
		}
	    }
	    return proposalAttributedByTeacher;
	}
	return null;
    }
    
    public boolean isValid() {
	return hasAnyGroupProposals() || hasProposalAttributed() || hasProposalAttributedByTeacher();
    }

}
