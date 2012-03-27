package net.sourceforge.fenixedu.domain.protocols;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolFactory.FilePermissionType;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.InternalPersonGroup;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;
import pt.utl.ist.fenix.tools.util.FileUtils;

public class ProtocolFile extends ProtocolFile_Base {

    public ProtocolFile() {
	super();
    }

    public ProtocolFile(String filename, InputStream fileInputStream, Group group) {
	super();
	init(getFilePath(), filename, filename, null, getFileByteArray(fileInputStream), group);
    }

    public byte[] getFileByteArray(InputStream fileInputStream) {
	byte[] fileByteArray = null;
	if (fileInputStream != null) {
	    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    try {
		try {
		    FileUtils.copy(fileInputStream, byteArrayOutputStream);
		    byteArrayOutputStream.flush();
		    fileByteArray = byteArrayOutputStream.toByteArray();
		    byteArrayOutputStream.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    } finally {
		try {
		    fileInputStream.close();
		    byteArrayOutputStream.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
	return fileByteArray;
    }

    private VirtualPath getFilePath() {
	final VirtualPath filePath = new VirtualPath();
	filePath.addNode(new VirtualPathNode("ProtocolFiles", "Protocol Files"));
	filePath.addNode(new VirtualPathNode("ProtocolFiles" + getOID(), "ProtocolFiles" + getOID()));
	return filePath;
    }

    public String getFilePermissionType() {
	if (getPermittedGroup() instanceof InternalPersonGroup) {
	    return FilePermissionType.IST_PEOPLE.toString();
	} else {
	    return FilePermissionType.RESPONSIBLES_AND_SCIENTIFIC_COUNCIL.toString();
	}
    }

    public boolean isVisibleToUser() {
	return getPermittedGroup().isMember(AccessControl.getPerson());
    }

    @Override
    public void delete() {
	removeProtocol();
	removeRootDomainObject();
	super.delete();
	deleteDomainObject();
    }

    @Service
    public void changePermissions() {
	if (getPermittedGroup() instanceof InternalPersonGroup) {
	    setPermittedGroup(getProtocol().getGroup(FilePermissionType.RESPONSIBLES_AND_SCIENTIFIC_COUNCIL));
	} else {
	    setPermittedGroup(getProtocol().getGroup(FilePermissionType.IST_PEOPLE));
	}
    }
}
