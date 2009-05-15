package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SearchExecutionCourseAttendsBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.util.email.ExecutionCourseSender;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.EmailsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class SearchExecutionCourseAttendsAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	CoordinatedDegreeInfo.setCoordinatorContext(request);
	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	// Integer objectCode =
	// Integer.valueOf(request.getParameter("objectCode"));
	ExecutionCourse executionCourse = getDomainObject(request, "objectCode");
	// ExecutionCourse executionCourse =
	// rootDomainObject.readExecutionCourseByOID(objectCode);
	SearchExecutionCourseAttendsBean searchExecutionCourseAttendsBean = new SearchExecutionCourseAttendsBean(executionCourse);
	executionCourse.searchAttends(searchExecutionCourseAttendsBean);
	request.setAttribute("searchBean", searchExecutionCourseAttendsBean);
	request.setAttribute("executionCourse", searchExecutionCourseAttendsBean.getExecutionCourse());
	return mapping.findForward("search");
    }

    public ActionForward sendEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	SearchExecutionCourseAttendsBean bean = (SearchExecutionCourseAttendsBean) getRenderedObject("mailViewState");
	ExecutionCourse executionCourse = bean.getExecutionCourse();
	Group studentsGroup = bean.getAttendsGroup();
	Recipient recipient = Recipient.createNewRecipient(bean.getLabel(), studentsGroup);
	Sender sender = ExecutionCourseSender.newInstance(executionCourse);
	return EmailsDA.sendEmail(request, sender, recipient);
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	SearchExecutionCourseAttendsBean bean = (SearchExecutionCourseAttendsBean) getRenderedObject();
	RenderUtils.invalidateViewState();
	bean.getExecutionCourse().searchAttends(bean);

	request.setAttribute("searchBean", bean);
	request.setAttribute("executionCourse", bean.getExecutionCourse());
	return mapping.findForward("search");
    }

}
