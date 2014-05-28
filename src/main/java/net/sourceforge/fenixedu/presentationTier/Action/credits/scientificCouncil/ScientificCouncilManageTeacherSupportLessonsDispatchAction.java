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
package net.sourceforge.fenixedu.presentationTier.Action.credits.scientificCouncil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageTeacherSupportLessonsDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "scientificCouncil", path = "/supportLessonsManagement",
        input = "/supportLessonsManagement.do?method=prepareEdit&page=0", formBean = "supportLessonForm",
        functionality = ScientificCouncilViewTeacherCreditsDA.class)
@Forwards({
        @Forward(name = "successfull-delete",
                path = "/scientificCouncil/degreeTeachingServiceManagement.do?method=showTeachingServiceDetails"),
        @Forward(name = "successfull-edit",
                path = "/scientificCouncil/degreeTeachingServiceManagement.do?method=showTeachingServiceDetails"),
        @Forward(name = "edit-support-lesson", path = "/credits/supportLessons/editSupportLesson.jsp"),
        @Forward(name = "teacher-not-found", path = "/scientificCouncil/credits.do?method=viewAnnualTeachingCredits") })
@Exceptions(
        value = {
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.presentationTier.Action.credits.ManageTeacherSupportLessonsDispatchAction.InvalidPeriodException.class,
                        key = "message.invalidPeriod", handler = org.apache.struts.action.ExceptionHandler.class,
                        path = "/supportLessonsManagement.do?method=prepareEdit&page=0", scope = "request"),
                @ExceptionHandling(type = net.sourceforge.fenixedu.domain.exceptions.DomainException.class,
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixDomainExceptionHandler.class,
                        scope = "request") })
public class ScientificCouncilManageTeacherSupportLessonsDispatchAction extends ManageTeacherSupportLessonsDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {

        DynaActionForm supportLessonForm = (DynaActionForm) form;
        String supportLesssonID = (String) supportLessonForm.get("supportLessonID");

        Professorship professorship = getDomainObject(supportLessonForm, "professorshipID");

        if (professorship == null) {
            return mapping.findForward("teacher-not-found");
        }

        SupportLesson supportLesson = null;
        if (!StringUtils.isEmpty(supportLesssonID)) {
            supportLesson = FenixFramework.getDomainObject(supportLesssonID);
            if (!professorship.getSupportLessons().contains(supportLesson)) {
                return mapping.findForward("teacher-not-found");
            }
        }

        prepareToEdit(supportLesson, professorship, supportLessonForm, request);
        return mapping.findForward("edit-support-lesson");
    }

    public ActionForward editSupportLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, InvalidPeriodException {

        editSupportLesson(form, request, RoleType.SCIENTIFIC_COUNCIL);
        return mapping.findForward("successfull-edit");
    }

    public ActionForward deleteSupportLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {

        deleteSupportLesson(request, form, RoleType.SCIENTIFIC_COUNCIL);
        return mapping.findForward("successfull-delete");
    }
}
