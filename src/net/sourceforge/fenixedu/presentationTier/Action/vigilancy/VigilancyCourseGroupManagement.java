package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class VigilancyCourseGroupManagement extends FenixDispatchAction {

    public ActionForward prepareEdition(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	VigilancyCourseGroupBean bean = new VigilancyCourseGroupBean();
	String oid = request.getParameter("gid");
	Integer idInternal = Integer.valueOf(oid);

	VigilantGroup group = (VigilantGroup) RootDomainObject.readDomainObjectByOID(
		VigilantGroup.class, idInternal);
	bean.setSelectedVigilantGroup(group);
	bean.setSelectedDepartment(group.getUnit().getDepartment());
	request.setAttribute("bean", bean);

	return mapping.findForward("editCourseGroup");
    }

    public ActionForward selectUnit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	VigilancyCourseGroupBean bean = (VigilancyCourseGroupBean) RenderUtils
		.getViewState("selectUnit").getMetaObject().getObject();
	request.setAttribute("bean", bean);
	RenderUtils.invalidateViewState("selectUnit");
	return mapping.findForward("editCourseGroup");
    }

    public ActionForward addExecutionCourseToGroup(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	IViewState viewState = RenderUtils.getViewState("addExecutionCourses");
	VigilancyCourseGroupBean bean = (VigilancyCourseGroupBean) viewState.getMetaObject().getObject();
	List<ExecutionCourse> executionCourses = bean.getCoursesToAdd();
	VigilantGroup group = bean.getSelectedVigilantGroup();

	if (executionCourses.size() > 0) {
	    List<ExecutionCourse> coursesUnableToAdd;
	    Object[] args = { group, executionCourses };
	    coursesUnableToAdd = (List<ExecutionCourse>) executeService(request,
		    "AddExecutionCourseToGroup", args);

	    request.setAttribute("coursesUnableToAdd", coursesUnableToAdd);
	}
	bean.setCoursesToAdd(new ArrayList<ExecutionCourse>());
	request.setAttribute("bean", bean);
	RenderUtils.invalidateViewState("addExecutionCourses");
	return mapping.findForward("editCourseGroup");

    }

    public ActionForward removeExecutionCoursesFromGroup(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	VigilancyCourseGroupBean bean = (VigilancyCourseGroupBean) RenderUtils.getViewState(
		"removeExecutionCourses").getMetaObject().getObject();
	List<ExecutionCourse> executionCourses = bean.getCourses();
	VigilantGroup group = bean.getSelectedVigilantGroup();

	try {
	    Object[] args = { group, executionCourses };
	    executeService(request, "RemoveExecutionCoursesFromGroup", args);
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), null);
	}

	request.setAttribute("bean", bean);
	RenderUtils.invalidateViewState("removeExecutionCourses");
	return mapping.findForward("editCourseGroup");
    }

    public ActionForward addExternalCourse(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	IViewState viewState = (IViewState) RenderUtils.getViewState("addExternalCourse");
	if (viewState == null) {
	    viewState = (IViewState) RenderUtils.getViewState("addExternalCourse-withoutjs");
	}
	VigilancyCourseGroupBean bean = (VigilancyCourseGroupBean) viewState.getMetaObject().getObject();

	ExecutionCourse course = bean.getExternalCourse();
	VigilantGroup group = bean.getSelectedVigilantGroup();
	if (course != null) {
	    List<ExecutionCourse> courses = new ArrayList<ExecutionCourse>();
	    courses.add(course);

	    try {
		Object[] args = { group, courses };
		executeService(request, "AddExecutionCourseToGroup", args);
	    } catch (DomainException e) {
		addActionMessage(request, e.getMessage());
	    }
	}
	if (RenderUtils.getViewState("addExternalCourse-withoutjs") != null) {
	    RenderUtils.invalidateViewState("addExternalCourse-withoutjs");
	}

	request.setAttribute("bean", bean);
	bean.setExternalCourse(null);
	return mapping.findForward("editCourseGroup");
    }
}