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
package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipManagementBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipManagementByEntryYearBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.TutorBean;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

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

        List<Teacher> possibleTutorsForExecutionDegree = executiondegree.getPossibleTutorsFromExecutionDegreeDepartments();

        if (!possibleTutorsForExecutionDegree.contains(teacher)) {
            addActionMessage(request, "error.tutor.cannotBeTutorOfExecutionDegree");
            return mapping.findForward("chooseTutor");
        }

        bean.setTeacher(teacher);

        if (!teacher.getActiveTutorships().isEmpty()) {
            List<TutorshipManagementByEntryYearBean> beans =
                    getTutorshipManagementBeansByEntryYear(teacher, teacher.getActiveTutorships());
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
        bean.setNumberOfCurrentTutorships(teacher.getNumberOfActiveTutorships());
        bean.setNumberOfPastTutorships(teacher.getNumberOfPastTutorships());

        List<Tutorship> activeTutorships = teacher.getActiveTutorships();
        Collections.sort(activeTutorships, Tutorship.TUTORSHIP_END_DATE_COMPARATOR);

        List<Tutorship> pastTutorships = teacher.getPastTutorships();
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
        coordinators.addAll(person.getCoordinators());
        coordinators.retainAll(executionDegree.getCoordinatorsList());

        return !coordinators.isEmpty();
    }

    private List<TutorBean> getAllPossibleTutors(TutorshipManagementBean bean, ExecutionDegree executionDegree) {
        List<TutorBean> tutorHistoryBeans = new ArrayList<TutorBean>();
        for (Teacher teacher : executionDegree.getPossibleTutorsFromExecutionDegreeDepartments()) {
            TutorBean historyBean = new TutorBean(bean.getExecutionDegreeID(), bean.getDegreeCurricularPlanID(), teacher);
            tutorHistoryBeans.add(historyBean);
        }
        Collections.sort(tutorHistoryBeans, new BeanComparator("teacher.teacherId"));
        return tutorHistoryBeans;
    }

    private List<TutorBean> getTutorsWithTutorshipHistory(TutorshipManagementBean bean, ExecutionDegree executionDegree) {
        List<TutorBean> tutorHistoryBeans = new ArrayList<TutorBean>();
        for (Teacher teacher : executionDegree.getPossibleTutorsFromExecutionDegreeDepartments()) {
            if (teacher.getNumberOfTutorships() != 0) {
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