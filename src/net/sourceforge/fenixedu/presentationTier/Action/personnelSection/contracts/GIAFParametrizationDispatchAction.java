package net.sourceforge.fenixedu.presentationTier.Action.personnelSection.contracts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/giafParametrization", module = "personnelSection")
@Forwards({
		@Forward(
				name = "show-contract-situations",
				path = "/personnelSection/contracts/showContractSituations.jsp",
				tileProperties = @Tile(title = "private.staffarea.interfacegiaf.situations")),
		@Forward(
				name = "show-professional-categories",
				path = "/personnelSection/contracts/showProfessionalCategories.jsp",
				tileProperties = @Tile(title = "private.staffarea.interfacegiaf.categories")),
		@Forward(
				name = "show-grantOwner-equivalences",
				path = "/personnelSection/contracts/showGrantOwnerEquivalences.jsp",
				tileProperties = @Tile(title = "private.staffarea.interfacegiaf.grantownerequivalences")),
		@Forward(
				name = "show-service-exemptions",
				path = "/personnelSection/contracts/showServiceExemptions.jsp",
				tileProperties = @Tile(title = "private.staffarea.interfacegiaf.serviceexemptions")),
		@Forward(name = "show-absences", path = "/personnelSection/contracts/showAbsences.jsp", tileProperties = @Tile(
				title = "private.staffarea.interfacegiaf.absences")) })
public class GIAFParametrizationDispatchAction extends FenixDispatchAction {

	public ActionForward showContractSituations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixServiceException, FenixFilterException {
		request.setAttribute("contractSituations", rootDomainObject.getContractSituationsSet());
		return mapping.findForward("show-contract-situations");
	}

	public ActionForward showProfessionalCategories(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixServiceException, FenixFilterException {
		request.setAttribute("professionalCategories", rootDomainObject.getProfessionalCategoriesSet());
		return mapping.findForward("show-professional-categories");
	}

	public ActionForward showGrantOwnerEquivalences(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixServiceException, FenixFilterException {
		request.setAttribute("grantOwnerEquivalences", rootDomainObject.getGrantOwnerEquivalencesSet());
		return mapping.findForward("show-grantOwner-equivalences");
	}

	public ActionForward showServiceExemptions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixServiceException, FenixFilterException {
		request.setAttribute("serviceExemptions", rootDomainObject.getServiceExemptionsSet());
		return mapping.findForward("show-service-exemptions");
	}

	public ActionForward showAbsences(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixServiceException, FenixFilterException {
		request.setAttribute("absences", rootDomainObject.getAbsencesSet());
		return mapping.findForward("show-absences");
	}

}