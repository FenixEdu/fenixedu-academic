package net.sourceforge.fenixedu.domain.phd.candidacy;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;

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
    protected void checkParameters(PhdProgramCandidacyProcess candidacyProcess, PhdIndividualProgramDocumentType documentType,
	    byte[] content, String filename, Person uploader) {

	check(candidacyProcess, "error.phd.candidacy.PhdProgramCandidacyProcessDocument.candidacyProcess.cannot.be.null");
	if (documentType == null || content == null || content.length == 0 || StringUtils.isEmpty(filename)) {
	    throw new DomainException(
		    "error.phd.candidacy.PhdProgramCandidacyProcessDocument.documentType.and.file.cannot.be.null");
	}
    }

    @Override
    public void delete() {
	removeLetter();
	super.delete();
    }
}
