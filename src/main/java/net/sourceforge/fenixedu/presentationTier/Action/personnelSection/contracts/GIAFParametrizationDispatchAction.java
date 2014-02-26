package net.sourceforge.fenixedu.presentationTier.Action.personnelSection.contracts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.personnelSection.PersonnelSectionApplication;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = PersonnelSectionApplication.class, path = "giaf-interface", titleKey = "title.giaf.interface",
        accessGroup = "#managers")
@Mapping(path = "/giafParametrization", module = "personnelSection")
@Forwards({ @Forward(name = "show-menu", path = "/personnelSection/contracts/showGiafMenu.jsp"),
        @Forward(name = "show-contract-situations", path = "/personnelSection/contracts/showContractSituations.jsp"),
        @Forward(name = "show-professional-categories", path = "/personnelSection/contracts/showProfessionalCategories.jsp"),
        @Forward(name = "show-grantOwner-equivalences", path = "/personnelSection/contracts/showGrantOwnerEquivalences.jsp"),
        @Forward(name = "show-service-exemptions", path = "/personnelSection/contracts/showServiceExemptions.jsp"),
        @Forward(name = "show-absences", path = "/personnelSection/contracts/showAbsences.jsp") })
public class GIAFParametrizationDispatchAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward showMenu(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("show-menu");
    }

    public ActionForward showContractSituations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        request.setAttribute("contractSituations", rootDomainObject.getContractSituationsSet());
        return mapping.findForward("show-contract-situations");
    }

    public ActionForward showProfessionalCategories(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        request.setAttribute("professionalCategories", rootDomainObject.getProfessionalCategoriesSet());
        return mapping.findForward("show-professional-categories");
    }

    public ActionForward showGrantOwnerEquivalences(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        request.setAttribute("grantOwnerEquivalences", rootDomainObject.getGrantOwnerEquivalencesSet());
        return mapping.findForward("show-grantOwner-equivalences");
    }

    public ActionForward showServiceExemptions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        request.setAttribute("serviceExemptions", rootDomainObject.getServiceExemptionsSet());
        return mapping.findForward("show-service-exemptions");
    }

    public ActionForward showAbsences(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        request.setAttribute("absences", rootDomainObject.getAbsencesSet());
        return mapping.findForward("show-absences");
    }

}