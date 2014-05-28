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
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.curriculum.ExtraCurricularActivity;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Pedro Santos (pedro.miguel.santos@ist.utl.pt)
 */
@Mapping(path = "/studentExtraCurricularActivities", module = "academicAdministration", functionality = SearchForStudentsDA.class)
@Forwards({ @Forward(name = "manageActivities",
        path = "/academicAdminOffice/student/extraCurricularActivities/manageActivities.jsp") })
public class StudentExtraCurricularActivitiesDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Student student = FenixFramework.getDomainObject(request.getParameter("studentId"));
        request.setAttribute("student", student);
        return mapping.findForward("manageActivities");
    }

    public ActionForward createActivity(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        ExtraCurricularActivity activity = getRenderedObject();
        RenderUtils.invalidateViewState();
        request.setAttribute("student", activity.getStudent());
        return mapping.findForward("manageActivities");
    }

    public ActionForward deleteActivity(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        ExtraCurricularActivity activity = FenixFramework.getDomainObject(request.getParameter("activityId"));
        RenderUtils.invalidateViewState();
        final Student student = activity.getStudent();
        activity.delete();
        request.setAttribute("student", student);
        return mapping.findForward("manageActivities");
    }
}
