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
package org.fenixedu.academic.service.services.administrativeOffice.candidacy;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.fenixedu.academic.domain.candidacy.CandidacyDocument;
import org.fenixedu.academic.domain.candidacy.CandidacyDocumentFile;
import org.fenixedu.academic.dto.candidacy.CandidacyDocumentUploadBean;

import pt.ist.fenixframework.Atomic;

import com.google.common.io.Files;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class SaveCandidacyDocumentFiles {

    @Atomic
    public static void run(List<CandidacyDocumentUploadBean> candidacyDocuments) {
        for (CandidacyDocumentUploadBean candidacyDocumentUploadBean : candidacyDocuments) {
            if (candidacyDocumentUploadBean.getTemporaryFile() != null) {

                String filename = candidacyDocumentUploadBean.getFilename();
                CandidacyDocument candidacyDocument = candidacyDocumentUploadBean.getCandidacyDocument();

                final byte[] content = read(candidacyDocumentUploadBean.getTemporaryFile());

                if (candidacyDocument.getFile() != null) {
                    candidacyDocument.getFile().delete();
                }

                final CandidacyDocumentFile candidacyDocumentFile = new CandidacyDocumentFile(filename, filename, content);
                candidacyDocument.setFile(candidacyDocumentFile);
            }
        }

    }

    private static byte[] read(final File file) {
        try {
            return Files.toByteArray(file);
        } catch (IOException e) {
            throw new Error(e);
        }
    }
}