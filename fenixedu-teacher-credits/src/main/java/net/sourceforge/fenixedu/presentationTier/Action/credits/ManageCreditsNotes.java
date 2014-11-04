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
package org.fenixedu.academic.ui.struts.action.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.credits.EditTeacherServiceNotes;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.teacher.TeacherService;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixframework.FenixFramework;

public class ManageCreditsNotes extends FenixDispatchAction {

    protected void getNote(ActionForm actionForm, Teacher teacher, ExecutionSemester executionSemester, String noteType) {

        TeacherService teacherService = TeacherService.getTeacherServiceByExecutionPeriod(teacher, executionSemester);
        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        if (teacherService != null && teacherService.getTeacherServiceNotes() != null) {
            dynaActionForm.set("managementFunctionNote", teacherService.getTeacherServiceNotes().getManagementFunctionNotes());
            dynaActionForm.set("serviceExemptionNote", teacherService.getTeacherServiceNotes().getServiceExemptionNotes());
            dynaActionForm
                    .set("masterDegreeTeachingNote", teacherService.getTeacherServiceNotes().getMasterDegreeTeachingNotes());
            dynaActionForm.set("otherNote", teacherService.getTeacherServiceNotes().getOthersNotes());
            dynaActionForm.set("functionsAccumulationNote", teacherService.getTeacherServiceNotes().getFunctionsAccumulation());
            dynaActionForm.set("thesisNote", teacherService.getTeacherServiceNotes().getThesisNote());
        }

        dynaActionForm.set("noteType", noteType);
        dynaActionForm.set("teacherId", teacher.getExternalId());
        dynaActionForm.set("executionPeriodId", executionSemester.getExternalId());
    }

    protected ActionForward editNote(HttpServletRequest request, DynaActionForm dynaActionForm, Teacher teacher,
            String executionPeriodId, RoleType roleType, ActionMapping mapping, String noteType) throws FenixServiceException {

        ExecutionSemester executionSemester = FenixFramework.getDomainObject(executionPeriodId);
        String managementFunctionNote, serviceExemptionNote, otherNote, masterDegreeTeachingNote, functionsAccumulation, thesisNote;

        managementFunctionNote =
                (!StringUtils.isEmpty(dynaActionForm.getString("managementFunctionNote"))) ? dynaActionForm
                        .getString("managementFunctionNote") : (noteType.equals("managementFunctionNote")) ? "" : null;

        serviceExemptionNote =
                (!StringUtils.isEmpty(dynaActionForm.getString("serviceExemptionNote"))) ? dynaActionForm
                        .getString("serviceExemptionNote") : (noteType.equals("serviceExemptionNote")) ? "" : null;

        otherNote =
                (!StringUtils.isEmpty(dynaActionForm.getString("otherNote"))) ? dynaActionForm.getString("otherNote") : (noteType
                        .equals("otherNote")) ? "" : null;

        masterDegreeTeachingNote =
                (!StringUtils.isEmpty(dynaActionForm.getString("masterDegreeTeachingNote"))) ? dynaActionForm
                        .getString("masterDegreeTeachingNote") : (noteType.equals("masterDegreeTeachingNote")) ? "" : null;

        functionsAccumulation =
                (!StringUtils.isEmpty(dynaActionForm.getString("functionsAccumulationNote"))) ? dynaActionForm
                        .getString("functionsAccumulationNote") : (noteType.equals("functionsAccumulationNote")) ? "" : null;

        thesisNote =
                (!StringUtils.isEmpty(dynaActionForm.getString("thesisNote"))) ? dynaActionForm.getString("thesisNote") : (noteType
                        .equals("thesisNote")) ? "" : null;

        try {
            EditTeacherServiceNotes.runEditTeacherServiceNotes(teacher, executionPeriodId, managementFunctionNote,
                    serviceExemptionNote, otherNote, masterDegreeTeachingNote, functionsAccumulation, thesisNote, roleType);
        } catch (DomainException domainException) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("error", new ActionMessage(domainException.getMessage(), domainException.getArgs()));
            saveMessages(request, actionMessages);
            getNote(dynaActionForm, teacher, executionSemester, noteType);
            return mapping.findForward("show-note");
        }

        request.setAttribute("teacherId", teacher.getExternalId());
        request.setAttribute("executionPeriodId", executionPeriodId);

        return mapping.findForward("edit-note");
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        request.setAttribute("teacherId", dynaActionForm.get("teacherId"));
        request.setAttribute("executionPeriodId", dynaActionForm.get("executionPeriodId"));
        return mapping.findForward("edit-note");
    }
}
