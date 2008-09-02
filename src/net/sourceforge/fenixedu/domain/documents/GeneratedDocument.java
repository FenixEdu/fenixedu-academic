package net.sourceforge.fenixedu.domain.documents;

import java.io.InputStream;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.IFileManager;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

/**
 * {@link GeneratedDocument}s are output files resulting of some process of the
 * application, all generated files are stored in dspace.
 * 
 * Generated documents have 3 core relations:
 * <ul>
 * <li>Addressee: Someone or something that is the interested party of the
 * document, like a person who receives some certificate document.</li>
 * <li>Operator: An employee who processes the document.</li>
 * <li>Source: A domain entity who feeds the information presented in the
 * document. This one may not make sense in all cases, it is only used when
 * needed.</li>
 * </ul>
 * 
 * @author Pedro Santos (pmrsa)
 */
public abstract class GeneratedDocument extends GeneratedDocument_Base {
    private static final String ROOT_DIR_DESCRIPTION = "Generated Documents";

    private static final String ROOT_DIR = "GeneratedDocuments";

    private static final String NO_ADDRESSEE_NODE = "NoAddressee";

    private static final String NO_ADDRESSEE_NODE_DESCRIPTION = "No Addressee";

    public GeneratedDocument() {
	super();
    }

    protected void init(GeneratedDocumentType type, Party addressee, Person operator, String filename, InputStream stream) {
	setType(type);
	setAddressee(addressee);
	setOperator(operator);
	IFileManager fileManager = FileManagerFactory.getFactoryInstance().getFileManager();
	FileDescriptor fileDescriptor = fileManager.saveFile(getVirtualPath(), filename, true, operator == null ? "script"
		: operator.getName(), getType().name(), stream);
	init(fileDescriptor.getFilename(), fileDescriptor.getFilename(), fileDescriptor.getMimeType(), fileDescriptor
		.getChecksum(), fileDescriptor.getChecksumAlgorithm(), fileDescriptor.getSize(), fileDescriptor.getUniqueId(),
		computePermittedGroup());
    }

    @Override
    public boolean isPersonAllowedToAccess(Person person) {
	if (person == null)
	    return false;
	if (person.equals(getOperator()))
	    return true;
	if (person.equals(getAddressee()))
	    return true;
	return super.isPersonAllowedToAccess(person);
    }

    protected VirtualPath getVirtualPath() {
	final VirtualPath filePath = new VirtualPath();
	filePath.addNode(new VirtualPathNode(ROOT_DIR, ROOT_DIR_DESCRIPTION));
	if (getAddressee() != null) {
	    filePath.addNode(new VirtualPathNode(getAddressee().getIdInternal().toString(), getAddressee().getName()));
	} else {
	    filePath.addNode(new VirtualPathNode(NO_ADDRESSEE_NODE, NO_ADDRESSEE_NODE_DESCRIPTION));
	}
	filePath.addNode(new VirtualPathNode(getType().name(), getType().name()));
	return filePath;
    }

    protected Group computePermittedGroup() {
	RoleGroup adminOffice = new RoleGroup(Role.getRoleByRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE));
	RoleGroup manager = new RoleGroup(Role.getRoleByRoleType(RoleType.MANAGER));
	return new GroupUnion(adminOffice, manager);
    }
}
