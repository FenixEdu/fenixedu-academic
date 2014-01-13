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
