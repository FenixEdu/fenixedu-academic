package net.sourceforge.fenixedu.domain.student.curriculum;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ExtraCurricularActivityType extends ExtraCurricularActivityType_Base {
    public ExtraCurricularActivityType(MultiLanguageString name) {
	setName(name);
	setRootDomainObject(RootDomainObject.getInstance());
    }
}
