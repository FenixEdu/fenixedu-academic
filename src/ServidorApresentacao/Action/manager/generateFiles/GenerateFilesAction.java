/*
 * Created on 27/Jan/2004
 *  
 */
package ServidorApresentacao.Action.manager.generateFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoExecutionYear;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Tânia Pousão
 *  
 */
public class GenerateFilesAction extends FenixDispatchAction
{
	public ActionForward firstPage(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		return mapping.findForward("firstPage");
	}

	public ActionForward prepareChooseForGenerateFiles(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		String file = request.getParameter("file");
		request.setAttribute("file", file);

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
		return mapping.findForward("chooseForGenerateFiles");
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

	public ActionForward generateGratuityFile(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		String fileType = request.getParameter("file");
		request.setAttribute("file", fileType);

		String executionYear = request.getParameter("executionYear");
		request.setAttribute("executionYear", executionYear);

		Object[] args = { null, executionYear, null, null };
		HashMap result = null;
		ActionErrors errors = new ActionErrors();
		try
		{
			result =
				(HashMap) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadGratuitySituationListByExecutionDegreeAndSpecializationInManager",
					args);
		}
		catch (FenixServiceException exception)
		{
			exception.printStackTrace();
			errors.add("noList", new ActionError("error.generateFiles.emptyList"));
			saveErrors(request, errors);
			return mapping.getInputForward();
		}
		if (result == null)
		{
			errors.add("noList", new ActionError("error.generateFiles.emptyList"));
			saveErrors(request, errors);
			return mapping.getInputForward();
		}

		//gratuity situation list
		List infoGratuitySituationList = (List) result.get(new Integer(0));
		if (infoGratuitySituationList == null || infoGratuitySituationList.size() <= 0)
		{
			errors.add("noList", new ActionError("error.generateFiles.emptyList"));
			saveErrors(request, errors);
			return mapping.getInputForward();
		}

		System.out.println("-->List: " + infoGratuitySituationList.size());
		System.out.println("-->File Type: " + fileType);
		//Create respective file
		File file = null;
		if (fileType.equals(new String("sibs")))
		{
			file = GratuityFileSIBS.buildFile(infoGratuitySituationList);

		}
		else if (fileType.equals(new String("letters")))
		{
			file = GratuityFileLetters.buildFile(infoGratuitySituationList);
		}

		if (file == null || file.length() == 0)
		{
			errors.add("noList", new ActionError("error.generateFiles.errorFile"));
			saveErrors(request, errors);
			return mapping.getInputForward();
		}
		return mapping.findForward("confirmation");
	}
}
