package net.sourceforge.fenixedu.domain.candidacy;

import net.sourceforge.fenixedu.domain.accessControl.NoOneGroup;
import pt.ist.fenixframework.Atomic;

public class GenericApplicationFile extends GenericApplicationFile_Base {

    public GenericApplicationFile(final GenericApplication application, final String displayName, final String fileName,
            final byte[] content) {
        super();
        init(fileName, displayName, content, new NoOneGroup());
        setGenericApplication(application);
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
