package net.sourceforge.fenixedu.dataTransferObject.student.elections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionPeriod;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.beanutils.BeanComparator;

public class StudentVoteBean implements Serializable {
	
	private DomainReference<Student> student;
	private boolean selectedStudent;
	
	

	public StudentVoteBean() {}
	
	public StudentVoteBean(Student student) {
		setStudent(student);
		setSelectedStudent(false);
	}
	
	
	
	public Student getStudent() {
		return (this.student == null) ? null : student.getObject();
		
	}

	public void setStudent(Student student) {
		this.student = (student != null) ? new DomainReference<Student>(student):null;
		
	}
	
	public boolean getSelectedStudent() {
		return selectedStudent;
	}

	public void setSelectedStudent(boolean selectedStudent) {
		this.selectedStudent = selectedStudent;
	}
	
	public List<Student> getSelectedStudentVote(String studentType) {
		final IUserView userView = AccessControl.getUserView();
    	final Student student = userView.getPerson().getStudent();
		final YearDelegateElection yearDelegateElection = getYearDelegateElectionForStudent(student);
		
		final DelegateElectionPeriod currentPeriod = (yearDelegateElection != null ? yearDelegateElection.getCurrentElectionPeriod() : null);
		List<Student> otherStudentsList = new ArrayList<Student>();
		
		if(currentPeriod != null && currentPeriod.isVotingPeriod()) {		
			if(studentType.equals("notCandidate")){
					otherStudentsList = yearDelegateElection.getNotCandidatedStudents();
			}
			if(studentType.equals("candidate")){
					otherStudentsList = yearDelegateElection.getCandidates();
			}
			List<Student> otherStudentsBeanList = new ArrayList<Student>();
			for(final Student studentList : otherStudentsList) {
				otherStudentsBeanList.add(studentList);
			}

				Collections.sort(otherStudentsBeanList, new BeanComparator("person.name"));
				return otherStudentsBeanList;
		}
		return new ArrayList<Student>();
	    }
	
	public  YearDelegateElection getYearDelegateElectionForStudent(Student student) {
		YearDelegateElection yearDelegateElection = null;
		
		final Registration registration = student.getLastActiveRegistration();
		
		final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
		if(registration != null){
			final int curricularYear = registration.getCurricularYear(currentExecutionYear);
			yearDelegateElection =  (YearDelegateElection)registration.getDegree().getYearDelegateElectionWithLastCandidacyPeriod(
					currentExecutionYear, CurricularYear.readByYear(curricularYear));
			return yearDelegateElection;
		}
		return null;
	}
	
	
	
}
