package net.sourceforge.fenixedu.domain;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;

public class UnitSiteBannerFile extends UnitSiteBannerFile_Base {

    public UnitSiteBannerFile() {
	super();
    }

    public UnitSiteBannerFile(VirtualPath path, String filename, String displayName, Collection<FileSetMetaData> metadata,
	    byte[] content, Group group) {
	this();
	init(path, filename, displayName, metadata, content, group);
    }

    public void delete() {
	removeRootDomainObject();
	removeBackgroundBanner();
	removeMainBanner();

	deleteDomainObject();
    }

}
