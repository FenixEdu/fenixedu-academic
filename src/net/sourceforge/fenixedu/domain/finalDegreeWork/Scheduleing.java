package net.sourceforge.fenixedu.domain.finalDegreeWork;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus;

import org.joda.time.DateTime;

public class Scheduleing extends Scheduleing_Base {

    public Scheduleing() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Date getEndOfProposalPeriod() {
	if (this.getEndOfProposalPeriodDate() != null && this.getEndOfProposalPeriodTime() != null) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(this.getEndOfProposalPeriodDate().getTime());
	    Calendar calendarTime = Calendar.getInstance();
	    calendarTime.setTimeInMillis(this.getEndOfProposalPeriodTime().getTime());
	    calendar.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
	    calendar.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
	    calendar.set(Calendar.SECOND, calendarTime.get(Calendar.SECOND));
	    return calendar.getTime();
	}
	return null;
    }

    public void setEndOfProposalPeriod(Date endOfProposalPeriod) {
	this.setEndOfProposalPeriodDate(endOfProposalPeriod);
	this.setEndOfProposalPeriodTime(endOfProposalPeriod);
    }

    public Date getStartOfProposalPeriod() {
	if (this.getStartOfProposalPeriodDate() != null && this.getStartOfProposalPeriodTime() != null) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(this.getStartOfProposalPeriodDate().getTime());
	    Calendar calendarTime = Calendar.getInstance();
	    calendarTime.setTimeInMillis(this.getStartOfProposalPeriodTime().getTime());
	    calendar.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
	    calendar.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
	    calendar.set(Calendar.SECOND, calendarTime.get(Calendar.SECOND));
	    return calendar.getTime();
	}
	return null;
    }

    public void setStartOfProposalPeriod(Date startOfProposalPeriod) {
	this.setStartOfProposalPeriodDate(startOfProposalPeriod);
	this.setStartOfProposalPeriodTime(startOfProposalPeriod);
    }

    public Date getStartOfCandidacyPeriod() {
	if (this.getStartOfCandidacyPeriodDate() != null && this.getStartOfCandidacyPeriodTime() != null) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(this.getStartOfCandidacyPeriodDate().getTime());
	    Calendar calendarTime = Calendar.getInstance();
	    calendarTime.setTimeInMillis(this.getStartOfCandidacyPeriodTime().getTime());
	    calendar.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
	    calendar.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
	    calendar.set(Calendar.SECOND, calendarTime.get(Calendar.SECOND));
	    return calendar.getTime();
	}
	return null;
    }

    public void setStartOfCandidacyPeriod(Date startOfCandidacyPeriod) {
	this.setStartOfCandidacyPeriodDate(startOfCandidacyPeriod);
	this.setStartOfCandidacyPeriodTime(startOfCandidacyPeriod);
    }

    public Date getEndOfCandidacyPeriod() {
	if (this.getEndOfCandidacyPeriodDate() != null && this.getEndOfCandidacyPeriodTime() != null) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(this.getEndOfCandidacyPeriodDate().getTime());
	    Calendar calendarTime = Calendar.getInstance();
	    calendarTime.setTimeInMillis(this.getEndOfCandidacyPeriodTime().getTime());
	    calendar.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
	    calendar.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
	    calendar.set(Calendar.SECOND, calendarTime.get(Calendar.SECOND));
	    return calendar.getTime();
	}
	return null;
    }

    public void setEndOfCandidacyPeriod(Date endOfCandidacyPeriod) {
	this.setEndOfCandidacyPeriodDate(endOfCandidacyPeriod);
	this.setEndOfCandidacyPeriodTime(endOfCandidacyPeriod);
    }

    public Collection<ExecutionDegree> getExecutionDegreesSortedByDegreeName() {
	final List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>(getExecutionDegrees());
	Collections.sort(executionDegrees, ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);
	return executionDegrees;
    }

    public Set<Proposal> findProposalsByStatus(final FinalDegreeWorkProposalStatus finalDegreeWorkProposalStatus) {
	final Set<Proposal> proposals = new HashSet<Proposal>();
	for (final Proposal proposal : getProposalsSet()) {
	    if (finalDegreeWorkProposalStatus.equals(proposal.getStatus())) {
		proposals.add(proposal);
	    }
	}
	return proposals;
    }

    public Set<Proposal> findPublishedProposals() {
	return findProposalsByStatus(FinalDegreeWorkProposalStatus.PUBLISHED_STATUS);
    }

    public Set<Proposal> findApprovedProposals() {
	return findProposalsByStatus(FinalDegreeWorkProposalStatus.APPROVED_STATUS);
    }

    public SortedSet<FinalDegreeWorkGroup> getGroupsSortedByStudentNumbers() {
	final SortedSet<FinalDegreeWorkGroup> groups = new TreeSet<FinalDegreeWorkGroup>(
		FinalDegreeWorkGroup.COMPARATOR_BY_STUDENT_NUMBERS);
	for (final ExecutionDegree executionDegree : getExecutionDegreesSet()) {
	    groups.addAll(executionDegree.getAssociatedFinalDegreeWorkGroupsSet());
	}
	return groups;
    }

    public SortedSet<FinalDegreeWorkGroup> getGroupsWithProposalsSortedByStudentNumbers() {
	final SortedSet<FinalDegreeWorkGroup> groups = new TreeSet<FinalDegreeWorkGroup>(
		FinalDegreeWorkGroup.COMPARATOR_BY_STUDENT_NUMBERS);
	for (final ExecutionDegree executionDegree : getExecutionDegreesSet()) {
	    for (final FinalDegreeWorkGroup group : executionDegree.getAssociatedFinalDegreeWorkGroupsSet()) {
		if (!group.getGroupProposalsSet().isEmpty()) {
		    groups.add(group);
		}
	    }
	}
	return groups;
    }

    public boolean isInsideProposalSubmissionPeriod() {
	final DateTime start = new DateTime(getStartOfProposalPeriod());
	final DateTime end = new DateTime(getEndOfProposalPeriod());
	return !start.isAfterNow() && !end.isBeforeNow();
    }

    public void delete() {
	removeRootDomainObject();
	deleteDomainObject();
    }

}
