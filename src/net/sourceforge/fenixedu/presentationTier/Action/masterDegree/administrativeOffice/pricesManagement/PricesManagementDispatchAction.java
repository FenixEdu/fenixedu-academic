/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.pricesManagement;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.postingRules.serviceRequests.CertificateRequestPRDTO;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests.CertificateRequestPR;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PricesManagementDispatchAction extends FenixDispatchAction {

    public ActionForward viewPrices(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        
        final SortedSet<PostingRule> sortedPostingRules = new TreeSet<PostingRule>(PostingRule.COMPARATOR_BY_EVENT_TYPE);
        sortedPostingRules.addAll(getUserView(request).getPerson().getEmployee()
                .getCurrentWorkingPlace().getServiceAgreementTemplate().getActivePostingRules());
        
        request.setAttribute("postingRules", sortedPostingRules);
        

        return mapping.findForward("viewPrices");
    }

    public ActionForward prepareEditPrice(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("postingRuleDTO", new CertificateRequestPRDTO(
                (CertificateRequestPR) rootDomainObject
                        .readPostingRuleByOID(getRequestParameterAsInteger(request, "postingRuleId"))));

        return mapping.findForward("editPrice");
    }

    public ActionForward editPrice(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        final CertificateRequestPRDTO certificateRequestPRDTO = (CertificateRequestPRDTO) RenderUtils
                .getViewState().getMetaObject().getObject();
        try {
            ServiceManagerServiceFactory.executeService(getUserView(request), "EditCertificateRequestPR",
                    new Object[] { certificateRequestPRDTO });
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());

            request.setAttribute("postingRuleDTO", certificateRequestPRDTO);

            return prepareEditPrice(mapping, form, request, response);
        }

        return viewPrices(mapping, form, request, response);
    }

}
