package net.sourceforge.fenixedu.domain.elections;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.StudentCurriculum;

public class YearDelegateElection extends YearDelegateElection_Base {
    
	/*
	 * When created, must have the following attributes specified
	 */
	public  YearDelegateElection(ExecutionYear executionYear, Degree degree, CurricularYear curricularYear, 
			DelegateElectionCandidacyPeriod candidacyPeriod) {
		super();
        
        setExecutionYear(executionYear);
        setDegree(degree);
        setCurricularYear(curricularYear);
        
        /*
		 * Must be invoked after setExecutionYear
		 */
        setCandidacyPeriod(candidacyPeriod);
    }
	
	@Override
	public void delete() {
		removeCurricularYear();
		super.delete();
	}
	
	
	@Override
    public void setCandidacyPeriod(DelegateElectionCandidacyPeriod candidacyPeriod) {
		if(candidacyPeriod != null) {
			validatePeriodGivenExecutionYear(getExecutionYear(), candidacyPeriod);
			
			if(hasVotingPeriod() && !candidacyPeriod.endsBefore(getVotingPeriod())){
	    		throw new DomainException("error.elections.edit.colidesWithVotingPeriod", 
	    				new String[] { getDegree().getSigla(), getCurricularYear().getYear().toString(),
	    					candidacyPeriod.getPeriod(), getVotingPeriod().getPeriod() });
	    	}
		}
		
		super.setCandidacyPeriod(candidacyPeriod);
		
	}
	
	@Override
    public void setVotingPeriod(DelegateElectionVotingPeriod votingPeriod) {
		if(votingPeriod != null) {
			validatePeriodGivenExecutionYear(getExecutionYear(), votingPeriod);
			
			if(!hasCandidacyPeriod()) {
				throw new DomainException("error.elections.createVotingPeriod.mustCreateCandidacyPeriod", 
						new String[] { getDegree().getSigla(), getCurricularYear().getYear().toString()});
			}
			
			if(!getCandidacyPeriod().endsBefore(votingPeriod)){
	    		throw new DomainException("error.elections.edit.colidesWithVotingPeriod", new String[] { getDegree().getSigla(), 
	    				getCurricularYear().getYear().toString(), getCandidacyPeriod().getPeriod(), votingPeriod.getPeriod() });
	    	}
		}
		
		super.setVotingPeriod(votingPeriod);
	}
	
    /*
     * Checks if given period belongs to given execution year
     */
    private void validatePeriodGivenExecutionYear(ExecutionYear executionYear, DelegateElectionPeriod period){
    	if(period.getStartDate().isBefore(executionYear.getBeginDateYearMonthDay()) || 
    			period.getEndDate().isAfter(executionYear.getEndDateYearMonthDay())) {	
    		throw new DomainException("error.elections.setPeriod.invalidPeriod", new String[]{getDegree().getSigla(),
    				getCurricularYear().getYear().toString(), period.getPeriod(), executionYear.getYear()});
    	}
    }
	
	public boolean getCanYearDelegateBeElected() {
    	return (getVotingPeriod().isCurrentPeriod() || hasElectedStudent() ? false : true);
    }
	
	/*
	 * Checks if the new election candidacy period colides with another election
	 * candidacy period, previously added to this degree and curricular year
	 */
    private static void checkNewElectionCandidacyPeriod (Degree degree, ExecutionYear executionYear, CurricularYear curricularYear, 
    		DelegateElectionCandidacyPeriod candidacyPeriod) throws DomainException{
    	
    	for(DelegateElection election : degree.getYearDelegateElectionsGivenExecutionYearAndCurricularYear(executionYear, curricularYear)) {
			if(!election.getCandidacyPeriod().endsBefore(candidacyPeriod)) {
				throw new DomainException("error.elections.newElection.invalidPeriod", new String[] { degree.getSigla(), 
						curricularYear.getYear().toString(), candidacyPeriod.getPeriod(), election.getCandidacyPeriod().getPeriod()});
			}
		}
	}
    
    /*
     * If there is a voting period ocurring, it's not possible to add a new election
     */
    private static void checkPreviousDelegateElectionExistence(Degree degree, CurricularYear curricularYear, ExecutionYear executionYear)
    		throws DomainException{
    	
    	final DelegateElection previousElection = degree.getYearDelegateElectionWithLastCandidacyPeriod(executionYear, curricularYear);
		if(previousElection != null && previousElection.hasVotingPeriod() && previousElection.getVotingPeriod().isCurrentPeriod()) {
	    		throw new DomainException("error.elections.newElection.currentVotingPeriodExists", new String[] { 
	    				degree.getSigla(), curricularYear.getYear().toString(), previousElection.getVotingPeriod().getPeriod()});
		}
			
		if(previousElection != null && !previousElection.getVotingPeriod().isPastPeriod()) { //future voting period (must be deleted)
			previousElection.getVotingPeriod().delete();
		}
    }
    
    /*
     * DOMAIN SERVICES
     */
    public static YearDelegateElection createDelegateElectionWithCandidacyPeriod(Degree degree, ExecutionYear executionYear, 
    		YearMonthDay candidacyPeriodStartDate, YearMonthDay candidacyPeriodEndDate, CurricularYear curricularYear) {
    	
    	DelegateElectionCandidacyPeriod period = new DelegateElectionCandidacyPeriod(candidacyPeriodStartDate, candidacyPeriodEndDate);
		checkNewElectionCandidacyPeriod(degree, executionYear, curricularYear, period);
		
		/* Checks if there is a previous delegate election and its state */
		checkPreviousDelegateElectionExistence(degree, curricularYear, executionYear);
		
		YearDelegateElection election = new YearDelegateElection(executionYear, degree, curricularYear, period);
		
		/* Add degree students to election students list */
		for (DegreeCurricularPlan dcp : degree.getActiveDegreeCurricularPlans()) {
			for (StudentCurricularPlan scp : dcp.getActiveStudentCurricularPlans()) {
				final StudentCurriculum studentCurriculum = new StudentCurriculum(scp.getRegistration());
				if(studentCurriculum.calculateCurricularYear(executionYear) == curricularYear.getYear()) {
					election.addStudents(scp.getRegistration().getStudent());
				}
			}
		}
		
		return election;
    }
    
    @Override
    public void createVotingPeriod(YearMonthDay startDate, YearMonthDay endDate) {
    	if(hasVotingPeriod()) {
			if (getVotingPeriod().isPastPeriod()) {
				throw new DomainException ("error.elections.createVotingPeriod.mustCreateNewCandidacyPeriod", 
						new String[] { getDegree().getSigla(), getCurricularYear().getYear().toString()});
			}
			if (getVotingPeriod().isCurrentPeriod()) {
				throw new DomainException ("error.elections.createVotingPeriod.onlyCanExtendPeriod", 
						new String[] { getDegree().getSigla(), getCurricularYear().getYear().toString()});
			}
		}

		DelegateElectionVotingPeriod period = new DelegateElectionVotingPeriod(startDate, endDate);
		setVotingPeriod(period);
    }
    
    @Override
    public void editCandidacyPeriod(YearMonthDay startDate, YearMonthDay endDate) {
    	DelegateElectionCandidacyPeriod period = getCandidacyPeriod();
		
		if (!period.isPastPeriod()) {
			try {
				period.delete();
				setCandidacyPeriod(new DelegateElectionCandidacyPeriod(startDate, endDate));
			} catch (DomainException ex) {
				throw new DomainException(ex.getMessage(), ex.getArgs());
			}
		}
		else {
			throw new DomainException ("error.yearDelegateElections.edit.pastPeriod", new String[] { getDegree().getSigla(),
				getCurricularYear().getYear().toString(), getCandidacyPeriod().getPeriod() });
		}
    }
    
    @Override
    public void editVotingPeriod(YearMonthDay startDate, YearMonthDay endDate) {
		if(!endDate.isAfter(getVotingEndDate()))
			throw new DomainException("error.elections.edit.newEndDateMustBeGreater", getDegree().getSigla(),
					getCurricularYear().getYear().toString());
		
		if (!getVotingPeriod().isPastPeriod()) {
			getVotingPeriod().setEndDate(endDate);	
		}
		else {
			throw new DomainException ("error.yearDelegateElections.edit.pastPeriod", new String[] { getDegree().getSigla(),
					getCurricularYear().getYear().toString(), getVotingPeriod().getPeriod() });
		}
    }
    
    @Override
    public void deleteCandidacyPeriod() {
		if (!getCandidacyPeriod().isPastPeriod()) {
			this.delete();
		}
		else {
			throw new DomainException ("error.yearDelegateElections.delete.pastPeriod", new String[] { getDegree().getSigla(),
					getCurricularYear().getYear().toString(), getCandidacyPeriod().getPeriod() });
		}
    }
    
    @Override
    public void deleteVotingPeriod(boolean removeElection) {
		if (hasVotingPeriod()){
			if(!getVotingPeriod().isPastPeriod()) {
				super.deleteVotingPeriod();
				if(removeElection) {
					this.deleteCandidacyPeriod();
				}
			}
			else {
				throw new DomainException ("error.yearDelegateElections.delete.pastPeriod", new String[] { getDegree().getSigla(),
						getCurricularYear().getYear().toString(), getVotingPeriod().getPeriod() });
			}
		}
    }
}
