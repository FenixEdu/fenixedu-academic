package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Collection;
import java.util.Collections;

import net.sourceforge.fenixedu.domain.accessControl.NoOneGroup;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class GenericApplicationFile extends GenericApplicationFile_Base {

    public GenericApplicationFile(final GenericApplication application, final String displayName, final String fileName,
            final byte[] content) {
        super();
        final Collection<FileSetMetaData> metadata = Collections.emptySet();
        init(getVirtualPath(application), fileName, displayName, metadata, content, new NoOneGroup());
        setGenericApplication(application);
    }

    protected VirtualPath getVirtualPath(final GenericApplication application) {
        final VirtualPath filePath = new VirtualPath();
        filePath.addNode(new VirtualPathNode("GenericApplication", "GenericApplication"));
        filePath.addNode(new VirtualPathNode("GenericApplication" + application.getExternalId(), application
                .getApplicationNumber()));
        return filePath;
    }

    @Atomic
    public void deleteFromApplication() {
        delete();
    }

    @Override
    protected void disconnect() {
        setGenericApplication(null);
        super.disconnect();
    }

    @Deprecated
    public boolean hasGenericApplication() {
        return getGenericApplication() != null;
    }

}
