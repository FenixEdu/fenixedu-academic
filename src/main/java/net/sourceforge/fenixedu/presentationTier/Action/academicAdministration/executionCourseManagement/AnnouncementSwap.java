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
package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/announcementSwap", module = "academicAdministration", functionality = EditExecutionCourseDA.class)
@Forwards({ @Forward(name = "chooseExecutionCourse",
        path = "/academicAdministration/executionCourseManagement/chooseExecutionCourse.jsp") })
public class AnnouncementSwap extends FenixDispatchAction {

    public ActionForward prepareSwap(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Boolean chooseNotLinked = Boolean.valueOf(request.getParameter("executionCoursesNotLinked"));
        String executionCourseId = request.getParameter("executionCourseId");
        String executionPeriodId = request.getParameter("executionPeriodId");

        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);
        ExecutionSemester executionPeriod = FenixFramework.getDomainObject(executionPeriodId);

        ExecutionCourseBean sessionBean = new ExecutionCourseBean();

        sessionBean.setSourceExecutionCourse(executionCourse);
        sessionBean.setExecutionSemester(executionPeriod);
        sessionBean.setChooseNotLinked(chooseNotLinked);

        if (!chooseNotLinked) {
            String executionDegreeId = request.getParameter("executionDegreeId");
            String curricularYearId = request.getParameter("curYearId");

            ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeId);
            CurricularYear curYear = FenixFramework.getDomainObject(curricularYearId);

            sessionBean.setExecutionDegree(executionDegree);
            sessionBean.setCurricularYear(curYear);
        }

        request.setAttribute("bean", new ExecutionCourseBean(executionCourse));
        request.setAttribute("sessionBean", sessionBean);
        RenderUtils.invalidateViewState("executionCourseBean");

        return mapping.findForward("chooseExecutionCourse");
    }

    public ActionForward swap(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        ExecutionCourseBean bean = getRenderedObject("executionCourseBean");

        try {
            checkPreConditions(bean);

            int numberOfAnnouncementsSwapped = 0;
            for (Announcement announcement : bean.getAnnouncements()) {
                announcement.swap(bean.getSourceExecutionCourse().getBoard(), bean.getDestinationExecutionCourse().getBoard());
                numberOfAnnouncementsSwapped++;
            }
            addActionMessage("success", request, "message.manager.executionCourseManagement.announcementsSwap.success",
                    Integer.toString(numberOfAnnouncementsSwapped));

        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage());
        }
        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState("executionCourseBean");

        return mapping.findForward("chooseExecutionCourse");

    }

    public ActionForward postBack(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        ExecutionCourseBean bean = getRenderedObject("executionCourseBean");

        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState("executionCourseBean");

        return mapping.findForward("chooseExecutionCourse");

    }

    private void checkPreConditions(ExecutionCourseBean bean) {
        if (bean.getSourceExecutionCourse() == null) {
            throw new DomainException("error.manager.executionCourseManagement.announcementsSwap.noSource");
        }
        if (bean.getDestinationExecutionCourse() == null) {
            throw new DomainException("error.manager.executionCourseManagement.announcementsSwap.noDestination");
        }
        if (bean.getSourceExecutionCourse() == bean.getDestinationExecutionCourse()) {
            throw new DomainException("error.selection.sameSourceDestinationCourse");
        }
        if (bean.getAnnouncements().isEmpty()) {
            throw new DomainException("error.manager.executionCourseManagement.announcementsSwap.noAnnouncements");
        }
    }
}
