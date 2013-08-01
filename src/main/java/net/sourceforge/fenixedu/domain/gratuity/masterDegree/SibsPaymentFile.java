package net.sourceforge.fenixedu.domain.gratuity.masterDegree;

import pt.ist.bennu.core.domain.Bennu;

public class SibsPaymentFile extends SibsPaymentFile_Base {

    public SibsPaymentFile() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public SibsPaymentFile(String filename) {
        this();
        setFilename(filename);
    }

    public static SibsPaymentFile readByFilename(String filename) {
        for (SibsPaymentFile sibsPaymentFile : Bennu.getInstance().getSibsPaymentFilesSet()) {
            if (sibsPaymentFile.getFilename().equals(filename)) {
                return sibsPaymentFile;
            }
        }
        return null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFileEntry> getSibsPaymentFileEntries() {
        return getSibsPaymentFileEntriesSet();
    }

    @Deprecated
    public boolean hasAnySibsPaymentFileEntries() {
        return !getSibsPaymentFileEntriesSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasFilename() {
        return getFilename() != null;
    }

}
