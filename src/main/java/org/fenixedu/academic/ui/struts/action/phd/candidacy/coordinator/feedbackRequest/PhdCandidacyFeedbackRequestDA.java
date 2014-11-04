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
package org.fenixedu.academic.ui.struts.action.phd.candidacy.coordinator.feedbackRequest;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramDocumentType;
import org.fenixedu.academic.domain.phd.PhdProgramProcessDocument;
import org.fenixedu.academic.domain.phd.alert.AlertService;
import org.fenixedu.academic.domain.phd.alert.AlertService.AlertMessage;
import org.fenixedu.academic.domain.phd.candidacy.PhdProgramCandidacyProcess;
import org.fenixedu.academic.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestElementBean;
import org.fenixedu.academic.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestProcess;
import org.fenixedu.academic.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestProcess.AddPhdCandidacyFeedbackRequestElements;
import org.fenixedu.academic.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestProcess.DeleteCandidacyFeedbackRequestElement;
import org.fenixedu.academic.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestProcess.EditSharedDocumentTypes;
import org.fenixedu.academic.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestProcessBean;
import org.fenixedu.academic.service.services.caseHandling.CreateNewProcess;
import org.fenixedu.academic.service.services.caseHandling.ExecuteProcessActivity;
import org.fenixedu.academic.ui.struts.action.phd.PhdDocumentsZip;
import org.fenixedu.academic.ui.struts.action.phd.candidacy.CommonPhdCandidacyDA;
import org.fenixedu.academic.ui.struts.action.phd.coordinator.PhdIndividualProgramProcessDA;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@Mapping(path = "/phdCandidacyFeedbackRequest", module = "coordinator", functionality = PhdIndividualProgramProcessDA.class)
@Forwards({
        @Forward(name = "manageFeedbackRequest", path = "/phd/candidacy/coordinator/feedbackRequest/manageFeedbackRequest.jsp"),
        @Forward(name = "uploadCandidacyFeedback",
                path = "/phd/candidacy/coordinator/feedbackRequest/uploadCandidacyFeedback.jsp") })
public class PhdCandidacyFeedbackRequestDA extends CommonPhdCandidacyDA {

    public ActionForward manageFeedbackRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        addSharedDocumentTypeNames(request);
        return mapping.findForward("manageFeedbackRequest");
    }

    private void addSharedDocumentTypeNames(HttpServletRequest request) {
        final PhdProgramCandidacyProcess process = getProcess(request);

        if (process.getFeedbackRequest() != null) {

            final StringBuilder builder = new StringBuilder();
            final Iterator<PhdIndividualProgramDocumentType> iter =
                    process.getFeedbackRequest().getSortedSharedDocumentTypes().iterator();

            while (iter.hasNext()) {
                builder.append(iter.next().getLocalizedName()).append(iter.hasNext() ? ", " : "");
            }

            request.setAttribute("sharedDocumentTypes", builder.toString());
        }
    }

    public ActionForward downloadSubmittedCandidacyFeedbackDocuments(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        final PhdProgramCandidacyProcess process = getProcess(request);

        writeFile(response, getZipDocumentsFilename(process.getIndividualProgramProcess()), PhdDocumentsZip.ZIP_MIME_TYPE,
                createZip(process.getFeedbackRequest().getSubmittedCandidacyFeedbackDocuments()));

        return null;
    }

    /*
     * Edit shared documents methods
     */
    public ActionForward prepareEditSharedDocuments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("feedbackRequestBean", new PhdCandidacyFeedbackRequestProcessBean(getProcess(request)));
        return manageFeedbackRequest(mapping, actionForm, request, response);
    }

    public ActionForward prepareEditSharedDocumentsInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("feedbackRequestBean", getRenderedObject("feedbackRequestBean"));
        return manageFeedbackRequest(mapping, actionForm, request, response);
    }

    static public class AvailableDocumentsToShare implements DataProvider {

        @Override
        public Converter getConverter() {
            return null;
        }

        @Override
        public Object provide(Object source, Object currentValue) {
            final PhdCandidacyFeedbackRequestProcessBean bean = (PhdCandidacyFeedbackRequestProcessBean) source;

            final Collection<PhdIndividualProgramDocumentType> documentTypes = new HashSet<PhdIndividualProgramDocumentType>();
            for (final PhdProgramProcessDocument document : bean.getCandidacyProcess().getLatestDocumentVersions()) {
                documentTypes.add(document.getDocumentType());
            }

            return documentTypes;
        }
    }

    public ActionForward editSharedDocuments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            final PhdProgramCandidacyProcess process = getProcess(request);

            if (process.getFeedbackRequest() == null) {
                CreateNewProcess.run(PhdCandidacyFeedbackRequestProcess.class, getRenderedObject("feedbackRequestBean"));
            } else {
                ExecuteProcessActivity.run(getProcess(request).getFeedbackRequest(), EditSharedDocumentTypes.class,
                        getRenderedObject("feedbackRequestBean"));
            }

            addSuccessMessage(request, "message.phd.candidacy.feedback.documents.edited.with.success");

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return prepareEditSharedDocumentsInvalid(mapping, actionForm, request, response);
        }

        return manageFeedbackRequest(mapping, actionForm, request, response);
    }

    /*
     * End of edit shared documents methods
     */

    /*
     * Add Candidacy Feedback Request Element
     */

    public ActionForward prepareAddCandidacyFeedbackRequestElement(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final PhdCandidacyFeedbackRequestElementBean bean = new PhdCandidacyFeedbackRequestElementBean(getProcess(request));
        bean.updateWithExistingPhdParticipants();
        setDefaultMailInformation(bean);

        request.setAttribute("elementBean", bean);
        return manageFeedbackRequest(mapping, actionForm, request, response);
    }

    private void setDefaultMailInformation(PhdCandidacyFeedbackRequestElementBean bean) {

        bean.setMailSubject(AlertService.getSubjectPrefixed(bean.getIndividualProgramProcess(),
                "message.phd.candidacy.feedback.default.subject"));

        bean.setMailBody(AlertService.getBodyText(
                bean.getIndividualProgramProcess(),
                AlertMessage.create("message.phd.candidacy.feedback.default.body", bean.getIndividualProgramProcess().getPerson()
                        .getName())));

    }

    static public class ExistingPhdParticipantsNotInCandidacyFeedbackRequestProcess implements DataProvider {

        @Override
        public Converter getConverter() {
            return new DomainObjectKeyArrayConverter();
        }

        @Override
        public Object provide(Object source, Object currentValue) {
            final PhdCandidacyFeedbackRequestElementBean bean = (PhdCandidacyFeedbackRequestElementBean) source;
            return bean.getExistingParticipants();
        }
    }

    public ActionForward addCandidacyFeedbackRequestElementInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("elementBean", getRenderedObject("elementBean"));
        return manageFeedbackRequest(mapping, actionForm, request, response);
    }

    public ActionForward addCandidacyFeedbackRequestElementPostBack(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        addCandidacyFeedbackRequestElementInvalid(mapping, actionForm, request, response);
        RenderUtils.invalidateViewState();
        return manageFeedbackRequest(mapping, actionForm, request, response);
    }

    public ActionForward addCandidacyFeedbackRequestElement(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        try {

            final PhdCandidacyFeedbackRequestElementBean bean = getRenderedObject("elementBean");

            if (bean.isExistingElement() && !bean.hasAnyParticipants()) {
                addErrorMessage(request, "label.phd.candidacy.feedback.must.select.elements");
                return addCandidacyFeedbackRequestElementInvalid(mapping, actionForm, request, response);
            }

            ExecuteProcessActivity.run(getProcess(request).getFeedbackRequest(), AddPhdCandidacyFeedbackRequestElements.class,
                    bean);

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            return addCandidacyFeedbackRequestElementInvalid(mapping, actionForm, request, response);
        }

        return manageFeedbackRequest(mapping, actionForm, request, response);
    }

    public ActionForward deleteCandidacyFeedbackRequestElement(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        try {

            ExecuteProcessActivity.run(getProcess(request).getFeedbackRequest(), DeleteCandidacyFeedbackRequestElement.class,
                    getDomainObject(request, "elementOid"));
            addSuccessMessage(request, "message.phd.candidacy.feedback.element.removed.with.success");

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
        }

        return manageFeedbackRequest(mapping, actionForm, request, response);
    }

    /*
     * End of Add Candidacy Feedback Request Element
     */
}
