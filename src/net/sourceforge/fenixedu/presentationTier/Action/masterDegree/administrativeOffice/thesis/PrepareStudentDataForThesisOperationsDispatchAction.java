package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * 
 * @author Shezad Anavarali (sana@mega.ist.utl.pt) -
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 * 
 */

public class PrepareStudentDataForThesisOperationsDispatchAction extends FenixDispatchAction {

    public ActionForward getStudentAndDegreeTypeForThesisOperations(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	request.setAttribute("registrations", Registration.readByNumberAndDegreeType((Integer) ((DynaActionForm) form)
		.get("studentNumber"), DegreeType.MASTER_DEGREE));
	return mapping.findForward("chooseSCP");
    }

    public ActionForward chooseSCP(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	StudentCurricularPlan studentCurricularPlan = rootDomainObject
		.readStudentCurricularPlanByOID((Integer) ((DynaActionForm) form).get("scpID"));

	request.setAttribute(SessionConstants.STUDENT, studentCurricularPlan.getRegistration());
	request.setAttribute("scpID", studentCurricularPlan.getIdInternal());
	request.setAttribute("studentCurricularPlan", studentCurricularPlan);

	return studentCurricularPlan.getMasterDegreeThesis() != null ? mapping.findForward("success") : mapping
		.findForward("createThesis");
    }
}