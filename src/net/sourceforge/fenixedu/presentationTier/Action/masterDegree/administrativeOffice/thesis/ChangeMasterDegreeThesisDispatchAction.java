package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * 
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */

public class ChangeMasterDegreeThesisDispatchAction extends FenixDispatchAction {

	public ActionForward getStudentAndMasterDegreeThesisDataVersion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		final Integer scpID = Integer.valueOf(request.getParameter("scpID"));
		StudentCurricularPlan studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(scpID);

		new MasterDegreeThesisOperations().transportStudentCurricularPlan(form, request, new ActionErrors(),
				studentCurricularPlan);

		MasterDegreeThesisDataVersion thesisDataVersion =
				studentCurricularPlan.getMasterDegreeThesis().getActiveMasterDegreeThesisDataVersion();

		if (!thesisDataVersion.getGuiders().isEmpty()) {
			request.setAttribute(PresentationConstants.GUIDERS_LIST, thesisDataVersion.getGuiders());
		}

		if (!thesisDataVersion.getAssistentGuiders().isEmpty()) {
			request.setAttribute(PresentationConstants.ASSISTENT_GUIDERS_LIST, thesisDataVersion.getAssistentGuiders());
		}

		if (!thesisDataVersion.getExternalAssistentGuiders().isEmpty()) {
			request.setAttribute(PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST,
					thesisDataVersion.getExternalAssistentGuiders());
		}

		if (!thesisDataVersion.getExternalGuiders().isEmpty()) {
			request.setAttribute(PresentationConstants.EXTERNAL_GUIDERS_LIST, thesisDataVersion.getExternalGuiders());
		}

		DynaActionForm changeMasterDegreeThesisForm = (DynaActionForm) form;
		changeMasterDegreeThesisForm.set("dissertationTitle", thesisDataVersion.getDissertationTitle());

		return mapping.findForward("start");

	}

	public ActionForward reloadForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try {
			operations.getTeachersByNumbers(form, request, "guidersNumbers", PresentationConstants.GUIDERS_LIST, actionErrors);
			operations.getTeachersByNumbers(form, request, "assistentGuidersNumbers",
					PresentationConstants.ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalAssistentGuidersIDs",
					PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalGuidersIDs", PresentationConstants.EXTERNAL_GUIDERS_LIST,
					actionErrors);

		} catch (Exception e1) {
			throw new FenixActionException(e1);
		}

		return mapping.findForward("start");

	}

}