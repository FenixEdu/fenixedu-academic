package net.sourceforge.fenixedu.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.util.ByteArray;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;

public class FileLocalContent extends FileLocalContent_Base {

    public FileLocalContent(File file, VirtualPath path, Collection<FileSetMetaData> metadata, byte[] content) {
        super();
        setFile(file);
        setPath(path);
        if (metadata != null) {
            for (FileSetMetaData entry : metadata) {
                new FileLocalContentMetadata(this, entry);
            }
        }
        setContent(new ByteArray(content));
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public Collection<FileSetMetaData> createMetadata() {
        Set<FileSetMetaData> metadata = new HashSet<FileSetMetaData>();
        for (FileLocalContentMetadata entry : super.getMetadataSet()) {
            metadata.add(entry.getEntry());
        }
        return metadata;
    }

    /**
     * @use {@link #createMetadata()})
     */
    @Deprecated
    @Override
    public Set<FileLocalContentMetadata> getMetadataSet() {
        return super.getMetadataSet();
    }

    public void delete() {
        setFile(null);
        for (FileLocalContentMetadata metadata : super.getMetadataSet()) {
            metadata.delete();
        }
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.FileLocalContentMetadata> getMetadata() {
        return getMetadataSet();
    }

    @Deprecated
    public boolean hasAnyMetadata() {
        return !getMetadataSet().isEmpty();
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasFile() {
        return getFile() != null;
    }

    @Deprecated
    public boolean hasContent() {
        return getContent() != null;
    }

    @Deprecated
    public boolean hasPath() {
        return getPath() != null;
    }

}
