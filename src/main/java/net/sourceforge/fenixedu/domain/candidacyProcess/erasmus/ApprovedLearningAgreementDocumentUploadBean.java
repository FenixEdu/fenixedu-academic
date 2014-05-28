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
package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.io.IOException;

import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ApprovedLearningAgreementDocumentUploadBean extends CandidacyProcessDocumentUploadBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public IndividualCandidacyDocumentFile createIndividualCandidacyDocumentFile(Class<? extends Process> processType,
            String documentIdNumber) throws IOException {
        String fileName = this.getFileName();
        long fileLength = this.getFileSize();
        IndividualCandidacyDocumentFileType type = this.getType();

        if (fileLength > MAX_FILE_SIZE) {
            throw new DomainException("error.file.to.big");
        }

        byte[] contents = readStreamContents();
        if (contents == null) {
            return null;
        }

        return ApprovedLearningAgreementDocumentFile.createCandidacyDocument(contents, fileName, processType.getSimpleName(),
                documentIdNumber);
    }

}
