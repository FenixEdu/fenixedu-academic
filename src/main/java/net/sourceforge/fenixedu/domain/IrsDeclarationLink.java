package net.sourceforge.fenixedu.domain;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class IrsDeclarationLink extends IrsDeclarationLink_Base {

    public IrsDeclarationLink() {
        super();
        final Bennu rootDomainObject = Bennu.getInstance();
        if (rootDomainObject.getIrsDeclarationLink() == null) {
            setRootDomainObject(rootDomainObject);
        } else {
            throw new Error("there.can.only.be.one!");
        }
        setAvailable(Boolean.FALSE);
        setTitle(new MultiLanguageString(Language.pt, "IRS"));
        setIrsLink("");
    }

    @Atomic
    public static IrsDeclarationLink getInstance() {
        final Bennu rootDomainObject = Bennu.getInstance();
        final IrsDeclarationLink irsDeclarationLink = rootDomainObject.getIrsDeclarationLink();
        return irsDeclarationLink == null ? new IrsDeclarationLink() : irsDeclarationLink;
    }

    @Atomic
    public static void set(MultiLanguageString title, Boolean available, String irsLink) {
        final Bennu rootDomainObject = Bennu.getInstance();
        final IrsDeclarationLink irsDeclarationLink = rootDomainObject.getIrsDeclarationLink();
        irsDeclarationLink.setTitle(title);
        irsDeclarationLink.setAvailable(available);
        irsDeclarationLink.setIrsLink(irsLink);
    }

    @Deprecated
    public boolean hasAvailable() {
        return getAvailable() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasIrsLink() {
        return getIrsLink() != null;
    }

    @Deprecated
    public boolean hasTitle() {
        return getTitle() != null;
    }

}
