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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.ui.struts.action.commons.tutorship.ViewStudentsByTutorDispatchAction;
import org.fenixedu.academic.ui.struts.action.pedagogicalCouncil.PedagogicalCouncilApp.TutorshipApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = TutorshipApp.class, path = "students-by-tutor", titleKey = "label.attends.shifts.tutorialorientation",
        bundle = "ApplicationResources")
@Mapping(path = "/viewStudentsByTutor", module = "pedagogicalCouncil")
@Forwards({ @Forward(name = "viewStudents", path = "/pedagogicalCouncil/tutorship/studentsByTutor.jsp") })
public class ViewStudentsByTutorWithSearchDispatchAction extends ViewStudentsByTutorDispatchAction {

    @EntryPoint
    public ActionForward prepareTutorSearch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("tutorateBean", new TutorSearchBean());
        return mapping.findForward("viewStudents");
    }

    public ActionForward viewStudentsByTutor(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        TutorSearchBean bean = getRenderedObject("tutorateBean");
        RenderUtils.invalidateViewState();
        request.setAttribute("tutorateBean", bean);
        if (bean.getTeacher() != null) {
            getTutorships(request, bean.getTeacher());
            request.setAttribute("tutor", bean.getTeacher().getPerson());
        }
        return mapping.findForward("viewStudents");
    }
}
