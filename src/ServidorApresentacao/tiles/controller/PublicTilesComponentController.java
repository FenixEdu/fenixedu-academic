package ServidorApresentacao.tiles.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.ControllerSupport;

import DataBeans.ISiteComponent;
import DataBeans.InfoEvaluation;
import DataBeans.InfoSiteAnnouncement;
import DataBeans.InfoSiteAssociatedCurricularCourses;
import DataBeans.InfoSiteBibliography;
import DataBeans.InfoSiteFirstPage;
import DataBeans.InfoSiteObjectives;
import DataBeans.InfoSiteProgram;
import DataBeans.InfoSiteSection;
import DataBeans.InfoSiteShifts;
import DataBeans.InfoSiteTimetable;
import DataBeans.SiteView;

/**
 * 
 * @author João Mota
 *         
 * 
 */
public class PublicTilesComponentController extends ControllerSupport {

	/**
	 * This Action is used for the jsp to know which jsp should include in in the tiles body component  
	 */

	public void perform(
		ComponentContext tileContext,
		HttpServletRequest request,
		HttpServletResponse response,
		ServletContext servletContext)
		throws ServletException, IOException {

		SiteView siteView = (SiteView) request.getAttribute("siteView");
		ISiteComponent component = siteView.getComponent();
		
		
		if (component instanceof InfoSiteFirstPage) {
			tileContext.putAttribute(
				"body",
				"/publico/viewExecutionCourse_bd.jsp");

		} else if (component instanceof InfoSiteAnnouncement) {
			tileContext.putAttribute(
				"body",
				"/publico/announcements_bd.jsp");
		} else if (component instanceof InfoSiteObjectives) {
			tileContext.putAttribute(
				"body",
				"/publico/objectives_bd.jsp");
		} else if (component instanceof InfoSiteProgram) {
			tileContext.putAttribute(
				"body",
				"/publico/program_bd.jsp");

		} else if (component instanceof InfoEvaluation) {
			tileContext.putAttribute(
				"body",
				"/publico/viewEvaluation_bd.jsp");
		} else if (component instanceof InfoSiteBibliography) {
			tileContext.putAttribute(
				"body",
				"/publico/bibliographicReferences_bd.jsp");
		} else if (component instanceof InfoSiteAssociatedCurricularCourses) {
			tileContext.putAttribute(
				"body",
				"/publico/curricularCourses_bd.jsp");
		} else if (component instanceof InfoSiteTimetable) {
			tileContext.putAttribute(
				"body",
				"/publico/viewTimeTable_bd.jsp");
		} else if (component instanceof InfoSiteShifts) {
			tileContext.putAttribute(
				"body",
				"/publico/viewExecutionCourseShifts_bd.jsp");

		} else if (component instanceof InfoSiteSection) {
			tileContext.putAttribute(
				"body",
				"/publico/viewSection_bd.jsp");
		}

	}

}
