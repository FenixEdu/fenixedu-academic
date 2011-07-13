package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.assiduousnessStructure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ExecuteFactoryMethod;
import net.sourceforge.fenixedu.dataTransferObject.directiveCouncil.assiduousnessStructure.AssiduousnessPersonFunctionFactory;
import net.sourceforge.fenixedu.dataTransferObject.directiveCouncil.assiduousnessStructure.AssiduousnessStructureSearch;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "directiveCouncil", path = "/assiduousnessStructure", input = "/assiduousnessStructure.do?method=showAssiduousnessStructure", attribute = "assiduousnessForm", formBean = "assiduousnessForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "create-assiduousness-person-function", path = "/directiveCouncil/assiduousnessStructure/createAssiduousnessPersonFunction.jsp"),
		@Forward(name = "edit-assiduousness-person-function", path = "/directiveCouncil/assiduousnessStructure/editAssiduousnessPersonFunction.jsp"),
		@Forward(name = "show-assiduousness-structure", path = "/directiveCouncil/assiduousnessStructure/showAssiduousnessStructure.jsp") })
public class AssiduousnessStructureDispatchAction extends FenixDispatchAction {

    public ActionForward showAssiduousnessStructure(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	AssiduousnessStructureSearch assiduousnessStructureSearch = getRenderedObject("assiduousnessStructureSearch");
	if (assiduousnessStructureSearch == null) {
	    assiduousnessStructureSearch = (AssiduousnessStructureSearch) request.getAttribute("assiduousnessStructureSearch");
	}
	if (assiduousnessStructureSearch == null) {
	    String personFunctionId = request.getParameter("personFunctionId");
	    if (personFunctionId != null) {
		PersonFunction personFunction = (PersonFunction) rootDomainObject.readAccountabilityByOID(new Integer(
			personFunctionId));
		assiduousnessStructureSearch = new AssiduousnessStructureSearch(personFunction);
	    } else {
		assiduousnessStructureSearch = new AssiduousnessStructureSearch();
	    }
	} else {
	    if (request.getParameter("addUnit") != null || request.getParameter("addPerson") != null
		    || request.getParameter("addPersons") != null) {
		AssiduousnessPersonFunctionFactory assiduousnessPersonFunctionFactory = new AssiduousnessPersonFunctionFactory(
			assiduousnessStructureSearch, request.getParameter("addPersons") != null);
		request.setAttribute("assiduousnessPersonFunctionFactory", assiduousnessPersonFunctionFactory);
		return mapping.findForward("create-assiduousness-person-function");
	    }
	    assiduousnessStructureSearch.setSearch();
	}
	request.setAttribute("assiduousnessStructureSearch", assiduousnessStructureSearch);
	return mapping.findForward("show-assiduousness-structure");
    }

    public ActionForward prepareEditPersonFunction(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	String idInternalString = request.getParameter("idInternal");
	PersonFunction personFunction = (PersonFunction) rootDomainObject.readAccountabilityByOID(new Integer(idInternalString));
	request.setAttribute("personFunction", personFunction);
	return mapping.findForward("edit-assiduousness-person-function");
    }

    public ActionForward prepareCreateAssiduousnessPersonFunction(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	AssiduousnessPersonFunctionFactory assiduousnessPersonFunctionFactory = getRenderedObject();
	request.setAttribute("assiduousnessPersonFunctionFactory", assiduousnessPersonFunctionFactory);
	return mapping.findForward("create-assiduousness-person-function");
    }

    public ActionForward createAssiduousnessPersonFunction(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	AssiduousnessPersonFunctionFactory assiduousnessPersonFunctionFactory = getRenderedObject();
	if (!isCancelled(request)) {
	    Object result = ExecuteFactoryMethod.run(assiduousnessPersonFunctionFactory);
	    if (result != null) {
		ActionMessages actionMessages = new ActionMessages();
		actionMessages.add("errorMessage", (ActionMessage) result);
		saveMessages(request, actionMessages);
		request.setAttribute("assiduousnessPersonFunctionFactory", assiduousnessPersonFunctionFactory);
		return mapping.findForward("create-assiduousness-person-function");
	    }
	}
	request
		.setAttribute("assiduousnessStructureSearch",
			new AssiduousnessStructureSearch(assiduousnessPersonFunctionFactory));
	return mapping.findForward("show-assiduousness-structure");
    }

    public ActionForward deletePersonFunction(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	String idInternalString = request.getParameter("idInternal");
	PersonFunction personFunction = (PersonFunction) rootDomainObject.readAccountabilityByOID(new Integer(idInternalString));
	Person person = personFunction.getPerson();
	personFunction.delete();

	AssiduousnessStructureSearch assiduousnessStructureSearch = new AssiduousnessStructureSearch();
	assiduousnessStructureSearch.setResponsible(person);
	request.setAttribute("assiduousnessStructureSearch", assiduousnessStructureSearch);
	return showAssiduousnessStructure(mapping, actionForm, request, response);
    }

}