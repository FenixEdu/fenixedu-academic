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
package org.fenixedu.academic.ui.struts.action.teacher.executionCourse;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.domain.BibliographicReference;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.dto.teacher.executionCourse.ImportContentBean;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.teacher.CreateBibliographicReference;
import org.fenixedu.academic.service.services.teacher.DeleteBibliographicReference;
import org.fenixedu.academic.service.services.teacher.EditBibliographicReference;
import org.fenixedu.academic.service.services.teacher.OrderBibliographicReferences;
import org.fenixedu.academic.ui.struts.action.teacher.ManageExecutionCourseDA;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Input;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(path = "/manageBibliographicReference", module = "teacher", functionality = ManageExecutionCourseDA.class,
        formBean = "bibliographicReferenceForm")
@Forwards({ @Forward(name = "bibliographicReference", path = "/teacher/executionCourse/bibliographicReference.jsp"),
        @Forward(name = "createBibliographicReference", path = "/teacher/executionCourse/createBibliographicReference.jsp"),
        @Forward(name = "importBibliographicReferences", path = "/teacher/executionCourse/importBibliographicReferences.jsp"),
        @Forward(name = "orderBibliographicReferences", path = "/teacher/executionCourse/orderBibliographicReferences.jsp"),
        @Forward(name = "editBibliographicReference", path = "/teacher/executionCourse/editBibliographicReference.jsp") })
public class ExecutionCourseBibliographyDA extends ManageExecutionCourseDA {

    // BIBLIOGRAPHIC REFERENCES

    @Input
    public ActionForward bibliographicReference(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("bibliographicReference");
    }

    public ActionForward prepareCreateBibliographicReference(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("createBibliographicReference");
    }

    public ActionForward createBibliographicReference(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String title = dynaActionForm.getString("title");
        final String authors = dynaActionForm.getString("authors");
        final String reference = dynaActionForm.getString("reference");
        final String year = dynaActionForm.getString("year");
        final String optional = dynaActionForm.getString("optional");

        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");

        CreateBibliographicReference.runCreateBibliographicReference(executionCourse.getExternalId(), title, authors, reference,
                year, Boolean.valueOf(optional));

        return mapping.findForward("bibliographicReference");
    }

    public ActionForward prepareEditBibliographicReference(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        final String bibliographicReferenceIDString = request.getParameter("bibliographicReferenceID");
        if (executionCourse != null && bibliographicReferenceIDString != null && bibliographicReferenceIDString.length() > 0) {
            final BibliographicReference bibliographicReference =
                    findBibliographicReference(executionCourse, bibliographicReferenceIDString);
            if (bibliographicReference != null) {
                final DynaActionForm dynaActionForm = (DynaActionForm) form;
                dynaActionForm.set("title", bibliographicReference.getTitle());
                dynaActionForm.set("authors", bibliographicReference.getAuthors());
                dynaActionForm.set("reference", bibliographicReference.getReference());
                dynaActionForm.set("year", bibliographicReference.getYear());
                dynaActionForm.set("optional", bibliographicReference.getOptional().toString());
            }
            request.setAttribute("bibliographicReference", bibliographicReference);
        }
        return mapping.findForward("editBibliographicReference");
    }

    public ActionForward editBibliographicReference(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String bibliographicReferenceIDString = request.getParameter("bibliographicReferenceID");
        final String title = dynaActionForm.getString("title");
        final String authors = dynaActionForm.getString("authors");
        final String reference = dynaActionForm.getString("reference");
        final String year = dynaActionForm.getString("year");
        final String optional = dynaActionForm.getString("optional");

        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        final BibliographicReference bibliographicReference =
                findBibliographicReference(executionCourse, bibliographicReferenceIDString);

        EditBibliographicReference.runEditBibliographicReference(bibliographicReference.getExternalId(), title, authors,
                reference, year, Boolean.valueOf(optional));

        return mapping.findForward("bibliographicReference");
    }

    public ActionForward deleteBibliographicReference(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final String bibliographicReferenceIDString = request.getParameter("bibliographicReferenceID");
        DeleteBibliographicReference.runDeleteBibliographicReference(bibliographicReferenceIDString);

        return mapping.findForward("bibliographicReference");
    }

    public ActionForward prepareImportBibliographicReferences(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("importContentBean", new ImportContentBean());

        return mapping.findForward("importBibliographicReferences");
    }

    public ActionForward prepareImportBibliographicReferencesPostBack(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        prepareImportContentPostBack(request);
        return mapping.findForward("importBibliographicReferences");
    }

    public ActionForward prepareImportBibliographicReferencesInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        prepareImportContentInvalid(request);
        return mapping.findForward("importBibliographicReferences");
    }

    public ActionForward listExecutionCoursesToImportBibliographicReferences(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        listExecutionCoursesToImportContent(request);
        return mapping.findForward("importBibliographicReferences");
    }

    public ActionForward importBibliographicReferences(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        importContent(request, "ImportBibliographicReferences");
        return mapping.findForward("bibliographicReferences");
    }

    private BibliographicReference findBibliographicReference(ExecutionCourse executionCourse, String bibliographicReferenceID) {
        for (final BibliographicReference bibliographicReference : executionCourse.getAssociatedBibliographicReferencesSet()) {
            if (bibliographicReference.getExternalId().equals(bibliographicReferenceID)) {
                return bibliographicReference;
            }
        }
        return null;
    }

    public ActionForward prepareSortBibliography(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ExecutionCourse executionCourse = getExecutionCourse(request);
        boolean optional = request.getParameter("optional") != null;
        List<BibliographicReference> references;

        if (optional) {
            references = getOptionalBibliographicReferences(executionCourse);
        } else {
            references = getMainBibliographicReferences(executionCourse);
        }

        request.setAttribute("references", references);
        request.setAttribute("optional", optional);

        return mapping.findForward("orderBibliographicReferences");
    }

    private List<BibliographicReference> getMainBibliographicReferences(ExecutionCourse executionCourse) {
        List<BibliographicReference> references = new ArrayList<BibliographicReference>();

        for (BibliographicReference reference : executionCourse.getOrderedBibliographicReferences()) {
            if (!reference.isOptional()) {
                references.add(reference);
            }
        }

        return references;
    }

    private List<BibliographicReference> getOptionalBibliographicReferences(ExecutionCourse executionCourse) {
        List<BibliographicReference> references = new ArrayList<BibliographicReference>();

        for (BibliographicReference reference : executionCourse.getOrderedBibliographicReferences()) {
            if (reference.isOptional()) {
                references.add(reference);
            }
        }

        return references;
    }

    public ActionForward sortBibliographyReferences(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        ExecutionCourse executionCourse = getExecutionCourse(request);
        boolean optional = request.getParameter("optional") != null;

        String orderString = request.getParameter("referencesOrder");

        List<BibliographicReference> initialReferences;
        if (optional) {
            initialReferences = getOptionalBibliographicReferences(executionCourse);
        } else {
            initialReferences = getMainBibliographicReferences(executionCourse);
        }

        List<BibliographicReference> orderedReferences = new ArrayList<BibliographicReference>();

        String[] nodes = orderString.split(",");
        for (String node : nodes) {
            String[] parts = node.split("-");

            Integer itemIndex = getId(parts[0]);
            orderedReferences.add(initialReferences.get(itemIndex - 1));
        }

        List<BibliographicReference> finalOrderedReferences;
        if (optional) {
            finalOrderedReferences = getMainBibliographicReferences(executionCourse);
            finalOrderedReferences.addAll(orderedReferences);
        } else {
            finalOrderedReferences = orderedReferences;
            finalOrderedReferences.addAll(getOptionalBibliographicReferences(executionCourse));
        }

        OrderBibliographicReferences.run(executionCourse, finalOrderedReferences);
        return mapping.findForward("bibliographicReferences");
    }

    private Integer getId(String id) {
        if (id == null) {
            return null;
        }

        try {
            return new Integer(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
