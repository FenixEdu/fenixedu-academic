package net.sourceforge.fenixedu.domain.research.result;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.IFileManager;

public class ResultDocumentFile extends ResultDocumentFile_Base {
    
    public enum ResultDocumentFilePermissionType {
	PUBLIC, RESEARCHER, INSTITUTION;
	
	public String getName() {
	    return name();
	}
	
	public static ResultDocumentFilePermissionType getDefaultType() {
	    return PUBLIC;
	}
    }
    
    public ResultDocumentFile() {
	super();
    }

    public ResultDocumentFile(Result result, String filename, String displayName, String mimeType,
	    String checksum, String checksumAlgorithm, Integer size,
	    String externalStorageIdentification, Group permittedGroup) {
	this();
	checkParameters(result, filename);
	super.setResult(result);
	initChecked(filename, displayName, mimeType, checksum, checksumAlgorithm, size,
		externalStorageIdentification, permittedGroup);
    }

    @Checked("ResultPredicates.documentFileWritePredicate")
    private void initChecked(String filename, String displayName, String mimeType, String checksum,
	    String checksumAlgorithm, Integer size, String externalStorageIdentification,
	    Group permittedGroup) {
	init(filename, displayName, mimeType, checksum, checksumAlgorithm, size,
		externalStorageIdentification, permittedGroup);
    }
    
    @Override
    public void setResult(Result result) {
	throw new DomainException("error.researcher.ResultDocumentFile.call","setResult");
    }

    @Override
    public void removeResult() {
	throw new DomainException("error.researcher.ResultDocumentFile.call","removeResult");
    }

    @Checked("ResultPredicates.documentFileWritePredicate")
    public void delete() {
	super.setResult(null);
	removeRootDomainObject();
	deleteDomainObject();
	deleteFile();
    }
    
    public static Group getPermittedGroup(ResultDocumentFilePermissionType permissionType) {
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
    
    public static ResultDocumentFile readByOID(Integer oid) {
	final ResultDocumentFile documentFile = (ResultDocumentFile) RootDomainObject.getInstance().readFileByOID(oid);
	
	if (documentFile==null) 
	    throw new DomainException("error.researcher.ResultDocumentFile.null");
	    
	return documentFile;
    }
    
    public String getDownloadUrl() {
	StringBuilder downloadUrl = new StringBuilder();
	downloadUrl.append(FileManagerFactory.getFileManager().getDirectDownloadUrlFormat());
	downloadUrl.append("/");
	downloadUrl.append(getExternalStorageIdentification());
	downloadUrl.append("/");
	downloadUrl.append(getFilename());
	return downloadUrl.toString();
    }
    
    private void deleteFile() {
	final IFileManager fileManager = FileManagerFactory.getFileManager();
	fileManager.deleteFile(getExternalStorageIdentification());
    }

    private void checkParameters(Result result, String filename) {
	if (result == null) 
	    throw new DomainException("error.researcher.ResultDocumentFile.result.null");
	if (filename==null||filename.equals(""))
	    throw new DomainException("error.researcher.ResultDocumentFile.filename.null");
    }
}
