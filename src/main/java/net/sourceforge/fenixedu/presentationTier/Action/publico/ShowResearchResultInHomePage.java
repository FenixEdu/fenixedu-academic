package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.domain.homepage.Homepage;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;

public class ShowResearchResultInHomePage extends ShowResearchResult {

    @Override
    protected void putSiteOnRequest(HttpServletRequest request) {
        FunctionalityContext context = FunctionalityContext.getCurrentContext(request);
        Container container = null;
        if (context != null) {
            container = context.getSelectedContainer();
        }
        if (container instanceof Homepage) {
            request.setAttribute("homepage", container);
        }
    }

}
