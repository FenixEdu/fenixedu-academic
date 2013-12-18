package net.sourceforge.fenixedu.domain.library;

import org.fenixedu.bennu.core.domain.Bennu;

public class LibraryDocument extends LibraryDocument_Base {

    public LibraryDocument() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    @Deprecated
    public boolean hasCardDocument() {
        return getCardDocument() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasLetterDocument() {
        return getLetterDocument() != null;
    }

}
