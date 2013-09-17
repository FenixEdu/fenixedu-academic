package net.sourceforge.fenixedu.domain;

public abstract class QueueJobWithFile extends QueueJobWithFile_Base {

    public QueueJobWithFile() {
        super();
    }

    @Deprecated
    public boolean hasFile() {
        return getFile() != null;
    }

    @Deprecated
    public boolean hasContentType() {
        return getContentType() != null;
    }

}
