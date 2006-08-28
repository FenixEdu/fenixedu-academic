package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideList;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.util.NumberUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 *  
 */
public class GuideListingByStateDispatchAction extends FenixDispatchAction {

    public ActionForward prepareChooseState(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm chooseGuide = (DynaActionForm) form;

        chooseGuide.set("year", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));

        return mapping.findForward("PrepareSuccess");
    }

    public ActionForward chooseState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = getUserView(request);

        DynaActionForm chooseGuide = (DynaActionForm) form;

        Integer year = new Integer((String) chooseGuide.get("year"));
        String state = (String) chooseGuide.get("state");

        GuideState situationOfGuide = null;
        if ((state != null) && (state.length() > 0)) {
            situationOfGuide = GuideState.valueOf(state);
        }

        List guideList = null;
        try {
            Object args[] = { year, situationOfGuide };
            guideList = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ListGuidesByState", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("year", year);

        if (!guideList.isEmpty()) {
            List result = prepareList(guideList);
            request.setAttribute("listOfGuides", result);
        }

        return mapping.findForward("ShowList");
    }

    /**
     * @param guideList
     * @return
     */
    private List prepareList(List guideList) {

        List nonPayedGuides = new ArrayList();
        List payedGuides = new ArrayList();
        List annulledGuides = new ArrayList();

        Double nonPayedGuidesTotal = new Double(0);
        Double payedGuidesTotal = new Double(0);
        Double annulledGuidesTotal = new Double(0);

        Iterator iterator = guideList.iterator();
        while (iterator.hasNext()) {
            InfoGuide infoGuide = (InfoGuide) iterator.next();

            if (infoGuide.getInfoGuideSituation().getSituation().equals(GuideState.ANNULLED)) {
                annulledGuides.add(infoGuide);
                annulledGuidesTotal = NumberUtils.formatNumber(new Double(annulledGuidesTotal
                        .floatValue()
                        + infoGuide.getTotal().floatValue()), 2);
            } else if (infoGuide.getInfoGuideSituation().getSituation().equals(
                    GuideState.PAYED)) {
                payedGuides.add(infoGuide);
                payedGuidesTotal = NumberUtils.formatNumber(new Double(payedGuidesTotal.floatValue()
                        + infoGuide.getTotal().floatValue()), 2);
            } else if (infoGuide.getInfoGuideSituation().getSituation().equals(
                    GuideState.NON_PAYED)) {
                nonPayedGuides.add(infoGuide);
                nonPayedGuidesTotal = NumberUtils.formatNumber(new Double(nonPayedGuidesTotal
                        .floatValue()
                        + infoGuide.getTotal().floatValue()), 2);
            }
        }

        // Create the presantation object

        List result = new ArrayList();
        if (!annulledGuides.isEmpty()) {
            InfoGuideList infoGuideList = new InfoGuideList();
            infoGuideList.setGuides(annulledGuides);
            infoGuideList.setSituation(GuideState.ANNULLED.name());
            infoGuideList.setTotal(annulledGuidesTotal);
            result.add(infoGuideList);
        }

        if (!payedGuides.isEmpty()) {
            InfoGuideList infoGuideList = new InfoGuideList();
            infoGuideList.setGuides(payedGuides);
            infoGuideList.setSituation(GuideState.PAYED.name());
            infoGuideList.setTotal(payedGuidesTotal);
            result.add(infoGuideList);
        }

        if (!nonPayedGuides.isEmpty()) {
            InfoGuideList infoGuideList = new InfoGuideList();
            infoGuideList.setGuides(nonPayedGuides);
            infoGuideList.setSituation(GuideState.NON_PAYED.name());
            infoGuideList.setTotal(nonPayedGuidesTotal);
            result.add(infoGuideList);
        }

        if (result.isEmpty()) {
            return null;
        }
        return result;

    }

}