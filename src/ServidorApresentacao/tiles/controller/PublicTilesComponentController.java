package ServidorApresentacao.tiles.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.ControllerSupport;

import DataBeans.ISiteComponent;
import DataBeans.InfoEvaluationMethod;
import DataBeans.InfoSiteAnnouncement;
import DataBeans.InfoSiteAssociatedCurricularCourses;
import DataBeans.InfoSiteBibliography;
import DataBeans.InfoSiteCurricularCourse;
import DataBeans.InfoSiteEvaluation;
import DataBeans.InfoSiteEvaluationMethods;
import DataBeans.InfoSiteFirstPage;
import DataBeans.InfoSiteMarks;
import DataBeans.InfoSiteObjectives;
import DataBeans.InfoSiteProgram;
import DataBeans.InfoSiteProjects;
import DataBeans.InfoSiteSection;
import DataBeans.InfoSiteShifts;
import DataBeans.InfoSiteShiftsAndGroups;
import DataBeans.InfoSiteStudentGroup;
import DataBeans.InfoSiteSummaries;
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
	 * This Action is used for the jsp to know which jsp should include in the tiles body component  
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
			tileContext.putAttribute("body", "/publico/announcements_bd.jsp");
		} else if (component instanceof InfoSiteCurricularCourse) {
			
			tileContext.putAttribute(
				"body",
				"/publico/viewCurricularCourse_bd.jsp");
		} else if (component instanceof InfoSiteSummaries) {
			tileContext.putAttribute("body", "/publico/summaries_bd.jsp");
		} else if (component instanceof InfoSiteObjectives) {
			tileContext.putAttribute("body", "/publico/objectives_bd.jsp");
		} else if (component instanceof InfoSiteProgram) {
			tileContext.putAttribute("body", "/publico/program_bd.jsp");

		} else if (component instanceof InfoSiteEvaluationMethods) {
			tileContext.putAttribute("body", "/publico/viewEvaluation_bd.jsp");
		} else if (component instanceof InfoEvaluationMethod) {
					tileContext.putAttribute("body", "/publico/viewEvaluationMethod_bd.jsp");			
		} else if (component instanceof InfoSiteBibliography) {
			tileContext.putAttribute(
				"body",
				"/publico/bibliographicReferences_bd.jsp");
		} else if (component instanceof InfoSiteAssociatedCurricularCourses) {
			tileContext.putAttribute(
				"body",
				"/publico/curricularCourses_bd.jsp");
		} else if (component instanceof InfoSiteTimetable) {
			tileContext.putAttribute("body", "/publico/viewTimeTable_bd.jsp");
		} else if (component instanceof InfoSiteShifts) {
			tileContext.putAttribute(
				"body",
				"/publico/viewExecutionCourseShifts_bd.jsp");

		} else if (component instanceof InfoSiteSection) {
			tileContext.putAttribute("body", "/publico/viewSection_bd.jsp");
		} else if (component instanceof InfoSiteEvaluation) {
			tileContext.putAttribute("body", "/publico/exam.jsp");
		} else if (component instanceof InfoSiteMarks) {
			tileContext.putAttribute(
				"body",
				"/publico/viewPublishedMarks_bd.jsp");
		}else 
		
		if (component instanceof InfoSiteProjects) 
		{
			//List infoGroupPropertiesList = ((InfoSiteProjects)component).getInfoGroupPropertiesList();
				tileContext.putAttribute("body","/publico/viewProjectsName_bd.jsp");
		}
		else if (component instanceof InfoSiteShiftsAndGroups) {
					tileContext.putAttribute("body","/publico/viewShiftsAndGroups_bd.jsp");
				}
	
		else if (component instanceof InfoSiteStudentGroup) {
			tileContext.putAttribute("body","/publico/viewStudentGroupInformation_bd.jsp");
			}
	}

}
