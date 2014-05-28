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
/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.gep.inquiries;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.DeleteExecutionCourseResultsBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.DeleteProfessorshipResultsBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.gep.GepApplication.GepInquiriesApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author - Ricardo Rodrigues (ricardo.rodrigues@ist.utl.pt)
 * 
 */
@StrutsFunctionality(app = GepInquiriesApp.class, path = "delete-results", titleKey = "link.inquiries.deleteResults")
@Mapping(path = "/deleteInquiryResults", module = "gep")
@Forwards(@Forward(name = "deleteResults", path = "/gep/inquiries/deleteInquiryResults.jsp"))
public class DeleteInquiriesResultsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("deleteExecutionCourseResults", new DeleteExecutionCourseResultsBean());
        request.setAttribute("deleteProfessorshipResults", new DeleteProfessorshipResultsBean());
        return mapping.findForward("deleteResults");
    }

    public ActionForward deleteExecutionCourseResults(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        DeleteExecutionCourseResultsBean deleteExecutionCourseResults = getRenderedObject("deleteExecutionCourseResults");
        RenderUtils.invalidateViewState();

        try {
            if (deleteExecutionCourseResults.deleteResults()) {
                request.setAttribute("deleteSuccessful", "true");
            } else {
                request.setAttribute("nothingDeleted", "true");
            }

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getKey(), e.getArgs());
        }

        request.setAttribute("deleteExecutionCourseResults", deleteExecutionCourseResults);
        request.setAttribute("deleteProfessorshipResults", new DeleteProfessorshipResultsBean());
        return mapping.findForward("deleteResults");
    }

    public ActionForward deleteTeacherResults(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        DeleteProfessorshipResultsBean deleteProfessorshipResults = getRenderedObject("deleteProfessorshipResults");
        RenderUtils.invalidateViewState();

        try {
            if (deleteProfessorshipResults.deleteResults()) {
                request.setAttribute("deleteSuccessful", "true");
            } else {
                request.setAttribute("nothingDeleted", "true");
            }
        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getKey(), e.getArgs());
        }

        request.setAttribute("deleteExecutionCourseResults", new DeleteExecutionCourseResultsBean());
        request.setAttribute("deleteProfessorshipResults", deleteProfessorshipResults);
        return mapping.findForward("deleteResults");
    }

    public ActionForward deleteAllTeachersResults(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        DeleteProfessorshipResultsBean deleteAllProfessorshipResults = getRenderedObject("deleteAllProfessorshipResults");
        RenderUtils.invalidateViewState();

        try {
            if (deleteAllProfessorshipResults.deleteAllTeachersResults()) {
                request.setAttribute("deleteSuccessful", "true");
            } else {
                request.setAttribute("nothingDeleted", "true");
            }
        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getKey(), e.getArgs());
        }

        request.setAttribute("deleteExecutionCourseResults", new DeleteExecutionCourseResultsBean());
        request.setAttribute("deleteProfessorshipResults", deleteAllProfessorshipResults);
        return mapping.findForward("deleteResults");
    }
}