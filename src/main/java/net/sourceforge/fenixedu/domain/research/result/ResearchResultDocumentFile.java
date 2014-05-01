package net.sourceforge.fenixedu.domain.research.result;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.predicates.ResultPredicates;
import pt.ist.fenixframework.FenixFramework;

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

    ResearchResultDocumentFile(byte[] content, ResearchResult result, String filename, String displayName,
            FileResultPermittedGroupType permittedGroupType) {
        this();
        checkParameters(result, filename, displayName, permittedGroupType);
        super.setResult(result);
        init(filename, displayName, content, getPermittedGroup(permittedGroupType));
        super.setFileResultPermittedGroupType(permittedGroupType);
    }

    public void setEdit(String displayName, FileResultPermittedGroupType fileResultPermittedGroupType) {
        check(this, ResultPredicates.documentFileWritePredicate);
        super.setDisplayName(displayName);
        changeFilePermission(fileResultPermittedGroupType);
        this.getResult().setModifiedByAndDate();
    }

    @Override
    public final void delete() {
        super.setResult(null);
        super.delete();
    }

    public final static Group getPermittedGroup(FileResultPermittedGroupType permissionType) {
        switch (permissionType) {
        case INSTITUTION:
            return new RoleGroup(Role.getRoleByRoleType(RoleType.PERSON));

        case PUBLIC:
            return new EveryoneGroup();

        case RESEARCHER:
            return new RoleGroup(Role.getRoleByRoleType(RoleType.RESEARCHER));

        default:
            return new EveryoneGroup();
        }
    }

    @Override
    public void setFileResultPermittedGroupType(FileResultPermittedGroupType fileResultPermittedGroupType) {
        super.setFileResultPermittedGroupType(fileResultPermittedGroupType);
        super.setPermittedGroup(getPermittedGroup(fileResultPermittedGroupType));

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
        setFileResultPermittedGroupType(fileResultPermittedGroupType);
    }

    public void moveFileToNewResearchResultType(ResearchResult result) {
        super.setResult(result);
    }

    @Override
    public void setResult(ResearchResult result) {
        throw new DomainException("error.researcher.ResultDocumentFile.call", "setResult");
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
