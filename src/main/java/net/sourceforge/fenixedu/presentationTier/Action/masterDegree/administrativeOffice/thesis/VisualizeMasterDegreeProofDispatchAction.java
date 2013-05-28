package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.MasterDegreeProofVersion;
import net.sourceforge.fenixedu.domain.MasterDegreeThesis;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
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

public class VisualizeMasterDegreeProofDispatchAction extends FenixDispatchAction {

    public ActionForward getStudentAndMasterDegreeProofVersion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final Integer scpID = Integer.valueOf(request.getParameter("scpID"));
        StudentCurricularPlan studentCurricularPlan = AbstractDomainObject.fromExternalId(scpID);

        new MasterDegreeThesisOperations().transportStudentCurricularPlan(form, request, new ActionErrors(),
                studentCurricularPlan);

        final List<MasterDegreeProofVersion> masterDegreeProofHistory =
                studentCurricularPlan.readNotActiveMasterDegreeProofVersions();
        if (!masterDegreeProofHistory.isEmpty()) {
            request.setAttribute(PresentationConstants.MASTER_DEGREE_PROOF_HISTORY, masterDegreeProofHistory);
        }

        final MasterDegreeThesis masterDegreeThesis = studentCurricularPlan.getMasterDegreeThesis();
        MasterDegreeProofVersion masterDegreeProofVersion = masterDegreeThesis.getActiveMasterDegreeProofVersion();

        if (masterDegreeProofVersion == null) {
            throw new NonExistingActionException("error.exception.masterDegree.nonExistingMasterDegreeProofDataToDisplay",
                    mapping.findForward("errorNonExistingProofVersion"));
        }

        if (masterDegreeProofVersion.getJuries().isEmpty() == false) {
            request.setAttribute(PresentationConstants.JURIES_LIST, masterDegreeProofVersion.getJuries());
        }

        if (masterDegreeProofVersion.getExternalJuries().isEmpty() == false) {
            request.setAttribute(PresentationConstants.EXTERNAL_JURIES_LIST, masterDegreeProofVersion.getExternalJuries());
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String formattedProofDate = null;
        Date proofDate = masterDegreeProofVersion.getProofDate();
        if (proofDate != null) {
            formattedProofDate = simpleDateFormat.format(proofDate);
        }

        String formattedThesisDeliveryDate = null;
        Date thesisDeliveryDate = masterDegreeProofVersion.getThesisDeliveryDate();
        if (thesisDeliveryDate != null) {
            formattedThesisDeliveryDate = simpleDateFormat.format(thesisDeliveryDate);
        }

        Date lastModification = new Date(masterDegreeProofVersion.getLastModification().getTime());
        simpleDateFormat.applyPattern("dd-MM-yyyy k:mm:ss");
        String formattedLastModification = simpleDateFormat.format(lastModification);

        request.setAttribute(PresentationConstants.DISSERTATION_TITLE, masterDegreeThesis
                .getActiveMasterDegreeThesisDataVersion().getDissertationTitle());
        request.setAttribute(PresentationConstants.FINAL_RESULT, masterDegreeProofVersion.getFinalResult().name());
        request.setAttribute(PresentationConstants.ATTACHED_COPIES_NUMBER, masterDegreeProofVersion.getAttachedCopiesNumber());
        request.setAttribute(PresentationConstants.PROOF_DATE, formattedProofDate);
        request.setAttribute(PresentationConstants.THESIS_DELIVERY_DATE, formattedThesisDeliveryDate);
        request.setAttribute(PresentationConstants.RESPONSIBLE_EMPLOYEE, masterDegreeProofVersion.getResponsibleEmployee());
        request.setAttribute(PresentationConstants.LAST_MODIFICATION, formattedLastModification);

        return mapping.findForward("start");

    }

}