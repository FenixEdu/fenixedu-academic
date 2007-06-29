package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
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

public class VisualizeMasterDegreeThesisHistoryDispatchAction extends FenixDispatchAction {

    public ActionForward getStudentAndMasterDegreeThesisDataVersion(ActionMapping mapping,
	    ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

	Integer masterDegreeThesisDataVersionID = Integer.valueOf(request
		.getParameter("masterDegreeThesisDataVersionID"));

	new MasterDegreeThesisOperations().getStudentByNumberAndDegreeType(form, request,
		new ActionErrors());

	MasterDegreeThesisDataVersion masterDegreeThesisDataVersion = rootDomainObject
		.readMasterDegreeThesisDataVersionByOID(masterDegreeThesisDataVersionID);

	if (!masterDegreeThesisDataVersion.getGuiders().isEmpty())
	    request.setAttribute(SessionConstants.GUIDERS_LIST, masterDegreeThesisDataVersion
		    .getGuiders());

	if (!masterDegreeThesisDataVersion.getAssistentGuiders().isEmpty())
	    request.setAttribute(SessionConstants.ASSISTENT_GUIDERS_LIST, masterDegreeThesisDataVersion
		    .getAssistentGuiders());

	if (!masterDegreeThesisDataVersion.getExternalAssistentGuiders().isEmpty())
	    request.setAttribute(SessionConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST,
		    masterDegreeThesisDataVersion.getExternalAssistentGuiders());

	if (!masterDegreeThesisDataVersion.getExternalGuiders().isEmpty()) {
	    request.setAttribute(SessionConstants.EXTERNAL_GUIDERS_LIST, masterDegreeThesisDataVersion
		    .getExternalGuiders());
	}

	Date lastModification = new Date(masterDegreeThesisDataVersion.getLastModification().getTime());
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy k:mm:ss");
	String formattedLastModification = simpleDateFormat.format(lastModification);

	request.setAttribute(SessionConstants.RESPONSIBLE_EMPLOYEE, masterDegreeThesisDataVersion
		.getResponsibleEmployee());
	request.setAttribute(SessionConstants.LAST_MODIFICATION, formattedLastModification);
	request.setAttribute(SessionConstants.DISSERTATION_TITLE, masterDegreeThesisDataVersion
		.getDissertationTitle());

	return mapping.findForward("start");

    }

}