package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.GiafInterfaceBean;
import net.sourceforge.fenixedu.domain.ManagementGroups;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class GiafInterfaceFile extends GiafInterfaceFile_Base {

    public GiafInterfaceFile(GiafInterfaceBean giafInterfaceBean, GiafInterfaceDocument giafInterfaceDocument) {
	super();
	init(getFilePath(), giafInterfaceBean.getFilename(), giafInterfaceBean.getFilename(), null,
		giafInterfaceBean.getFileByteArray(), createPermittedGroup());
	setGiafInterfaceDocument(giafInterfaceDocument);
    }

    private VirtualPath getFilePath() {
	final VirtualPath filePath = new VirtualPath();
	filePath.addNode(new VirtualPathNode("GiafInterfaceFiles", "GiafInterface Files"));
	filePath.addNode(new VirtualPathNode("GiafInterface" + getOID(), "GiafInterface" + getOID()));
	return filePath;
    }

    private Group createPermittedGroup() {
	List<ManagementGroups> managementGroups = RootDomainObject.getInstance().getManagementGroups();
	return new GroupUnion(managementGroups.iterator().next().getAssiduousnessManagers(), managementGroups.iterator().next()
		.getAssiduousnessSectionStaff());
    }

    @Override
    public void delete() {
	removeGiafInterfaceDocument();
	super.delete();
    }
}
