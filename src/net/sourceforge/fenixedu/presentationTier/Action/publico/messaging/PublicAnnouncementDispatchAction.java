/*
 * Author : Goncalo Luiz
 * Creation Date: Jul 21, 2006,2:11:27 PM
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico.messaging;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.AnnouncementManagement;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 *         <br>
 *         Created on Jul 21, 2006,2:11:27 PM
 * 
 */
public class PublicAnnouncementDispatchAction extends AnnouncementManagement {

    @Override
    public ActionForward start(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        return null;
    }

    @Override
    protected String getExtraRequestParameters(HttpServletRequest request) {
        return "";
    }

    @Override
    protected String getContextInformation(HttpServletRequest request) {
        return "/publicAnnouncements";
    }

    @Override
    protected Collection<AnnouncementBoard> boardsToView(HttpServletRequest request) throws Exception {
        return new ArrayList<AnnouncementBoard>();
    }
}
