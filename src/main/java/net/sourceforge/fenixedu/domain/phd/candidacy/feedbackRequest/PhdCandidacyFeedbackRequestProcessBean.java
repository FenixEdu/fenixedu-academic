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
package net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;

public class PhdCandidacyFeedbackRequestProcessBean implements Serializable {

    static private final long serialVersionUID = 1L;

    private PhdProgramCandidacyProcess candidacyProcess;

    private List<PhdIndividualProgramDocumentType> sharedDocuments;

    public PhdCandidacyFeedbackRequestProcessBean() {
        super();
    }

    public PhdCandidacyFeedbackRequestProcessBean(PhdProgramCandidacyProcess process) {

        setCandidacyProcess(process);

        if (process.hasFeedbackRequest()) {
            setSharedDocuments(new ArrayList<PhdIndividualProgramDocumentType>(process.getFeedbackRequest()
                    .getSortedSharedDocumentTypes()));
        }
    }

    public PhdProgramCandidacyProcess getCandidacyProcess() {
        return candidacyProcess;
    }

    public void setCandidacyProcess(PhdProgramCandidacyProcess candidacyProcess) {
        this.candidacyProcess = candidacyProcess;
    }

    public List<PhdIndividualProgramDocumentType> getSharedDocuments() {
        return sharedDocuments;
    }

    public void setSharedDocuments(List<PhdIndividualProgramDocumentType> sharedDocuments) {
        this.sharedDocuments = sharedDocuments;
    }

}
