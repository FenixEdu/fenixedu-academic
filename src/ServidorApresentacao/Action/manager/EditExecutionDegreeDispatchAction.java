/*
 * Created on 18/Ago/2003
 */
package ServidorApresentacao.Action.manager;

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
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoTeacher;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac1
 */

public class EditExecutionDegreeDispatchAction extends FenixDispatchAction {

	public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		HttpSession session = request.getSession(false);
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		
		DynaActionForm dynaForm = (DynaActionForm) form;

		Integer executionDegreeId = new Integer(request.getParameter("executionDegreeId"));

		InfoExecutionDegree oldInfoExecutionDegree = null;
		Object args[] = { executionDegreeId };
		GestorServicos manager = GestorServicos.manager();

		try {
				oldInfoExecutionDegree = (InfoExecutionDegree) manager.executar(userView, "ReadExecutionDegree", args);
				
		} catch (NonExistingServiceException e) {
			throw new NonExistingActionException("message.nonExistingExecutionDegree", mapping.findForward("readDegreeCurricularPlan"));
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}

		String label, value;
		List result;
		
		/*   Needed service and creation of bean of InfoTeachers for use in jsp   */
		try {
			result = (List) manager.executar(userView, "ReadAllTeachers", null);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		ArrayList infoTeachersList = new ArrayList();
		if (result != null) {
			InfoTeacher infoTeacher;
			Iterator iter = result.iterator();
			while (iter.hasNext()) {
				infoTeacher = (InfoTeacher) iter.next();
				value = infoTeacher.getIdInternal().toString();
				label = infoTeacher.getTeacherNumber() + " - " + infoTeacher.getInfoPerson().getNome();
				infoTeachersList.add(new LabelValueBean(label, value));
			}
			request.setAttribute("infoTeachersList", infoTeachersList);
		}

		/*   Needed service and creation of bean of InfoExecutionYears for use in jsp   */
		try {
			result = (List) manager.executar(userView, "ReadAllExecutionYears", null);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		ArrayList infoExecutionYearsList = new ArrayList();
		if (result != null) {
			InfoExecutionYear infoExecutionYear;
			Iterator iter = result.iterator();
			while (iter.hasNext()) {
				infoExecutionYear = (InfoExecutionYear) iter.next();
				value = infoExecutionYear.getIdInternal().toString();
				label = infoExecutionYear.getYear();
				infoExecutionYearsList.add(new LabelValueBean(label, value));
			}
			request.setAttribute("infoExecutionYearsList", infoExecutionYearsList);
		}
		
		dynaForm.set("tempExamMap", oldInfoExecutionDegree.getTemporaryExamMap().toString());
		dynaForm.set("coordinatorId", oldInfoExecutionDegree.getInfoCoordinator().getIdInternal().toString());
		dynaForm.set("executionYearId", oldInfoExecutionDegree.getInfoExecutionYear().getIdInternal().toString());

		return mapping.findForward("editExecutionDegree");
	}

	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		HttpSession session = request.getSession(false);
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));
		Integer executionDegreeId = new Integer(request.getParameter("executionDegreeId"));

		DynaActionForm dynaForm = (DynaValidatorForm) form;
		String executionYearString = (String) dynaForm.get("executionYearId");
		String coordinatorIdString = (String) dynaForm.get("coordinatorId");
		String tempExamMapString = (String) dynaForm.get("tempExamMap");

		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();

		InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
		infoExecutionYear.setIdInternal(new Integer(executionYearString));
		infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);
		
		InfoTeacher infoTeacher = new InfoTeacher();
		infoTeacher.setIdInternal(new Integer(coordinatorIdString));
		infoExecutionDegree.setInfoCoordinator(infoTeacher);
		
		infoExecutionDegree.setTemporaryExamMap(new Boolean(tempExamMapString));

		InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
		infoDegreeCurricularPlan.setIdInternal(degreeCurricularPlanId);
		infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
		infoExecutionDegree.setIdInternal(executionDegreeId);

		Object args[] = { infoExecutionDegree };

		GestorServicos manager = GestorServicos.manager();

		try {
				manager.executar(userView, "EditExecutionDegree", args);
				
		} catch (ExistingServiceException e) {
			throw new ExistingActionException("message.manager.existing.execution.degree");
		} catch (NonExistingServiceException ex) {
			throw new NonExistingActionException(ex.getMessage(), mapping.findForward("readDegreeCurricularPlan"));
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}
		
		return mapping.findForward("readDegreeCurricularPlan");
	}
}