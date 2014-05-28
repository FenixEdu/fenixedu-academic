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
package net.sourceforge.fenixedu.domain.phd;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;

import org.apache.commons.lang.StringUtils;

public class PhdThesisReportFeedbackDocument extends PhdThesisReportFeedbackDocument_Base {

    private PhdThesisReportFeedbackDocument() {
        super();
    }

    public PhdThesisReportFeedbackDocument(ThesisJuryElement element, String remarks, byte[] content, String filename,
            Person uploader) {
        this();
        String[] args = {};

        // first set jury element and then init document
        if (element == null) {
            throw new DomainException("error.PhdThesisReportFeedbackDocument.invalid.element", args);
        }
        setJuryElement(element);

        init(element.getProcess(), PhdIndividualProgramDocumentType.JURY_REPORT_FEEDBACK, remarks, content, filename, uploader);
    }

    @Override
    protected void checkParameters(PhdProgramProcess process, PhdIndividualProgramDocumentType documentType, byte[] content,
            String filename, Person uploader) {

        if (documentType == null || content == null || content.length == 0 || StringUtils.isEmpty(filename)) {
            throw new DomainException("error.phd.PhdProgramProcessDocument.documentType.and.file.cannot.be.null");
        }

    }

    @Override
    protected void setDocumentVersion(PhdProgramProcess process, PhdIndividualProgramDocumentType documentType) {
        if (documentType.isVersioned()) {
            if (process != null) {
                super.setDocumentVersion(process.getLastVersionNumber(documentType) + 1);

            } else {
                super.setDocumentVersion(getJuryElement().getFeedbackDocumentsSet().size() + 1);
            }
        } else {
            super.setDocumentVersion(1);
        }
    }

    @Override
    protected void disconnect() {
        setJuryElement(null);
        super.disconnect();
    }

    public boolean isAssignedToProcess() {
        return hasPhdProgramProcess();
    }

    @Override
    public boolean isLast() {
        return getJuryElement().getLastFeedbackDocument() == this;
    }

    @Override
    public PhdProgramProcessDocument getLastVersion() {
        return getJuryElement().getLastFeedbackDocument();
    }

    @Deprecated
    public boolean hasJuryElement() {
        return getJuryElement() != null;
    }

}
