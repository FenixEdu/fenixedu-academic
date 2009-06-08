package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class PhdProgramCandidacyProcessDocument extends PhdProgramCandidacyProcessDocument_Base {
    
    protected PhdProgramCandidacyProcessDocument() {
	super();
    }

    public PhdProgramCandidacyProcessDocument(PhdProgramCandidacyProcess candidacyProcess,
	    PhdIndividualProgramDocumentType documentType, String remarks, byte[] content, String filename, Person uploader) {
	this();
	init(candidacyProcess, documentType, remarks, content, filename, uploader);

    }

    @SuppressWarnings("unchecked")
    protected void init(PhdProgramCandidacyProcess candidacyProcess, PhdIndividualProgramDocumentType documentType, String remarks,
	    byte[] content, String filename, Person uploader) {

	checkParameters(candidacyProcess, documentType, content, filename, uploader);

	super.setPhdCandidacyProcess(candidacyProcess);
	super.setDocumentType(documentType);
	super.setRemarks(remarks);
	super.setUploader(uploader);
	super.init(getVirtualPath(), filename, filename, Collections.EMPTY_SET, content, new RoleGroup(
		RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE));

    }

    protected void checkParameters(PhdProgramCandidacyProcess candidacyProcess, PhdIndividualProgramDocumentType documentType,
	    byte[] content, String filename, Person uploader) {

	check(candidacyProcess, "error.phd.candidacy.PhdProgramCandidacyProcessDocument.candidacyProcess.cannot.be.null");
	check(uploader,
		"error.net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessDocument.uploader.cannot.be.null");

	if (documentType == null || content == null || content.length == 0 || StringUtils.isEmpty(filename)) {
	    throw new DomainException(
		    "error.phd.candidacy.PhdProgramCandidacyProcessDocument.documentType.and.file.cannot.be.null");
	}
    }

    /**
     * <pre>
     * Format /PhdIndividualProgram/{processId}/CandidacyDocuments
     * </pre>
     * 
     * @return
     */
    protected VirtualPath getVirtualPath() {
	final VirtualPath filePath = new VirtualPath();
	filePath.addNode(new VirtualPathNode("PhdIndividualProgram", "PhdIndividualProgram"));
	filePath.addNode(new VirtualPathNode(getPhdCandidacyProcess().getIndividualProgramProcess().getIdInternal().toString(),
		getPhdCandidacyProcess().getIndividualProgramProcess().getIdInternal().toString()));
	filePath.addNode(new VirtualPathNode("CandidacyDocuments", "CandidacyDocuments"));

	return filePath;
    }

    public void delete() {
	removePhdCandidacyProcess();
	super.delete();
    }

}
