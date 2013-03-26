package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.io.IOException;

import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ApprovedLearningAgreementDocumentUploadBean extends CandidacyProcessDocumentUploadBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public IndividualCandidacyDocumentFile createIndividualCandidacyDocumentFile(Class<? extends CandidacyProcess> processType,
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
