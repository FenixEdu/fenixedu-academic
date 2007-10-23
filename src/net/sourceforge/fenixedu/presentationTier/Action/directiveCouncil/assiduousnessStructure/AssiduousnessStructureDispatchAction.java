package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.assiduousnessStructure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.directiveCouncil.assiduousnessStructure.AssiduousnessPersonFunctionFactory;
import net.sourceforge.fenixedu.dataTransferObject.directiveCouncil.assiduousnessStructure.AssiduousnessStructureSearch;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

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
	    if ((assiduousnessStructureSearch.getSearchPerson() != null && !assiduousnessStructureSearch
		    .getSearchPerson())
		    && assiduousnessStructureSearch.getUnit() == null) {
		assiduousnessStructureSearch.setSearchPerson(null);
	    }
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
	AssiduousnessStructureSearch assiduousnessStructureSearch = (AssiduousnessStructureSearch) getRenderedObject("assiduousnessStructureSearch");
	AssiduousnessPersonFunctionFactory assiduousnessPersonFunctionFactory = new AssiduousnessPersonFunctionFactory(
		assiduousnessStructureSearch, false);
	request.setAttribute("assiduousnessPersonFunctionFactory", assiduousnessPersonFunctionFactory);
	return mapping.findForward("create-assiduousness-person-function");
    }

    public ActionForward createAssiduousnessPersonFunction(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	AssiduousnessPersonFunctionFactory assiduousnessPersonFunctionFactory = (AssiduousnessPersonFunctionFactory) getRenderedObject();
	if (!isCancelled(request)) {
	    executeService(request, "ExecuteFactoryMethod",
		    new Object[] { assiduousnessPersonFunctionFactory });
	}
	request.setAttribute("assiduousnessStructureSearch", new AssiduousnessStructureSearch(
		assiduousnessPersonFunctionFactory));
	return mapping.findForward("show-assiduousness-structure");
    }
}