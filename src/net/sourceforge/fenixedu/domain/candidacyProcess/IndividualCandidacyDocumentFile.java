package net.sourceforge.fenixedu.domain.candidacyProcess;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class IndividualCandidacyDocumentFile extends IndividualCandidacyDocumentFile_Base {

	private static final String ROOT_DIR_DESCRIPTION = "Documents associated with an Individual Candidacy";
	private static final String ROOT_DIR = "IndividualCandidacyDocumentFile";

	protected IndividualCandidacyDocumentFile() {
		super();
		this.setCandidacyFileActive(Boolean.TRUE);
	}

	protected IndividualCandidacyDocumentFile(IndividualCandidacyDocumentFileType type, IndividualCandidacy candidacy,
			byte[] contents, String filename) {
		this();
		this.setCandidacyFileActive(Boolean.TRUE);
		addIndividualCandidacy(candidacy);
		setCandidacyFileType(type);
		init(getVirtualPath(candidacy), filename, filename, null, contents, null);

		if (type.equals(IndividualCandidacyDocumentFileType.PHOTO)) {
			// storeToContentManager();
		}
	}

	protected IndividualCandidacyDocumentFile(IndividualCandidacyDocumentFileType type, byte[] contents, String filename,
			VirtualPath path) {
		this();
		this.setCandidacyFileActive(Boolean.TRUE);
		setCandidacyFileType(type);
		init(path, filename, filename, null, contents, null);

		if (type.equals(IndividualCandidacyDocumentFileType.PHOTO)) {
			// storeToContentManager();
		}
	}

	protected VirtualPath getVirtualPath(final IndividualCandidacy candidacy) {
		final VirtualPath filePath = new VirtualPath();
		filePath.addNode(new VirtualPathNode(ROOT_DIR, ROOT_DIR_DESCRIPTION));

		filePath.addNode(new VirtualPathNode(candidacy.getCandidacyProcess().getClass().getSimpleName(), candidacy
				.getCandidacyProcess().getClass().getSimpleName()));
		filePath.addNode(new VirtualPathNode(candidacy.getPersonalDetails().getDocumentIdNumber(), candidacy.getPersonalDetails()
				.getDocumentIdNumber()));

		// FIXME Anil : Add to VirtualPathNode the execution year of this
		// candidacy
		return filePath;
	}

	protected static VirtualPath obtainVirtualPath(String processName, String documentIdNumber) {
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
