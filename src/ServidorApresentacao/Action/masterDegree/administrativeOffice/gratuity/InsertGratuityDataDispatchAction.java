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
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionYear;
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
import Util.Specialization;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Fernanda Quitério 7/Jan/2003
 *  
 */
public class InsertGratuityDataDispatchAction extends DispatchAction
{

	public ActionForward prepareInsertChooseExecutionYear(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		//execution years
		List executionYears = null;
		Object[] args = {
		};
		try
		{
			executionYears =
				(List) ServiceManagerServiceFactory.executeService(
					null,
					"ReadNotClosedExecutionYears",
					args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException();
		}

		if (executionYears != null && !executionYears.isEmpty())
		{
			ComparatorChain comparator = new ComparatorChain();
			comparator.addComparator(new BeanComparator("year"), true);
			Collections.sort(executionYears, comparator);

			List executionYearLabels = buildLabelValueBeanForJsp(executionYears);
			request.setAttribute("executionYears", executionYearLabels);
		}
		return mapping.findForward("prepareInsertGratuityData");

	}

	private List buildLabelValueBeanForJsp(List infoExecutionYears)
	{
		List executionYearLabels = new ArrayList();
		CollectionUtils.collect(infoExecutionYears, new Transformer()
		{
			public Object transform(Object arg0)
			{
				InfoExecutionYear infoExecutionYear = (InfoExecutionYear) arg0;

				LabelValueBean executionYear =
					new LabelValueBean(infoExecutionYear.getYear(), infoExecutionYear.getYear());
				return executionYear;
			}
		}, executionYearLabels);
		return executionYearLabels;
	}

	public ActionForward prepareInsertGratuityDataChooseDegree(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm gratuityForm = (DynaActionForm) form;

		String executionYear = (String) gratuityForm.get("executionYear");

		InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
		infoExecutionYear.setYear(executionYear);

		Object args[] = { infoExecutionYear };
		List executionDegreeList = null;
		try
		{
			executionDegreeList =
				(List) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadExecutionDegreesByExecutionYear",
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

		return prepareInsertChooseExecutionYear(mapping, form, request, response);
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
		DynaValidatorForm aForm,
		HttpServletRequest request,
		String property,
		String name)
	{
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
		DynaValidatorForm gratuityForm = (DynaValidatorForm) form;

		String executionYear = (String) gratuityForm.get("executionYear");
		String degree = (String) gratuityForm.get("degree");
		System.out.println(degree);
		Integer degreeId = Integer.valueOf(StringUtils.substringAfter(degree, "#"));
		System.out.println(degreeId);
		String degreeName = degree.substring(0, degree.indexOf("#"));
		System.out.println(degreeName);
//		String degreeCurricularPlan = degree.substring(degree.indexOf("-") + 1, degree.indexOf("#"));
//		System.out.println(degreeCurricularPlan);
		request.setAttribute("degree", degreeName);
		gratuityForm.set("degree",degreeName);
		request.setAttribute("executionYear", executionYear);
//		request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);


		InfoGratuityValues infoGratuityValues = null;
		Object args[] = { degreeId };
		try
		{
			infoGratuityValues =
				(InfoGratuityValues) ServiceUtils.executeService(
					userView,
					"ReadGratuityValuesByExecutionDegree",
					args);
		}
		catch (FenixServiceException ex)
		{
			throw new FenixActionException();
		}

		if (infoGratuityValues == null)
		{
			infoGratuityValues = new InfoGratuityValues();
/*			InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
			infoExecutionYear.setYear(executionYear);
			InfoDegree infoDegree = new InfoDegree();
			infoDegree.setNome(degreeName);
			InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
			infoDegreeCurricularPlan.setName(degreeCurricularPlan);
			infoDegreeCurricularPlan.setInfoDegree(infoDegree);
			InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
			infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);
			infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

			infoGratuityValues.setInfoExecutionDegree(infoExecutionDegree);*/
		}
		fillForm(gratuityForm, infoGratuityValues);
		request.setAttribute("infoPaymentPhases", infoGratuityValues.getInfoPaymentPhases());
		return mapping.findForward("insertGratuityData");
	}

	private void fillForm(DynaValidatorForm aForm, InfoGratuityValues infoGratuityValues)
	{
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

			if (infoGratuityValues.getRegistrationPayment().booleanValue())
			{
				aForm.set("registrationPayment", infoGratuityValues.getRegistrationPayment());

				InfoPaymentPhase infoPaymentPhase = findRegistrationPayment(infoGratuityValues);

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

				removeRegistrationPaymentFromPhases(infoGratuityValues);
			}
			Collections.sort(infoGratuityValues.getInfoPaymentPhases(), new BeanComparator("endDate"));
		}
	}

	private void removeRegistrationPaymentFromPhases(InfoGratuityValues infoGratuityValues)
	{
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
	}

	private InfoPaymentPhase findRegistrationPayment(InfoGratuityValues infoGratuityValues)
	{
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
		return infoPaymentPhase;
	}
}