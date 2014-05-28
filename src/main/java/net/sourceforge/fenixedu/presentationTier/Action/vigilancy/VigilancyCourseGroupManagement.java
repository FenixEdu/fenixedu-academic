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
package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy.AddExecutionCourseToGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy.RemoveExecutionCoursesFromGroup;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "examCoordination", path = "/vigilancy/vigilancyCourseGroupManagement",
        functionality = VigilantGroupManagement.class)
@Forwards(value = { @Forward(name = "editCourseGroup", path = "/examCoordinator/vigilancy/editVigilancyCourseGroup.jsp") })
public class VigilancyCourseGroupManagement extends FenixDispatchAction {

    public ActionForward prepareEdition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        VigilancyCourseGroupBean bean = new VigilancyCourseGroupBean();
        String oid = request.getParameter("gid");

        VigilantGroup group = (VigilantGroup) FenixFramework.getDomainObject(oid);
        bean.setSelectedVigilantGroup(group);
        bean.setSelectedDepartment(getDepartment(group));
        request.setAttribute("bean", bean);

        return mapping.findForward("editCourseGroup");
    }

    private Department getDepartment(VigilantGroup group) {
        Unit unit = group.getUnit();
        if (unit.isDepartmentUnit()) {
            return unit.getDepartment();
        }
        if (unit.isScientificAreaUnit()) {
            ScientificAreaUnit scientificAreaUnit = (ScientificAreaUnit) unit;
            return scientificAreaUnit.getDepartmentUnit().getDepartment();
        }
        return null;
    }

    public ActionForward selectUnit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        VigilancyCourseGroupBean bean =
                (VigilancyCourseGroupBean) RenderUtils.getViewState("selectUnit").getMetaObject().getObject();
        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState("selectUnit");
        return mapping.findForward("editCourseGroup");
    }

    public ActionForward addExecutionCourseToGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IViewState viewState = RenderUtils.getViewState("addExecutionCourses");
        VigilancyCourseGroupBean bean = (VigilancyCourseGroupBean) viewState.getMetaObject().getObject();
        List<ExecutionCourse> executionCourses = bean.getCoursesToAdd();
        VigilantGroup group = bean.getSelectedVigilantGroup();

        if (executionCourses.size() > 0) {
            List<ExecutionCourse> coursesUnableToAdd;

            coursesUnableToAdd = AddExecutionCourseToGroup.run(group, executionCourses);

            request.setAttribute("coursesUnableToAdd", coursesUnableToAdd);
        }
        bean.setCoursesToAdd(new ArrayList<ExecutionCourse>());
        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState("addExecutionCourses");
        return mapping.findForward("editCourseGroup");

    }

    public ActionForward removeExecutionCoursesFromGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        VigilancyCourseGroupBean bean =
                (VigilancyCourseGroupBean) RenderUtils.getViewState("removeExecutionCourses").getMetaObject().getObject();
        List<ExecutionCourse> executionCourses = bean.getCourses();
        VigilantGroup group = bean.getSelectedVigilantGroup();

        try {

            RemoveExecutionCoursesFromGroup.run(group, executionCourses);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
        }

        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState("removeExecutionCourses");
        return mapping.findForward("editCourseGroup");
    }

    public ActionForward addExternalCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IViewState viewState = RenderUtils.getViewState("addExternalCourse");
        if (viewState == null) {
            viewState = RenderUtils.getViewState("addExternalCourse-withoutjs");
        }
        VigilancyCourseGroupBean bean = (VigilancyCourseGroupBean) viewState.getMetaObject().getObject();

        ExecutionCourse course = bean.getExternalCourse();
        VigilantGroup group = bean.getSelectedVigilantGroup();
        if (course != null) {
            List<ExecutionCourse> courses = new ArrayList<ExecutionCourse>();
            courses.add(course);

            try {

                AddExecutionCourseToGroup.run(group, courses);
            } catch (DomainException e) {
                addActionMessage(request, e.getMessage());
            }
        }
        if (RenderUtils.getViewState("addExternalCourse-withoutjs") != null) {
            RenderUtils.invalidateViewState("addExternalCourse-withoutjs");
        }

        request.setAttribute("bean", bean);
        bean.setExternalCourse(null);
        return mapping.findForward("editCourseGroup");
    }
}