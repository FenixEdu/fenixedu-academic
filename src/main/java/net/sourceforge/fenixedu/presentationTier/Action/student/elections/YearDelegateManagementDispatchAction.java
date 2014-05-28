/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.student.elections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.elections.AddCandidateYearDelegateElections;
import net.sourceforge.fenixedu.applicationTier.Servico.student.elections.RemoveCandidateYearDelegateElections;
import net.sourceforge.fenixedu.applicationTier.Servico.student.elections.VoteYearDelegateElections;
import net.sourceforge.fenixedu.dataTransferObject.student.elections.StudentVoteBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.elections.DelegateElection;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionPeriod;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionResultsByStudentDTO;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionVotingPeriod;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.student.StudentApplication.StudentParticipateApp;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = StudentParticipateApp.class, path = "year-delegate-management",
        titleKey = "link.student.yearDelegateElections")
@Mapping(module = "student", path = "/yearDelegateManagement")
@Forwards(@Forward(name = "showYearDelegateManagement", path = "/student/elections/yearDelegateManagement.jsp"))
public class YearDelegateManagementDispatchAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Person person = getLoggedPerson(request);
        final YearDelegateElection yearDelegateElection = getYearDelegateElectionForStudent(person.getStudent());

        final DelegateElectionPeriod currentPeriod =
                (yearDelegateElection != null ? yearDelegateElection.getCurrentElectionPeriod() : null);
        StudentVoteBean studentVoteBean = new StudentVoteBean();

        if (currentPeriod != null && currentPeriod.isCandidacyPeriod()) {
            request.setAttribute("currentYearDelegateElection", yearDelegateElection);

            YearDelegateElection personDelegateElection = getPersonYearDelegateElection(person.getStudent());
            if (personDelegateElection != null) {
                // j� � candidato: pode remover-se da lista de candidatos
                request.setAttribute("candidatedYearDelegate", Collections.singletonList(personDelegateElection));
            }

            final List<Student> candidates = new ArrayList<Student>(yearDelegateElection.getCandidatesSet());
            Collections.sort(candidates, Student.NAME_COMPARATOR);
            request.setAttribute("candidates", candidates);
        }

        if (currentPeriod != null && currentPeriod.isVotingPeriod()) {
            if (((DelegateElectionVotingPeriod) currentPeriod).getVotingStudentsSet().contains(person.getStudent())) {
                // aluno ja votou: pode apenas verificar em quem votou e alterar
                // voto?
                request.setAttribute("votedYearDelegate", yearDelegateElection);
            } else {
                // aluno ainda nao votou: pode votar
                final Collection<Student> candidatesList = yearDelegateElection.getCandidatesSet();

                List<StudentVoteBean> candidatesBeanList = new ArrayList<StudentVoteBean>();
                for (final Student student : candidatesList) {
                    candidatesBeanList.add(new StudentVoteBean(student));
                }

                final Collection<Student> otherStudentsList = yearDelegateElection.getNotCandidatedStudents();

                List<StudentVoteBean> otherStudentsBeanList = new ArrayList<StudentVoteBean>();
                for (final Student student : otherStudentsList) {
                    otherStudentsBeanList.add(new StudentVoteBean(student));
                }
                Collections.sort(candidatesBeanList, new BeanComparator("student.person.name"));
                Collections.sort(otherStudentsBeanList, new BeanComparator("student.person.name"));

                request.setAttribute("notVotedYearDelegate", yearDelegateElection);
                request.setAttribute("candidatesBeanList", candidatesBeanList);
                request.setAttribute("otherStudentsBeanList", studentVoteBean);

            }
        }

        if (yearDelegateElection != null && yearDelegateElection.hasLastVotingPeriod()
                && yearDelegateElection.getLastVotingPeriod().isPastPeriod()) {
            final List<DelegateElectionResultsByStudentDTO> electionResultsByStudentDTOList =
                    yearDelegateElection.getLastVotingPeriod().getDelegateElectionResults();

            request.setAttribute("yearDelegateResultsElection", electionResultsByStudentDTOList);
            request.setAttribute("yearDelegateElection", yearDelegateElection);
        }

        return mapping.findForward("showYearDelegateManagement");
    }

    private YearDelegateElection getPersonYearDelegateElection(Student student) {
        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        for (DelegateElection delegateElection : student.getElectionsWithStudentCandidaciesSet()) {
            if (delegateElection instanceof YearDelegateElection) {
                YearDelegateElection yearDelegateElection = (YearDelegateElection) delegateElection;
                if (yearDelegateElection.getExecutionYear() == executionYear) {
                    return yearDelegateElection;
                }
            }
        }
        return null;
    }

    public ActionForward addCandidateStudent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Person person = getLoggedPerson(request);
        final YearDelegateElection yearDelegateElection = getYearDelegateElectionForStudent(person.getStudent());

        try {
            AddCandidateYearDelegateElections.run(yearDelegateElection, person.getStudent());
        } catch (FenixServiceException ex) {
            addActionMessage(request, ex.getMessage(), ex.getArgs());
        }
        return prepare(mapping, actionForm, request, response);
    }

    public ActionForward removeCandidateStudent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Person person = getLoggedPerson(request);
        final YearDelegateElection yearDelegateElection = getPersonYearDelegateElection(person.getStudent());

        try {
            RemoveCandidateYearDelegateElections.run(yearDelegateElection, person.getStudent());
        } catch (FenixServiceException ex) {
            addActionMessage(request, ex.getMessage(), ex.getArgs());
        }
        return prepare(mapping, actionForm, request, response);
    }

    public ActionForward vote(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, Exception {

        final Person person = getLoggedPerson(request);
        final YearDelegateElection yearDelegateElection = getYearDelegateElectionForStudent(person.getStudent());
        final StudentVoteBean voteBean = (StudentVoteBean) RenderUtils.getViewState("prepareVote").getMetaObject().getObject();

        final Student votedStudent = voteBean.getStudent();
        try {
            VoteYearDelegateElections.run(yearDelegateElection, person.getStudent(), votedStudent);
        } catch (DomainException ex) {
            addActionMessage(request, ex.getMessage(), ex.getArgs());
        } catch (FenixServiceException ex) {
            addActionMessage(request, ex.getMessage(), ex.getArgs());
        }

        return prepare(mapping, actionForm, request, response);
    }

    public ActionForward prepareVote(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, Exception {

        final Person person = getLoggedPerson(request);
        final YearDelegateElection yearDelegateElection = getYearDelegateElectionForStudent(person.getStudent());
        StudentVoteBean voteBean = null;

        if (RenderUtils.getViewState("candidate") != null) {
            voteBean = (StudentVoteBean) RenderUtils.getViewState("candidate").getMetaObject().getObject();
        } else if (RenderUtils.getViewState("otherStudentsBeanList") != null) {
            voteBean = getRenderedObject("otherStudentsBeanList");

        }
        voteBean = getStudentVoteOrBlankVote(voteBean);
        request.setAttribute("studentVote", voteBean.getStudent());
        request.setAttribute("blankVote", isBlankVote(voteBean));
        request.setAttribute("votedStudentBean", voteBean);
        request.setAttribute("notVotedYearDelegate", yearDelegateElection);

        return mapping.findForward("showYearDelegateManagement");
    }

    /*
     * AUXIALIARY METHODS
     */

    private YearDelegateElection getYearDelegateElectionForStudent(Student student) {
        final List<Registration> registrations = student.getActiveRegistrations();
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        for (Registration registration : registrations) {
            if (registration != null) {
                YearDelegateElection yearDelegateElectionsGivenExecutionYear =
                        registration.getYearDelegateElectionsGivenExecutionYear(currentExecutionYear);
                if (yearDelegateElectionsGivenExecutionYear != null) {
                    return yearDelegateElectionsGivenExecutionYear;
                }
            }
        }
        return null;
    }

    private StudentVoteBean getStudentVoteOrBlankVote(StudentVoteBean voteBean) {
        if (isBlankVote(voteBean)) {
            return new StudentVoteBean();
        }
        return voteBean;
    }

    private boolean isBlankVote(StudentVoteBean voteBean) {
        return voteBean == null || voteBean.getStudent() == null;
    }

}