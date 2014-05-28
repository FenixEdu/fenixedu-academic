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
package net.sourceforge.fenixedu.domain.phd.candidacy;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcess;

import org.apache.commons.lang.StringUtils;

public class PhdCandidacyRefereeLetterFile extends PhdCandidacyRefereeLetterFile_Base {

    private PhdCandidacyRefereeLetterFile() {
        super();
    }

    PhdCandidacyRefereeLetterFile(final PhdProgramCandidacyProcess candidacyProcess, final String filename, final byte[] content) {
        this();
        init(candidacyProcess, PhdIndividualProgramDocumentType.RECOMMENDATION_LETTER, null, content, filename, null);
    }

    @Override
    protected void checkParameters(PhdProgramProcess candidacyProcess, PhdIndividualProgramDocumentType documentType,
            byte[] content, String filename, Person uploader) {

        String[] args = {};
        if (candidacyProcess == null) {
            throw new DomainException("error.phd.PhdProgramProcessDocument.candidacyProcess.cannot.be.null", args);
        }
        if (documentType == null || content == null || content.length == 0 || StringUtils.isEmpty(filename)) {
            throw new DomainException("error.phd.PhdProgramProcessDocument.documentType.and.file.cannot.be.null");
        }
    }

    @Override
    public void delete() {
        setLetter(null);
        super.delete();
    }

    @Deprecated
    public boolean hasLetter() {
        return getLetter() != null;
    }

}
