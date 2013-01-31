package net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.AddPersonRole;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * 
 * @author Paulo Zenida and Dinis Martins
 * 
 *         Date: 2006-02-22
 */
@Mapping(
		module = "manager",
		path = "/personManagement/createPerson",
		input = "createPerson",
		scope = "request",
		parameter = "method")
@Forwards(value = {
		@Forward(name = "Prepare", path = "/manager/personManagement/createPerson.jsp", tileProperties = @Tile(
				title = "private.staffarea.interfacegiaf.interfacegiaf.createperson")),
		@Forward(name = "Success", path = "/manager/personManagement/createPersonSuccess.jsp", tileProperties = @Tile(
				title = "private.staffarea.interfacegiaf.interfacegiaf.createperson")) })
public class CreatePersonAction extends FenixDispatchAction {

	public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws FenixFilterException, FenixServiceException {
		return mapping.findForward("Prepare");
	}

	public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws FenixFilterException, FenixServiceException {

		final Person person = (Person) RenderUtils.getViewState().getMetaObject().getObject();

		AddPersonRole.run(person, RoleType.PERSON);

		// It simply returns Success - The object is created by the renderer
		// mechanism
		request.setAttribute("person", person);
		return mapping.findForward("Success");
	}

}