package ServidorApresentacao.Action.degreeAdministrativeOffice.depercated;

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
import ServidorApresentacao.Action.degreeAdministrativeOffice.PrepareStudentDataDispatchAction;
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

			Integer executionPeriodOID = null;
			try {
				executionPeriodOID = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("executionPeriodOID"));
			} catch (NumberFormatException e) {
				executionPeriodOID = (Integer) request.getAttribute("executionPeriodOID");
				getStudentByNumberAndDegreeTypeForm.set("executionPeriodOID", executionPeriodOID.toString());
			}

			List infoExecutionDegreesList = null;
			Integer degreeType = null;
			try {
				try {
					degreeType = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("degreeType"));
				} catch (NumberFormatException e) {
					degreeType = (Integer) request.getAttribute("degreeType");
					getStudentByNumberAndDegreeTypeForm.set("degreeType", degreeType.toString());
				}

//				InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) session.getServletContext().getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);
				InfoExecutionPeriod infoExecutionPeriod = PrepareStudentDataForEnrolmentWithoutRulesDispatchAction.getExecutionPeriod(request, executionPeriodOID);

				TipoCurso realDegreeType = new TipoCurso(degreeType);
				Object args[] = { infoExecutionPeriod.getInfoExecutionYear(), realDegreeType };
				infoExecutionDegreesList = (List) ServiceUtils.executeService(userView, "ReadExecutionDegreesByExecutionYearAndDegreeType", args);

				if(degreeType.intValue() == TipoCurso.MESTRADO) {
					TipoCurso realDegreeType2 = TipoCurso.LICENCIATURA_OBJ;
					Object args2[] = { infoExecutionPeriod.getInfoExecutionYear(), realDegreeType2 };
					List infoExecutionDegreesListToAdd = (List) ServiceUtils.executeService(userView, "ReadExecutionDegreesByExecutionYearAndDegreeType", args2);
					infoExecutionDegreesList.addAll(infoExecutionDegreesListToAdd);
				}

			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}

			request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, executionPeriodOID);

			request.setAttribute(SessionConstants.DEGREE_LIST, this.getExecutionDegreesLableValueBeanList(infoExecutionDegreesList, degreeType));

			request.setAttribute(SessionConstants.ENROLMENT_YEAR_LIST_KEY, this.generateCurricularYearList());
			request.setAttribute(SessionConstants.ENROLMENT_SEMESTER_LIST_KEY, this.generateCurricularSemesterList());

			return mapping.findForward(forwards[0]);
		} else {
			return mapping.getInputForward();
		}
	}

	public ActionForward error(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		List infoExecutionPeriodsList = null;
		try {
			HttpSession session = request.getSession();
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			infoExecutionPeriodsList = (List) ServiceUtils.executeService(userView, "ReadAllExecutionPeriodsForEnrollment", null);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		request.setAttribute(SessionConstants.EXECUTION_PERIOD, PrepareExecutionPeriodForEnrolmentContextAction.getExecutionPeriodsLableValueBeanList(infoExecutionPeriodsList));

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

	private List getExecutionDegreesLableValueBeanList(List infoExecutionDegreesList, Integer degreeType) {
		ArrayList result = null;
		if ( (infoExecutionDegreesList != null) && (!infoExecutionDegreesList.isEmpty()) ) {
			result = new ArrayList();
			Iterator iterator = infoExecutionDegreesList.iterator();
			while(iterator.hasNext()) {
				InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
//				Integer index = new Integer(infoExecutionDegreesList.indexOf(infoExecutionDegree));
				if( (degreeType.intValue() == TipoCurso.MESTRADO) && (infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso().equals(TipoCurso.LICENCIATURA_OBJ)) ) {
					result.add(new LabelValueBean("Licenciatura em " + infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome(), infoExecutionDegree.getIdInternal().toString()));
				} else {
					result.add(new LabelValueBean(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome(), infoExecutionDegree.getIdInternal().toString()));
				}
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

	public static InfoExecutionPeriod getExecutionPeriod(HttpServletRequest request, Integer executionPeriodOID) throws FenixActionException {
		InfoExecutionPeriod executionPeriod = null;
		try {
			HttpSession session = request.getSession();
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			Object[] args = {executionPeriodOID};
			executionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(userView, "ReadExecutionPeriodByOIDForEnrollment", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		return executionPeriod;
	}

}