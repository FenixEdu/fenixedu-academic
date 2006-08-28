package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author CIIST
 */
public class InsertSectionAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        String sectionName = (String) dynaForm.get("name");
        String order = (String) dynaForm.get("sectionOrder");

        HttpSession session = request.getSession(false);

        InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

        IUserView userView = getUserView(request);

        InfoSection parentSection = (InfoSection) session.getAttribute(SessionConstants.INFO_SECTION);

        Integer sectionOrder = new Integer(order);

        List childrenSections = (ArrayList) session.getAttribute(SessionConstants.CHILDREN_SECTIONS);

        if (sectionOrder.equals(new Integer(-1)))
            sectionOrder = new Integer(childrenSections.size());

        InfoSection infoSection = new InfoSection(sectionName, sectionOrder, infoSite, parentSection);

        Object args[] = { infoSection };
        try {
            ServiceManagerServiceFactory.executeService(userView, "InsertSection", args);
        } catch (ExistingServiceException e) {
            throw new ExistingActionException("Uma secção com esse nome", e);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        Object args1[] = { infoSite };
        List sections;
        try {
            sections = (ArrayList) ServiceManagerServiceFactory.executeService(userView, "ReadSections",
                    args1);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        Collections.sort(sections);
        session.setAttribute(SessionConstants.SECTIONS, sections);
        return mapping.findForward("viewSite");
    }
}