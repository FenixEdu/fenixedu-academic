package ServidorApresentacao.Action.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoDegree;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoGratuityValues;
import DataBeans.InfoPaymentPhase;
import DataBeans.comparators.ComparatorByNameForInfoExecutionDegree;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.Data;
import Util.PeriodState;
import Util.Specialization;

/**
 * @author Fernanda Quitério 7/Jan/2003
 *  
 */
public class InsertGratuityDataDispatchAction extends DispatchAction
{

	public ActionForward prepareInsertChooseExecutionPeriod(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		IUserView userView = SessionUtils.getUserView(request);

		List infoExecutionPeriods = null;
		try
		{
			infoExecutionPeriods =
				(List) ServiceUtils.executeService(userView, "ReadExecutionPeriods", null);
		}
		catch (FenixServiceException ex)
		{
			throw new FenixActionException();
		}

		if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty())
		{
			infoExecutionPeriods = excludeClosedExecutionPeriods(infoExecutionPeriods);
			ComparatorChain comparator = new ComparatorChain();
			comparator.addComparator(new BeanComparator("infoExecutionYear.year"), true);
			comparator.addComparator(new BeanComparator("name"), true);
			Collections.sort(infoExecutionPeriods, comparator);

			List executionPeriodLabels = buildLabelValueBeanForJsp(infoExecutionPeriods);
			request.setAttribute(SessionConstants.LIST_EXECUTION_PERIODS, executionPeriodLabels);
		}
		return mapping.findForward("prepareInsertGratuityData");

	}

	private List excludeClosedExecutionPeriods(List infoExecutionPeriods)
	{
		// exclude closed execution periods
		infoExecutionPeriods = (List) CollectionUtils.select(infoExecutionPeriods, new Predicate()
		{
			public boolean evaluate(Object input)
			{
				InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) input;
				if (!infoExecutionPeriod.getState().equals(PeriodState.CLOSED))
				{
					return true;
				}
				return false;
			}
		});
		return infoExecutionPeriods;
	}

	private List buildLabelValueBeanForJsp(List infoExecutionPeriods)
	{
		List executionPeriodLabels = new ArrayList();
		CollectionUtils.collect(infoExecutionPeriods, new Transformer()
		{
			public Object transform(Object arg0)
			{
				InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) arg0;

				LabelValueBean executionPeriod =
					new LabelValueBean(
						infoExecutionPeriod.getName()
							+ " - "
							+ infoExecutionPeriod.getInfoExecutionYear().getYear(),
						infoExecutionPeriod.getName()
							+ " - "
							+ infoExecutionPeriod.getInfoExecutionYear().getYear()
							+ "#"
							+ infoExecutionPeriod.getIdInternal().toString());
				return executionPeriod;
			}
		}, executionPeriodLabels);
		return executionPeriodLabels;
	}

	public ActionForward prepareInsertGratuityDataChooseDegree(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		IUserView userView = SessionUtils.getUserView(request);

		Integer executionPeriodId =
			separateLabel(form, request, "executionPeriod", "executionPeriodName");

		Object args[] = { executionPeriodId };
		List executionDegreeList = null;
		try
		{
			executionDegreeList =
				(List) ServiceUtils.executeService(
					userView,
					"ReadExecutionDegreesByExecutionPeriodIdForGratuity",
					args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());
		List executionDegreeLabels = buildExecutionDegreeLabelValueBean(executionDegreeList);

		request.setAttribute(SessionConstants.DEGREES, executionDegreeLabels);
		request.setAttribute("specializations", Specialization.toArrayList());
		request.setAttribute("showNextSelects", "true");

		return prepareInsertChooseExecutionPeriod(mapping, form, request, response);
	}

	private List buildExecutionDegreeLabelValueBean(List executionDegreeList)
	{
		List executionDegreeLabels = new ArrayList();
		Iterator iterator = executionDegreeList.iterator();
		while (iterator.hasNext())
		{
			InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
			String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

			name =
				infoExecutionDegree
					.getInfoDegreeCurricularPlan()
					.getInfoDegree()
					.getTipoCurso()
					.toString()
					+ " em "
					+ name;

			name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree)
				? "-" + infoExecutionDegree.getInfoDegreeCurricularPlan().getName()
				: "";

			executionDegreeLabels.add(
				new LabelValueBean(name, name + "#" + infoExecutionDegree.getIdInternal().toString()));
		}
		return executionDegreeLabels;
	}

	private boolean duplicateInfoDegree(
		List executionDegreeList,
		InfoExecutionDegree infoExecutionDegree)
	{
		InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
		Iterator iterator = executionDegreeList.iterator();

		while (iterator.hasNext())
		{
			InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) iterator.next();
			if (infoDegree.equals(infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree())
				&& !(infoExecutionDegree.equals(infoExecutionDegree2)))
				return true;

		}
		return false;
	}

	private Integer separateLabel(
		ActionForm form,
		HttpServletRequest request,
		String property,
		String name)
	{
		DynaValidatorForm aForm = (DynaValidatorForm) form;
		// the value returned to action is a string name#idInternal
		String object = (String) aForm.get(property);
		Integer objectId = Integer.valueOf(StringUtils.substringAfter(object, "#"));
		object = object.substring(0, object.indexOf("#"));
		aForm.set(name, object);
		return objectId;
	}

	public ActionForward prepareInsertGratuityData(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		IUserView userView = SessionUtils.getUserView(request);

		//		InfoGratuity infoGratuity = null;
		//		try
		//		{
		//			infoGratuity =
		//			(InfoGratuity) ServiceUtils.executeService(userView, "ReadGratuityDataByExecutionDegreeId",
		// null);
		//		}
		//		catch (FenixServiceException ex)
		//		{
		//			throw new FenixActionException();
		//		}

		InfoGratuityValues infoGratuityValues = new InfoGratuityValues();

		fillForm(form, infoGratuityValues);

		request.setAttribute("infoGratuityValues", infoGratuityValues);
		return mapping.findForward("insertGratuityData");

	}

	private void fillForm(ActionForm form, InfoGratuityValues infoGratuityValues)
	{
		DynaValidatorForm aForm = (DynaValidatorForm) form;

		aForm.set("annualValue", infoGratuityValues.getAnualValue());
		aForm.set("scholarPart", infoGratuityValues.getScholarShipValue());
		aForm.set("thesisPart", infoGratuityValues.getFinalProofValue());
		if (infoGratuityValues.getProofRequestPayment() != null
			&& infoGratuityValues.getProofRequestPayment().equals(Boolean.TRUE))
		{
			aForm.set("paymentWhen", Boolean.TRUE);
		}
		if (infoGratuityValues.getCourseValue() != null)
		{
			aForm.set("unitaryValueCourse", infoGratuityValues.getCourseValue());
		}
		else
		{
			aForm.set("unitaryValueCredit", infoGratuityValues.getCreditValue());
		}
		if (infoGratuityValues.getEndPayment() != null)
		{
			aForm.set(
				"finalDateTotalPayment",
				Data.format2DayMonthYear(infoGratuityValues.getEndPayment(), "/"));
			aForm.set("totalPayment", Boolean.TRUE);
		}
		if (infoGratuityValues.getStartPayment() != null)
		{
			aForm.set(
				"initialDateTotalPayment",
				Data.format2DayMonthYear(infoGratuityValues.getStartPayment(), "/"));
		}
		if (infoGratuityValues.getInfoPaymentPhases() != null
			&& infoGratuityValues.getInfoPaymentPhases().size() > 0)
		{
			aForm.set("partialPayment", Boolean.TRUE);
			InfoPaymentPhase infoPaymentPhase =
				(
					InfoPaymentPhase) CollectionUtils
						.find(infoGratuityValues.getInfoPaymentPhases(), new Predicate()
			{
				public boolean evaluate(Object arg0)
				{
					InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) arg0;

					if (infoPaymentPhase.getDescription() != null
						&& infoPaymentPhase.getDescription().equals(
							ServidorApresentacao
								.Action
								.masterDegree
								.utils
								.SessionConstants
								.REGISTRATION_PAYMENT))
					{
						return true;
					}
					return false;
				}
			});
			if (infoPaymentPhase != null)
			{
				aForm.set("registrationPayment", Boolean.TRUE);
				aForm.set(
					"finalDateRegistrationPayment",
					Data.format2DayMonthYear(infoPaymentPhase.getEndDate(), "/"));
				aForm.set("registrationPaymentValue", infoPaymentPhase.getValue());
				if (infoPaymentPhase.getStartDate() != null)
				{
					aForm.set(
						"initialDateRegistrationPayment",
						Data.format2DayMonthYear(infoPaymentPhase.getStartDate(), "/"));
				}
			}

			CollectionUtils.filter(infoGratuityValues.getInfoPaymentPhases(), new Predicate()
			{
				public boolean evaluate(Object arg0)
				{
					InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) arg0;

					if (infoPaymentPhase.getDescription() != null
						&& !infoPaymentPhase.getDescription().equals(
							ServidorApresentacao
								.Action
								.masterDegree
								.utils
								.SessionConstants
								.REGISTRATION_PAYMENT))
					{
						return true;
					}
					return false;
				}
			});

			Collections.sort(infoGratuityValues.getInfoPaymentPhases(), new BeanComparator("endDate"));
		}
	}
}