package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalencePlanEntry;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EquivalencePlan;
import net.sourceforge.fenixedu.domain.EquivalencePlanEntry;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.CourseGroupEquivalencePlanEntry.CourseGroupEquivalencePlanEntryCreator;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalencePlanEntry.CurricularCourseEquivalencePlanEntryCreator;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class EquivalencyPlanDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));
	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward showPlan(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	return mapping.findForward("showPlan");
    }

    public ActionForward showTable(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	request.setAttribute("viewTable", Boolean.TRUE);
	final CurricularCourse curricularCourse = getCurricularCourse(request);
	if (curricularCourse != null) {
	    final EquivalencePlan equivalencePlan = getEquivalencePlan(request);
	    final Set<net.sourceforge.fenixedu.domain.CurricularCourseEquivalencePlanEntry> curricularCourseEquivalencePlanEntries = curricularCourse.getNewCurricularCourseEquivalencePlanEntry(equivalencePlan);
	    request.setAttribute("curricularCourseEquivalencePlanEntries", curricularCourseEquivalencePlanEntries);
	}
	final CourseGroup courseGroup = getCourseGroup(request);
	if (courseGroup != null) {
	    request.setAttribute("courseGroupEquivalencePlanEntries", courseGroup.getEquivalencePlanEntrySet());
	}
	return mapping.findForward("showPlan");
    }

    public ActionForward prepareAddEquivalency(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	CurricularCourseEquivalencePlanEntryCreator courseEquivalencePlanEntryCreator = (CurricularCourseEquivalencePlanEntryCreator) getRenderedObject();
	final EquivalencePlan equivalencePlan;
	if (courseEquivalencePlanEntryCreator == null) {
	    equivalencePlan = getEquivalencePlan(request);
	    final CurricularCourse curricularCourse = getCurricularCourse(request);
	    courseEquivalencePlanEntryCreator = new CurricularCourseEquivalencePlanEntryCreator(equivalencePlan, curricularCourse);
	    courseEquivalencePlanEntryCreator.setDestinationCurricularCourseToAdd(curricularCourse);
	} else {
	    equivalencePlan = courseEquivalencePlanEntryCreator.getEquivalencePlan();
	}
	request.setAttribute("equivalencePlan", equivalencePlan);
	request.setAttribute("curricularCourseEquivalencePlanEntryCreator", courseEquivalencePlanEntryCreator);
	return mapping.findForward("addEquivalency");
    }

    public ActionForward prepareAddCourseGroupEquivalency(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	CourseGroupEquivalencePlanEntryCreator courseGroupEquivalencePlanEntryCreator = (CourseGroupEquivalencePlanEntryCreator) getRenderedObject();
	final EquivalencePlan equivalencePlan;
	if (courseGroupEquivalencePlanEntryCreator == null) {
	    equivalencePlan = getEquivalencePlan(request);
	    final CourseGroup courseGroup = getCourseGroup(request);
	    courseGroupEquivalencePlanEntryCreator = new CourseGroupEquivalencePlanEntryCreator(equivalencePlan, courseGroup);
	} else {
	    equivalencePlan = courseGroupEquivalencePlanEntryCreator.getEquivalencePlan();
	}
	request.setAttribute("equivalencePlan", equivalencePlan);
	request.setAttribute("courseGroupEquivalencePlanEntryCreator", courseGroupEquivalencePlanEntryCreator);
	return mapping.findForward("addCourseGroupEquivalency");
    }

    public ActionForward deleteEquivalency(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final EquivalencePlanEntry equivalencePlanEntry = getEquivalencePlanEntry(request);
	final Object[] args = { equivalencePlanEntry };
	executeService(request, "DeleteEquivalencePlanEntry", args);
	return mapping.findForward("showPlan");
    }

    private EquivalencePlanEntry getEquivalencePlanEntry(HttpServletRequest request) {
	final String equivalencePlanEntryIDString = request.getParameter("equivalencePlanEntryID");
	final Integer equivalencePlanEntryID = getInteger(equivalencePlanEntryIDString);
	return equivalencePlanEntryID == null ? null
		: (EquivalencePlanEntry) RootDomainObject.getInstance().readEquivalencePlanEntryByOID(equivalencePlanEntryID);
    }

    private CurricularCourseEquivalencePlanEntry getCurricularCourseEquivalencePlanEntry(
	    HttpServletRequest request) {
	final String curricularCourseEquivalencePlanEntryIDString = request
		.getParameter("curricularCourseEquivalencePlanEntryID");
	final Integer curricularCourseEquivalencePlanEntryID = getInteger(curricularCourseEquivalencePlanEntryIDString);
	return curricularCourseEquivalencePlanEntryID == null ? null
		: (CurricularCourseEquivalencePlanEntry) RootDomainObject.getInstance()
			.readEquivalencePlanEntryByOID(curricularCourseEquivalencePlanEntryID);
    }

    private CourseGroup getCourseGroup(HttpServletRequest request) {
	final String courseGroupIDString = request.getParameter("courseGroupID");
	final Integer courseGroupID = getInteger(courseGroupIDString);
	return courseGroupID == null ? null : (CourseGroup) RootDomainObject.getInstance()
		.readDegreeModuleByOID(courseGroupID);
    }

    private CurricularCourse getCurricularCourse(HttpServletRequest request) {
	final String curricularCourseIDString = request.getParameter("curricularCourseID");
	final Integer curricularCourseID = getInteger(curricularCourseIDString);
	return curricularCourseID == null ? null : (CurricularCourse) RootDomainObject.getInstance()
		.readDegreeModuleByOID(curricularCourseID);
    }

    private EquivalencePlan getEquivalencePlan(HttpServletRequest request) {
	final String equivalencePlanIDString = request.getParameter("equivalencePlanID");
	final Integer equivalencePlanID = getInteger(equivalencePlanIDString);
	return equivalencePlanID == null ? null : RootDomainObject.getInstance()
		.readEquivalencePlanByOID(equivalencePlanID);
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
	final String degreeCurricularPlanIDString = request.getParameter("degreeCurricularPlanID");
	final Integer degreeCurricularPlanID = getInteger(degreeCurricularPlanIDString);
	return degreeCurricularPlanID == null ? null : RootDomainObject.getInstance()
		.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
    }

    private Integer getInteger(final String string) {
	return isValidNumber(string) ? Integer.valueOf(string) : null;
    }

    private boolean isValidNumber(final String string) {
	return string != null && string.length() > 0 && StringUtils.isNumeric(string);
    }

}
