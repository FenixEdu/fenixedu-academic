/*
 * Created on 2003/09/06
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.AnnouncementManagement;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Luis Cruz
 * @author Gonçalo Luiz
 */
public class ManageAdvisoriesDA extends AnnouncementManagement {

    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("returnAction", this.getContextInformation(request));
        request.setAttribute("returnMethod", "start");
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected Collection<AnnouncementBoard> boardsToView(HttpServletRequest request) throws Exception {
        final IUserView userView = getUserView(request);
        final Collection<AnnouncementBoard> boardsToShow = new ArrayList<AnnouncementBoard>();
        for (AnnouncementBoard board : rootDomainObject.getInstitutionUnit().getBoards()) {
            if (board.getWriters() == null || board.getWriters().allows(userView)) {
                boardsToShow.add(board);
            }
        }
        return boardsToShow;
    }

    @Override
    protected String getContextInformation(HttpServletRequest request) {
        return "/manageAdvisories.do";
    }

    @Override
    protected String getExtraRequestParameters(HttpServletRequest request) {
        return "";
    }    
}