package ServidorApresentacao.Action.coordinator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoDegreeCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Fernanda Quitério 
 * 06/Nov/2003
 */
public class DegreeCurricularPlanManagementDispatchAction extends FenixDispatchAction {
	public ActionForward showActiveCurricularCourses(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer infoExecutionDegreeCode = new Integer(request.getParameter("infoExecutionDegreeCode"));
		request.setAttribute("infoExecutionDegreeCode", infoExecutionDegreeCode);

		InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
		ActionErrors errors = new ActionErrors();
		Object[] args = { infoExecutionDegreeCode };
		try {
			infoDegreeCurricularPlan =
				(InfoDegreeCurricularPlan) ServiceUtils.executeService(userView, "ReadActiveDegreeCurricularPlanByExecutionDegreeCode", args);

		} catch (IllegalArgumentException e) {
			errors.add("nullCode", new ActionError("error.coordinator.noExecutionDegree"));
			saveErrors(request, errors);
		} catch (NonExistingServiceException e) {
			errors.add("chosenDegree", new ActionError("error.coordinator.chosenDegree"));
			saveErrors(request, errors);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		if (infoDegreeCurricularPlan == null) {
			errors.add("noDegreeCurricularPlan", new ActionError("error.coordinator.noDegreeCurricularPlan"));
			saveErrors(request, errors);
		}

		//order list by year, next semester, next course
		if (infoDegreeCurricularPlan.getCurricularCourses() != null) {
			List allActiveCurricularCourseScopes = new ArrayList();
			Iterator iter = infoDegreeCurricularPlan.getCurricularCourses().iterator();
			while (iter.hasNext()) {
				InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iter.next();
				allActiveCurricularCourseScopes.addAll(infoCurricularCourse.getInfoScopes());
			}
			ComparatorChain comparatorChain = new ComparatorChain();
			comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.infoCurricularYear.year"));
			comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.semester"));
			comparatorChain.addComparator(new BeanComparator("infoCurricularCourse.name"));
			Collections.sort(allActiveCurricularCourseScopes, comparatorChain);

			request.setAttribute("allActiveCurricularCourseScopes", allActiveCurricularCourseScopes);
		}
		if (!errors.isEmpty()) {
			return mapping.getInputForward();
		}
		return mapping.findForward("showActiveCurricularCourses");
	}

	public ActionForward showCurricularCoursesHistory(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer infoExecutionDegreeCode = new Integer(request.getParameter("infoExecutionDegreeCode"));
		request.setAttribute("infoExecutionDegreeCode", infoExecutionDegreeCode);

		InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
		ActionErrors errors = new ActionErrors();
		Object[] args = { infoExecutionDegreeCode };
		try {
			infoDegreeCurricularPlan =
				(InfoDegreeCurricularPlan) ServiceUtils.executeService(userView, "ReadDegreeCurricularPlanHistoryByExecutionDegreeCode", args);

		} catch (IllegalArgumentException e) {
			errors.add("nullCode", new ActionError("error.coordinator.noExecutionDegree"));
			saveErrors(request, errors);
		} catch (NonExistingServiceException e) {
			errors.add("chosenDegree", new ActionError("error.coordinator.chosenDegree"));
			saveErrors(request, errors);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		if (infoDegreeCurricularPlan == null) {
			errors.add("noDegreeCurricularPlan", new ActionError("error.coordinator.noDegreeCurricularPlan"));
			saveErrors(request, errors);
		}

		if (infoDegreeCurricularPlan.getCurricularCourses() != null) {

			//order list by year, next semester, next course
			List allCurricularCourseScopes = new ArrayList();
			Iterator iter = infoDegreeCurricularPlan.getCurricularCourses().listIterator();
			while (iter.hasNext()) {
				InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iter.next();
				allCurricularCourseScopes.addAll(infoCurricularCourse.getInfoScopes());
			}
			ComparatorChain comparatorChain = new ComparatorChain();
			comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.infoCurricularYear.year"));
			comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.semester"));
			comparatorChain.addComparator(new BeanComparator("infoCurricularCourse.name"));
			comparatorChain.addComparator(new BeanComparator("beginDate.time"));
			Collections.sort(allCurricularCourseScopes, comparatorChain);

			// build hashmap for jsp
			HashMap curricularCourseScopesHashMap = new HashMap();
			Integer lastKey = new Integer(0);
			Iterator iterCurricularCourseScopes = allCurricularCourseScopes.iterator();
			while (iterCurricularCourseScopes.hasNext()) {
				InfoCurricularCourseScope curricularCourseScope = (InfoCurricularCourseScope) iterCurricularCourseScopes.next();

				List equalCurricularCourseScopes = null;
				if (lastKey.intValue() != 0) {
					InfoCurricularCourseScope lastIntroduced =
						(InfoCurricularCourseScope) ((List) curricularCourseScopesHashMap.get(lastKey)).get(0);

					if (scopesAreEqual(curricularCourseScope, lastIntroduced)) {
						equalCurricularCourseScopes = (List) curricularCourseScopesHashMap.get(lastKey);
						equalCurricularCourseScopes.add(curricularCourseScope);
						curricularCourseScopesHashMap.put(lastKey, equalCurricularCourseScopes);
						continue;
					}
				}
				equalCurricularCourseScopes = new ArrayList();
				equalCurricularCourseScopes.add(curricularCourseScope);
				curricularCourseScopesHashMap.put(curricularCourseScope.getIdInternal(), equalCurricularCourseScopes);
				lastKey = curricularCourseScope.getIdInternal();
			}

			request.setAttribute("allCurricularCourseScopes", allCurricularCourseScopes);
			request.setAttribute("curricularCourseScopesHashMap", curricularCourseScopesHashMap);
		}
		if (!errors.isEmpty()) {
			return mapping.getInputForward();
		}
		return mapping.findForward("showCurricularCoursesHistory");
	}

	private boolean scopesAreEqual(
		InfoCurricularCourseScope curricularCourseScope,
		InfoCurricularCourseScope nextCurricularCourseScope) {
		boolean result = false;
		if (curricularCourseScope
			.getInfoCurricularSemester()
			.getInfoCurricularYear()
			.getYear()
			.equals(nextCurricularCourseScope.getInfoCurricularSemester().getInfoCurricularYear().getYear())
			&& curricularCourseScope.getInfoCurricularSemester().getSemester().equals(
				nextCurricularCourseScope.getInfoCurricularSemester().getSemester())
			&& curricularCourseScope.getInfoCurricularCourse().getName().equalsIgnoreCase(
				nextCurricularCourseScope.getInfoCurricularCourse().getName())
			&& curricularCourseScope.getInfoBranch().getCode().equalsIgnoreCase(nextCurricularCourseScope.getInfoBranch().getCode())) {

			result = true;
		}
		return result;
	}
}