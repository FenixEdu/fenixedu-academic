package pt.ist.fenix.research.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.research.ResearcherApplication.CurriculumApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = CurriculumApp.class, path = "research", titleKey = "label.curriculum.research")
@Mapping(module = "researcher", path = "/showResearch")
@Forwards(value = { @Forward(name = "showResearch", path = "/researcher/showResearch.jsp") })
public class ShowResearchDispatchAction extends FenixAction {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("researchers", Authenticate.getUser().getUsername());
        super.execute(mapping, actionForm, request, response);
        return mapping.findForward("showResearch");
    }
}
