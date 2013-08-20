package net.sourceforge.fenixedu.presentationTier.Action.publico.rss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.messaging.ExecutionCourseAnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(module = "publico", path = "/announcementsRSS", scope = "session")
public class GenerateAnnoucementsRSS extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final String executionCourseIdString = request.getParameter("id");
        final ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(executionCourseIdString);
        if (executionCourse == null) {
            return forward("/publico/notFound.do");
        }
        final ExecutionCourseAnnouncementBoard announcementBoard = executionCourse.getBoard();
        if (announcementBoard == null) {
            return forward("/publico/executionCourse.do?method=notFound&executionCourseID=" + executionCourse.getExternalId());
        }
        return forward("/external/announcementsRSS.do?announcementBoardId=" + announcementBoard.getExternalId());
    }

    private ActionForward forward(String purl) {
        return new ActionForward(purl);
    }

}