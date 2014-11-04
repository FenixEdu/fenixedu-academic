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
/**
 * 
 */
package org.fenixedu.academic.ui.struts.action.candidate;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.candidacy.Candidacy;
import org.fenixedu.academic.domain.candidacy.CandidacyDocument;
import org.fenixedu.academic.dto.candidacy.CandidacyDocumentUploadBean;
import org.fenixedu.academic.service.services.administrativeOffice.candidacy.SaveCandidacyDocumentFiles;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.candidate.CandidateApplication.CandidateCandidaciesApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@StrutsFunctionality(app = CandidateCandidaciesApp.class, titleKey = "link.candidacies", path = "view-candidacies")
@Mapping(module = "candidate", path = "/viewCandidacies", formBean = "candidacyForm")
@Forwards(value = { @Forward(name = "uploadDocuments", path = "/candidate/uploadDocuments.jsp"),
        @Forward(name = "viewDetail", path = "/candidate/viewCandidacyDetails.jsp"),
        @Forward(name = "view", path = "/candidate/viewCandidacies.jsp") })
public class ViewCandidaciesDispatchAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("view");
    }

    public ActionForward viewDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final Candidacy candidacy = getCandidacy(request);
        request.setAttribute("canChangePersonalData", candidacy.getActiveCandidacySituation().canChangePersonalData());
        request.setAttribute("candidacy", candidacy);

        return mapping.findForward("viewDetail");
    }

    private Candidacy getCandidacy(HttpServletRequest request) {
        final String candidacyID = request.getParameter("candidacyID");
        for (final Candidacy candidacy : getUserView(request).getPerson().getCandidaciesSet()) {
            if (candidacy.getExternalId().equals(candidacyID)) {
                return candidacy;
            }
        }

        return null;
    }

    public ActionForward prepareUploadDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        fillRequest(request, getCandidacy(request));

        return mapping.findForward("uploadDocuments");
    }

    public ActionForward uploadDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        List<CandidacyDocumentUploadBean> beans =
                (List<CandidacyDocumentUploadBean>) RenderUtils.getViewState("candidacyDocuments").getMetaObject().getObject();

        for (CandidacyDocumentUploadBean bean : beans) {
            bean.createTemporaryFile();
        }

        SaveCandidacyDocumentFiles.run(beans);

        for (CandidacyDocumentUploadBean bean : beans) {
            bean.deleteTemporaryFile();
        }

        fillRequest(request, getCandidacy(beans));

        return mapping.findForward("uploadDocuments");
    }

    private void fillRequest(HttpServletRequest request, Candidacy candidacy) {
        if (RenderUtils.getViewState("candidacyDocuments") != null) {
            RenderUtils.invalidateViewState("candidacyDocuments");
        }
        List<CandidacyDocumentUploadBean> candidacyDocuments = new ArrayList<CandidacyDocumentUploadBean>();
        for (CandidacyDocument candidacyDocument : candidacy.getCandidacyDocumentsSet()) {
            candidacyDocuments.add(new CandidacyDocumentUploadBean(candidacyDocument));
        }

        request.setAttribute("candidacyDocuments", candidacyDocuments);
        request.setAttribute("candidacy", candidacy);
    }

    private Candidacy getCandidacy(List<CandidacyDocumentUploadBean> beans) {
        if (!beans.isEmpty()) {
            return beans.iterator().next().getCandidacyDocument().getCandidacy();
        }
        return null;
    }
}