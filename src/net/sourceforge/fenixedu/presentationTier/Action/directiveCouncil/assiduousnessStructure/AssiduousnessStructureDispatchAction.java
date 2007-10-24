package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.assiduousnessStructure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.directiveCouncil.assiduousnessStructure.AssiduousnessPersonFunctionFactory;
import net.sourceforge.fenixedu.dataTransferObject.directiveCouncil.assiduousnessStructure.AssiduousnessStructureSearch;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class AssiduousnessStructureDispatchAction extends FenixDispatchAction {

    public ActionForward showAssiduousnessStructure(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	AssiduousnessStructureSearch assiduousnessStructureSearch = (AssiduousnessStructureSearch) getRenderedObject("assiduousnessStructureSearch");
	if (assiduousnessStructureSearch == null) {
	    String personFunctionId = request.getParameter("personFunctionId");
	    if (personFunctionId != null) {
		PersonFunction personFunction = (PersonFunction) rootDomainObject
			.readAccountabilityByOID(new Integer(personFunctionId));
		assiduousnessStructureSearch = new AssiduousnessStructureSearch(personFunction);
	    } else {
		assiduousnessStructureSearch = new AssiduousnessStructureSearch();
	    }
	} else {
	    if (request.getParameter("addUnit") != null || request.getParameter("addPerson") != null
		    || request.getParameter("addPersons") != null) {
		AssiduousnessPersonFunctionFactory assiduousnessPersonFunctionFactory = new AssiduousnessPersonFunctionFactory(
			assiduousnessStructureSearch, request.getParameter("addPersons") != null);
		request.setAttribute("assiduousnessPersonFunctionFactory",
			assiduousnessPersonFunctionFactory);
		return mapping.findForward("create-assiduousness-person-function");
	    }
	    assiduousnessStructureSearch.setSearch();
	}
	request.setAttribute("assiduousnessStructureSearch", assiduousnessStructureSearch);
	return mapping.findForward("show-assiduousness-structure");
    }

    public ActionForward prepareEditPersonFunction(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	String idInternalString = request.getParameter("idInternal");
	PersonFunction personFunction = (PersonFunction) rootDomainObject
		.readAccountabilityByOID(new Integer(idInternalString));
	request.setAttribute("personFunction", personFunction);
	return mapping.findForward("edit-assiduousness-person-function");
    }

    public ActionForward prepareCreateAssiduousnessPersonFunction(ActionMapping mapping,
	    ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	AssiduousnessPersonFunctionFactory assiduousnessPersonFunctionFactory = (AssiduousnessPersonFunctionFactory) getRenderedObject();
	request.setAttribute("assiduousnessPersonFunctionFactory", assiduousnessPersonFunctionFactory);
	return mapping.findForward("create-assiduousness-person-function");
    }

    public ActionForward createAssiduousnessPersonFunction(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	AssiduousnessPersonFunctionFactory assiduousnessPersonFunctionFactory = (AssiduousnessPersonFunctionFactory) getRenderedObject();
	if (!isCancelled(request)) {
	    Object result = executeService(request, "ExecuteFactoryMethod",
		    new Object[] { assiduousnessPersonFunctionFactory });
	    if (result != null) {
		ActionMessages actionMessages = new ActionMessages();
		actionMessages.add("errorMessage", (ActionMessage) result);
		saveMessages(request, actionMessages);
		request.setAttribute("assiduousnessPersonFunctionFactory",
			assiduousnessPersonFunctionFactory);
		return mapping.findForward("create-assiduousness-person-function");
	    }
	}
	request.setAttribute("assiduousnessStructureSearch", new AssiduousnessStructureSearch(
		assiduousnessPersonFunctionFactory));
	return mapping.findForward("show-assiduousness-structure");
    }
}