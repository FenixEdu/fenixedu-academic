package ServidorApresentacao.Action.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;

/**
 * @author David Santos
 */

public class PrepareStudentEnrolmentDispatchAction extends DispatchAction {
	
	private final String[] forwards = { "startCurricularCourseEnrolmentWithRules", "startCurricularCourseEnrolmentWithoutRules", "errorWithRules", "errorWithoutRules", "startCurricularCourseEnrolmentOptionalWithoutRules", "errorOptionalWithoutRules", "startManualEquivalence", "errorManualEquivalence"};
	private final String[] messages = { "error.no.student.in.database", "error.no.degree.type.selected", "error.no.student.selected" };

	public ActionForward withRules(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		DynaActionForm getStudentByNumberAndDegreeTypeForm = (DynaActionForm) form;
		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		if(this.isFormEntriesValid((String) getStudentByNumberAndDegreeTypeForm.get("studentNumber"), (String) getStudentByNumberAndDegreeTypeForm.get("degreeType"), request)) {

			Integer degreeType = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("degreeType"));
			Integer studentNumber = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("studentNumber"));

			IUserView actor = null;

			Object args[] = { degreeType, studentNumber };
			try {
				actor = (IUserView) ServiceUtils.executeService(userView, "GetUserViewFromStudentNumberAndDegreeType", args);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}

			if(actor == null) {
				this.setNoStudentError(studentNumber, request);
				return mapping.findForward(forwards[2]);
			} else {
				session.setAttribute(SessionConstants.ENROLMENT_ACTOR_KEY, actor);
				return mapping.findForward(forwards[0]);
			}
		} else {
			return mapping.findForward(forwards[2]);
		}
	}

	public ActionForward withoutRules(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		DynaActionForm getStudentByNumberAndDegreeTypeForm = (DynaActionForm) form;
		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoStudent student = null;
		List infoExecutionDegreesList = null;

		if(this.isFormEntriesValid((String) getStudentByNumberAndDegreeTypeForm.get("studentNumber"), (String) getStudentByNumberAndDegreeTypeForm.get("degreeType"), request)) {

			Integer degreeType = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("degreeType"));
			Integer studentNumber = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("studentNumber"));

			try {
				Object args1[] = { degreeType, studentNumber };
				student = (InfoStudent) ServiceUtils.executeService(userView, "GetStudentByNumberAndDegreeType", args1);
				InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) session.getServletContext().getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);
				TipoCurso realDegreeType = new TipoCurso(degreeType);
				Object args2[] = { infoExecutionPeriod.getInfoExecutionYear(), realDegreeType };
				infoExecutionDegreesList = (List) ServiceUtils.executeService(userView, "ReadExecutionDegreesByExecutionYearAndDegreeType", args2);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}

			if(student == null) {
				this.setNoStudentError(studentNumber, request);
				return mapping.findForward(forwards[3]);
			} else {
				session.setAttribute(SessionConstants.ENROLMENT_ACTOR_KEY, student);
				request.setAttribute(SessionConstants.DEGREE_LIST, this.getExecutionDegreesLableValueBeanList(infoExecutionDegreesList));
				session.setAttribute(SessionConstants.DEGREES, infoExecutionDegreesList);
				return mapping.findForward(forwards[1]);
			}
		} else {
			return mapping.findForward(forwards[3]);
		}
	}

	public ActionForward optionalWithoutRules(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward result = this.withRules(mapping, form, request, response);
		if(result.getName().equals(forwards[0])) {
			return mapping.findForward(forwards[4]);
		} else /*if(result.getName().equals(forwards[2]))*/ {
			return mapping.findForward(forwards[5]);
		}
	}

	public ActionForward manualEquivalence(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward result = this.withRules(mapping, form, request, response);
		if(result.getName().equals(forwards[0])) {
			return mapping.findForward(forwards[6]);
		} else /*if(result.getName().equals(forwards[2]))*/ {
			return mapping.findForward(forwards[7]);
		}
	}

	private List getExecutionDegreesLableValueBeanList(List infoExecutionDegreesList) {
		ArrayList result = null;
		if ( (infoExecutionDegreesList != null) && (!infoExecutionDegreesList.isEmpty()) ) {
			result = new ArrayList();
			Iterator iterator = infoExecutionDegreesList.iterator();
			while(iterator.hasNext()) {
				InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
				Integer index = new Integer(infoExecutionDegreesList.indexOf(infoExecutionDegree));
				result.add(new LabelValueBean(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome(), index.toString()));
			}
		}
		return result;	
	}
	
	private void setNoStudentError(Integer studentNumber, HttpServletRequest request) {
		ActionErrors actionErrors = new ActionErrors();
//		actionErrors.add(ActionErrors.GLOBAL_ERROR, new ActionError(messages[0]));
		ActionError actionError = new ActionError(messages[0], studentNumber);
		actionErrors.add(messages[0], actionError);
		saveErrors(request, actionErrors);
	}

	private boolean isFormEntriesValid(String studentNumber, String degreeType, HttpServletRequest request) {
		ActionErrors actionErrors = new ActionErrors();
		ActionError actionError = null;
		boolean result = true;

		if(studentNumber == null) {
			actionError = new ActionError(messages[2]);
			actionErrors.add(messages[2], actionError);
			saveErrors(request, actionErrors);
			result = false;
		} else {
			try {
				new Integer(studentNumber);
			} catch (NumberFormatException e) {
				actionError = new ActionError(messages[2]);
				actionErrors.add(messages[2], actionError);
				saveErrors(request, actionErrors);
				result = false;
			}
		}

		if(degreeType == null) {
			actionError = new ActionError(messages[1]);
			actionErrors.add(messages[1], actionError);
			saveErrors(request, actionErrors);
			result = false;
		}

		return result;
	}
}