package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.messaging.ExecutionCourseForum;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author naat
 * 
 */
public class ExecutionCourseForumManagementDispatchAction extends FenixDispatchAction {

    private Integer getObjectCode(HttpServletRequest request) {
        Integer objectCode = null;
        String objectCodeString = request.getParameter("objectCode");
        if (objectCodeString == null) {
            objectCodeString = (String) request.getAttribute("objectCode");
        }
        if (objectCodeString != null) {
            objectCode = new Integer(objectCodeString);
        }
        return objectCode;
    }

    public ActionForward viewForuns(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException,
            FenixServiceException {

        IUserView userView = getUserView(request);
        Integer executionCourseId = getObjectCode(request);

        if (request.getParameter("createForum") != null) {
            ServiceUtils.executeService(getUserView(request), "CreateExecutionCourseForum",
                    new Object[] { executionCourseId, userView.getPerson().getIdInternal(),"Geral",
                            "Lista geral de discussão" });
        }

        SiteView siteView = (SiteView) ServiceUtils.executeService(userView, "ReadCourseInformation",
                new Object[] { executionCourseId });
        request.setAttribute("siteView", siteView);
        request.setAttribute("objectCode", executionCourseId);

        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);

        List<ExecutionCourseForum> foruns = executionCourse.getForuns();

        request.setAttribute("foruns", foruns);

        return mapping.findForward("viewForuns");
    }

}