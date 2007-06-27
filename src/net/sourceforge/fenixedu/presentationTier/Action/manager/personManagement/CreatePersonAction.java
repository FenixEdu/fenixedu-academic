package net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author Paulo Zenida and Dinis Martins
 * 
 * Date: 2006-02-22
 */
public class CreatePersonAction extends FenixDispatchAction {

	public ActionForward prepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws FenixFilterException, FenixServiceException {
		return mapping.findForward("Prepare");
	}

	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws FenixFilterException, FenixServiceException {

		final Person person = (Person) RenderUtils.getViewState().getMetaObject().getObject();

        final Object[] args = { person, RoleType.PERSON };
        ServiceUtils.executeService(getUserView(request), "AddPersonRole", args);

		// It simply returns Success - The object is created by the renderer
		// mechanism
		request.setAttribute("person", person);
		return mapping.findForward("Success");
	}

}