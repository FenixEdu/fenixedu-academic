package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.List;

import net.sourceforge.fenixedu.domain.ManagementGroups;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;

import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class ClosedMonthFile extends ClosedMonthFile_Base {
    public ClosedMonthFile() {
	super();
    }

    public ClosedMonthFile(Partial closedMonth, String filename, byte[] content) {
	this();
	init(getFilePath(closedMonth), filename, filename, null, content, createPermittedGroup());
    }

    @Override
    public void delete() {
	removeClosedMonthDocument();
	super.delete();
    }

    private VirtualPath getFilePath(Partial closedMonth) {
	final VirtualPath filePath = new VirtualPath();
	filePath.addNode(new VirtualPathNode("ClosedMonthFiles", "ClosedMonth Files"));
	filePath.addNode(new VirtualPathNode("ClosedMonth" + getIdInternal(), closedMonth.get(DateTimeFieldType.year()) + "-"
		+ closedMonth.get(DateTimeFieldType.monthOfYear())));
	return filePath;
    }

    private Group createPermittedGroup() {
	List<ManagementGroups> managementGroups = RootDomainObject.getInstance().getManagementGroups();
	return managementGroups.iterator().next().getAssiduousnessManagers();
    }
}
