package net.sourceforge.fenixedu.domain.candidacyProcess;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class IndividualCandidacyDocumentFile extends IndividualCandidacyDocumentFile_Base {

    private static final String ROOT_DIR_DESCRIPTION = "Documents associated with an Individual Candidacy";
    private static final String ROOT_DIR = "IndividualCandidacyDocumentFile";

    public IndividualCandidacyDocumentFile() {
	super();
	this.setCandidacyFileActive(Boolean.TRUE);
    }

    public IndividualCandidacyDocumentFile(IndividualCandidacyDocumentFileType type, IndividualCandidacy candidacy,
	    byte[] contents, String filename) {
	this.setCandidacyFileActive(Boolean.TRUE);
	setIndividualCandidacy(candidacy);
	setCandidacyFileType(type);
	init(getVirtualPath(), filename, filename, null, contents, null);

	if (type.equals(IndividualCandidacyDocumentFileType.PHOTO)) {
	    storeToContentManager();
	}
    }

    private IndividualCandidacyDocumentFile(IndividualCandidacyDocumentFileType type, byte[] contents, String filename,
	    VirtualPath path) {
	this.setCandidacyFileActive(Boolean.TRUE);
	setCandidacyFileType(type);
	init(path, filename, filename, null, contents, null);

	if (type.equals(IndividualCandidacyDocumentFileType.PHOTO)) {
	    storeToContentManager();
	}
    }

    private VirtualPath getVirtualPath() {
	final VirtualPath filePath = new VirtualPath();
	filePath.addNode(new VirtualPathNode(ROOT_DIR, ROOT_DIR_DESCRIPTION));

	filePath.addNode(new VirtualPathNode(this.getIndividualCandidacy().getCandidacyProcess().getClass().getSimpleName(), this
		.getIndividualCandidacy().getCandidacyProcess().getClass().getSimpleName()));
	filePath.addNode(new VirtualPathNode(this.getIndividualCandidacy().getPersonalDetails().getDocumentIdNumber(), this
		.getIndividualCandidacy().getPersonalDetails().getDocumentIdNumber()));

	// FIXME Anil : Add to VirtualPathNode the execution year of this
	// candidacy
	return filePath;
    }

    private static VirtualPath obtainVirtualPath(String processName, String documentIdNumber) {
	final VirtualPath filePath = new VirtualPath();
	filePath.addNode(new VirtualPathNode(ROOT_DIR, ROOT_DIR_DESCRIPTION));

	filePath.addNode(new VirtualPathNode(processName, processName));
	filePath.addNode(new VirtualPathNode(documentIdNumber, documentIdNumber));

	// FIXME Anil : Add to VirtualPathNode the execution year of this
	// candidacy
	return filePath;
    }

    @Service
    public static IndividualCandidacyDocumentFile createCandidacyDocument(byte[] contents, String filename,
	    IndividualCandidacyDocumentFileType type, String processName, String documentIdNumber) {
	return new IndividualCandidacyDocumentFile(type, contents, filename, obtainVirtualPath(processName, documentIdNumber));
    }
}
