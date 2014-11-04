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
package org.fenixedu.academic.ui.struts.action.pedagogicalCouncil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.dto.coordinator.tutor.TutorshipManagementBean;
import org.fenixedu.academic.dto.coordinator.tutor.TutorshipManagementByEntryYearBean;
import org.fenixedu.academic.dto.teacher.tutor.TutorBean;
import org.fenixedu.academic.domain.Coordinator;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.Tutorship;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

public class TutorManagementDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Person person = getLoggedPerson(request);
        final String forwardTo = request.getParameter("forwardTo");

        TutorshipManagementBean bean = null;
        if (RenderUtils.getViewState("choosetutorshipManagementBean") != null) {
            bean =
                    (TutorshipManagementBean) RenderUtils.getViewState("choosetutorshipManagementBean").getMetaObject()
                            .getObject();
        } else {
            bean = getTutorshipBeanWithRequestParameters(request);
        }

        final ExecutionDegree executionDegree = (ExecutionDegree) FenixFramework.getDomainObject(bean.getExecutionDegreeID());

        if (!validateDegreeTypeAccessRestrictions(executionDegree)) {
            addActionMessage(request, "error.tutor.notAuthorized.notBolonhaOrLEEC");
            return mapping.findForward("notAuthorized");
        }

        if (!validateCoordinationAccessRestrictions(person, executionDegree)) {
            addActionMessage(request, "error.tutor.notAuthorized.notCoordinatorOfDegree");
            return mapping.findForward("notAuthorized");
        }

        if (request.getAttribute("chooseFromList") != null) {
            request.setAttribute("chooseFromList", "true");
        }

        request.setAttribute("tutorshipManagementBean", bean);
        return mapping.findForward(forwardTo);
    }

    public ActionForward prepareChooseTutor(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        TutorshipManagementBean bean = (TutorshipManagementBean) request.getAttribute("tutorshipManagementBean");

        if (request.getParameter("chooseFromList") != null) {
            ExecutionDegree executiondegree = FenixFramework.getDomainObject(bean.getExecutionDegreeID());
            List<TutorBean> possibleTutorsForExecutionDegree = getAllPossibleTutors(bean, executiondegree);

            request.setAttribute("tutors", possibleTutorsForExecutionDegree);
        }

        request.setAttribute("tutorshipManagementBean", bean);
        return mapping.findForward("chooseTutor");
    }

    public ActionForward readTutor(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        TutorshipManagementBean bean = (TutorshipManagementBean) request.getAttribute("tutorshipManagementBean");

        final ExecutionDegree executiondegree = FenixFramework.getDomainObject(bean.getExecutionDegreeID());
        final Teacher teacher = User.findByUsername(bean.getTeacherId()).getPerson().getTeacher();

        if (teacher == null) {
            addActionMessage(request, "error.tutor.unExistTeacher", new String[] { bean.getTeacherId() });
            return mapping.findForward("chooseTutor");
        }

        List<Teacher> possibleTutorsForExecutionDegree =
                Tutorship.getPossibleTutorsFromExecutionDegreeDepartments(executiondegree);

        if (!possibleTutorsForExecutionDegree.contains(teacher)) {
            addActionMessage(request, "error.tutor.cannotBeTutorOfExecutionDegree");
            return mapping.findForward("chooseTutor");
        }

        bean.setTeacher(teacher);

        if (!Tutorship.getActiveTutorships(teacher).isEmpty()) {
            List<TutorshipManagementByEntryYearBean> beans =
                    getTutorshipManagementBeansByEntryYear(teacher, Tutorship.getActiveTutorships(teacher));
            request.setAttribute("tutorshipManagementBeansByEntryYear", beans);
        }

        request.setAttribute("tutorshipManagementBean", bean);
        return mapping.findForward("showStudentsByTutor");
    }

    public ActionForward prepareChooseTutorHistory(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final TutorshipManagementBean bean = (TutorshipManagementBean) request.getAttribute("tutorshipManagementBean");
        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(bean.getExecutionDegreeID());

        if (request.getParameter("filtered") != null) {
            request.setAttribute("teachers", getTutorsWithTutorshipHistory(bean, executionDegree));
        } else {
            request.setAttribute("teachers", getAllPossibleTutors(bean, executionDegree));
        }

        request.setAttribute("tutorshipManagementBean", bean);
        return mapping.findForward("chooseTutorHistory");
    }

    public ActionForward viewTutorshipHistory(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String forwardTo = request.getParameter("forwardTo");

        TutorshipManagementBean bean = getTutorshipBeanWithRequestParameters(request);

        final Teacher teacher = User.findByUsername(bean.getTeacherId()).getPerson().getTeacher();
        bean.setTeacher(teacher);
        bean.setNumberOfCurrentTutorships(Tutorship.getActiveTutorships(teacher).size());
        bean.setNumberOfPastTutorships(Tutorship.getPastTutorships(teacher).size());

        List<Tutorship> activeTutorships = Tutorship.getActiveTutorships(teacher);
        Collections.sort(activeTutorships, Tutorship.TUTORSHIP_END_DATE_COMPARATOR);

        List<Tutorship> pastTutorships = Tutorship.getPastTutorships(teacher);
        Collections.sort(pastTutorships, Tutorship.TUTORSHIP_START_DATE_COMPARATOR);

        request.setAttribute("forwardTo", forwardTo);
        request.setAttribute("activeTutorships", activeTutorships);
        request.setAttribute("pastTutorships", pastTutorships);
        request.setAttribute("tutorshipManagementBean", bean);
        return mapping.findForward("showTutorshipHistory");
    }

    /*
     * AUXILIARY METHODS
     */
    @Override
    protected String getFromRequest(HttpServletRequest request, String id) {
        if (request.getParameter(id) != null) {
            return request.getParameter(id);
        }
        return (String) request.getAttribute(id);
    }

    protected Object getViewState(String id) {
        if (RenderUtils.getViewState(id) != null) {
            return RenderUtils.getViewState(id).getMetaObject().getObject();
        }
        return null;
    }

    protected TutorshipManagementBean getTutorshipBeanWithRequestParameters(HttpServletRequest request) {
        final String executionDegreeId = request.getParameter("executionDegreeId");
        final String degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
        final String teacherId = request.getParameter("teacherId");

        return ((teacherId != null) ? new TutorshipManagementBean(executionDegreeId, degreeCurricularPlanID, teacherId) : new TutorshipManagementBean(
                executionDegreeId, degreeCurricularPlanID));
    }

    /*
     * Only LEEC-pB or Bolonha (all degrees) coordinators can access this
     * section
     */
    protected boolean validateDegreeTypeAccessRestrictions(final ExecutionDegree executionDegree) {
        return !(executionDegree.isBolonhaDegree() && executionDegree.getDegree().getSigla().equals("LEEC-pB"));
    }

    /*
     * Only persons with coordinator role for this execution degree can access
     * this portal
     */
    protected boolean validateCoordinationAccessRestrictions(final Person person, final ExecutionDegree executionDegree) {
        List<Coordinator> coordinators = new ArrayList<Coordinator>();
        coordinators.addAll(person.getCoordinatorsSet());
        coordinators.retainAll(executionDegree.getCoordinatorsListSet());

        return !coordinators.isEmpty();
    }

    private List<TutorBean> getAllPossibleTutors(TutorshipManagementBean bean, ExecutionDegree executionDegree) {
        List<TutorBean> tutorHistoryBeans = new ArrayList<TutorBean>();
        for (Teacher teacher : Tutorship.getPossibleTutorsFromExecutionDegreeDepartments(executionDegree)) {
            TutorBean historyBean = new TutorBean(bean.getExecutionDegreeID(), bean.getDegreeCurricularPlanID(), teacher);
            tutorHistoryBeans.add(historyBean);
        }
        Collections.sort(tutorHistoryBeans, new BeanComparator("teacher.teacherId"));
        return tutorHistoryBeans;
    }

    private List<TutorBean> getTutorsWithTutorshipHistory(TutorshipManagementBean bean, ExecutionDegree executionDegree) {
        List<TutorBean> tutorHistoryBeans = new ArrayList<TutorBean>();
        for (Teacher teacher : Tutorship.getPossibleTutorsFromExecutionDegreeDepartments(executionDegree)) {
            if (teacher.getTutorshipsSet().size() != 0) {
                TutorBean historyBean = new TutorBean(bean.getExecutionDegreeID(), bean.getDegreeCurricularPlanID(), teacher);
                tutorHistoryBeans.add(historyBean);
            }
        }
        Collections.sort(tutorHistoryBeans, new BeanComparator("teacher.teacherId"));
        return tutorHistoryBeans;
    }

    /*
     * Returns a list of TutorManagementBeans for each entry year of students
     * associated with the tutor
     */
    protected List<TutorshipManagementByEntryYearBean> getTutorshipManagementBeansByEntryYear(Teacher teacher,
            List<Tutorship> tutorships) {
        Map<ExecutionYear, TutorshipManagementByEntryYearBean> tutorshipManagementBeansByEntryYear =
                new HashMap<ExecutionYear, TutorshipManagementByEntryYearBean>();

        for (final Tutorship tutorship : tutorships) {
            ExecutionYear entryYear = tutorship.getStudentCurricularPlan().getRegistration().getStartExecutionYear();

            if (!tutorshipManagementBeansByEntryYear.containsKey(entryYear)) {
                TutorshipManagementByEntryYearBean tutorManagementBean =
                        new TutorshipManagementByEntryYearBean(entryYear, teacher);
                tutorshipManagementBeansByEntryYear.put(entryYear, tutorManagementBean);
            }
        }

        ArrayList<TutorshipManagementByEntryYearBean> beans =
                new ArrayList<TutorshipManagementByEntryYearBean>(tutorshipManagementBeansByEntryYear.values());
        Collections.sort(beans, new BeanComparator("executionYear"));
        Collections.reverse(beans);

        return beans;
    }
}