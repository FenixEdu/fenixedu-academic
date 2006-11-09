package net.sourceforge.fenixedu.domain.research.result;

import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.Checked;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.IFileManager;

public class ResultDocumentFile extends ResultDocumentFile_Base {
    
    public enum FileResultPermittedGroupType {
	PUBLIC, INSTITUTION, RESEARCHER;
	
	public static FileResultPermittedGroupType getDefaultType() {
	    return PUBLIC;
	}
    }
    
    private ResultDocumentFile() {
	super();
    }

    ResultDocumentFile(Result result, String filename, String displayName, FileResultPermittedGroupType permittedGroupType,
	    String mimeType, String checksum, String checksumAlgorithm, Integer size, String externalStorageIdentification, 
	    Group permittedGroup) {
	this();
	checkParameters(result, filename, displayName, permittedGroupType);
	super.setResult(result);
	super.setFileResultPermittedGroupType(permittedGroupType);
	init(filename, displayName, mimeType, checksum, checksumAlgorithm, size,
		externalStorageIdentification, permittedGroup);
    }

    @Checked("ResultPredicates.documentFileWritePredicate")
    public void setEdit(String displayName, FileResultPermittedGroupType fileResultPermittedGroupType) {
	super.setDisplayName(displayName);
	changeFilePermission(fileResultPermittedGroupType);
	this.getResult().setModifiedByAndDate();
    }

    public final void delete() {
	super.setResult(null);
	removeRootDomainObject();
	deleteDomainObject();
	deleteFile();
    }
    
    public final static Group getPermittedGroup(FileResultPermittedGroupType permissionType) {
	switch (permissionType) {
	case INSTITUTION:
	    return new RoleGroup(Role.getRoleByRoleType(RoleType.PERSON));
	    
	case PUBLIC:
	    return null;
	    
	case RESEARCHER:
	    return new RoleGroup(Role.getRoleByRoleType(RoleType.RESEARCHER));

	default:
	    return null;
	}
    }
    
    public final static ResultDocumentFile readByOID(Integer oid) {
	final ResultDocumentFile documentFile = (ResultDocumentFile) RootDomainObject.getInstance().readFileByOID(oid);
	
	if (documentFile==null) 
	    throw new DomainException("error.researcher.ResultDocumentFile.null");
	    
	return documentFile;
    }
    
    private void deleteFile() {
	final IFileManager fileManager = FileManagerFactory.getFileManager();
	fileManager.deleteFile(getExternalStorageIdentification());
    }

    private void checkParameters(Result result, String filename, String displayName, FileResultPermittedGroupType permittedGroupType) {
	if (result == null) 
	    throw new DomainException("error.researcher.ResultDocumentFile.result.null");
	if (filename==null||filename.equals(""))
	    throw new DomainException("error.researcher.ResultDocumentFile.filename.null");
	if (displayName==null||displayName.equals(""))
	    throw new DomainException("error.researcher.ResultDocumentFile.displayName.null");
	if (permittedGroupType == null) {
	    throw new DomainException("error.researcher.ResultDocumentFile.permittedGroupType.null");
	}
    }
    
    private void changeFilePermission(FileResultPermittedGroupType fileResultPermittedGroupType) {
	final Group group = getPermittedGroup(fileResultPermittedGroupType);
	super.setFileResultPermittedGroupType(fileResultPermittedGroupType);
	super.setPermittedGroup(group);
	FileManagerFactory.getFileManager().changeFilePermissions(getExternalStorageIdentification(), (group != null) ? true : false);
    }

    @Override
    public void setResult(Result result) {
	throw new DomainException("error.researcher.ResultDocumentFile.call","setResult");
    }

    @Override
    public void removeResult() {
	throw new DomainException("error.researcher.ResultDocumentFile.call","removeResult");
    }
    
    @Override
    public void setFileResultPermittedGroupType(FileResultPermittedGroupType fileResultPermittedGroupType) {
	throw new DomainException("error.researcher.ResultDocumentFile.call","setFileResultPermittedGroupType");
    }

    /**
     * This is not domain logic. Is used only to simplify the access to the url on presentation.
     */
    public String getDownloadUrl() {
	StringBuilder downloadUrl = new StringBuilder();
	downloadUrl.append(FileManagerFactory.getFileManager().getDirectDownloadUrlFormat());
	downloadUrl.append("/");
	downloadUrl.append(getExternalStorageIdentification());
	downloadUrl.append("/");
	downloadUrl.append(getFilename());
	return downloadUrl.toString();
    }
}
