package net.sourceforge.fenixedu.domain;

import pt.utl.ist.fenix.tools.file.FileSetMetaData;

public class FileLocalContentMetadata extends FileLocalContentMetadata_Base {

	public FileLocalContentMetadata(FileLocalContent content, FileSetMetaData entry) {
		super();
		setContent(content);
		setEntry(entry);
	}

	@Override
	protected RootDomainObject getRootDomainObject() {
		return getContent().getRootDomainObject();
	}

	public void delete() {
		removeContent();
		deleteDomainObject();
	}
}
