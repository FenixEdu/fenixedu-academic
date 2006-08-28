/*
 * Created on 14/Mar/2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.guide;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * This is the Action to choose, visualize and edit a Guide.
 *  
 */
public class ChooseGuideDispatchAction extends FenixDispatchAction {

    public ActionForward prepareChoose(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {

            String action = request.getParameter("action");
            DynaActionForm chooseGuide = (DynaActionForm) form;

            session.removeAttribute(SessionConstants.GUIDE);
            session.removeAttribute(SessionConstants.GUIDE_LIST);
            session.removeAttribute(SessionConstants.REQUESTER_NUMBER);

            if (action.equals("visualize")) {
                session.setAttribute(SessionConstants.ACTION, "label.action.visualizeGuide");
            } else if (action.equals("edit")) {
                session.setAttribute(SessionConstants.ACTION, "label.action.editGuide");
            }

            chooseGuide.set("guideYear", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));

            return mapping.findForward("PrepareReady");
        }

        throw new Exception();

    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {

            DynaActionForm chooseGuide = (DynaActionForm) form;

            IUserView userView = getUserView(request);

            // Get the Information
            Integer guideNumber = new Integer((String) chooseGuide.get("guideNumber"));
            Integer guideYear = new Integer((String) chooseGuide.get("guideYear"));

            Object args[] = { guideNumber, guideYear };

            List result = null;
            try {
                result = (List) ServiceManagerServiceFactory.executeService(userView, "ChooseGuide",
                        args);
            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException("A Guia", e);
            }

            request.setAttribute(SessionConstants.GUIDE_LIST, result);
            if (result.size() == 1) {
                request.setAttribute(SessionConstants.GUIDE, result.get(0));
                return mapping.findForward("ActionReady");
            }

            request.setAttribute(SessionConstants.GUIDE_YEAR, guideYear);
            request.setAttribute(SessionConstants.GUIDE_NUMBER, guideNumber);

            return mapping.findForward("ShowVersionList");
        }

        throw new Exception();
    }

    public ActionForward chooseVersion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {

            IUserView userView = getUserView(request);

            // Get the Information
            Integer guideNumber = new Integer(request.getParameter("number"));
            Integer guideYear = new Integer(request.getParameter("year"));
            Integer guideVersion = new Integer(request.getParameter("version"));

            InfoGuide infoGuide = null;

            try {
                Object args[] = { guideNumber, guideYear, guideVersion };
                infoGuide = (InfoGuide) ServiceManagerServiceFactory.executeService(userView,
                        "ChooseGuide", args);
            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException("A Versão da Guia", e);
            }

            List guideList = null;
            try {
                Object args[] = { guideNumber, guideYear };
                guideList = (List) ServiceManagerServiceFactory.executeService(userView, "ChooseGuide",
                        args);
            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException("A Guia", e);
            }

            request.setAttribute(SessionConstants.GUIDE_LIST, guideList);
            request.setAttribute(SessionConstants.GUIDE, infoGuide);
            return mapping.findForward("ActionReady");
        }

        throw new Exception();
    }

}