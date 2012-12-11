package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.contents.Attachment;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;

public class FileContent extends FileContent_Base {

    protected FileContent() {
	super();

	setAttachment(null);
    }

    protected FileContent(Attachment attachment) {
	super();

	setAttachment(attachment);
    }

    public FileContent(Attachment attachment, VirtualPath path, String filename, String displayName,
	    Collection<FileSetMetaData> metadata, byte[] content, Group group) {
	this(attachment);
	init(path, filename, displayName, metadata, content, group);
    }

    public FileContent(VirtualPath path, String filename, String displayName, Collection<FileSetMetaData> metadata,
	    byte[] content, Group group) {
	init(path, filename, displayName, metadata, content, group);
    }

    @Override
    public void delete() {
	Attachment attachment = getAttachment();
	if (attachment != null) {
	    attachment.delete();
	    setAttachment(null);
	}
	super.delete();
    }

    public static FileContent readByOID(Integer idInternal) {
	return (FileContent) RootDomainObject.getInstance().readFileByOID(idInternal);
    }

    public static List<FileContent> readAllFileItems() {
	List<FileContent> fileItems = new ArrayList<FileContent>();

	for (File file : RootDomainObject.getInstance().getFiles()) {
	    if (file instanceof FileContent) {
		fileItems.add((FileContent) file);
	    }
	}

	return fileItems;
    }

    public Site getSite() {
	return getAttachment().getSite();
    }

    private String processDisplayName(String name) {
	return name.replace('\\', '-').replace('/', '-');
    }

    @Override
    public void setDisplayName(String displayName) {
	super.setDisplayName(processDisplayName(displayName));
	final Attachment attachment = getAttachment();
	if (attachment != null) {
	    attachment.logEditFileToItem();
	}
    }

    public void logEditFile() {
	final Attachment attachment = getAttachment();
	if (attachment != null) {
	    attachment.logEditFile();
	}
    }

    public void logItemFilePermittedGroup() {
	final Attachment attachment = getAttachment();
	if (attachment != null) {
	    attachment.logItemFilePermittedGroup();
	}
    }
}