package ServidorApresentacao.Action.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoGuide;
import DataBeans.InfoGuideList;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.NumberUtils;
import Util.SituationOfGuide;
import framework.factory.ServiceManagerServiceFactory;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 *  
 */
public class GuideListingByStateDispatchAction extends DispatchAction {

    public ActionForward prepareChooseState(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm chooseGuide = (DynaActionForm) form;

        chooseGuide.set("year", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));

        request.setAttribute("situations", SituationOfGuide.toArrayList());

        return mapping.findForward("PrepareSuccess");
    }

    public ActionForward chooseState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        DynaActionForm chooseGuide = (DynaActionForm) form;

        Integer year = new Integer((String) chooseGuide.get("year"));
        String state = (String) chooseGuide.get("state");

        SituationOfGuide situationOfGuide = null;
        if ((state != null) && (state.length() > 0)) {
            situationOfGuide = new SituationOfGuide(state);
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

            if (infoGuide.getInfoGuideSituation().getSituation().equals(SituationOfGuide.ANNULLED_TYPE)) {
                annulledGuides.add(infoGuide);
                annulledGuidesTotal = NumberUtils.formatNumber(new Double(annulledGuidesTotal
                        .floatValue()
                        + infoGuide.getTotal().floatValue()), 2);
            } else if (infoGuide.getInfoGuideSituation().getSituation().equals(
                    SituationOfGuide.PAYED_TYPE)) {
                payedGuides.add(infoGuide);
                payedGuidesTotal = NumberUtils.formatNumber(new Double(payedGuidesTotal.floatValue()
                        + infoGuide.getTotal().floatValue()), 2);
            } else if (infoGuide.getInfoGuideSituation().getSituation().equals(
                    SituationOfGuide.NON_PAYED_TYPE)) {
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
            infoGuideList.setSituation(SituationOfGuide.ANNULLED_STRING);
            infoGuideList.setTotal(annulledGuidesTotal);
            result.add(infoGuideList);
        }

        if (!payedGuides.isEmpty()) {
            InfoGuideList infoGuideList = new InfoGuideList();
            infoGuideList.setGuides(payedGuides);
            infoGuideList.setSituation(SituationOfGuide.PAYED_STRING);
            infoGuideList.setTotal(payedGuidesTotal);
            result.add(infoGuideList);
        }

        if (!nonPayedGuides.isEmpty()) {
            InfoGuideList infoGuideList = new InfoGuideList();
            infoGuideList.setGuides(nonPayedGuides);
            infoGuideList.setSituation(SituationOfGuide.NON_PAYED_STRING);
            infoGuideList.setTotal(nonPayedGuidesTotal);
            result.add(infoGuideList);
        }

        if (result.isEmpty()) {
            return null;
        }
        return result;

    }

}