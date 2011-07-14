package net.sourceforge.fenixedu.presentationTier.Action.manager.enrolments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/specialSeason/specialSeasonStatusTracker", module = "departmentAdmOffice")
@Forwards( { @Forward(name = "selectCourse", path = "/departmentAdmOffice/lists/specialSeason/selectCourse.jsp"),
	@Forward(name = "listStudents", path = "/departmentAdmOffice/lists/specialSeason/listStudents.jsp") })
public class SpecialSeasonDepartmentStatusDA extends SpecialSeasonStatusTrackerDA {
    
    @Override
    public ActionForward selectCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	SpecialSeasonStatusTrackerBean bean = getRenderedObject();
	if(bean == null) {
	   bean = new SpecialSeasonStatusTrackerBean();
	   bean.setDepartment(getDepartment());
	}
	request.setAttribute("bean", bean);
	RenderUtils.invalidateViewState();
	return mapping.findForward("selectCourse");
    }
    
    @Override
    protected Department getDepartment() {
	return AccessControl.getPerson().getEmployee().getCurrentDepartmentWorkingPlace();
    }

}
