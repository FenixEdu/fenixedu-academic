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

public class VisualizeMasterDegreeProofDispatchAction extends FenixDispatchAction {

    public ActionForward getStudentAndMasterDegreeProofVersion(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	final Integer scpID = Integer.valueOf(request.getParameter("scpID"));
	StudentCurricularPlan studentCurricularPlan = rootDomainObject
		.readStudentCurricularPlanByOID(scpID);

	new MasterDegreeThesisOperations().transportStudentCurricularPlan(form, request,
		new ActionErrors(), studentCurricularPlan);

	final List<MasterDegreeProofVersion> masterDegreeProofHistory = studentCurricularPlan
		.readNotActiveMasterDegreeProofVersions();
	if (!masterDegreeProofHistory.isEmpty()) {
	    request.setAttribute(SessionConstants.MASTER_DEGREE_PROOF_HISTORY, masterDegreeProofHistory);
	}

	final MasterDegreeThesis masterDegreeThesis = studentCurricularPlan.getMasterDegreeThesis();
	MasterDegreeProofVersion masterDegreeProofVersion = masterDegreeThesis
		.getActiveMasterDegreeProofVersion();

	if (masterDegreeProofVersion == null) {
	    throw new NonExistingActionException(
		    "error.exception.masterDegree.nonExistingMasterDegreeProofDataToDisplay", mapping
			    .findForward("errorNonExistingProofVersion"));
	}

	if (masterDegreeProofVersion.getJuries().isEmpty() == false)
	    request.setAttribute(SessionConstants.JURIES_LIST, masterDegreeProofVersion.getJuries());

	if (masterDegreeProofVersion.getExternalJuries().isEmpty() == false) {
	    request.setAttribute(SessionConstants.EXTERNAL_JURIES_LIST, masterDegreeProofVersion
		    .getExternalJuries());
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

	request.setAttribute(SessionConstants.DISSERTATION_TITLE, masterDegreeThesis
		.getActiveMasterDegreeThesisDataVersion().getDissertationTitle());
	request.setAttribute(SessionConstants.FINAL_RESULT, masterDegreeProofVersion.getFinalResult()
		.name());
	request.setAttribute(SessionConstants.ATTACHED_COPIES_NUMBER, masterDegreeProofVersion
		.getAttachedCopiesNumber());
	request.setAttribute(SessionConstants.PROOF_DATE, formattedProofDate);
	request.setAttribute(SessionConstants.THESIS_DELIVERY_DATE, formattedThesisDeliveryDate);
	request.setAttribute(SessionConstants.RESPONSIBLE_EMPLOYEE, masterDegreeProofVersion
		.getResponsibleEmployee());
	request.setAttribute(SessionConstants.LAST_MODIFICATION, formattedLastModification);

	return mapping.findForward("start");

    }

}