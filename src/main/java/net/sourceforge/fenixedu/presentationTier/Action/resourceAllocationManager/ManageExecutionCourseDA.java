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
package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.DeleteCourseLoad;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.EditExecutionCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadClassesByExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.CourseLoadBean;
import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
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