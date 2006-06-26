/*
 * Created on Jun 26, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.accounting.PaymentsManagementDTO;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class PaymentsManagementDispatchAction extends FenixDispatchAction {

    public ActionForward firstPage(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        return mapping.findForward("success");
    }

    protected PaymentsManagementDTO searchEventsForCandidacy(Integer candidacyNumber) {

        final Candidacy candidacy = Candidacy.readByCandidacyNumber(candidacyNumber);
        final PaymentsManagementDTO managementDTO = new PaymentsManagementDTO();
        for (final Event event : candidacy.getPerson().getEventsSet()) {
            if (!event.isClosed()) {
                managementDTO.getEntryDTOs().addAll(event.calculateEntries());
            }
        }
        return managementDTO;
    }

}
