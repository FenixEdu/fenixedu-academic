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
package net.sourceforge.fenixedu.presentationTier.Action.phd.thesis.teacher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdDocumentsZip;
import net.sourceforge.fenixedu.presentationTier.Action.phd.teacher.PhdIndividualProgramProcessDA;
import net.sourceforge.fenixedu.presentationTier.Action.phd.thesis.CommonPhdThesisProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdThesisProcess", module = "teacher", functionality = PhdIndividualProgramProcessDA.class)
@Forwards({ @Forward(name = "manageThesisDocuments", path = "/phd/thesis/teacher/manageThesisDocuments.jsp"),
        @Forward(name = "juryReporterFeedbackUpload", path = "/phd/thesis/teacher/juryReporterFeedbackUpload.jsp"),
        @Forward(name = "scheduleThesisMeeting", path = "/phd/thesis/teacher/scheduleThesisMeeting.jsp") })
public class PhdThesisProcessDA extends CommonPhdThesisProcessDA {

    private static final List<PhdIndividualProgramDocumentType> AVAILABLE_DOCUMENTS_TO_TEACHER = Arrays
            .asList(new PhdIndividualProgramDocumentType[] {
                    PhdIndividualProgramDocumentType.DISSERTATION_OR_FINAL_WORK_DOCUMENT,
                    PhdIndividualProgramDocumentType.FINAL_THESIS, PhdIndividualProgramDocumentType.JURY_PRESIDENT_ELEMENT,
                    PhdIndividualProgramDocumentType.JURY_REPORT_FEEDBACK,
                    PhdIndividualProgramDocumentType.MAXIMUM_GRADE_GUIDER_PROPOSAL,
                    PhdIndividualProgramDocumentType.PROVISIONAL_THESIS,
                    PhdIndividualProgramDocumentType.PUBLIC_PRESENTATION_SEMINAR_REPORT,
                    PhdIndividualProgramDocumentType.THESIS_ABSTRACT });

    @Override
    public ActionForward manageThesisDocuments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        List<PhdProgramProcessDocument> sharedDocuments = new ArrayList<PhdProgramProcessDocument>();

        PhdThesisProcess thesisProcess = getProcess(request);
        Set<PhdProgramProcessDocument> latestDocumentVersions = thesisProcess.getLatestDocumentVersions();
        for (PhdProgramProcessDocument phdProgramProcessDocument : latestDocumentVersions) {
            if (AVAILABLE_DOCUMENTS_TO_TEACHER.contains(phdProgramProcessDocument.getDocumentType())) {
                sharedDocuments.add(phdProgramProcessDocument);
            }
        }

        request.setAttribute("sharedDocuments", sharedDocuments);

        return super.manageThesisDocuments(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward downloadThesisDocuments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        writeFile(response, getThesisDocumentsFilename(request), PhdDocumentsZip.ZIP_MIME_TYPE, createThesisZip(request));
        return null;
    }

    protected byte[] createThesisZip(HttpServletRequest request) throws IOException {
        PhdThesisProcess thesisProcess = getProcess(request);
        List<PhdProgramProcessDocument> sharedDocuments = new ArrayList<PhdProgramProcessDocument>();
        Set<PhdProgramProcessDocument> latestDocumentVersions = thesisProcess.getLatestDocumentVersions();

        for (PhdProgramProcessDocument phdProgramProcessDocument : latestDocumentVersions) {
            if (AVAILABLE_DOCUMENTS_TO_TEACHER.contains(phdProgramProcessDocument.getDocumentType())) {
                sharedDocuments.add(phdProgramProcessDocument);
            }
        }

        return PhdDocumentsZip.zip(sharedDocuments);
    }
}
