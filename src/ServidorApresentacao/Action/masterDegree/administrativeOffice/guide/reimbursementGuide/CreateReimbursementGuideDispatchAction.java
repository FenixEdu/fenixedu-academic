/*
 * Created on 24/Mar/2004
 *  
 */
package ServidorApresentacao.Action.masterDegree.administrativeOffice.guide.reimbursementGuide;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoGuide;
import DataBeans.InfoGuideEntry;
import DataBeans.guide.reimbursementGuide.InfoReimbursementGuideEntry;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.InvalidGuideSituationActionException;
import ServidorApresentacao.Action.exceptions.NoEntryChosenActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.SituationOfGuide;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class CreateReimbursementGuideDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer guideNumber = new Integer(this.getFromRequest("number", request));
        Integer guideYear = new Integer(this.getFromRequest("year", request));
        Integer guideVersion = new Integer(this.getFromRequest("version", request));

        InfoGuide infoGuide = null;

        Object args[] = { guideNumber, guideYear, guideVersion };
        try {
            infoGuide = (InfoGuide) ServiceUtils.executeService(userView, "ChooseGuide", args);

            request.setAttribute(SessionConstants.GUIDE, infoGuide);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoGuide.getInfoGuideSituation().getSituation().equals(SituationOfGuide.PAYED_TYPE)) {
            return mapping.findForward("start");
        }

        throw new InvalidGuideSituationActionException(mapping.findForward("error"));

    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm createReimbursementGuideForm = (DynaActionForm) form;

        Double[] valuesList = (Double[]) createReimbursementGuideForm.get("values");
        String[] justificationsList = (String[]) createReimbursementGuideForm.get("justifications");
        String remarks = (String) createReimbursementGuideForm.get("remarks");
        Integer number = (Integer) createReimbursementGuideForm.get("number");
        Integer version = (Integer) createReimbursementGuideForm.get("version");
        Integer year = (Integer) createReimbursementGuideForm.get("year");

        try {
            Object args[] = { number, year, version };
            InfoGuide infoGuide = (InfoGuide) ServiceUtils.executeService(userView, "ChooseGuide", args);

            if (infoGuide.getInfoGuideEntries().size() != valuesList.length)
                throw new FenixActionException("Incoerent guide entries number", mapping
                        .findForward("start"));
            //HOUSTON, we have a problem...

            Iterator it = infoGuide.getInfoGuideEntries().iterator();
            InfoReimbursementGuideEntry infoReimbursementGuideEntry = null;
            List infoReimbursementGuideEntries = new ArrayList();

            for (int i = 0; i < valuesList.length; i++) {
                infoReimbursementGuideEntry = new InfoReimbursementGuideEntry();
                infoReimbursementGuideEntry.setInfoGuideEntry((InfoGuideEntry) it.next());
                infoReimbursementGuideEntry.setJustification(justificationsList[i]);
                infoReimbursementGuideEntry.setValue(valuesList[i]);

                if ((justificationsList[i].length() > 0) && (valuesList[i].doubleValue() > 0))
                    infoReimbursementGuideEntries.add(infoReimbursementGuideEntry);
            }

            if (infoReimbursementGuideEntries.size() == 0)
                throw new NoEntryChosenActionException(); //,mapping.findForward("error")

            Object createArgs[] = { infoGuide.getIdInternal(), remarks, infoReimbursementGuideEntries,
                    userView };
            Integer reimbursementGuideID = (Integer) ServiceUtils.executeService(userView,
                    "CreateReimbursementGuide", createArgs);

            request.setAttribute(SessionConstants.REIMBURSEMENT_GUIDE, reimbursementGuideID);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e.getMessage(), mapping.findForward("start"));
        }

        return mapping.findForward("success");
    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null) {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }

}