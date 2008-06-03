package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ResearchUnitSite;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchContract;
import net.sourceforge.fenixedu.presentationTier.Action.publico.LoginRequestManagement;
import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResearchUnitSiteManagementDA extends CustomUnitSiteManagementDA {

	public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward("editResearchSite");
	}

	public ActionForward managePeople(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		ResearchContractBean bean = new ResearchContractBean();
		bean.setUnit(getSite(request).getUnit());
		request.setAttribute("bean", bean);
		return mapping.findForward("managePeople");
	}

	public ActionForward managePeoplePostBack(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		ResearchContractBean bean = (ResearchContractBean) RenderUtils.getViewState(
				"createPersonContract").getMetaObject().getObject();
		request.setAttribute("bean", bean);
		RenderUtils.invalidateViewState();
		return mapping.findForward("managePeople");
	}

	public ActionForward addPersonWrapper(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
	    	ResearchContractBean bean = (ResearchContractBean) RenderUtils.getViewState("createPersonContract").getMetaObject().getObject();
		return (request.getParameter("createPerson") != null || (bean.getPerson()!=null && StringUtils.isEmpty(bean.getPerson().getEmail()))) ? prepareAddNewPerson(mapping, actionForm,
				request, response) : addPerson(mapping, actionForm, request, response);
	}

	public ActionForward prepareAddNewPerson(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		IViewState viewState = RenderUtils.getViewState();
		if (viewState != null && getSite(request).hasManagers(getLoggedPerson(request))) {
			ResearchContractBean bean = (ResearchContractBean) viewState.getMetaObject().getObject();
			Person person = bean.getPerson(); 
			if(person != null) {
			    bean.setDocumentType(person.getIdDocumentType());
			    bean.setDocumentIDNumber(person.getDocumentIdNumber());
			}
			request.setAttribute("bean", bean);
			return mapping.findForward("externalPersonExtraInfo");
		}
		return managePeople(mapping, actionForm, request, response);
	}

	public ActionForward addNewPerson(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		IViewState viewState = RenderUtils.getViewState("extraInfo");
		if (viewState != null && getSite(request).hasManagers(getLoggedPerson(request))) {
			ResearchContractBean bean = (ResearchContractBean) viewState.getMetaObject().getObject();
			try {
				executeService("CreateResearchContract", new Object[] { bean, getLoggedPerson(request),
						LoginRequestManagement.getRequestURL(request) });
			} catch (FenixServiceException e) {
				addActionMessage(request, e.getMessage());
				return managePeople(mapping, actionForm, request, response);
			} catch (DomainException e) {
				addActionMessage(request, e.getMessage());
				return managePeople(mapping, actionForm, request, response);
			}
		}
		return managePeople(mapping, actionForm, request, response);
	}

	public ActionForward addPerson(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		IViewState viewState = RenderUtils.getViewState("createPersonContract");
		if (viewState != null && getSite(request).hasManagers(getLoggedPerson(request))) {
			ResearchContractBean bean = (ResearchContractBean) viewState.getMetaObject().getObject();
			if (bean.getPerson() == null) {
				return managePeoplePostBack(mapping, actionForm, request, response);
			}
			try {
				executeService("CreateResearchContract", new Object[] { bean, getLoggedPerson(request),
						LoginRequestManagement.getRequestURL(request) });
			} catch (DomainException e) {
				addActionMessage(request, e.getMessage());
				return managePeople(mapping, actionForm, request, response);
			}
			RenderUtils.invalidateViewState();
		}
		return managePeople(mapping, actionForm, request, response);
	}

	public ActionForward removePerson(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		String contractID = request.getParameter("cid");
		ResearchContract contract = (ResearchContract) RootDomainObject.readDomainObjectByOID(
				ResearchContract.class, Integer.valueOf(contractID));
		try {
			executeService("DeleteResearchContract", new Object[] { contract });
		} catch (DomainException e) {
			addActionMessage(request, e.getMessage());
		}

		return managePeople(mapping, actionForm, request, response);
	}

	public ActionForward editContract(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String contractID = request.getParameter("cid");

		if (contractID != null) {
			ResearchContract contract = (ResearchContract) RootDomainObject.readDomainObjectByOID(
					ResearchContract.class, Integer.valueOf(contractID));
			request.setAttribute("contract", contract);
			return mapping.findForward("editContract");
		}

		return managePeople(mapping, actionForm, request, response);
	}

	@Override
	protected ResearchUnitSite getSite(HttpServletRequest request) {
	    return (ResearchUnitSite) super.getSite(request);
	}

	@Override
	protected String getAuthorNameForFile(HttpServletRequest request) {
		return getSite(request).getUnit().getName();
	}

	@Override
	protected String getItemLocationForFile(HttpServletRequest request, Item item, Section section) {
		return null;
	}

}
