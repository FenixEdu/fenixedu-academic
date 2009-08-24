package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.util.Collections;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.DeleteFileRequest;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class PhdProgramCandidacyProcessDocument extends PhdProgramCandidacyProcessDocument_Base {

    public static Comparator<PhdProgramCandidacyProcessDocument> COMPARATOR_BY_UPLOAD_TIME = new Comparator<PhdProgramCandidacyProcessDocument>() {
	public int compare(PhdProgramCandidacyProcessDocument leftPhdProgramCandidacyProcessDocument,
		PhdProgramCandidacyProcessDocument rightPhdProgramCandidacyProcessDocument) {
	    int comparationResult = leftPhdProgramCandidacyProcessDocument.getUploadTime().compareTo(
		    rightPhdProgramCandidacyProcessDocument.getUploadTime());
	    return (comparationResult == 0) ? leftPhdProgramCandidacyProcessDocument.getIdInternal().compareTo(
		    rightPhdProgramCandidacyProcessDocument.getIdInternal()) : comparationResult;
	}
    };

    protected PhdProgramCandidacyProcessDocument() {
	super();
    }

    public PhdProgramCandidacyProcessDocument(PhdProgramCandidacyProcess candidacyProcess,
	    PhdIndividualProgramDocumentType documentType, String remarks, byte[] content, String filename, Person uploader) {
	this();
	init(candidacyProcess, documentType, remarks, content, filename, uploader);

    }

    @SuppressWarnings("unchecked")
    protected void init(PhdProgramCandidacyProcess candidacyProcess, PhdIndividualProgramDocumentType documentType,
	    String remarks, byte[] content, String filename, Person uploader) {

	checkParameters(candidacyProcess, documentType, content, filename, uploader);

	super.setPhdCandidacyProcess(candidacyProcess);
	super.setDocumentType(documentType);
	super.setRemarks(remarks);
	super.setUploader(uploader);
	super.init(getVirtualPath(), filename, filename, Collections.EMPTY_SET, content, new RoleGroup(
		RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE));
	storeToContentManager();
    }

    protected void checkParameters(PhdProgramCandidacyProcess candidacyProcess, PhdIndividualProgramDocumentType documentType,
	    byte[] content, String filename, Person uploader) {

	check(candidacyProcess, "error.phd.candidacy.PhdProgramCandidacyProcessDocument.candidacyProcess.cannot.be.null");

	if (!candidacyProcess.isPublicCandidacy()) {
	    check(uploader,
		    "error.net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessDocument.uploader.cannot.be.null");
	}

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

    @Override
    protected void disconnect() {
	super.disconnect();
	removeUploader();
	removePhdCandidacyProcess();
    }

    /*
     * This method works properly because disconnect is re-implemented and
     * super.disconnect is called first
     */
    @Override
    protected void createDeleteFileRequest() {
	Person person = AccessControl.getPerson();
	if (person == null) {
	    person = getPhdCandidacyProcess().getPerson();
	}
	new DeleteFileRequest(person, getExternalStorageIdentification());
    }

    public boolean hasType(final PhdIndividualProgramDocumentType type) {
	return getDocumentType().equals(type);
    }

}
