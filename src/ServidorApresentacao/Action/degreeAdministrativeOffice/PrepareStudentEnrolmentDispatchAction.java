package ServidorApresentacao.Action.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
	
	private final String[] forwards = { "startCurricularCourseEnrolmentWithRules", "startCurricularCourseEnrolmentWithoutRules" };

	public ActionForward withRules(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		DynaActionForm getStudentByNumberAndDegreeTypeForm = (DynaActionForm) form;
		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer degreeType = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("degreeType"));
		Integer studentNumber = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("studentNumber"));
		Object args[] = { degreeType, studentNumber };

		IUserView actor = null;
		try {
			actor = (IUserView) ServiceUtils.executeService(userView, "GetUserViewFromStudentNumberAndDegreeType", args);
//			TODO DAVID-RICARDO: Fazer qq coisa se o student não existir, isto é, se o aluno que se quer inscrever não se encontrar na base de dados.
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		session.setAttribute(SessionConstants.ENROLMENT_ACTOR_KEY, actor);
		return mapping.findForward(forwards[0]);
	}

	public ActionForward withoutRules(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		DynaActionForm getStudentByNumberAndDegreeTypeForm = (DynaActionForm) form;
		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer degreeType = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("degreeType"));
		Integer studentNumber = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("studentNumber"));

		InfoStudent student = null;
		List infoExecutionDegreesList = null;
		try {
			Object args1[] = { degreeType, studentNumber };
			student = (InfoStudent) ServiceUtils.executeService(userView, "GetStudentByNumberAndDegreeType", args1);
//			TODO DAVID-RICARDO: Fazer qq coisa se o student não existir, isto é, se o aluno que se quer inscrever não se encontrar na base de dados.
			InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) session.getServletContext().getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);
			TipoCurso realDegreeType = new TipoCurso(degreeType);
			Object args2[] = { infoExecutionPeriod.getInfoExecutionYear(), realDegreeType };
			infoExecutionDegreesList = (List) ServiceUtils.executeService(userView, "ReadExecutionDegreesByExecutionYearAndDegreeType", args2);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		session.setAttribute(SessionConstants.ENROLMENT_ACTOR_KEY, student);
		request.setAttribute(SessionConstants.DEGREE_LIST, this.getExecutionDegreesLableValueBeanList(infoExecutionDegreesList));
		session.setAttribute(SessionConstants.DEGREES, infoExecutionDegreesList);
		return mapping.findForward(forwards[1]);
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
}