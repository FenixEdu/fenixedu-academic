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
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;

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
    public boolean isPersonAllowedToAccess(Person person) {
        PhdIndividualProgramProcess process = (PhdIndividualProgramProcess) getPhdProgramProcess();

        if (!process.getCandidacyProcess().isPublicCandidacy()) {
            return super.isPersonAllowedToAccess(person);
        }

        if (!process.getCandidacyProcess().getPublicPhdCandidacyPeriod().isOpen()) {
            return super.isPersonAllowedToAccess(person);
        }

        return true;
    }

    @Deprecated
    public boolean hasPhdGuider() {
        return getPhdGuider() != null;
    }

}
