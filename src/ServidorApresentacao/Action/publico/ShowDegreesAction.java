package ServidorApresentacao.Action.publico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Tânia Pousão Created on 9/Out/2003
 */
public class ShowDegreesAction extends FenixContextDispatchAction {

	public ActionForward nonMaster(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);
		InfoExecutionYear infoExecutionYear = null;
		if (infoExecutionPeriod != null) {
			infoExecutionYear = infoExecutionPeriod.getInfoExecutionYear();
		}

		GestorServicos gestor = GestorServicos.manager();
		Object[] args = { infoExecutionYear };
		List executionDegreesList = null;
		try {
			//ReadExecutionDegreesByExecutionYear
			executionDegreesList = (List) gestor.executar(null, "ReadNonMasterExecutionDegreesByExecutionYear", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		//buil a list of degrees by execution degrees list
		List degreesList = buildDegreesList(executionDegreesList);

		//put both list in request
		request.setAttribute("degreesList", degreesList);
		request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);

		System.out.println("DegreeList: " + degreesList.size());

		return mapping.findForward("showDegrees");
	}

	public ActionForward master(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);
		InfoExecutionYear infoExecutionYear = null;
		String ano = null;
		if (infoExecutionPeriod != null) {
			infoExecutionYear = infoExecutionPeriod.getInfoExecutionYear();
			ano = infoExecutionYear.getYear();
		}

		GestorServicos gestor = GestorServicos.manager();
		Object[] args = { ano };

		List executionDegreesList = null;
		try {
			//ReadExecutionDegreesByExecutionYear
			executionDegreesList = (List) gestor.executar(null, "ReadMasterDegrees", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		//buil a list of degrees by execution degrees list
		List degreesList = buildDegreesList(executionDegreesList);

		//put both list in request
		request.setAttribute("degreesList", degreesList);
		request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);

		System.out.println("DegreeList: " + degreesList.size());

		return mapping.findForward("showDegrees");
	}

	private List buildDegreesList(List executionDegreesList) {
		if (executionDegreesList == null) {
			return null;
		}
		
		List degreesList = new ArrayList();
		
		ListIterator listIterator = executionDegreesList.listIterator();
		while (listIterator.hasNext()) {
			InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) listIterator.next();

			if (!degreesList.contains(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree())) {
				degreesList.add(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree());
			}
		}
		
		//order list by alphabetic order of the code
		Collections.sort(degreesList, new BeanComparator("nome"));
		
		return degreesList;
	}
}
