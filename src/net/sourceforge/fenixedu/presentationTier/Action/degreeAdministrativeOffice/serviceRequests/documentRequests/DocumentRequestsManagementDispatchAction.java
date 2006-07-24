package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.serviceRequests.documentRequests;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class DocumentRequestsManagementDispatchAction extends FenixDispatchAction {

	public ActionForward showOperations(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("showOperations");
	}

	public ActionForward viewNewRequests(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("viewNewRequests");
	}

	public ActionForward prepareSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("prepareSearch");
	}

	public ActionForward search(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) {

		DynaActionForm form = (DynaActionForm) actionForm;

		DocumentRequestType documentRequestType = (DocumentRequestType) getEnum(form,
				"documentRequestType", DocumentRequestType.class);

		AcademicServiceRequestSituationType requestSituationType = (AcademicServiceRequestSituationType) getEnum(
				form, "requestSituationType", AcademicServiceRequestSituationType.class);

		Boolean isUrgent = (Boolean) form.get("isUrgent");
		Student student = getStudent(form.getString("studentNumber"));

		request.setAttribute("documentRequestsResult", getAdministrativeOffice().searchDocumentsBy(
				documentRequestType, requestSituationType, isUrgent, student));

		return mapping.findForward("showDocumentRequests");
	}

	private Student getStudent(String studentNumberString) {
		try {
			Integer number = Integer.valueOf(studentNumberString);
			return Student.readStudentByNumberAndDegreeType(number, DegreeType.DEGREE);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public AdministrativeOffice getAdministrativeOffice() {
		return AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.DEGREE);
	}

	private Enum getEnum(DynaActionForm form, String name, Class type) {
		return (form.get(name) == null || form.getString(name).length() == 0) ? null : Enum.valueOf(
				type, form.getString(name));
	}

}
