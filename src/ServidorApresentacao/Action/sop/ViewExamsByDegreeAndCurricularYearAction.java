package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoClass;
import DataBeans.InfoExam;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoViewExamByDayAndShift;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.Season;

/**
 * @author jpvl
 */
public class ViewExamsByDegreeAndCurricularYearAction extends Action {
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		SessionUtils.validSessionVerification(request, mapping);

		HttpSession session = request.getSession(false);
		if (session != null) {
			GestorServicos gestor = GestorServicos.manager();
			IUserView userView =
				(IUserView) session.getAttribute(SessionConstants.U_VIEW);
			InfoClass infoClass = new InfoClass();

			InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) session.getAttribute(
					SessionConstants.INFO_EXECUTION_PERIOD_KEY);
			InfoExecutionDegree infoExecutionDegree =
				(InfoExecutionDegree) session.getAttribute(
					SessionConstants.INFO_EXECUTION_DEGREE_KEY);

			Integer curricularYear =
				(Integer) session.getAttribute(
					SessionConstants.CURRICULAR_YEAR_KEY);

			// read execution courses of executionDegree, executionPeriof and CurricularYear
			Object[] argsReadCourses =
				{ infoExecutionDegree, infoExecutionPeriod, curricularYear };
			List infoCoursesList =
				(List) gestor.executar(
					userView,
					"LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular",
					argsReadCourses);

			// read exams of each of the execution courses previouly read
			InfoExecutionCourse infoExecutionCourse = null;
			InfoViewExamByDayAndShift infoViewExam = null;
			Object argsReadExams[] = new Object[1];
			List infoExamsOfExecutionCourse = null;
			ArrayList infoViewExams = new ArrayList();
			for (int i = 0; i < infoCoursesList.size(); i++) {
				infoExecutionCourse =
					(InfoExecutionCourse) infoCoursesList.get(i);
				argsReadExams[0] = infoExecutionCourse;
				infoExamsOfExecutionCourse =
					(List) gestor.executar(
						userView,
						"ReadExamsByExecutionCourse",
						argsReadExams);

				InfoViewExamByDayAndShift notScheduledExam = 
					new InfoViewExamByDayAndShift(
						new InfoExam(
							null,
							null,
							null,
							null,
							new InfoExecutionCourse()),
						null,
						null);
						
				if (infoExamsOfExecutionCourse.size() == 1) {
					infoViewExam =
						(InfoViewExamByDayAndShift) infoExamsOfExecutionCourse.get(0);
					notScheduledExam.setNumberStudentesAttendingCourse(infoViewExam.getNumberStudentesAttendingCourse());
					notScheduledExam.getInfoExam().setInfoExecutionCourse(infoViewExam.getInfoExam().getInfoExecutionCourse());
					if (infoViewExam
						.getInfoExam()
						.getSeason()
						.equals(new Season(Season.SEASON1))) {
						infoViewExams.add(infoViewExam);
						infoViewExams.add(notScheduledExam);
					} else {
						infoViewExams.add(notScheduledExam);
						infoViewExams.add(infoViewExam);
					}
				} else
					infoViewExams.addAll(infoExamsOfExecutionCourse);
			}

			if (infoViewExams != null && infoViewExams.isEmpty()) {
				session.removeAttribute(SessionConstants.INFO_EXAMS_KEY);
			} else {
				session.setAttribute(
					SessionConstants.INFO_EXAMS_KEY,
					infoViewExams);
			}

			return mapping.findForward("Sucess");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}
}
