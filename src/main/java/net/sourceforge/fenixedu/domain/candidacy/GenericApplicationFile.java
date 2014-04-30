package net.sourceforge.fenixedu.domain.candidacy;

import org.fenixedu.bennu.core.groups.NobodyGroup;

import pt.ist.fenixframework.Atomic;

public class GenericApplicationFile extends GenericApplicationFile_Base {

    public GenericApplicationFile(final GenericApplication application, final String displayName, final String fileName,
            final byte[] content) {
        super();
        init(fileName, displayName, content, NobodyGroup.get());
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
