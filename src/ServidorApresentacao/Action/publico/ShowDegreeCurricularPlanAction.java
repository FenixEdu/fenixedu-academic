package ServidorApresentacao.Action.publico;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Tânia Pousão
 * Created on 9/Out/2003
 */
public class ShowDegreeCurricularPlanAction extends FenixContextAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		super.execute(mapping, form, request, response);

		InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);
	//	InfoExecutionYear infoExecutionYear = null;
		//if (infoExecutionPeriod != null) {
		//	InfoExecutionYear infoExecutionYear = infoExecutionPeriod.getInfoExecutionYear();
	//	}

		Integer degreeCurricularPlanId = null;
		String degreeCurricularPlanIdString = request.getParameter("degreeCurricularPlanId");
		if (degreeCurricularPlanIdString == null) {
			degreeCurricularPlanIdString = (String) request.getAttribute("degreeCurricularPlanId");
		}
		degreeCurricularPlanId = new Integer(degreeCurricularPlanIdString);

		GestorServicos gestor = GestorServicos.manager();
		Object[] args = { degreeCurricularPlanId };

		List curricularCourseScopesList = null;
		try {
			curricularCourseScopesList = (List) gestor.executar(null, "ReadCurricularCourseScopeListByDegreeCurricularPlan", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		//order list by year, next semester, next course 
		if (curricularCourseScopesList != null) {
			ComparatorChain comparatorChain = new ComparatorChain();
			comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.infoCurricularYear.year"));
			comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.semester"));
			comparatorChain.addComparator(new BeanComparator("infoCurricularCourse.name"));
			Collections.sort(curricularCourseScopesList, comparatorChain);
		}
		
		request.setAttribute("curricularCourseScopesList", curricularCourseScopesList);
		request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);

		System.out.println("CurricularCourseScopesList: " + curricularCourseScopesList.size());
		
		return mapping.findForward("showDegreeCurricularPlan");
	}
}