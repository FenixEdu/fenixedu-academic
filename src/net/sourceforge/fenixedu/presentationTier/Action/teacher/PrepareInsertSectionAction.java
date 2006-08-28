/*
 * Created on 7/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author CIIST
 * 
 * To change this generated comment go to Window>Preferences>Java>Code
 * Generation>Code and Comments
 */
public class PrepareInsertSectionAction extends FenixDispatchAction {

    public ActionForward prepareInsertRegularSection(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession(false);

        InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

        IUserView userView = getUserView(request);

        InfoSection parentSection = (InfoSection) session.getAttribute(SessionConstants.INFO_SECTION);

        List sections;
        Object args[] = { infoSite, parentSection };

        try {
            sections = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                    "ReadSectionsBySiteAndSuperiorSection", args);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        if (sections.size() != 0) {
            Collections.sort(sections);
            session.setAttribute(SessionConstants.CHILDREN_SECTIONS, sections);
        } else
            session.removeAttribute(SessionConstants.CHILDREN_SECTIONS);

        return mapping.findForward("createSection");
    }

    public ActionForward prepareInsertRootSection(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession(false);

        InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

        IUserView userView = getUserView(request);

        session.removeAttribute(SessionConstants.INFO_SECTION);

        List sections;
        Object args[] = { infoSite, null };

        try {
            sections = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                    "ReadSectionsBySiteAndSuperiorSection", args);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        if (sections.size() != 0) {
            Collections.sort(sections);
            session.setAttribute(SessionConstants.CHILDREN_SECTIONS, sections);
        } else
            session.removeAttribute(SessionConstants.CHILDREN_SECTIONS);

        return mapping.findForward("createSection");

    }
}