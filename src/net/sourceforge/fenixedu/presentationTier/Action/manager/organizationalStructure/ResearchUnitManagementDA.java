package net.sourceforge.fenixedu.presentationTier.Action.manager.organizationalStructure;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonNameBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResearchUnitManagementDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	PartyType partyType = PartyType.readPartyTypeByType(PartyTypeEnum.RESEARCH_UNIT);
	request.setAttribute("resultUnits", new ArrayList<Party>(partyType.getParties()));
	return mapping.findForward("showResearchUnits");

    }

    public ActionForward createSite(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	String unitId = request.getParameter("unitID");
	ResearchUnit unit = (ResearchUnit) RootDomainObject.readDomainObjectByOID(ResearchUnit.class, Integer
		.valueOf(unitId));

	try {
	    executeService("CreateResearchUnitSite", new Object[] { unit });
	} catch (DomainException e) {
	    addActionMessage("error", request, e.getKey(), e.getArgs());
	}
	return prepare(mapping, actionForm, request, response);
    }

    public ActionForward editManagers(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	String unitId = request.getParameter("unitID");
	ResearchUnit unit = (ResearchUnit) RootDomainObject.readDomainObjectByOID(ResearchUnit.class, Integer
		.valueOf(unitId));
	PersonNameBean bean = new PersonNameBean();

	request.setAttribute("personBean", bean);
	request.setAttribute("unit", unit);
	return mapping.findForward("editResearchUnitSiteManagers");
    }

    public ActionForward addManager(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	IViewState viewState = RenderUtils.getViewState("addManager");
	if (viewState != null) {
		PersonNameBean bean = (PersonNameBean) viewState.getMetaObject().getObject();
	    String unitId = request.getParameter("unitID");
	    ResearchUnit unit = (ResearchUnit) RootDomainObject.readDomainObjectByOID(ResearchUnit.class,
		    Integer.valueOf(unitId));
	    RenderUtils.invalidateViewState("addManager");

	    try {
		executeService("AddResearchUnitSiteManager", new Object[] { unit.getSite(), bean.getPerson() });
	    } catch (DomainException e) {
		addActionMessage("error", request, e.getKey(), e.getArgs());
	    }

	}
	return editManagers(mapping, actionForm, request, response);
    }

    public ActionForward removeManager(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	Integer personId = Integer.valueOf(request.getParameter("personID"));
	Integer unitId = Integer.valueOf(request.getParameter("unitID"));

	Person person = (Person) RootDomainObject.readDomainObjectByOID(Person.class, personId);
	Unit unit = (Unit) RootDomainObject.readDomainObjectByOID(Unit.class, unitId);

	try {
	    executeService("RemoveResearchUnitSiteManager", new Object[] { unit.getSite(), person });
	} catch (DomainException e) {
	    addActionMessage("error", request, e.getKey(), e.getArgs());
	}

	return editManagers(mapping, actionForm, request, response);
    }

}
