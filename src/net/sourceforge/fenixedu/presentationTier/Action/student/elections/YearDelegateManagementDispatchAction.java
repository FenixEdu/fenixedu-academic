package net.sourceforge.fenixedu.presentationTier.Action.student.elections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.elections.ElectionPeriodBean;
import net.sourceforge.fenixedu.dataTransferObject.student.elections.StudentVoteBean;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionPeriod;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionResultsByStudentDTO;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentCurriculum;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class YearDelegateManagementDispatchAction extends FenixDispatchAction {
	
	public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		final Person person = getLoggedPerson(request);
		YearDelegateElection yearDelegateElection = getYearDelegateElectionForStudent(person.getStudent());
		
		DelegateElectionPeriod currentPeriod = (yearDelegateElection != null ? yearDelegateElection.getCurrentElectionPeriod() : null);
		
		if(currentPeriod != null && currentPeriod.isCandidacyPeriod()) {
			if(yearDelegateElection.getCandidates().contains(person.getStudent())) {
				//já é candidato: pode remover-se da lista de candidatos
				request.setAttribute("candidatedYearDelegate", yearDelegateElection);
			} else {
				request.setAttribute("notCandidatedYearDelegate", yearDelegateElection);
				//não é candidato: pode adicionar-se à lista de candidatos
			}
			
			List<Student> candidates = new ArrayList<Student>(yearDelegateElection.getCandidates());
			Collections.sort(candidates, new BeanComparator("person.name"));
			request.setAttribute("candidates", candidates);
		}
		
		if(currentPeriod != null && currentPeriod.isVotingPeriod()) {
			if(yearDelegateElection.getVotingStudents().contains(person.getStudent())) {
				// aluno ja votou: pode apenas verificar em quem votou e alterar voto?
				request.setAttribute("votedYearDelegate", yearDelegateElection);
			} else {
				//aluno ainda nao votou: pode votar
				List<Student> candidatesList = yearDelegateElection.getCandidates();
				List<StudentVoteBean> candidatesBeanList = new ArrayList<StudentVoteBean>();
				for(Student student : candidatesList) {
					candidatesBeanList.add(new StudentVoteBean(student));
				}
				
				List<Student> otherStudentsList = yearDelegateElection.getNotCandidatedStudents();
				List<StudentVoteBean> otherStudentsBeanList = new ArrayList<StudentVoteBean>();
				for(Student student : otherStudentsList) {
					otherStudentsBeanList.add(new StudentVoteBean(student));
				}
				
				Collections.sort(candidatesBeanList, new BeanComparator("student.person.name"));
				Collections.sort(otherStudentsBeanList, new BeanComparator("student.person.name"));
				
				request.setAttribute("notVotedYearDelegate", yearDelegateElection);
				request.setAttribute("candidatesBeanList", candidatesBeanList);
				request.setAttribute("otherStudentsBeanList", otherStudentsBeanList);
			}
		}
		
		if(yearDelegateElection != null && yearDelegateElection.hasVotingPeriod() && yearDelegateElection.getVotingPeriod().isPastPeriod()) {
			List<DelegateElectionResultsByStudentDTO> electionResultsByStudentDTOList = yearDelegateElection.getDelegateElectionResults();

			request.setAttribute("yearDelegateResultsElection", electionResultsByStudentDTOList);
			request.setAttribute("yearDelegateElection", yearDelegateElection);
		}
		
		
			

		return mapping.findForward("showYearDelegateManagement");
	}
	
	public ActionForward addCandidateStudent(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		final Person person = getLoggedPerson(request);
		YearDelegateElection yearDelegateElection = getYearDelegateElectionForStudent(person.getStudent());

		Object[] args = null;
		args = new Object[] { yearDelegateElection,  person.getStudent()};

		try {
			executeService(request, "AddCandidateYearDelegateElections", args);
		} catch (FenixServiceException ex) {
			addActionMessage(request, ex.getMessage(), ex.getArgs());
		}
		return prepare(mapping, actionForm, request, response);
	}
	
	public ActionForward removeCandidateStudent(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		final Person person = getLoggedPerson(request);
		YearDelegateElection yearDelegateElection = getYearDelegateElectionForStudent(person.getStudent());
		
		Object[] args = null;
		args = new Object[] { yearDelegateElection,  person.getStudent()};

		try {
			executeService(request, "RemoveCandidateYearDelegateElections", args);
		} catch (FenixServiceException ex) {
			addActionMessage(request, ex.getMessage(), ex.getArgs());
		}
		return prepare(mapping, actionForm, request, response);
	}
	
	public ActionForward vote(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		final Person person = getLoggedPerson(request);
		YearDelegateElection yearDelegateElection = getYearDelegateElectionForStudent(person.getStudent());
		
		Student votedStudent = null;
		if(RenderUtils.getViewState("candidate") != null) {
			StudentVoteBean voteBean = (StudentVoteBean)RenderUtils.getViewState("candidate").getMetaObject().getObject();
			votedStudent = voteBean.getStudent();
		} else {
			votedStudent = getVotedStudent("otherStudentsBeanList");
		} 
		
		try {
			executeService(request, "VoteYearDelegateElections", new Object[] { yearDelegateElection, person.getStudent(),  votedStudent});
		} catch (FenixServiceException ex) {
			addActionMessage(request, ex.getMessage(), ex.getArgs());
		}
		
		return prepare(mapping, actionForm, request, response);
	}
	
	/*
	 * AUXIALIARY METHODS
	 */
	
	private Student getVotedStudent(String beanListId){
		if(RenderUtils.getViewState(beanListId) != null) {
			List<StudentVoteBean> studentVoteBeanList = (List<StudentVoteBean>)RenderUtils.getViewState(beanListId).getMetaObject().getObject();
			
			for(StudentVoteBean studentVoteBean : studentVoteBeanList) {
				if(studentVoteBean.getSelectedStudent()) {
					return studentVoteBean.getStudent();
				}
			}
		}
		return null;
	}
	
	private YearDelegateElection getYearDelegateElectionForStudent(Student student) {
		YearDelegateElection yearDelegateElection = null;
		
		Registration registration = student.getLastActiveRegistration();
		
		final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
		final StudentCurriculum studentCurriculum = new StudentCurriculum(registration);
		final int curricularYear = studentCurriculum.calculateCurricularYear(currentExecutionYear);
		yearDelegateElection =  (YearDelegateElection)registration.getDegree().getYearDelegateElectionWithLastCandidacyPeriod(
				currentExecutionYear, CurricularYear.readByYear(curricularYear));
		
		return yearDelegateElection;
	}

}
