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
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

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
				IUserView userView = (IUserView) session.getAttribute("UserView");				
				InfoClass infoClass = new InfoClass();
			
				InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) session.getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);
				InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) session.getAttribute(SessionConstants.INFO_EXECUTION_DEGREE_KEY); 
			
				Integer curricularYear=(Integer) session.getAttribute(SessionConstants.CURRICULAR_YEAR_KEY);

			
			// read execution courses of executionDegree, executionPeriof and CurricularYear
			Object[] argsReadCourses =
				{ infoExecutionDegree, infoExecutionPeriod, curricularYear };
			List infoCoursesList =
				(List) gestor.executar(
					userView,
					"LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular",
					argsReadCourses);

			System.out.println("Disciplinas Execução lidas = " + infoCoursesList.size());
			// read exams of each of the execution courses previouly read
			InfoExecutionCourse infoExecutionCourse = null;
			InfoExam infoExam = null;
			Object argsReadExams[] = new Object[1];
			List infoExamsOfExecutionCourse = null;
			ArrayList infoExams = new ArrayList();
			for (int i = 0; i < infoCoursesList.size(); i++) {
				infoExecutionCourse =
					(InfoExecutionCourse) infoCoursesList.get(i);
				argsReadExams[0] = infoExecutionCourse;
				infoExamsOfExecutionCourse =
					(List) gestor.executar(userView, "ReadExamsByExecutionCourse", argsReadExams);
				System.out.println("Exames lidos = " + infoExamsOfExecutionCourse.size());
				infoExams.addAll(infoExamsOfExecutionCourse);
				/*				for(int j = 0; j < infoExamsOfExecutionCourse.size(); j++) {
									infoExam = (InfoExam) infoExamsOfExecutionCourse.get(j);
									if(!(infoExams.contains(infoExam)))
										infoExams.add(infoExam);
								}
				*/
			}

			if (infoExams != null && infoExams.isEmpty()) {
				session.removeAttribute(SessionConstants.INFO_EXAMS_KEY);
			} else {
				System.out.println("Pos em sessao!!");
				session.setAttribute(SessionConstants.INFO_EXAMS_KEY, infoExams);
			}

			return mapping.findForward("Sucess");
			} else
				throw new Exception();
			// nao ocorre... pedido passa pelo filtro Autorizacao
		}
}

