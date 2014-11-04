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
package org.fenixedu.academic.domain.phd.candidacy;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramDocumentType;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdParticipant;
import org.fenixedu.bennu.core.domain.User;

public class PhdGuiderAcceptanceLetter extends PhdGuiderAcceptanceLetter_Base {

    protected PhdGuiderAcceptanceLetter() {
        super();
    }

    public PhdGuiderAcceptanceLetter(PhdParticipant guider, PhdIndividualProgramDocumentType documentType, String remarks,
            byte[] content, String filename, Person uploader) {
        this();

        init(guider, documentType, remarks, content, filename, uploader);
    }

    private void init(PhdParticipant guider, PhdIndividualProgramDocumentType documentType, String remarks, byte[] content,
            String filename, Person uploader) {
        PhdIndividualProgramProcess process = guider.getIndividualProcess();

        checkParameters(guider, documentType);
        super.init(process, documentType, remarks, content, filename, uploader);

        setPhdGuider(guider);
    }

    protected void checkParameters(PhdParticipant guider, PhdIndividualProgramDocumentType documentType) {
        checkDocumentType(documentType);

        if (guider == null) {
            throw new DomainException("phd.candidacy.PhdGuiderAcceptanceLetter.guider.required");
        }
    }

    private void checkDocumentType(PhdIndividualProgramDocumentType documentType) {
        if (PhdIndividualProgramDocumentType.GUIDER_ACCEPTANCE_LETTER.equals(documentType)) {
            return;
        }

        if (PhdIndividualProgramDocumentType.ASSISTENT_GUIDER_ACCEPTANCE_LETTER.equals(documentType)) {
            return;
        }

        throw new DomainException("phd.candidacy.PhdGuiderAcceptanceLetter.invalid.type");
    }

    @Override
    public boolean isAccessible(User user) {
        PhdIndividualProgramProcess process = (PhdIndividualProgramProcess) getPhdProgramProcess();

        if (!process.getCandidacyProcess().isPublicCandidacy()) {
            return super.isAccessible(user);
        }

        if (!process.getCandidacyProcess().getPublicPhdCandidacyPeriod().isOpen()) {
            return super.isAccessible(user);
        }

        return true;
    }

}
