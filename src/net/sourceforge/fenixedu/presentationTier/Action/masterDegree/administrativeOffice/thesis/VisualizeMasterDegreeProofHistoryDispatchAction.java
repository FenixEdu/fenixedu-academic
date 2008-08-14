package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.MasterDegreeProofVersion;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;

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

	new MasterDegreeThesisOperations().getStudentByNumberAndDegreeType(form, request, new ActionErrors());

	Integer masterDegreeProofVersionID = Integer.valueOf(request.getParameter("masterDegreeProofVersionID"));
	MasterDegreeProofVersion masterDegreeProofVersion = rootDomainObject
		.readMasterDegreeProofVersionByOID(masterDegreeProofVersionID);

	if (masterDegreeProofVersion.getJuries().isEmpty() == false)
	    request.setAttribute(SessionConstants.JURIES_LIST, masterDegreeProofVersion.getJuries());

	if (masterDegreeProofVersion.getExternalJuries().isEmpty() == false) {
	    request.setAttribute(SessionConstants.EXTERNAL_JURIES_LIST, masterDegreeProofVersion.getExternalJuries());
	}

	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

	String proofDate = null;
	String thesisDeliveryDate = null;

	if (masterDegreeProofVersion.getProofDate() != null) {
	    proofDate = simpleDateFormat.format(masterDegreeProofVersion.getProofDate());
	    request.setAttribute(SessionConstants.PROOF_DATE, proofDate);
	}

	if (masterDegreeProofVersion.getThesisDeliveryDate() != null) {
	    thesisDeliveryDate = simpleDateFormat.format(masterDegreeProofVersion.getThesisDeliveryDate());
	    request.setAttribute(SessionConstants.THESIS_DELIVERY_DATE, thesisDeliveryDate);

	}

	Date lastModification = new Date(masterDegreeProofVersion.getLastModification().getTime());
	simpleDateFormat.applyPattern("dd-MM-yyyy k:mm:ss");
	String formattedLastModification = simpleDateFormat.format(lastModification);

	request.setAttribute(SessionConstants.FINAL_RESULT, masterDegreeProofVersion.getFinalResult().name());
	request.setAttribute(SessionConstants.ATTACHED_COPIES_NUMBER, masterDegreeProofVersion.getAttachedCopiesNumber());

	request.setAttribute(SessionConstants.RESPONSIBLE_EMPLOYEE, masterDegreeProofVersion.getResponsibleEmployee());
	request.setAttribute(SessionConstants.LAST_MODIFICATION, formattedLastModification);

	return mapping.findForward("start");

    }

}