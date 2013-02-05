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
