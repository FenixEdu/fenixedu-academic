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
