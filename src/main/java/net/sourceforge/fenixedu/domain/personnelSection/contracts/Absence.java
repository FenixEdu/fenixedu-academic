package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class Absence extends Absence_Base {

    public Absence(final String giafId, final MultiLanguageString name) {
        super();
        String[] args1 = {};
        if (giafId == null || giafId.isEmpty()) {
            throw new DomainException("", args1);
        }
        String[] args = {};
        if (name == null) {
            throw new DomainException("", args);
        }
        setRootDomainObject(RootDomainObject.getInstance());
        setGiafId(giafId);
        setName(name);
        setImportAbsence(false);
        setIsSabaticalOrEquivalent(false);
        setHasMandatoryCredits(true);
        setGiveCredits(false);
    }

    @Atomic
    public void edit(final MultiLanguageString name) {
        String[] args = {};
        if (name == null) {
            throw new DomainException("", args);
        }
        setName(name);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonAbsence> getPersonAbsences() {
        return getPersonAbsencesSet();
    }

    @Deprecated
    public boolean hasAnyPersonAbsences() {
        return !getPersonAbsencesSet().isEmpty();
    }

    @Deprecated
    public boolean hasIsSabaticalOrEquivalent() {
        return getIsSabaticalOrEquivalent() != null;
    }

    @Deprecated
    public boolean hasGiveCredits() {
        return getGiveCredits() != null;
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasHasMandatoryCredits() {
        return getHasMandatoryCredits() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasImportAbsence() {
        return getImportAbsence() != null;
    }

    @Deprecated
    public boolean hasGiafId() {
        return getGiafId() != null;
    }

}
