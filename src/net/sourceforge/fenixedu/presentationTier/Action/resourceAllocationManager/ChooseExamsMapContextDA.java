/**
 * Project sop 
 * 
 * Package presentationTier.Action.sop
 * 
 * Created on 2/Apr/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurrentExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadExecutionDegreesByExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ChooseExamsMapContextDA extends FenixContextDispatchAction {

	public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InfoExecutionPeriod infoExecutionPeriod = setExecutionContext(request);

		/* Criar o bean de semestres */
		List semestres = new ArrayList();
		semestres.add(new LabelValueBean("escolher", ""));
		semestres.add(new LabelValueBean("1 º", "1"));
		semestres.add(new LabelValueBean("2 º", "2"));
		request.setAttribute("semestres", semestres);

		List curricularYearsList = new ArrayList();
		curricularYearsList.add("1");
		curricularYearsList.add("2");
		curricularYearsList.add("3");
		curricularYearsList.add("4");
		curricularYearsList.add("5");
		request.setAttribute(PresentationConstants.CURRICULAR_YEAR_LIST_KEY, curricularYearsList);

		/* Cria o form bean com as licenciaturas em execucao. */

		List executionDegreeList = ReadExecutionDegreesByExecutionYear.run(infoExecutionPeriod.getInfoExecutionYear());

		Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

		List licenciaturas = new ArrayList();

		licenciaturas.add(new LabelValueBean("escolher", ""));

		Iterator iterator = executionDegreeList.iterator();

		int index = 0;
		while (iterator.hasNext()) {
			InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
			String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

			name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getDegreeType().toString() + " de " + name;

			name +=
					duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-"
							+ infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";

			licenciaturas.add(new LabelValueBean(name, String.valueOf(index++)));
		}

		request.setAttribute(PresentationConstants.INFO_EXECUTION_DEGREE_LIST_KEY, executionDegreeList);

		request.setAttribute(PresentationConstants.DEGREES, licenciaturas);

		return mapping.findForward("chooseExamsMapContext");
	}

	public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaActionForm chooseExamContextoForm = (DynaActionForm) form;

		String[] selectedCurricularYears = (String[]) chooseExamContextoForm.get("selectedCurricularYears");

		Boolean selectAllCurricularYears = (Boolean) chooseExamContextoForm.get("selectAllCurricularYears");

		if ((selectAllCurricularYears != null) && selectAllCurricularYears.booleanValue()) {
			String[] allCurricularYears = { "1", "2", "3", "4", "5" };
			selectedCurricularYears = allCurricularYears;
		}

		List curricularYears = new ArrayList(selectedCurricularYears.length);
		for (String selectedCurricularYear : selectedCurricularYears) {
			curricularYears.add(new Integer(selectedCurricularYear));
			if (selectedCurricularYear.equals("1")) {
				request.setAttribute(PresentationConstants.CURRICULAR_YEARS_1, "1");
			}
			if (selectedCurricularYear.equals("2")) {
				request.setAttribute(PresentationConstants.CURRICULAR_YEARS_2, "2");
			}
			if (selectedCurricularYear.equals("3")) {
				request.setAttribute(PresentationConstants.CURRICULAR_YEARS_3, "3");
			}
			if (selectedCurricularYear.equals("4")) {
				request.setAttribute(PresentationConstants.CURRICULAR_YEARS_4, "4");
			}
			if (selectedCurricularYear.equals("5")) {
				request.setAttribute(PresentationConstants.CURRICULAR_YEARS_5, "5");
			}
		}

		request.setAttribute(PresentationConstants.CURRICULAR_YEARS_LIST, curricularYears);

		int index = Integer.parseInt((String) chooseExamContextoForm.get("index"));

		InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

		List executionDegreeList = ReadExecutionDegreesByExecutionYear.run(infoExecutionPeriod.getInfoExecutionYear());
		Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());
		// ////////

		InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) executionDegreeList.get(index);

		if (infoExecutionDegree != null) {
			request.setAttribute(PresentationConstants.EXECUTION_DEGREE, infoExecutionDegree);
			request.setAttribute(PresentationConstants.EXECUTION_DEGREE_OID, infoExecutionDegree.getIdInternal().toString());
		} else {
			return mapping.findForward("Licenciatura execucao inexistente");
		}

		return mapping.findForward("showExamsMap");

	}

	/**
	 * Method existencesOfInfoDegree.
	 * 
	 * @param executionDegreeList
	 * @param infoExecutionDegree
	 * @return int
	 */
	private boolean duplicateInfoDegree(List executionDegreeList, InfoExecutionDegree infoExecutionDegree) {
		InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
		Iterator iterator = executionDegreeList.iterator();

		while (iterator.hasNext()) {
			InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) iterator.next();
			if (infoDegree.equals(infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree())
					&& !(infoExecutionDegree.equals(infoExecutionDegree2))) {
				return true;
			}

		}
		return false;
	}

	/**
	 * Method setExecutionContext.
	 * 
	 * @param request
	 */
	private InfoExecutionPeriod setExecutionContext(HttpServletRequest request) throws Exception {

		InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) request.getAttribute(PresentationConstants.INFO_EXECUTION_PERIOD_KEY);
		if (infoExecutionPeriod == null) {
			IUserView userView = UserView.getUser();
			infoExecutionPeriod = ReadCurrentExecutionPeriod.run();

			request.setAttribute(PresentationConstants.INFO_EXECUTION_PERIOD_KEY, infoExecutionPeriod);
		}
		return infoExecutionPeriod;
	}

}