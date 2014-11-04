/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.resourceAllocationManager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.CourseLoad;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.InfoClass;
import org.fenixedu.academic.dto.InfoExecutionCourse;
import org.fenixedu.academic.dto.resourceAllocationManager.CourseLoadBean;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.resourceAllocationManager.DeleteCourseLoad;
import org.fenixedu.academic.service.services.resourceAllocationManager.EditExecutionCourse;
import org.fenixedu.academic.service.services.resourceAllocationManager.ReadClassesByExecutionCourse;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.base.FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz & Sara Ribeiro
 *
 */
@Mapping(path = "/manageExecutionCourse", module = "resourceAllocationManager", functionality = ManageExecutionCoursesDA.class)
@Forwards(@Forward(name = "ManageExecutionCourse", path = "/resourceAllocationManager/manageExecutionCourse_bd.jsp"))
public class ManageExecutionCourseDA extends FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        InfoExecutionCourse infoExecutionCourse =
                (InfoExecutionCourse) request.getAttribute(PresentationConstants.EXECUTION_COURSE);
        ExecutionCourse executionCourse = infoExecutionCourse.getExecutionCourse();
        readAndSetExecutionCourseClasses(request, executionCourse);
        request.setAttribute("courseLoadBean", new CourseLoadBean(executionCourse));
        return mapping.findForward("ManageExecutionCourse");
    }

    public ActionForward preparePostBack(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        CourseLoadBean bean = getRenderedObject("courseLoadBeanID");
        ShiftType type = bean.getType();
        if (type != null) {
            CourseLoad courseLoad = bean.getExecutionCourse().getCourseLoadByShiftType(type);
            if (courseLoad != null) {
                bean.setUnitQuantity(courseLoad.getUnitQuantity());
                bean.setTotalQuantity(courseLoad.getTotalQuantity());
            } else {
                bean.setUnitQuantity(null);
                bean.setTotalQuantity(null);
            }
        }

        readAndSetExecutionCourseClasses(request, bean.getExecutionCourse());
        RenderUtils.invalidateViewState("courseLoadBeanID");
        request.setAttribute("courseLoadBean", bean);
        return mapping.findForward("ManageExecutionCourse");
    }

    public ActionForward showCourseLoad(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        CourseLoadBean bean = getRenderedObject("courseLoadBeanID");
        readAndSetExecutionCourseClasses(request, bean.getExecutionCourse());
        request.setAttribute("courseLoadBean", bean);
        return mapping.findForward("ManageExecutionCourse");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        CourseLoadBean bean = getRenderedObject("courseLoadBeanID");
        try {
            EditExecutionCourse.run(bean);

        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            readAndSetExecutionCourseClasses(request, bean.getExecutionCourse());
            request.setAttribute("courseLoadBean", bean);
            return mapping.findForward("ManageExecutionCourse");
        }

        bean.setType(null);
        bean.setUnitQuantity(null);
        bean.setTotalQuantity(null);

        readAndSetExecutionCourseClasses(request, bean.getExecutionCourse());
        request.setAttribute("courseLoadBean", bean);
        return mapping.findForward("ManageExecutionCourse");
    }

    public ActionForward deleteCourseLoad(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        CourseLoad courseLoad = getCourseLoadFromParameter(request);
        ExecutionCourse executionCourse = courseLoad.getExecutionCourse();
        try {
            DeleteCourseLoad.run(courseLoad);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
        }

        readAndSetExecutionCourseClasses(request, executionCourse);
        request.setAttribute("courseLoadBean", new CourseLoadBean(executionCourse));
        return mapping.findForward("ManageExecutionCourse");
    }

    private void readAndSetExecutionCourseClasses(HttpServletRequest request, ExecutionCourse executionCourse)
            throws FenixServiceException {

        List<InfoClass> infoClasses = ReadClassesByExecutionCourse.runReadClassesByExecutionCourse(executionCourse);

        if (infoClasses != null && !infoClasses.isEmpty()) {
            Collections.sort(infoClasses, new BeanComparator("nome"));
            request.setAttribute(PresentationConstants.LIST_INFOCLASS, infoClasses);
        }
    }

    private CourseLoad getCourseLoadFromParameter(final HttpServletRequest request) {
        final String idString =
                request.getParameterMap().containsKey("courseLoadID") ? request.getParameter("courseLoadID") : null;
        return FenixFramework.getDomainObject(idString);
    }
}