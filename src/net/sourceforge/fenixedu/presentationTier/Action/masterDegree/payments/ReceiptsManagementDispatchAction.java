/*
 * Created on Jun 26, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.payments;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.PaymentsManagementDTO;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ReceiptsManagementDispatchAction extends PaymentsManagementDispatchAction {

    public ActionForward doPayment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {

        final List<EntryDTO> paymentEntryDTOs = getSelectedEntryDTOs(request);
        final Person selectedPerson = getPerson(request);

        try {
            ServiceUtils.executeService(getUserView(request), "CreatePaymentsForEvents", new Object[] {
                    selectedPerson, getUserView(request).getPerson().getUser(), paymentEntryDTOs });
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey(), ex.getArgs());
            final PaymentsManagementDTO paymentsManagementDTO = (PaymentsManagementDTO) RenderUtils
                    .getViewState("paymentsManagementDTO").getMetaObject().getObject();
            request.setAttribute("paymentsManagementDTO", paymentsManagementDTO);

            return mapping.findForward("showEvents");
        }

        return mapping.findForward("printReceipt");
    }

    @SuppressWarnings("unchecked")
    private List<EntryDTO> getSelectedEntryDTOs(HttpServletRequest request) {

        List<EntryDTO> entryDTOs = (List<EntryDTO>) RenderUtils.getViewState("payment-entries")
                .getMetaObject().getObject();

        final List<EntryDTO> selectedEntryDTOs = new ArrayList<EntryDTO>();
        for (final EntryDTO entryDTO : entryDTOs) {
            if (entryDTO.isSelected()) {
                selectedEntryDTOs.add(entryDTO);
            }
        }

        return selectedEntryDTOs;
    }

    private Person getPerson(HttpServletRequest request) {
        return (Person) rootDomainObject
                .readPartyByOID(getRequestParameterAsInteger(request, "personId"));
    }

}
