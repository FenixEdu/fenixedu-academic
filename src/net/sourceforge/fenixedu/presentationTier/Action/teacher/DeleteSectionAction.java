/*
 * Created on 7/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

/**
 * @author lmac1
 *  
 */
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.UserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

public class DeleteSectionAction extends FenixAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession(false);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

        InfoSection infoSection = (InfoSection) session.getAttribute(SessionConstants.INFO_SECTION);
        InfoSection infoSuperiorSection = infoSection.getSuperiorInfoSection();

        try {
            Object deleteSectionArguments[] = { infoSection };
            ServiceManagerServiceFactory.executeService(userView, "DeleteSection",
                    deleteSectionArguments);

            session.removeAttribute(SessionConstants.INFO_SECTION);
            session.removeAttribute(SessionConstants.SECTIONS);

            InfoSite infoSite = infoSection.getInfoSite();
            Object readSectionsArguments[] = { infoSite };
            List allInfoSections = (List) ServiceManagerServiceFactory.executeService(null,
                    "ReadSections", readSectionsArguments);

            Collections.sort(allInfoSections);
            session.setAttribute(SessionConstants.SECTIONS, allInfoSections);

            if (infoSuperiorSection == null) {

                return mapping.findForward("AccessSiteManagement");
            }

            session.setAttribute(SessionConstants.INFO_SECTION, infoSuperiorSection);
            return mapping.findForward("AccessSectionManagement");

        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

    }

}