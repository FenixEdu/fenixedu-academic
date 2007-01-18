package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.Module;

public class SimpleFunctionalityContext extends AbstractFunctionalityContext {

    public SimpleFunctionalityContext(HttpServletRequest request) {
        super(request);
    }

    @Override
    public Module getSelectedModule() {
        return null;
    }

    @Override
    public Functionality getSelectedFunctionality() {
        return null;
    }
    
}
