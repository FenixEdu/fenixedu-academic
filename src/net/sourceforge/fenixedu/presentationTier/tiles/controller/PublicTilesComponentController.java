package net.sourceforge.fenixedu.presentationTier.tiles.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluationMethod;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteAssociatedCurricularCourses;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteBibliography;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEvaluationMarks;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEvaluationMethods;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEvaluations;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteFirstPage;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteMarks;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteObjectives;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteProgram;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteProjects;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShifts;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShiftsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteTimetable;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;

import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.ControllerSupport;

/**
 * 
 * @author João Mota
 * 
 * 
 */
public class PublicTilesComponentController extends ControllerSupport {

    /**
     * This Action is used for the jsp to know which jsp should include in the
     * tiles body component
     */

    public void perform(ComponentContext tileContext, HttpServletRequest request,
            HttpServletResponse response, ServletContext servletContext) {

        SiteView siteView = (SiteView) request.getAttribute("siteView");
        ISiteComponent component = siteView.getComponent();

        if (component instanceof InfoSiteFirstPage) {
            tileContext.putAttribute("body", "/publico/viewExecutionCourse_bd.jsp");
        } else if (component instanceof InfoSiteCurricularCourse) {
            tileContext.putAttribute("body", "/publico/viewCurricularCourse_bd.jsp");
        } else if (component instanceof InfoSiteObjectives) {
            tileContext.putAttribute("body", "/publico/objectives_bd.jsp");
        } else if (component instanceof InfoSiteProgram) {
            tileContext.putAttribute("body", "/publico/program_bd.jsp");
        } else if (component instanceof InfoSiteEvaluationMethods) {
            tileContext.putAttribute("body", "/publico/viewEvaluation_bd.jsp");
        } else if (component instanceof InfoEvaluationMethod) {
            tileContext.putAttribute("body", "/publico/viewEvaluationMethod_bd.jsp");
        } else if (component instanceof InfoSiteBibliography) {
            tileContext.putAttribute("body", "/publico/bibliographicReferences_bd.jsp");
        } else if (component instanceof InfoSiteAssociatedCurricularCourses) {
            tileContext.putAttribute("body", "/publico/curricularCourses_bd.jsp");
        } else if (component instanceof InfoSiteTimetable) {
            tileContext.putAttribute("body", "/publico/viewTimeTable_bd.jsp");
        } else if (component instanceof InfoSiteShifts) {
            tileContext.putAttribute("body", "/publico/viewExecutionCourseShifts_bd.jsp");
        } else if (component instanceof InfoSiteSection) {
            tileContext.putAttribute("body", "/publico/viewSection_bd.jsp");
        } else if (component instanceof InfoSiteEvaluations) {
            tileContext.putAttribute("body", "/publico/evaluations.jsp");
        } else if (component instanceof InfoSiteEvaluationMarks) {
            tileContext.putAttribute("body", "/publico/viewMarks.jsp");
        } else if (component instanceof InfoSiteMarks) {
            tileContext.putAttribute("body", "/publico/viewPublishedMarks_bd.jsp");
        } else

        if (component instanceof InfoSiteProjects) {
            tileContext.putAttribute("body", "/publico/viewProjectsName_bd.jsp");
        } else if (component instanceof InfoSiteShiftsAndGroups) {
            tileContext.putAttribute("body", "/publico/viewShiftsAndGroups_bd.jsp");
        }

        else if (component instanceof InfoSiteStudentGroup) {
            tileContext.putAttribute("body", "/publico/viewStudentGroupInformation_bd.jsp");
        } else if (component instanceof InfoSiteStudentsAndGroups) {
            tileContext.putAttribute("body", "/publico/viewStudentsAndgroups_bd.jsp");
        }
    }

}