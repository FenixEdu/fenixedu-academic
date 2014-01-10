package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ProfessionalContractType extends ProfessionalContractType_Base {

    public ProfessionalContractType(final String giafId, final MultiLanguageString name) {
        super();
        String[] args1 = {};
        if (giafId == null || giafId.isEmpty()) {
            throw new DomainException("", args1);
        }
        String[] args = {};
        if (name == null) {
            throw new DomainException("", args);
        }
        setRootDomainObject(Bennu.getInstance());
        setGiafId(giafId);
        setName(name);
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
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData> getGiafProfessionalData() {
        return getGiafProfessionalDataSet();
    }

    @Deprecated
    public boolean hasAnyGiafProfessionalData() {
        return !getGiafProfessionalDataSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasGiafId() {
        return getGiafId() != null;
    }

}
