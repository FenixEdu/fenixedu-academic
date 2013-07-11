package net.sourceforge.fenixedu.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
        for (FileLocalContentMetadata entry : super.getMetadata()) {
            metadata.add(entry.getEntry());
        }
        return metadata;
    }

    /**
     * @use {@link #createMetadata()})
     */
    @Override
    public List<FileLocalContentMetadata> getMetadata() {
        throw new UnsupportedOperationException();
    }

    /**
     * @use {@link #createMetadata()})
     */
    @Override
    public Set<FileLocalContentMetadata> getMetadataSet() {
        throw new UnsupportedOperationException();
    }

    /**
     * @use {@link #createMetadata()})
     */
    @Override
    public Iterator<FileLocalContentMetadata> getMetadataIterator() {
        throw new UnsupportedOperationException();
    }

    public void delete() {
        setFile(null);
        for (FileLocalContentMetadata metadata : super.getMetadataSet()) {
            metadata.delete();
        }
        setRootDomainObject(null);
        deleteDomainObject();
    }
}
