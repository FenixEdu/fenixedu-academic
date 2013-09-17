package net.sourceforge.fenixedu.domain.library;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class LibraryDocument extends LibraryDocument_Base {

    public LibraryDocument() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    @Deprecated
    public boolean hasCardDocument() {
        return getCardDocument() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasLetterDocument() {
        return getLetterDocument() != null;
    }

}
