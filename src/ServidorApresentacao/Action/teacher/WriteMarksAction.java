package ServidorApresentacao.Action.teacher;

import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoMark;
import DataBeans.InfoSiteMarks;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.util.Cloner;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.strategys.IDegreeCurricularPlanStrategy;
import ServidorApresentacao.Action.exceptions.FenixActionException;

/**
 * @author Tânia Pousão
 *
 */
public class WriteMarksAction extends DispatchAction {
	public ActionForward loadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		return null;
	}

	public ActionForward writeMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession();
		ActionErrors actionErrors = new ActionErrors();

		List marksList = null;
		
		//TODO: transform form into list with student's number and students's mark
		
		Integer objectCode = getObjectCode(request);
		Integer examCode = getExamCode(request);

		IUserView userView = (IUserView) session.getAttribute("UserView");

		Object[] args = { objectCode, examCode, marksList };
		GestorServicos manager = GestorServicos.manager();
		TeacherAdministrationSiteView siteView = null;
		
		try {
			siteView = (TeacherAdministrationSiteView) manager.executar(userView, "InsertExamMarks", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		
		//		check for errors in service
		InfoSiteMarks infoSiteMarks = (InfoSiteMarks) siteView.getComponent();
		if (infoSiteMarks.getMarksListErrors() != null && infoSiteMarks.getMarksListErrors().size() > 0) {
			ListIterator iterator = infoSiteMarks.getMarksListErrors().listIterator();
			while (iterator.hasNext()) {
				InfoMark infoMark = (InfoMark) iterator.next();
				if (isValidMark(infoMark)) {
					actionErrors.add("studentExistence", new ActionError("errors.student.nonExisting", infoMark.getInfoFrequenta().getAluno().getNumber()));
				} else {
					actionErrors.add("invalidMark",
						new ActionError("errors.invalidMark", infoMark.getMark(), infoMark.getInfoFrequenta().getAluno().getNumber()));
				}
			}
			saveErrors(request, actionErrors);
			return mapping.getInputForward();

		}

		request.setAttribute("objectCode", objectCode);

		return mapping.findForward("marksList");
	}

	private Integer getExamCode(HttpServletRequest request) {
		String examCodeString = (String) request.getAttribute("examCode");
		if (examCodeString == null) {
			examCodeString = request.getParameter("examCode");
		}
		Integer examCode = new Integer(examCodeString);
		return examCode;
	}

	private Integer getObjectCode(HttpServletRequest request) {
		Integer objectCode = null;
		String objectCodeString = (String) request.getAttribute("objectCode");
		if (objectCodeString == null) {
			objectCodeString = request.getParameter("objectCode");
		}
		if (objectCodeString != null) {
			objectCode = new Integer(objectCodeString);
		}
		return objectCode;
	}

	private boolean isValidMark(InfoMark infoMark) {
		InfoDegreeCurricularPlan infoDegreeCurricularPlan =
			infoMark.getInfoFrequenta().getEnrolment().getInfoStudentCurricularPlan().getInfoDegreeCurricularPlan();
		IDegreeCurricularPlan degreeCurricularPlan =
			Cloner.copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(infoDegreeCurricularPlan);

		// test marks by execution course: strategy 
		IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory.getInstance();
		IDegreeCurricularPlanStrategy degreeCurricularPlanStrategy =
			degreeCurricularPlanStrategyFactory.getDegreeCurricularPlanStrategy(degreeCurricularPlan);

		return degreeCurricularPlanStrategy.checkMark(infoMark.getMark());
	}
}