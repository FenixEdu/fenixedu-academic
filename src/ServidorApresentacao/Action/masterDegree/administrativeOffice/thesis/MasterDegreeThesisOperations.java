package ServidorApresentacao.Action.masterDegree.administrativeOffice.thesis;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoExternalPerson;
import DataBeans.InfoStudent;
import DataBeans.InfoTeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * 
 * @author :
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *
 */

public class MasterDegreeThesisOperations extends DispatchAction {

	public boolean getStudentByNumberAndDegreeType(ActionForm form, HttpServletRequest request, ActionErrors actionErrors)
		throws FenixActionException {

		boolean result = false;

		DynaActionForm getStudentByNumberAndDegreeTypeForm = (DynaActionForm) form;
		IUserView userView = SessionUtils.getUserView(request);

		Integer degreeType = Integer.valueOf(this.getFromRequest("degreeType", request));
		Integer studentNumber = Integer.valueOf(this.getFromRequest("studentNumber", request));

		if ((degreeType == null) || (studentNumber == null)) {

			try {
				degreeType = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("degreeType"));
				studentNumber = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("studentNumber"));
			} catch (NumberFormatException e) {
				degreeType = (Integer) request.getAttribute("degreeType");
				studentNumber = (Integer) request.getAttribute("studentNumber");
				getStudentByNumberAndDegreeTypeForm.set("degreeType", degreeType.toString());
				getStudentByNumberAndDegreeTypeForm.set("studentNumber", studentNumber.toString());
			}
		}

		InfoStudent infoStudent = null;

		Object args[] = { degreeType, studentNumber };
		try {
			infoStudent = (InfoStudent) ServiceUtils.executeService(userView, "GetStudentByNumberAndDegreeType", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		if (infoStudent == null) {
			actionErrors.add("error.no.student.in.database", new ActionError("error.no.student.in.database", studentNumber.toString()));
			result = false;
		} else {
			request.setAttribute(SessionConstants.STUDENT, infoStudent);
			result = true;
		}

		return result;
	}

	public ArrayList getTeachersByNumbers(
		ActionForm form,
		HttpServletRequest request,
		String teachersNumbersListField,
		String sessionConstant,
		ActionErrors actionErrors)
		throws FenixActionException {

		boolean result = false;

		DynaActionForm masterDegreeThesisForm = (DynaActionForm) form;
		IUserView userView = SessionUtils.getUserView(request);

		Integer[] teachersNumbersList = (Integer[]) masterDegreeThesisForm.get(teachersNumbersListField);

		ArrayList infoTeachersList = new ArrayList();

		InfoTeacher infoTeacher = null;
		Object args[] = new Object[1];
		Integer teacherNumber = null;

		for (int i = 0; i < teachersNumbersList.length; i++) {

			teacherNumber = (Integer) teachersNumbersList[i];

			if (teacherNumber.intValue() == 0)
				continue;

			args[0] = teacherNumber;

			try {
				infoTeacher = (InfoTeacher) ServiceUtils.executeService(userView, "ReadTeacherByNumber", args);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}

			if (infoTeacher == null)
				actionErrors.add("error.no.teacher.in.database", new ActionError("error.no.teacher.in.database", teacherNumber.toString()));
			else if (infoTeachersList.contains(infoTeacher) == false) {
				infoTeachersList.add(infoTeacher);
			}

		}

		if (infoTeachersList.isEmpty() == false)
			request.setAttribute(sessionConstant, infoTeachersList);

		return infoTeachersList;

	}

	public void getExternalPersonsByName(ActionForm form, HttpServletRequest request, String sessionConstant, ActionErrors actionErrors)
		throws FenixActionException {

		DynaActionForm masterDegreeThesisForm = (DynaActionForm) form;
		IUserView userView = SessionUtils.getUserView(request);

		String externalAssistentGuiderName = (String) masterDegreeThesisForm.get("externalAssistentGuiderName");

		ArrayList infoExternalPersonsList = null;
		Object args[] = { externalAssistentGuiderName };

		try {
			if (!externalAssistentGuiderName.equals(""))
				infoExternalPersonsList = (ArrayList) ServiceUtils.executeService(userView, "SearchExternalPersonsByName", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		if (infoExternalPersonsList.isEmpty() == false)
			request.setAttribute(sessionConstant, infoExternalPersonsList);
		
		request.setAttribute(SessionConstants.EXTERNAL_ASSISTENT_GUIDERS_SEARCH_RESULTS_FLAG, new Boolean(true));

	}

	public ArrayList getExternalPersonsByIDs(
		ActionForm form,
		HttpServletRequest request,
		String externalPersonsIDsListField,
		String sessionConstant,
		ActionErrors actionErrors)
		throws FenixActionException {

		boolean result = false;

		DynaActionForm masterDegreeThesisForm = (DynaActionForm) form;
		IUserView userView = SessionUtils.getUserView(request);

		Integer[] externalPersonsIDsList = (Integer[]) masterDegreeThesisForm.get(externalPersonsIDsListField);

		ArrayList infoExternalPersonsList = new ArrayList();

		InfoExternalPerson infoExternalPerson = null;
		Object args[] = new Object[1];
		Integer externalPersonID = null;

		for (int i = 0; i < externalPersonsIDsList.length; i++) {

			externalPersonID = (Integer) externalPersonsIDsList[i];
			args[0] = externalPersonID;

			try {
				infoExternalPerson = (InfoExternalPerson) ServiceUtils.executeService(userView, "ReadExternalPersonByID", args);
			} catch (NonExistingServiceException e) {
				actionErrors.add(e.getMessage(), new ActionError(e.getMessage(), externalPersonID.toString()));
				continue;
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}

			if (infoExternalPersonsList.contains(infoExternalPerson) == false) {
				infoExternalPersonsList.add(infoExternalPerson);
			}

		}

		if (infoExternalPersonsList.isEmpty() == false)
			request.setAttribute(sessionConstant, infoExternalPersonsList);

		return infoExternalPersonsList;

	}

	private String getFromRequest(String parameter, HttpServletRequest request) {
		String parameterString = request.getParameter(parameter);
		if (parameterString == null) {
			parameterString = (String) request.getAttribute(parameter);
		}
		return parameterString;
	}

}