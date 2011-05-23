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

}
