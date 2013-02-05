package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ProfessionalContractType extends ProfessionalContractType_Base {

    public ProfessionalContractType(final String giafId, final MultiLanguageString name) {
        super();
        check(giafId, "");
        check(name, "");
        setRootDomainObject(RootDomainObject.getInstance());
        setGiafId(giafId);
        setName(name);
    }

    @Service
    public void edit(final MultiLanguageString name) {
        check(name, "");
        setName(name);
    }
}
