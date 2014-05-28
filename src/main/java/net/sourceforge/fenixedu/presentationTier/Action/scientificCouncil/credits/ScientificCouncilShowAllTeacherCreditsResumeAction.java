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
package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ShowAllTeacherCreditsResumeAction;
import net.sourceforge.fenixedu.presentationTier.Action.credits.scientificCouncil.ScientificCouncilViewTeacherCreditsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "scientificCouncil", path = "/showAllTeacherCreditsResume", formBean = "teacherCreditsSheetForm",
        functionality = ScientificCouncilViewTeacherCreditsDA.class)
@Forwards(value = { @Forward(name = "search-teacher-form", path = "search-for-teacher-credits"),
        @Forward(name = "teacher-not-found", path = "search-for-teacher-credits"),
        @Forward(name = "show-all-credits-resume", path = "/credits/commons/listAllTeacherCreditsResume.jsp") })
@Exceptions(value = { @ExceptionHandling(type = java.lang.NumberFormatException.class, key = "errors.invalid.teacher-number",
        handler = org.apache.struts.action.ExceptionHandler.class,
        path = "/showAllTeacherCreditsResume.do?method=prepareTeacherSearch&page=0", scope = "request") })
public class ScientificCouncilShowAllTeacherCreditsResumeAction extends ShowAllTeacherCreditsResumeAction {

    public ActionForward prepareTeacherSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        dynaForm.set("method", "showTeacherCreditsResume");
        return mapping.findForward("search-teacher-form");
    }

    public ActionForward showTeacherCreditsResume(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm dynaActionForm = (DynaActionForm) form;
        Teacher teacher = Teacher.readByIstId(dynaActionForm.getString("teacherId").trim());
        if (teacher == null) {
            request.setAttribute("teacherNotFound", "teacherNotFound");
            dynaActionForm.set("method", "showTeacherCreditsResume");
            return mapping.findForward("teacher-not-found");
        }
        readAllTeacherCredits(request, teacher);
        return mapping.findForward("show-all-credits-resume");
    }
}
