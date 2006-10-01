package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Person.ExternalPersonBeanFactoryCreator;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ExternalPersonDA extends FenixDispatchAction {

    public ActionForward prepareCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	ExternalPersonBeanFactoryCreator externalPersonBean = (ExternalPersonBeanFactoryCreator) getRenderedObject();
	if (externalPersonBean == null) {
	    externalPersonBean = new ExternalPersonBeanFactoryCreator();
	}
	request.setAttribute("externalPersonBean", externalPersonBean);
	
    	return mapping.findForward("showCreateForm");
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {
	final Person person = (Person) executeFactoryMethod(request);
	request.setAttribute("person", person);
	RenderUtils.invalidateViewState();
    	return mapping.findForward("showCreatedPerson");
    }

}
