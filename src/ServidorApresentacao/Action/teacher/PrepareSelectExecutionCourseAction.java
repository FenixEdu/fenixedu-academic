package ServidorApresentacao.Action.teacher;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.ISiteComponent;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteSection;
import DataBeans.InfoSiteShiftsAndGroups;
import DataBeans.SiteView;
import DataBeans.TeacherAdministrationSiteView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author joaosa & rmalo
 */
public class PrepareSelectExecutionCourseAction extends FenixContextAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException, FenixFilterException {
		try {
			super.execute(mapping, form, request, response);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		

		String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
		Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);		
		ISiteComponent shiftsAndGroupsView = new InfoSiteShiftsAndGroups();
		readSiteView(request, shiftsAndGroupsView, null, groupPropertiesCode, null);
		

		InfoExecutionCourse executionCourse = new InfoExecutionCourse();

		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) request.getAttribute(
				SessionConstants.EXECUTION_PERIOD);
				
		InfoExecutionDegree infoExecutionDegree =
			RequestUtils.getExecutionDegreeFromRequest(
				request,
				infoExecutionPeriod.getInfoExecutionYear());

		executionCourse.setInfoExecutionPeriod(infoExecutionPeriod);

		Integer curricularYear = (Integer) request.getAttribute("curYear");

		Object argsSelectExecutionCourse[] =
			{ infoExecutionDegree, infoExecutionPeriod, curricularYear };

		List infoExecutionCourses;
		try {
			infoExecutionCourses =
				(List) ServiceManagerServiceFactory.executeService(
					null,
					"SelectExportExecutionCourse",
					argsSelectExecutionCourse);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		Collections.sort(infoExecutionCourses,new BeanComparator("nome"));
		request.setAttribute("exeCourseList", infoExecutionCourses);
		return mapping.findForward("sucess");
	}

	private SiteView readSiteView(HttpServletRequest request, ISiteComponent firstPageComponent,
			Integer infoExecutionCourseCode, Object obj1, Object obj2)
			throws FenixActionException, FenixFilterException
			{

		HttpSession session = getSession(request);

		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer objectCode = null;
		if (infoExecutionCourseCode == null)
		{
			objectCode = getObjectCode(request);
			infoExecutionCourseCode = objectCode;
		}

		ISiteComponent commonComponent = new InfoSiteCommon();
		Object[] args = {infoExecutionCourseCode, commonComponent, firstPageComponent, objectCode, obj1,
				obj2};

		try
		{
			TeacherAdministrationSiteView siteView = (TeacherAdministrationSiteView) ServiceUtils
					.executeService(userView, "TeacherAdministrationSiteComponentService", args);
	request.setAttribute("siteView", siteView);
	request.setAttribute("objectCode", ((InfoSiteCommon) siteView.getCommonComponent())
					.getExecutionCourse().getIdInternal());
	if (siteView.getComponent() instanceof InfoSiteSection)
	{
		request.setAttribute("infoSection", ((InfoSiteSection) siteView.getComponent())
						.getSection());
	}

	return siteView;

		} catch (FenixServiceException e)
		{
	throw new FenixActionException(e);
		}

			}

	private Integer getObjectCode(HttpServletRequest request)
	{
		Integer objectCode = null;
		String objectCodeString = request.getParameter("objectCode");
		if (objectCodeString == null)
		{
			objectCodeString = (String) request.getAttribute("objectCode");
		}
		if (objectCodeString != null)
		{
			objectCode = new Integer(objectCodeString);
		}
		return objectCode;
	}
	
	
}
