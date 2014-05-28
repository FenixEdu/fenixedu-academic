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
package net.sourceforge.fenixedu.domain.phd.guidance;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcess;

import org.apache.commons.lang.StringUtils;

public class PhdGuidanceDocument extends PhdGuidanceDocument_Base {

    public PhdGuidanceDocument() {
        super();
    }

    public PhdGuidanceDocument(PhdProgramProcess process, PhdIndividualProgramDocumentType documentType, String remarks,
            byte[] content, String filename, Person uploader) {
        this();
        init(process, documentType, remarks, content, filename, uploader);
    }

    @Override
    protected void init(PhdProgramProcess process, PhdIndividualProgramDocumentType documentType, String remarks, byte[] content,
            String filename, Person uploader) {
        checkParameters(process, documentType, remarks);
        super.init(process, documentType, remarks, content, filename, uploader);
    }

    private void checkParameters(PhdProgramProcess process, PhdIndividualProgramDocumentType documentType, String remarks) {
        if (!process.isProcessIndividualProgram()) {
            throw new DomainException("error.phd.guidance.PhdGuidanceDocument.process.must.be.individual.program.process");
        }

        if (!documentType.isForGuidance()) {
            throw new DomainException("error.phd.guidance.PhdGuidanceDocument.type.must.be.for.guidance");
        }

        if (StringUtils.isEmpty(remarks)) {
            throw new DomainException("error.phd.guidance.PhdGuidanceDocument.remarks.must.not.be.empty");
        }
    }

}
