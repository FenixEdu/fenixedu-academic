package net.sourceforge.fenixedu.presentationTier.Action.research;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonNameBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.Prize;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PrizeManagement extends FenixDispatchAction {

	public ActionForward listPrizes(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute("prizes", getLoggedPerson(request).getPrizes());
		return mapping.findForward("listPrizes");
	}

	public ActionForward prepareDelete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		request.setAttribute("deleteRequest", "deleteRequest");
		return showPrize(mapping, form, request, response);
	}

	public ActionForward deletePrize(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		if (request.getParameter("confirm") == null) {
			return showPrize(mapping, form, request, response);
		}

		Prize prize = getPrize(request);
		if (prize.isDeletableByUser((getLoggedPerson(request)))) {
			try {
				executeService("DeletePrize", new Object[] { prize });
			} catch (DomainException e) {
				addActionMessage(request, e.getMessage());
			}
		}
		return listPrizes(mapping, form, request, response);
	}

	public ActionForward editPrize(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		Prize prize = getPrize(request);
		if (prize != null && prize.isEditableByUser(getLoggedPerson(request))) {
			request.setAttribute("prize", prize);
		}
		return mapping.findForward("editPrize");
	}

	public ActionForward prepareCreatePrize(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		request.setAttribute("bean", new PrizeBean(getLoggedPerson(request)));
		return mapping.findForward("createPrize");
	}

	public ActionForward createPrize(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		IViewState viewState = RenderUtils.getViewState("createPrize");
		if (viewState != null) {
			PrizeBean bean = (PrizeBean) viewState.getMetaObject().getObject();
			try {
				executeService("CreatePrize", new Object[] { bean.getName(), bean.getDescription(),
						bean.getYear(), bean.getPerson() });
			} catch (DomainException e) {
				addActionMessage(request, e.getMessage());
			}
		}
		return listPrizes(mapping, form, request, response);
	}

	public ActionForward showPrize(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		Prize prize = getPrize(request);
		request.setAttribute("prize", prize);
		return mapping.findForward("viewPrize");
	}

	public ActionForward personPostBack(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		ActionForward returnAction = associatePerson(mapping, form, request, response);
		RenderUtils.invalidateViewState("createAssociation");
		return returnAction;
	}

	public ActionForward associatePerson(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		Prize prize = getPrize(request);
		request.setAttribute("prize", prize);

		IViewState viewState = RenderUtils.getViewState("createAssociation");
		PersonNameBean bean = (viewState != null) ? (PersonNameBean) viewState.getMetaObject()
				.getObject() : new PersonNameBean();

		request.setAttribute("bean", bean);
		return mapping.findForward("personAssociation");
	}

	public ActionForward associatePersonToPrize(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		Prize prize = getPrize(request);
		IViewState viewState = RenderUtils.getViewState("createAssociation");
		if (viewState != null) {
			PersonNameBean bean = (PersonNameBean) viewState.getMetaObject().getObject();
			if (bean.isExternal() && bean.getPersonName() == null
					&& request.getParameter("create") == null) {
				request.setAttribute("prompt-creation", "true");
			} else {
				try {
					executeService("AddPartyToPrize", new Object[] { bean, prize });
					RenderUtils.invalidateViewState("createAssociation");
				} catch (DomainException e) {
					addActionMessage(request, e.getMessage());
				}
			}
		}

		return associatePerson(mapping, form, request, response);
	}

	public ActionForward removePersonFromPrize(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		Prize prize = getPrize(request);
		String personID = request.getParameter("pid");
		Person person = (Person) RootDomainObject.readDomainObjectByOID(Person.class, Integer
				.valueOf(personID));

		if (person != null && prize != null && !prize.isLastParticipation()) {
			try {
				executeService("RemovePartyFromPrize", new Object[] { person, prize });
			} catch (DomainException e) {
				addActionMessage(request, e.getMessage());
			}
		}

		return associatePerson(mapping, form, request, response);
	}

	public ActionForward unitPostBack(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		ActionForward returnAction = associateUnit(mapping, form, request, response);
		RenderUtils.invalidateViewState("createAssociation");
		return returnAction;
	}

	public ActionForward associateUnit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		Prize prize = getPrize(request);
		request.setAttribute("prize", prize);

		IViewState viewState = RenderUtils.getViewState("createAssociation");
		UnitNameBean bean = (viewState != null) ? (UnitNameBean) viewState.getMetaObject().getObject()
				: new UnitNameBean();

		request.setAttribute("bean", bean);

		return mapping.findForward("editUnits");
	}

	public ActionForward associateUnitToPrize(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		Prize prize = getPrize(request);
		IViewState viewState = RenderUtils.getViewState("createAssociation");
		if (viewState != null) {
			UnitNameBean bean = (UnitNameBean) viewState.getMetaObject().getObject();
			if (bean.isExternal() && bean.getUnitName() == null
					&& request.getParameter("create") == null) {
				request.setAttribute("prompt-creation", "true");
			} else {
				try {
					executeService("AddPartyToPrize", new Object[] { bean, prize });
					RenderUtils.invalidateViewState("createAssociation");
				} catch (DomainException e) {
					addActionMessage(request, e.getMessage());
				}
			}
		}

		return associateUnit(mapping, form, request, response);
	}

	public ActionForward removeUnitFromPrize(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		Prize prize = getPrize(request);
		String unitID = request.getParameter("uid");
		Unit unit = (Unit) RootDomainObject.readDomainObjectByOID(Unit.class, Integer.valueOf(unitID));

		if (unit != null && prize != null) {
			try {
				executeService("RemovePartyFromPrize", new Object[] { unit, prize });
			} catch (DomainException e) {
				addActionMessage(request, e.getMessage());
			}
		}

		return associateUnit(mapping, form, request, response);
	}

	private Prize getPrize(HttpServletRequest request) {
		String prizeID = request.getParameter("oid");
		Prize prize = (Prize) RootDomainObject.readDomainObjectByOID(Prize.class, Integer
				.valueOf(prizeID));
		return prize;
	}
	
}
