package net.sourceforge.fenixedu.domain.phd;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DeleteFileRequest;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class PhdProgramProcessDocument extends PhdProgramProcessDocument_Base {

    public static Comparator<PhdProgramProcessDocument> COMPARATOR_BY_UPLOAD_TIME = new Comparator<PhdProgramProcessDocument>() {
	public int compare(PhdProgramProcessDocument left, PhdProgramProcessDocument right) {
	    int comparationResult = left.getUploadTime().compareTo(right.getUploadTime());
	    return (comparationResult == 0) ? left.getIdInternal().compareTo(right.getIdInternal()) : comparationResult;
	}
    };

    protected PhdProgramProcessDocument() {
	super();
    }

    public PhdProgramProcessDocument(PhdProgramProcess process, PhdIndividualProgramDocumentType documentType, String remarks,
	    byte[] content, String filename, Person uploader) {
	this();
	init(process, documentType, remarks, content, filename, uploader);

    }

    @SuppressWarnings("unchecked")
    protected void init(PhdProgramProcess process, PhdIndividualProgramDocumentType documentType, String remarks, byte[] content,
	    String filename, Person uploader) {

	checkParameters(process, documentType, content, filename, uploader);
	
	setDocumentVersion(process, documentType);

	super.setPhdProgramProcess(process);
	super.setDocumentType(documentType);
	super.setRemarks(remarks);
	super.setUploader(uploader);
	super.init(getVirtualPath(), filename, filename, Collections.EMPTY_SET, content, new RoleGroup(
		RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE));
	storeToContentManager();
    }

    protected void setDocumentVersion(PhdProgramProcess process, PhdIndividualProgramDocumentType documentType) {
	if (documentType.isVersioned()) {
	    final Set<PhdProgramProcessDocument> documentsByType = process.getDocumentsByType(documentType);
	    super.setDocumentVersion(documentsByType.isEmpty() ? 1 : documentsByType.size() + 1);
	} else {
	    super.setDocumentVersion(1);
	}
    }

    protected void checkParameters(PhdProgramProcess process, PhdIndividualProgramDocumentType documentType, byte[] content,
	    String filename, Person uploader) {

	check(process, "error.phd.PhdProgramProcessDocument.process.cannot.be.null");
	check(uploader,
		"error.net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessDocument.uploader.cannot.be.null");

	if (documentType == null || content == null || content.length == 0 || StringUtils.isEmpty(filename)) {
	    throw new DomainException("error.phd.PhdProgramProcessDocument.documentType.and.file.cannot.be.null");
	}
    }

    /**
     * <pre>
     * Format /PhdIndividualProgram/{processId}
     * </pre>
     * 
     * @return
     */
    protected VirtualPath getVirtualPath() {
	final VirtualPath filePath = new VirtualPath();
	filePath.addNode(new VirtualPathNode("PhdIndividualProgram", "PhdIndividualProgram"));
	filePath.addNode(new VirtualPathNode(getPhdProgramProcess().getIndividualProgramProcess().getIdInternal().toString(),
		getPhdProgramProcess().getIndividualProgramProcess().getIdInternal().toString()));
	return filePath;
    }

    @Override
    protected void disconnect() {
	super.disconnect();
	removeUploader();
	removePhdProgramProcess();
    }

    /*
     * This method works properly because disconnect is re-implemented and
     * super.disconnect is called first
     */
    @Override
    protected void createDeleteFileRequest() {
	Person person = AccessControl.getPerson();
	if (person == null) {
	    person = getPhdProgramProcess().getPerson();
	}
	new DeleteFileRequest(person, getExternalStorageIdentification());
    }

    public boolean hasType(final PhdIndividualProgramDocumentType type) {
	return getDocumentType().equals(type);
    }

    @Override
    public boolean isPersonAllowedToAccess(Person person) {
	if (person != null) {
	    if (getPhdProgramProcess().getPerson() == person || person.hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		    || getPhdProgramProcess().getIndividualProgramProcess().isCoordinatorForPhdProgram(person)
		    || getPhdProgramProcess().getIndividualProgramProcess().isGuiderOrAssistentGuider(person)) {
		return true;
	    }
	}

	return false;
    }

    @Override
    public boolean isPrivate() {
	return true;
    }

    @Override
    public Group getPermittedGroup() {
	throw new DomainException("error.phd.PhdProgramProcessDocument.use.isPersonAllowedToAccess.method.instead");
    }

    @Override
    public String getDownloadUrl() {
	return FileManagerFactory.getFactoryInstance().getFileManager().formatDownloadUrl(getExternalStorageIdentification(),
		getFilename());
    }

}
