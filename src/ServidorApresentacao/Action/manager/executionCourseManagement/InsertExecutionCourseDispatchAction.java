package ServidorApresentacao.Action.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.PeriodState;

/**
 * @author Fernanda Quitério 17/Dez/2003
 * 
 */
public class InsertExecutionCourseDispatchAction extends FenixDispatchAction
{
	public ActionForward prepareInsertExecutionCourse(
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
			
			ComparatorChain comparator = new ComparatorChain();
			comparator.addComparator(new BeanComparator("infoExecutionYear.year"),true);
			comparator.addComparator(new BeanComparator("name"), true);
			Collections.sort(infoExecutionPeriods, comparator);

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
							infoExecutionPeriod.getIdInternal().toString());
					return executionPeriod;
				}
			}, executionPeriodLabels);

			request.setAttribute(SessionConstants.LIST_EXECUTION_PERIODS, executionPeriodLabels);
		}
		
		DynaActionForm executionCourseForm = (DynaActionForm) form;
		executionCourseForm.set("theoreticalHours", new String("0.0"));
		executionCourseForm.set("praticalHours", new String("0.0"));
		executionCourseForm.set("theoPratHours", new String("0.0"));
		executionCourseForm.set("labHours", new String("0.0"));
		
		return mapping.findForward("insertExecutionCourse");
	}

	public ActionForward insertExecutionCourse(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
	throws FenixActionException
	{
		IUserView userView = SessionUtils.getUserView(request);

		InfoExecutionCourse infoExecutionCourse = fillInfoExecutionCourse(form, request);

		Object args[] = { infoExecutionCourse };
		try
		{
			ServiceUtils.executeService(userView, "InsertExecutionCourseAtExecutionPeriod", args);
		}
		catch (ExistingServiceException ex)
		{
			throw new ExistingActionException(ex.getMessage(), ex);
		}
		catch (NonExistingServiceException exception)
		{
			throw new NonExistingActionException(
					exception.getMessage(),
					mapping.getInputForward());
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		return mapping.findForward("firstPage");
	}

	private InfoExecutionCourse fillInfoExecutionCourse(ActionForm form, HttpServletRequest request)
	{
		DynaActionForm dynaForm = (DynaValidatorForm) form;

		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();

		String name = (String) dynaForm.get("name");
		infoExecutionCourse.setNome(name);

		String code = (String) dynaForm.get("code");
		infoExecutionCourse.setSigla(code);

		InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
		infoExecutionPeriod.setIdInternal(new Integer((String) dynaForm.get("executionPeriodId")));
		infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);

		String labHours = (String) dynaForm.get("labHours");
		if (labHours.compareTo("") != 0)
		{
			infoExecutionCourse.setLabHours(new Double(labHours));
		}
		String praticalHours = (String) dynaForm.get("praticalHours");
		if (praticalHours.compareTo("") != 0)
		{
			infoExecutionCourse.setPraticalHours(new Double(praticalHours));
		}
		String theoPratHours = (String) dynaForm.get("theoPratHours");
		if (theoPratHours.compareTo("") != 0)
		{
			infoExecutionCourse.setTheoPratHours(new Double(theoPratHours));
		}
		String theoreticalHours = (String) dynaForm.get("theoreticalHours");
		if (theoreticalHours.compareTo("") != 0)
		{
			infoExecutionCourse.setTheoreticalHours(new Double(theoreticalHours));
		}
		String comment = new String("");
		if ((String) dynaForm.get("comment") != null)
		{
			comment = (String) dynaForm.get("comment");
		}
		infoExecutionCourse.setComment(comment);
		return infoExecutionCourse;
	}
}