/*
 * Created on 2003/07/16
 * 
 */
package ServidorApresentacao.Action.sop;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.comparators.ExecutionPeriodComparator;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.sop.CreateWorkingArea.ExistingExecutionPeriod;
import ServidorAplicacao.Servico.sop.CreateWorkingArea.InvalidExecutionPeriod;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.InvalidArgumentsActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Luis Crus & Sara Ribeiro
 */
public class ManageWorkingAreaDA extends FenixDispatchAction {

	/**
	 * Prepare information to show existing execution periods
	 * and working areas.
	 **/
	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		IUserView userView = SessionUtils.getUserView(request);

		try {
			List infoExecutionPeriods =
				(List) ServiceUtils.executeService(
					userView,
					"ReadAllExecutionPeriods",
					null);

			if (infoExecutionPeriods != null
				&& !infoExecutionPeriods.isEmpty()) {

				Collections.sort(
					infoExecutionPeriods,
					new ExecutionPeriodComparator());

				List realExecutionPeriods =
					(List) CollectionUtils.select(
						infoExecutionPeriods,
						new RealExecutionPeriodPredicate());

				List workingAreas =
					(List) CollectionUtils.select(
						infoExecutionPeriods,
						new WorkingAreasPredicate());

				if (realExecutionPeriods != null
					&& !realExecutionPeriods.isEmpty()) {
					request.setAttribute(
						SessionConstants.LIST_EXECUTION_PERIODS,
						realExecutionPeriods);
				}

				if (workingAreas != null && !workingAreas.isEmpty()) {
					request.setAttribute(
						SessionConstants.LIST_WORKING_AREAS,
						workingAreas);
				}
			}
		} catch (FenixServiceException ex) {
			throw new FenixActionException(
				"Problemas de comunicação com a base de dados.",
				ex);
		}

		return mapping.findForward("Manage");
	}

	/**
	 * Prepare information to show existing execution periods
	 * and working areas.
	 **/
	public ActionForward createWorkingArea(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		DynaValidatorForm createWorkingAreaForm = (DynaValidatorForm) form;

		Integer semesterToCreate =
			new Integer((String) createWorkingAreaForm.get("semesterToCreate"));
		String yearToCreate =
			(String) createWorkingAreaForm.get("yearToCreate");
		Integer semesterToExportDataFrom =
			new Integer(
				(String) createWorkingAreaForm.get("semesterToExportDataFrom"));
		String yearToExportDataFrom =
			(String) createWorkingAreaForm.get("yearToExportDataFrom");

		IUserView userView = SessionUtils.getUserView(request);

		InfoExecutionYear infoExecutionYearOfWorkingArea =
			new InfoExecutionYear(yearToCreate);
		InfoExecutionPeriod infoExecutionPeriodOfWorkingArea =
			new InfoExecutionPeriod(
				"" + semesterToCreate + "º Semestre",
				infoExecutionYearOfWorkingArea);
		infoExecutionPeriodOfWorkingArea.setSemester(
			new Integer(semesterToCreate.intValue()));
		InfoExecutionYear infoExecutionYearToExportDataFrom =
			new InfoExecutionYear(yearToExportDataFrom);
		InfoExecutionPeriod infoExecutionPeriodToExportDataFrom =
			new InfoExecutionPeriod(
				"" + semesterToExportDataFrom + "º Semestre",
				infoExecutionYearToExportDataFrom);
		infoExecutionPeriodToExportDataFrom.setSemester(
			semesterToExportDataFrom);

		Object[] argsCreateWorkingArea =
			{
				infoExecutionPeriodOfWorkingArea,
				infoExecutionPeriodToExportDataFrom };
		try {
			Boolean result =
				(Boolean) ServiceUtils.executeService(
					userView,
					"CreateWorkingArea",
					argsCreateWorkingArea);
		} catch (InvalidExecutionPeriod ex) {
			throw new InvalidArgumentsActionException(
				"O periodo indicado para importar os dados",
				ex);
		} catch (ExistingExecutionPeriod ex) {
			throw new ExistingActionException(
				"A área de trabalho indicada",
				ex);
		} catch (FenixServiceException ex) {
			throw new FenixActionException(
				"Problemas a criar a área de trabalho.",
				ex);
		}

		return mapping.findForward("Sucess");
	}

	/**
	 * Prepare information to show existing execution periods
	 * and working areas.
	 **/
	public ActionForward deleteWorkingArea(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		String year = request.getParameter("year");
		Integer semester = new Integer(request.getParameter("semester"));

		InfoExecutionYear infoExecutionYear = new InfoExecutionYear(year);
		InfoExecutionPeriod infoExecutionPeriod =
			new InfoExecutionPeriod(
				"AT:" + semester + "º Semestre",
				infoExecutionYear);
		infoExecutionPeriod.setSemester(new Integer(semester.intValue()));

		IUserView userView = SessionUtils.getUserView(request);

		Object[] argsDeleteWorkingArea = { infoExecutionPeriod };
		try {
			Boolean result =
				(Boolean) ServiceUtils.executeService(
					userView,
					"DeleteWorkingArea",
					argsDeleteWorkingArea);
		} catch (FenixServiceException ex) {
			throw new FenixActionException(
				"Problemas a apagar a área de trabalho.",
				ex);
		}

		return prepare(mapping, form, request, response);
	}

	/**
	 * Prepare information to show existing execution periods
	 * and working areas.
	 **/
	public ActionForward exportData(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		String year = request.getParameter("year");
		Integer semester = new Integer(request.getParameter("semester"));

		return prepare(mapping, form, request, response);
	}

	private class RealExecutionPeriodPredicate implements Predicate {
		public RealExecutionPeriodPredicate() {
		}
		/**
		 * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
		 */
		public boolean evaluate(Object listElement) {
			InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) listElement;
			return infoExecutionPeriod.getSemester().intValue() > 0;
		}
	}

	private class WorkingAreasPredicate implements Predicate {
		public WorkingAreasPredicate() {
		}
		/**
		 * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
		 */
		public boolean evaluate(Object listElement) {
			InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) listElement;
			return infoExecutionPeriod.getSemester().intValue() <= 0;
		}
	}

}
