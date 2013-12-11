package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ProfessionalRelation extends ProfessionalRelation_Base {

    public ProfessionalRelation(final String giafId, final MultiLanguageString name, final Boolean fullTimeEquivalent) {
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
        setFullTimeEquivalent(fullTimeEquivalent);
    }

    @Atomic
    public void edit(final MultiLanguageString name, final Boolean fullTimeEquivalent) {
        String[] args = {};
        if (name == null) {
            throw new DomainException("", args);
        }
        setName(name);
        setFullTimeEquivalent(fullTimeEquivalent);
    }
}
