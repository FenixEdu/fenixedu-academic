package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/editExecutionCourseChooseExPeriod", module = "academicAdministration")
@Forwards({
        @Forward(name = "editChooseExecutionPeriod",
                path = "/academicAdministration/executionCourseManagement/editChooseExecutionPeriod.jsp"),
        @Forward(name = "editChooseCourseAndYear",
                path = "/academicAdministration/executionCourseManagement/editChooseCourseAndYear.jsp"),
        @Forward(name = "editExecutionCourse",
                path = "/academicAdministration/executionCourseManagement/listExecutionCourseActions.jsp") })
public class EditExecutionCourseDA extends FenixDispatchAction {

    public ActionForward prepareEditExecutionCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("sessionBean", new ExecutionCourseBean());

        return mapping.findForward("editChooseExecutionPeriod");
    }

    public ActionForward secondPrepareEditExecutionCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        ExecutionCourseBean bean = getRenderedObject("sessionBeanJSP");

        /*
         * Se chooseNotLinked=checked, entao limpar as dropboxes do curso e ano
         * curricular.
         */
        if (bean.getChooseNotLinked() != null) {
            if (bean.getChooseNotLinked()) {
                bean.setExecutionDegree(null);
                bean.setCurricularYear(null);
            }
        }

        request.setAttribute("sessionBean", bean);

        RenderUtils.invalidateViewState("sessionBeanJSP");

        return mapping.findForward("editChooseCourseAndYear");
    }

    public ActionForward listExecutionCourseActions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        ExecutionCourseBean bean = getRenderedObject("sessionBeanJSP");
        request.setAttribute("sessionBean", bean);

        return mapping.findForward("editExecutionCourse");
    }
}
