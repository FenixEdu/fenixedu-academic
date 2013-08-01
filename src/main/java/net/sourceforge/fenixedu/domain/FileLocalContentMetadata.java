package net.sourceforge.fenixedu.domain;

import pt.utl.ist.fenix.tools.file.FileSetMetaData;

public class FileLocalContentMetadata extends FileLocalContentMetadata_Base {

    public FileLocalContentMetadata(FileLocalContent content, FileSetMetaData entry) {
        super();
        setContent(content);
        setEntry(entry);
    }

    public void delete() {
        setContent(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasEntry() {
        return getEntry() != null;
    }

    @Deprecated
    public boolean hasContent() {
        return getContent() != null;
    }

}
