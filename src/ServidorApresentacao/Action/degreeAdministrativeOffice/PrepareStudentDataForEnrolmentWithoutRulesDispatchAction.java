package ServidorApresentacao.Action.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;

/**
 * @author David Santos
 */

public class PrepareStudentDataForEnrolmentWithoutRulesDispatchAction extends PrepareStudentDataDispatchAction {
	
	private final String[] forwards = { "startCurricularCourseEnrolmentWithoutRules", "error" };

	public ActionForward getStudentAndDegreeTypeForEnrolmentWithoutRules(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		boolean isSuccess = super.getStudentByNumberAndDegreeType(form, request);

		if(isSuccess) {
			DynaActionForm getStudentByNumberAndDegreeTypeForm = (DynaActionForm) form;
			HttpSession session = request.getSession();

			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

			List infoExecutionDegreesList = null;
			try {
				Integer degreeType = null;
				try {
					degreeType = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("degreeType"));
				} catch (NumberFormatException e) {
					degreeType = (Integer) request.getAttribute("degreeType");
					getStudentByNumberAndDegreeTypeForm.set("degreeType", degreeType.toString());
				}
				InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) session.getServletContext().getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);
				TipoCurso realDegreeType = new TipoCurso(degreeType);
				Object args[] = { infoExecutionPeriod.getInfoExecutionYear(), realDegreeType };
				infoExecutionDegreesList = (List) ServiceUtils.executeService(userView, "ReadExecutionDegreesByExecutionYearAndDegreeType", args);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}

			request.setAttribute(SessionConstants.DEGREE_LIST, this.getExecutionDegreesLableValueBeanList(infoExecutionDegreesList));

			request.setAttribute(SessionConstants.ENROLMENT_YEAR_LIST_KEY, this.generateCurricularYearList());
			request.setAttribute(SessionConstants.ENROLMENT_SEMESTER_LIST_KEY, this.generateCurricularSemesterList());

			return mapping.findForward(forwards[0]);
		} else {
			return mapping.getInputForward();
		}
	}

	public ActionForward error(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward(forwards[1]);
	}

	private List generateCurricularYearList() {
		List years = new ArrayList();
		years.add("label.first.year");
		years.add("label.second.year");
		years.add("label.third.year");
		years.add("label.fourth.year");
		years.add("label.fiveth.year");
		return years;
	}

	private List generateCurricularSemesterList() {
		List years = new ArrayList();
		years.add("label.first.semester");
		years.add("label.second.semester");
		return years;
	}

	private List getExecutionDegreesLableValueBeanList(List infoExecutionDegreesList) {
		ArrayList result = null;
		if ( (infoExecutionDegreesList != null) && (!infoExecutionDegreesList.isEmpty()) ) {
			result = new ArrayList();
			Iterator iterator = infoExecutionDegreesList.iterator();
			while(iterator.hasNext()) {
				InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
				Integer index = new Integer(infoExecutionDegreesList.indexOf(infoExecutionDegree));
				result.add(new LabelValueBean(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome(), index.toString()));
			}
		}
		this.sort(result);
		result.add(0, new LabelValueBean("Escolha", ""));
		return result;	
	}

	private void sort(List listOfLabelValueBeans) {
		ComparatorChain comparatorChain = new ComparatorChain();
		comparatorChain.addComparator(new BeanComparator("label"));
		if(listOfLabelValueBeans != null) {
			Collections.sort(listOfLabelValueBeans, comparatorChain);
		}
	}
}