package net.sourceforge.fenixedu.domain.research.result;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;

public class ResearchResultDocumentFile extends ResearchResultDocumentFile_Base {

    public enum FileResultPermittedGroupType {
        PUBLIC, INSTITUTION, RESEARCHER;

        public static FileResultPermittedGroupType getDefaultType() {
            return PUBLIC;
        }
    }

    private ResearchResultDocumentFile() {
        super();
    }

    ResearchResultDocumentFile(final VirtualPath path, Collection<FileSetMetaData> metadata, byte[] content,
            ResearchResult result, String filename, String displayName, FileResultPermittedGroupType permittedGroupType,
            Group permittedGroup) {
        this();
        checkParameters(result, filename, displayName, permittedGroupType);
        super.setResult(result);
        super.setFileResultPermittedGroupType(permittedGroupType);
//	init(filename, displayName, mimeType, checksum, checksumAlgorithm, size, externalStorageIdentification, permittedGroup);
        init(path, filename, displayName, metadata, content, permittedGroup);
    }

    @Checked("ResultPredicates.documentFileWritePredicate")
    public void setEdit(String displayName, FileResultPermittedGroupType fileResultPermittedGroupType) {
        super.setDisplayName(displayName);
        changeFilePermission(fileResultPermittedGroupType);
        this.getResult().setModifiedByAndDate();
    }

    @Override
    public final void delete() {
        super.setResult(null);
        super.delete();
    }

    @Override
    protected Boolean deletItemOnDelete() {
        return Boolean.FALSE;
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

    public final static ResearchResultDocumentFile readByOID(String oid) {
        final ResearchResultDocumentFile documentFile = FenixFramework.getDomainObject(oid);

        if (documentFile == null) {
            throw new DomainException("error.researcher.ResultDocumentFile.null");
        }

        return documentFile;
    }

    private void checkParameters(ResearchResult result, String filename, String displayName,
            FileResultPermittedGroupType permittedGroupType) {
        if (result == null) {
            throw new DomainException("error.researcher.ResultDocumentFile.result.null");
        }
        if (filename == null || filename.equals("")) {
            throw new DomainException("error.researcher.ResultDocumentFile.filename.null");
        }
        if (displayName == null || displayName.equals("")) {
            throw new DomainException("error.researcher.ResultDocumentFile.displayName.null");
        }
        if (permittedGroupType == null) {
            throw new DomainException("error.researcher.ResultDocumentFile.permittedGroupType.null");
        }
    }

    private void changeFilePermission(FileResultPermittedGroupType fileResultPermittedGroupType) {
        final Group group = getPermittedGroup(fileResultPermittedGroupType);
        super.setFileResultPermittedGroupType(fileResultPermittedGroupType);
        super.setPermittedGroup(group);
        FileManagerFactory.getFactoryInstance().getContentFileManager()
                .changeFilePermissions(getExternalStorageIdentification(), (group != null) ? true : false);
    }

    public void moveFileToNewResearchResultType(ResearchResult result) {
        super.setResult(result);
    }

    @Override
    public void setResult(ResearchResult result) {
        throw new DomainException("error.researcher.ResultDocumentFile.call", "setResult");
    }

    @Override
    public void removeResult() {
        throw new DomainException("error.researcher.ResultDocumentFile.call", "removeResult");
    }

    @Deprecated
    public boolean hasResult() {
        return getResult() != null;
    }

    @Deprecated
    public boolean hasVisible() {
        return getVisible() != null;
    }

    @Deprecated
    public boolean hasFileResultPermittedGroupType() {
        return getFileResultPermittedGroupType() != null;
    }

}
