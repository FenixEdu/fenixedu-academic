package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoCurricularYear;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.comparators.ComparatorByNameForInfoExecutionDegree;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class ChooseContextDA extends FenixContextDispatchAction {

	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		IUserView userView = SessionUtils.getUserView(request);

		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) request.getAttribute(
				SessionConstants.EXECUTION_PERIOD);

		/* Obtain a list of curricular years */
		List labelListOfCurricularYears = getLabelListOfCurricularYears();
		request.setAttribute(
			SessionConstants.LABELLIST_CURRICULAR_YEARS,
			labelListOfCurricularYears);

		/* Obtain a list of degrees for the specified execution year */
		Object argsLerLicenciaturas[] =
			{ infoExecutionPeriod.getInfoExecutionYear()};
		List executionDegreeList =
			(List) ServiceUtils.executeService(
				userView,
				"ReadExecutionDegreesByExecutionYear",
				argsLerLicenciaturas);

		/* Sort the list of degrees */
		Collections.sort(
			executionDegreeList,
			new ComparatorByNameForInfoExecutionDegree());

		/* Generate a label list for the above list of degrees */
		List labelListOfExecutionDegrees =
			getLabelListOfExecutionDegrees(executionDegreeList);
		request.setAttribute("licenciaturas", labelListOfExecutionDegrees);

		return mapping.findForward("ShowChooseForm");
	}

	public ActionForward choose(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		DynaActionForm chooseScheduleContext = (DynaActionForm) form;

		IUserView userView = SessionUtils.getUserView(request);

		/* Determine Selected Curricular Year */
		Integer anoCurricular =
			new Integer((String) chooseScheduleContext.get("curricularYear"));

		Object argsReadCurricularYearByOID[] = { anoCurricular };
		InfoCurricularYear infoCurricularYear =
			(InfoCurricularYear) ServiceUtils.executeService(
				userView,
				"ReadCurricularYearByOID",
				argsReadCurricularYearByOID);

		request.setAttribute(
			SessionConstants.CURRICULAR_YEAR,
			infoCurricularYear);

		/* Determine Selected Execution Degree */
		Integer executionDegreeOID =
			new Integer(
				(String) chooseScheduleContext.get("executionDegreeOID"));

		Object argsReadExecutionDegreeByOID[] = { executionDegreeOID };
		InfoExecutionDegree infoExecutionDegree =
			(InfoExecutionDegree) ServiceUtils.executeService(
				userView,
				"ReadExecutionDegreeByOID",
				argsReadExecutionDegreeByOID);

		if (infoExecutionDegree == null) {
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(
				"errors.invalid.execution.degree",
				new ActionError("errors.invalid.execution.degree"));
			saveErrors(request, actionErrors);
			return mapping.getInputForward();
		} else {
			request.setAttribute(
				SessionConstants.EXECUTION_DEGREE,
				infoExecutionDegree);
			return mapping.findForward("ManageSchedules");
		}
	}

	/**
	 * @return
	 */
	private List getLabelListOfCurricularYears() {
		ArrayList labelListOfCurricularYears = new ArrayList();
		labelListOfCurricularYears.add(new LabelValueBean("escolher", ""));
		labelListOfCurricularYears.add(new LabelValueBean("1 º", "1"));
		labelListOfCurricularYears.add(new LabelValueBean("2 º", "2"));
		labelListOfCurricularYears.add(new LabelValueBean("3 º", "3"));
		labelListOfCurricularYears.add(new LabelValueBean("4 º", "4"));
		labelListOfCurricularYears.add(new LabelValueBean("5 º", "5"));
		return labelListOfCurricularYears;
	}

	/**
	 * @return
	 */
	private List getLabelListOfExecutionDegrees(List executionDegreeList) {
		List labelListOfExecutionDegrees =
			(List) CollectionUtils.collect(
				executionDegreeList,
				new EXECUTION_DEGREE_2_EXECUTION_DEGREE_LABEL());
		labelListOfExecutionDegrees.add(0, new LabelValueBean("escolher", ""));
		return labelListOfExecutionDegrees;
	}

	private class EXECUTION_DEGREE_2_EXECUTION_DEGREE_LABEL
		implements Transformer {

		/* (non-Javadoc)
		 * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
		 */
		public Object transform(Object arg0) {
			InfoExecutionDegree infoExecutionDegree =
				(InfoExecutionDegree) arg0;

			String name =
				infoExecutionDegree
					.getInfoDegreeCurricularPlan()
					.getInfoDegree()
					.getNome();

			name =
				infoExecutionDegree
					.getInfoDegreeCurricularPlan()
					.getInfoDegree()
					.getTipoCurso()
					.toString()
					+ " de "
					+ name;

			return new LabelValueBean(
				name,
				infoExecutionDegree.getIdInternal().toString());
		}
	}

}