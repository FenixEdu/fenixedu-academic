package ServidorApresentacao.Action.equivalence;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author David Santos in Apr 30, 2004
 */

public class CreateStudentCurricularPlanDispatchAction extends DispatchAction
{
	public ActionForward showForm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception
	{
		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		String degreeTypeCode = request.getParameter("degreeType");
		String studentNumber = request.getParameter("studentNumber");
		String backLink = request.getParameter("backLink");

		TipoCurso degreeType = new TipoCurso();
		degreeType.setTipoCurso(Integer.valueOf(degreeTypeCode));

		Object args1[] = {studentNumber, degreeType};

		InfoStudent infoStudent = null;

		try
		{
			infoStudent = (InfoStudent) ServiceManagerServiceFactory.executeService(userView, "ReadStudentByNumberAndDegreeType",
				args1);
		} catch (FenixServiceException e)
		{
			return mapping.getInputForward();
		}
		
		if (infoStudent == null)
		{
			return mapping.getInputForward();
		}

		List infoBranches = null;

		Object[] args2 = { null, null, studentNumber };
		try
		{
			infoBranches = (List) ServiceManagerServiceFactory.executeService(userView,
				"ReadSpecializationAndSecundaryAreasByStudent", args2);
		}
		catch (Exception e)
		{
			return mapping.getInputForward();
		}

		if ( (infoBranches == null) || (infoBranches.isEmpty()) )
		{
			return mapping.getInputForward();
		}

		List infoDegreeCurricularPlans = null;

		Object[] args3 = { degreeType };
		try
		{
			infoDegreeCurricularPlans = (List) ServiceManagerServiceFactory.executeService(userView,
				"ReadAllDegreeCurricularPlansByDegreeTypeExceptForPASTOnes", args3);
		}
		catch (Exception e)
		{
			return mapping.getInputForward();
		}

		if ( (infoDegreeCurricularPlans == null) || (infoDegreeCurricularPlans.isEmpty()) )
		{
			return mapping.getInputForward();
		}

		request.setAttribute("degreeType", degreeTypeCode);
		request.setAttribute("studentOID", infoStudent.getIdInternal());
		request.setAttribute("infoBranches", infoBranches);
		request.setAttribute("infoDegreeCurricularPlans", buildLabelValueBeanForDegreeCurricularPlans(infoDegreeCurricularPlans));
		request.setAttribute("studentCurricularPlanStates", buildLabelValueBeanForStudentCurricularPlanStates());
		request.setAttribute("backLink", backLink);

		return mapping.findForward("showFormToCreateStudentCurricularPlan");
	}



	// ============================================================================================================	
	// ============================================================================================================	
	// ============================================================================================================	
	
	
	private List buildLabelValueBeanForDegreeCurricularPlans(List infoDegreeCurricularPlans)
	{
		List labelValueBeans = new ArrayList();
		CollectionUtils.collect(infoDegreeCurricularPlans, new Transformer()
		{
			public Object transform(Object arg0)
			{
				InfoDegreeCurricularPlan infoDegreeCurricularPlan = (InfoDegreeCurricularPlan) arg0;

				LabelValueBean labelValueBean = new LabelValueBean(infoDegreeCurricularPlan.getInfoDegree().getNome() + " - "
					+ infoDegreeCurricularPlan.getName(), infoDegreeCurricularPlan.getIdInternal().toString());

				return labelValueBean;
			}
		}, labelValueBeans);
		return labelValueBeans;
	}

	private List buildLabelValueBeanForStudentCurricularPlanStates()
	{
		List labelValueBeans = new ArrayList();
		
		

		return labelValueBeans;
	}
}