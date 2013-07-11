package net.sourceforge.fenixedu.domain.student.curriculum;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ExtraCurricularActivityType extends ExtraCurricularActivityType_Base {
    public ExtraCurricularActivityType() {
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public ExtraCurricularActivityType(MultiLanguageString name) {
        setName(name);
        setRootDomainObject(RootDomainObject.getInstance());
    }

    @Service
    public void delete() {
        if (hasAnyExtraCurricularActivity()) {
            throw new DomainException("error.extraCurricularActivityTypes.unableToDeleteUsedType", this.getName().getContent());
        }
        removeRootDomainObject();
        super.deleteDomainObject();
    }

    public String getNamePt() {
        return getName().getContent(Language.pt);
    }

    public void setNamePt(String name) {
        if (getName() == null) {
            setName(new MultiLanguageString(Language.pt, name));
        } else {
            setName(getName().with(Language.pt, name));
        }
    }

    public String getNameEn() {
        return getName().getContent(Language.en);
    }

    public void setNameEn(String name) {
        if (getName() == null) {
            setName(new MultiLanguageString(Language.en, name));
        } else {
            setName(getName().with(Language.en, name));
        }
    }
}
