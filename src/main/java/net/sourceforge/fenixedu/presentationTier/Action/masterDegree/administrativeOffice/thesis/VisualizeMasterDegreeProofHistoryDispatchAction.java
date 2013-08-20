package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.MasterDegreeProofVersion;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * 
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */

public class VisualizeMasterDegreeProofHistoryDispatchAction extends FenixDispatchAction {

    public ActionForward getStudentAndMasterDegreeProofVersion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        new MasterDegreeThesisOperations().getStudentByNumberAndDegreeType(form, request, new ActionErrors());

        String masterDegreeProofVersionID = request.getParameter("masterDegreeProofVersionID");
        MasterDegreeProofVersion masterDegreeProofVersion = AbstractDomainObject.fromExternalId(masterDegreeProofVersionID);

        if (masterDegreeProofVersion.getJuries().isEmpty() == false) {
            request.setAttribute(PresentationConstants.JURIES_LIST, masterDegreeProofVersion.getJuries());
        }

        if (masterDegreeProofVersion.getExternalJuries().isEmpty() == false) {
            request.setAttribute(PresentationConstants.EXTERNAL_JURIES_LIST, masterDegreeProofVersion.getExternalJuries());
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String proofDate = null;
        String thesisDeliveryDate = null;

        if (masterDegreeProofVersion.getProofDate() != null) {
            proofDate = simpleDateFormat.format(masterDegreeProofVersion.getProofDate());
            request.setAttribute(PresentationConstants.PROOF_DATE, proofDate);
        }

        if (masterDegreeProofVersion.getThesisDeliveryDate() != null) {
            thesisDeliveryDate = simpleDateFormat.format(masterDegreeProofVersion.getThesisDeliveryDate());
            request.setAttribute(PresentationConstants.THESIS_DELIVERY_DATE, thesisDeliveryDate);

        }

        Date lastModification = new Date(masterDegreeProofVersion.getLastModification().getTime());
        simpleDateFormat.applyPattern("dd-MM-yyyy k:mm:ss");
        String formattedLastModification = simpleDateFormat.format(lastModification);

        request.setAttribute(PresentationConstants.FINAL_RESULT, masterDegreeProofVersion.getFinalResult().name());
        request.setAttribute(PresentationConstants.ATTACHED_COPIES_NUMBER, masterDegreeProofVersion.getAttachedCopiesNumber());

        request.setAttribute(PresentationConstants.RESPONSIBLE_EMPLOYEE, masterDegreeProofVersion.getResponsibleEmployee());
        request.setAttribute(PresentationConstants.LAST_MODIFICATION, formattedLastModification);

        return mapping.findForward("start");

    }

}