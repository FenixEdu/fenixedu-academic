/*
 * Created on 24/Set/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadExecutionCoursesByExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author lmac1
 */
@Mapping(module = "manager", path = "/readExecutionCourses", input = "/readExecutionPeriods.do", scope = "request")
@Forwards(value = { @Forward(
		name = "readExecutionCourses",
		path = "/manager/readExecutionCoursesByExecutionPeriod_bd.jsp",
		tileProperties = @Tile(navLocal = "/manager/executionCourseManagement/mainMenu.jsp")) })
public class ReadExecutionCoursesAction extends FenixAction {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws FenixActionException, FenixFilterException {
		IUserView userView = UserView.getUser();

		Integer executionPeriodId = new Integer(request.getParameter("executionPeriodId"));
		List executionCourses = null;

		try {
			executionCourses = ReadExecutionCoursesByExecutionPeriod.run(executionPeriodId);

			if (executionCourses != null && executionCourses.size() > 0) {
				Collections.sort(executionCourses, new BeanComparator("nome"));
				InfoExecutionPeriod infoExecutionPeriod =
						((InfoExecutionCourse) executionCourses.get(0)).getInfoExecutionPeriod();

				String executionPeriodNameAndYear =
						new String(infoExecutionPeriod.getName() + "-" + infoExecutionPeriod.getInfoExecutionYear().getYear());
				request.setAttribute("executionPeriodNameAndYear", executionPeriodNameAndYear);
			}
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		request.setAttribute("infoExecutionCoursesList", executionCourses);

		return mapping.findForward("readExecutionCourses");
	}
}