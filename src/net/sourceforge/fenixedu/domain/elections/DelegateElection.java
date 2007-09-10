package net.sourceforge.fenixedu.domain.elections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Student;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.YearMonthDay;

public abstract class DelegateElection extends DelegateElection_Base {
	
	public static final Comparator<DelegateElection> ELECTION_COMPARATOR_BY_CANDIDACY_START_DATE = new BeanComparator("candidacyStartDate");
	
	public static final Comparator<DelegateElection> ELECTION_COMPARATOR_BY_VOTING_START_DATE = new BeanComparator("votingStartDate");
	
	public static final Comparator<DelegateElection> ELECTION_COMPARATOR_BY_VOTING_START_DATE_AND_CANDIDACY_START_DATE = new Comparator<DelegateElection>() {
		public int compare(DelegateElection e1, DelegateElection e2) {
			if (e1.getVotingStartDate() == null && e2.getVotingStartDate() != null) {
				return -1;
			}
			else if(e1.getVotingStartDate() != null && e2.getVotingStartDate() == null) {
				return 1;
			}
			else if(e1.getVotingStartDate() == null && e2.getVotingStartDate() == null) {
				return (e1.getCandidacyStartDate().isBefore(e2.getCandidacyStartDate()) ? -1 : 1);
			}
			else {
				return (e1.getVotingStartDate().isBefore(e2.getVotingStartDate()) ? -1 : 1); 
			}
		}
	};
	
	protected DelegateElection(){
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}
	
	public YearMonthDay getCandidacyStartDate(){
		if(hasCandidacyPeriod())
			return getCandidacyPeriod().getStartDate();
		else
			return null;
	}
	
	public YearMonthDay getCandidacyEndDate(){
		if(hasCandidacyPeriod())
			return getCandidacyPeriod().getEndDate();
		else
			return null;
	}
	
	public YearMonthDay getVotingStartDate(){
		if(hasVotingPeriod())
			return getVotingPeriod().getStartDate();
		else
			return null;
	}
	
	public YearMonthDay getVotingEndDate(){
		if(hasVotingPeriod())
			return getVotingPeriod().getEndDate();
		else
			return null;
	}
    
    /**
     * This method is responsible for deleting the object and all its
     * references
     */
    public void delete() {
    	if(hasCandidacyPeriod())
    		getCandidacyPeriod().delete();
    	
    	if(hasVotingPeriod())
    		deleteVotingPeriod();
    	
    	super.getStudents().clear();
    	super.getCandidates().clear();
    	
    	removeElectedStudent();
    	removeDegree();
    	removeExecutionYear();

    	super.removeRootDomainObject();
    	super.deleteDomainObject();
    }
    
    protected void deleteVotingPeriod() {
    	getVotingPeriod().delete();
    	
		for(; hasAnyVotes(); getVotes().get(0).delete())
    		;
		
		if(hasElectedStudent())
			removeElectedStudent();
		
		super.getVotingStudents().clear();
    }
    
    public DelegateElectionPeriod getLastElectionPeriod() {
    	if(hasVotingPeriod())
    		return getVotingPeriod();
    	else
    		return getCandidacyPeriod();
    }
    
    public DelegateElectionPeriod getCurrentElectionPeriod() {
    	if(hasVotingPeriod() && getVotingPeriod().isCurrentPeriod())
    		return getVotingPeriod();
    	else if (getCandidacyPeriod().isCurrentPeriod())
    		return getCandidacyPeriod();
    	return null;
    }
    
    public boolean hasCandidacyPeriodIntersecting(YearMonthDay startDate, YearMonthDay endDate){
    	if(!(startDate.isAfter(getCandidacyEndDate()) || startDate.isEqual(getCandidacyEndDate()) ||
    			endDate.isBefore(getCandidacyStartDate()) || endDate.isEqual(getCandidacyStartDate())))
    		return true;
    	return false;
    }
    
    public boolean hasVotingPeriodIntersecting(YearMonthDay startDate, YearMonthDay endDate){
    	if(!(startDate.isAfter(getVotingEndDate()) || startDate.isEqual(getVotingEndDate()) ||
    			endDate.isBefore(getVotingStartDate()) || endDate.isEqual(getVotingStartDate())))
    		return true;
    	return false;
    }
    
    public boolean hasVotedStudent(Student student){
    	for(DelegateElectionVote vote : getVotes()) {
    		if(vote.getStudent().equals(student)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public List<Student> getNotCandidatedStudents() {
    	List<Student> result = new ArrayList<Student>(super.getStudents());
    	result.removeAll(getCandidates());
    	return result;
    }
    
    public List<DelegateElectionResultsByStudentDTO> getDelegateElectionResults() {
    	Map<Student,DelegateElectionResultsByStudentDTO> votingResultsMap = new HashMap<Student, DelegateElectionResultsByStudentDTO>();
    	
		Student student = null;
		int totalVoteCount = 0;
		int studentVotesCount = 0;
		int totalStudentsCount = getCandidatesCount() + getStudentsCount();
		
		for(DelegateElectionVote vote : getVotes()) {
			totalVoteCount++;
			student = vote.getStudent();
			if(votingResultsMap.containsKey(student)) {
				DelegateElectionResultsByStudentDTO votingResults = votingResultsMap.get(student);
				studentVotesCount = votingResults.getVotesNumber() + 1;
				votingResults.setVotesNumber(studentVotesCount);
			} else {
				votingResultsMap.put(student, new DelegateElectionResultsByStudentDTO(this, student));
			}
		}
		
		List<DelegateElectionResultsByStudentDTO> votingResultsBeanList = new ArrayList<DelegateElectionResultsByStudentDTO>(votingResultsMap.values());
		for(DelegateElectionResultsByStudentDTO results : votingResultsBeanList) {
			results.calculateResults(totalStudentsCount, totalVoteCount);
		}
		
    	return votingResultsBeanList;
    }
    
    
    /*
     * DOMAIN SERVICES
     */
    public abstract void createVotingPeriod(YearMonthDay startDate, YearMonthDay endDate);
    
    public abstract void editCandidacyPeriod(YearMonthDay startDate, YearMonthDay endDate);
    
    public abstract void editVotingPeriod(YearMonthDay startDate, YearMonthDay endDate);
    
    public abstract void deleteCandidacyPeriod();
    
    public abstract void deleteVotingPeriod(boolean removeElection);
}
