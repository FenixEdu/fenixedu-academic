package net.sourceforge.fenixedu.domain.contents;

import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class FunctionalityCall extends FunctionalityCall_Base {

    public FunctionalityCall(Functionality functionality) {
	super();
	setFunctionality(functionality);
    }

    @Override
    public MultiLanguageString getName() {
	return getFunctionality().getName();
    }

    @Override
    public String getPath() {
	return getFunctionality().getPath();
    }

    @Override
    protected void disconnect() {
	removeFunctionality();
	removeRootDomainObject();
    }
}
