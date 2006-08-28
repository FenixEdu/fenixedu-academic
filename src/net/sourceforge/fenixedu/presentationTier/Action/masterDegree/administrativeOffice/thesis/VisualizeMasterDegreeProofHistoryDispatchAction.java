package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeProofVersion;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */

public class VisualizeMasterDegreeProofHistoryDispatchAction extends FenixDispatchAction {

    public ActionForward getStudentAndMasterDegreeProofVersion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);
        Integer masterDegreeProofVersionID = Integer.valueOf(request
                .getParameter("masterDegreeProofVersionID"));

        MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
        ActionErrors actionErrors = new ActionErrors();
        boolean isSuccess = operations.getStudentByNumberAndDegreeType(form, request, actionErrors);

        if (!isSuccess) {
            throw new NonExistingActionException("error.exception.masterDegree.nonExistentStudent",
                    mapping.findForward("error"));

        }

        InfoMasterDegreeProofVersion infoMasterDegreeProofVersion = null;

        /* * * get master degree proof history * * */
        Object argsMasterDegreeProofVersion[] = { masterDegreeProofVersionID };
        try {
            infoMasterDegreeProofVersion = (InfoMasterDegreeProofVersion) ServiceUtils.executeService(
                    userView, "ReadMasterDegreeProofVersionByID", argsMasterDegreeProofVersion);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(
                    "error.exception.masterDegree.nonExistingMasterDegreeProofDataToDisplay", mapping
                            .findForward("errorNonExistingProofVersion"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoMasterDegreeProofVersion.getInfoJuries().isEmpty() == false)
            request.setAttribute(SessionConstants.JURIES_LIST, infoMasterDegreeProofVersion
                    .getInfoJuries());

        if (infoMasterDegreeProofVersion.getInfoExternalJuries().isEmpty() == false) {
            request.setAttribute(SessionConstants.EXTERNAL_JURIES_LIST, infoMasterDegreeProofVersion
                    .getInfoExternalJuries());
        }
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String proofDate = null;
        String thesisDeliveryDate = null;

        if (infoMasterDegreeProofVersion.getProofDate() != null) {
            proofDate = simpleDateFormat.format(infoMasterDegreeProofVersion.getProofDate());
            request.setAttribute(SessionConstants.PROOF_DATE, proofDate);
        }

        if (infoMasterDegreeProofVersion.getThesisDeliveryDate() != null) {
            thesisDeliveryDate = simpleDateFormat.format(infoMasterDegreeProofVersion
                    .getThesisDeliveryDate());
            request.setAttribute(SessionConstants.THESIS_DELIVERY_DATE, thesisDeliveryDate);

        }

        Date lastModification = new Date(infoMasterDegreeProofVersion.getLastModification().getTime());
        simpleDateFormat.applyPattern("dd-MM-yyyy k:mm:ss");
        String formattedLastModification = simpleDateFormat.format(lastModification);

        request.setAttribute(SessionConstants.FINAL_RESULT, infoMasterDegreeProofVersion.getFinalResult().name());
        request.setAttribute(SessionConstants.ATTACHED_COPIES_NUMBER, infoMasterDegreeProofVersion
                .getAttachedCopiesNumber());

        request.setAttribute(SessionConstants.RESPONSIBLE_EMPLOYEE, infoMasterDegreeProofVersion
                .getInfoResponsibleEmployee());
        request.setAttribute(SessionConstants.LAST_MODIFICATION, formattedLastModification);

        return mapping.findForward("start");

    }

}