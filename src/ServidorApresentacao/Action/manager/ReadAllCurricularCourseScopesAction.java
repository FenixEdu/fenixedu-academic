/*
 * Created on 16/Ago/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Fernanda Quitério
 * 28/10/2003
 * 
 */
public class ReadAllCurricularCourseScopesAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {

		IUserView userView = SessionUtils.getUserView(request);
		Integer curricularCourseId = new Integer(request.getParameter("curricularCourseId"));
		Integer degreeId = new Integer(request.getParameter("degreeId"));
		Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));
		request.setAttribute("curricularCourseId", curricularCourseId);
		request.setAttribute("degreeId", degreeId);
		request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanId);

		Object args[] = { curricularCourseId };

		List curricularCourseScopes = new ArrayList();
		try {
			curricularCourseScopes = (List) ServiceUtils.executeService(userView, "ReadCurricularCourseScopes", args);

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		if (curricularCourseScopes != null && !curricularCourseScopes.isEmpty()) {
			ComparatorChain comparatorChain = new ComparatorChain();
			comparatorChain.addComparator(new BeanComparator("beginDate.time"));
			Collections.sort(curricularCourseScopes, comparatorChain);

			InfoCurricularCourse infoCurricularCourse =
				((InfoCurricularCourseScope) curricularCourseScopes.get(0)).getInfoCurricularCourse();
			request.setAttribute("infoCurricularCourse", infoCurricularCourse);
		}

		request.setAttribute("curricularCourseScopesList", curricularCourseScopes);
		return mapping.findForward("viewCurricularCourseScopes");
	}
}
