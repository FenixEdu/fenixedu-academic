package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext;

public class SimpleFunctionalityContext extends AbstractFunctionalityContext {

    public SimpleFunctionalityContext(HttpServletRequest request) {
	super(request);
    }

    @Override
    public Container getSelectedTopLevelContainer() {
	return null;
    }

    public String getCurrentContextPath() {
	return null;
    }

}
