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

public class PrepareStudentDataDispatchAction extends DispatchAction {
	
	private final String[] forwards = { "startCurricularCourseEnrolmentWithRules", "startCurricularCourseEnrolmentWithoutRules", "startOptionalCurricularCourseEnrolmentWithoutRules", "startManualEquivalence" };

	public ActionForward getStudentAndDegreeTypeForEnrolmentWithRules(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		boolean isSuccess = this.getUserViewFromStudentNumberAndDegreeType(form, request);
		if(isSuccess) {
			return mapping.findForward(forwards[0]);
		} else {
			return mapping.getInputForward();
		}
	}

	public ActionForward getStudentAndDegreeTypeForEnrolmentWithoutRules(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		boolean isSuccess = this.getUserViewFromStudentNumberAndDegreeType(form, request);

		if(isSuccess) {
			DynaActionForm getStudentByNumberAndDegreeTypeForm = (DynaActionForm) form;
			HttpSession session = request.getSession();

			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

			List infoExecutionDegreesList = null;
			InfoStudent infoStudent = null;

			Integer degreeTypeInt = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("degreeType"));
			Integer studentNumber = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("studentNumber"));

			try {
				Object args[] = { degreeTypeInt, studentNumber };
				infoStudent = (InfoStudent) ServiceUtils.executeService(userView, "GetStudentByNumberAndDegreeType", args);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}

			try {
				Integer degreeType = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("degreeType"));
				InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) session.getServletContext().getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);
				TipoCurso realDegreeType = new TipoCurso(degreeType);
				Object args[] = { infoExecutionPeriod.getInfoExecutionYear(), realDegreeType };
				infoExecutionDegreesList = (List) ServiceUtils.executeService(userView, "ReadExecutionDegreesByExecutionYearAndDegreeType", args);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}

			session.setAttribute(SessionConstants.ENROLMENT_ACTOR_KEY, infoStudent);
			request.setAttribute(SessionConstants.DEGREE_LIST, this.getExecutionDegreesLableValueBeanList(infoExecutionDegreesList));
			session.setAttribute(SessionConstants.DEGREES, infoExecutionDegreesList);

			return mapping.findForward(forwards[1]);
		} else {
			return mapping.getInputForward();
		}
	}

	public ActionForward getStudentAndDegreeTypeForEnrolmentInOptionalWithoutRules(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean isSuccess = this.getUserViewFromStudentNumberAndDegreeType(form, request);
		if(isSuccess) {
			return mapping.findForward(forwards[2]);
		} else {
			return mapping.getInputForward();
		}
	}

	public ActionForward getStudentAndDegreeTypeForManualEquivalence(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean isSuccess = this.getUserViewFromStudentNumberAndDegreeType(form, request);
		if(isSuccess) {
			return mapping.findForward(forwards[3]);
		} else {
			return mapping.getInputForward();
		}
	}

	public boolean getUserViewFromStudentNumberAndDegreeType(ActionForm form, HttpServletRequest request) throws Exception {

		boolean result = false;

		DynaActionForm getStudentByNumberAndDegreeTypeForm = (DynaActionForm) form;
		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

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
			result = false;
		} else {
			session.setAttribute(SessionConstants.ENROLMENT_ACTOR_KEY, actor);
			result = true;
		}
		
		return result;
	}

	private List getExecutionDegreesLableValueBeanList(List infoExecutionDegreesList) {
		ArrayList result = null;
		if ( (infoExecutionDegreesList != null) && (!infoExecutionDegreesList.isEmpty()) ) {
			result = new ArrayList();
			result.add(new LabelValueBean("", ""));
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
		ActionError actionError = new ActionError("error.no.student.in.database", studentNumber.toString());
		actionErrors.add("error.no.student.in.database", actionError);
		saveErrors(request, actionErrors);
	}
}