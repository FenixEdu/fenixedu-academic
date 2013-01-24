package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.commons.student.AbstractCurriculumLinesLocationManagementDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/curriculumLinesLocationManagement", module = "academicAdministration")
@Forwards({
	@Forward(name = "showCurriculum", path = "/academicAdminOffice/curriculum/curriculumLines/location/showCurriculum.jsp"),
	@Forward(name = "chooseNewLocation", path = "/academicAdminOffice/curriculum/curriculumLines/location/chooseNewLocation.jsp"),
	@Forward(name = "backToStudentEnrolments", path = "/studentEnrolments.do?method=prepare") })
public class CurriculumLinesLocationManagementDA extends AbstractCurriculumLinesLocationManagementDA {

    public ActionForward backToStudentEnrolments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("backToStudentEnrolments");
    }

}
