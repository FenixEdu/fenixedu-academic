package ServidorApresentacao.Action.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoStudent;
import DataBeans.degreeAdministrativeOffice.InfoCurricularCourseEnromentWithoutRules;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;

/**
 * @author David Santos
 */

public class PrepareStudentDataForEnrolmentWithoutRulesDispatchAction extends PrepareStudentDataAction {
	
	private final String[] forwards = { "startCurricularCourseEnrolmentWithoutRules", "error" };

	public ActionForward getStudentAndDegreeTypeForEnrolmentWithoutRules(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		boolean isSuccess = super.getStudentByNumberAndDegreeType(form, request);

		if(isSuccess) {
			DynaActionForm getStudentByNumberAndDegreeTypeForm = (DynaActionForm) form;
			HttpSession session = request.getSession();

			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

			List infoExecutionDegreesList = null;
			try {
				Integer degreeType = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("degreeType"));
				InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) session.getServletContext().getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);
				TipoCurso realDegreeType = new TipoCurso(degreeType);
				Object args[] = { infoExecutionPeriod.getInfoExecutionYear(), realDegreeType };
				infoExecutionDegreesList = (List) ServiceUtils.executeService(userView, "ReadExecutionDegreesByExecutionYearAndDegreeType", args);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}

			InfoStudent infoStudent = (InfoStudent) request.getAttribute(SessionConstants.STUDENT);
			InfoCurricularCourseEnromentWithoutRules infoCurricularCourseEnromentWithoutRules = new InfoCurricularCourseEnromentWithoutRules();
			infoCurricularCourseEnromentWithoutRules.setInfoExecutionDegreesList(infoExecutionDegreesList);
			infoCurricularCourseEnromentWithoutRules.setInfoStudent(infoStudent);

			session.setAttribute(SessionConstants.ENROLMENT_WITHOUT_RULES_INFO_KEY, infoCurricularCourseEnromentWithoutRules);
			request.setAttribute(SessionConstants.ENROLMENT_YEAR_LIST_KEY, this.generateCurricularYearList());
			request.setAttribute(SessionConstants.ENROLMENT_SEMESTER_LIST_KEY, this.generateCurricularSemesterList());

			return mapping.findForward(forwards[0]);
		} else {
			return mapping.getInputForward();
		}
	}

	public ActionForward error(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward(forwards[1]);
	}

	private List generateCurricularYearList() {
		List years = new ArrayList();
		years.add("label.first.year");
		years.add("label.second.year");
		years.add("label.third.year");
		years.add("label.fourth.year");
		years.add("label.fiveth.year");
		return years;
	}

	private List generateCurricularSemesterList() {
		List years = new ArrayList();
		years.add("label.first.semester");
		years.add("label.second.semester");
		return years;
	}
}