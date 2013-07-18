/*
 * Created on 14/Mar/2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.guide;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.ChooseGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 *         This is the Action to choose, visualize and edit a Guide.
 * 
 */
public class ChooseGuideDispatchAction extends FenixDispatchAction {

    public ActionForward prepareChoose(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String action = request.getParameter("action");
        DynaActionForm chooseGuide = (DynaActionForm) form;

        if (action.equals("visualize")) {
            request.setAttribute(PresentationConstants.ACTION, "label.action.visualizeGuide");
        } else if (action.equals("edit")) {
            request.setAttribute(PresentationConstants.ACTION, "label.action.editGuide");
        }

        chooseGuide.set("guideYear", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));

        return mapping.findForward("PrepareReady");

    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaActionForm chooseGuide = (DynaActionForm) form;

        // Get the Information
        Integer guideNumber = new Integer((String) chooseGuide.get("guideNumber"));
        Integer guideYear = new Integer((String) chooseGuide.get("guideYear"));

        List result = null;
        try {
            result = ChooseGuide.runChooseGuide(guideNumber, guideYear);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("A Guia", e);
        }

        request.setAttribute(PresentationConstants.GUIDE_LIST, result);
        if (result.size() == 1) {
            request.setAttribute(PresentationConstants.GUIDE, result.get(0));
            return mapping.findForward("ActionReady");
        }

        request.setAttribute(PresentationConstants.GUIDE_YEAR, guideYear);
        request.setAttribute(PresentationConstants.GUIDE_NUMBER, guideNumber);

        return mapping.findForward("ShowVersionList");
    }

    public ActionForward chooseVersion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        // Get the Information
        Integer guideNumber = new Integer(request.getParameter("number"));
        Integer guideYear = new Integer(request.getParameter("year"));
        Integer guideVersion = new Integer(request.getParameter("version"));

        InfoGuide infoGuide = null;

        try {
            infoGuide = ChooseGuide.runChooseGuide(guideNumber, guideYear, guideVersion);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("A Versão da Guia", e);
        }

        List guideList = null;
        try {
            guideList = ChooseGuide.runChooseGuide(guideNumber, guideYear);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("A Guia", e);
        }

        request.setAttribute(PresentationConstants.GUIDE_LIST, guideList);
        request.setAttribute(PresentationConstants.GUIDE, infoGuide);
        return mapping.findForward("ActionReady");

    }

}