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
package org.fenixedu.academic.ui.struts.action.student.elections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.student.elections.AddCandidateYearDelegateElections;
import org.fenixedu.academic.service.services.student.elections.RemoveCandidateYearDelegateElections;
import org.fenixedu.academic.service.services.student.elections.VoteYearDelegateElections;
import org.fenixedu.academic.dto.student.elections.StudentVoteBean;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.elections.DelegateElection;
import org.fenixedu.academic.domain.elections.DelegateElectionPeriod;
import org.fenixedu.academic.domain.elections.DelegateElectionResultsByStudentDTO;
import org.fenixedu.academic.domain.elections.DelegateElectionVotingPeriod;
import org.fenixedu.academic.domain.elections.YearDelegateElection;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.student.StudentApplication.StudentParticipateApp;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

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
                for (DelegateElection delegateElection : student.getDelegateElectionsSet()) {
                    if (delegateElection instanceof YearDelegateElection) {
                        if (delegateElection.getExecutionYear().equals(currentExecutionYear)
                                && delegateElection.getDegree().equals(registration.getDegree())) {
                            return (YearDelegateElection) delegateElection;
                        }
                    }
                }
                return null;
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