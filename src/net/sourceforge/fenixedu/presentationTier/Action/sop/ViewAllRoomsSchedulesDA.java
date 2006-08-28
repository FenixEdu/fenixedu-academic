package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Luis Cruz e Sara Ribeiro
 */
public class ViewAllRoomsSchedulesDA extends FenixContextDispatchAction {

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        if (session != null) {
            /* Criar o bean de pavilhoes */
            List pavillionsNamesList = new ArrayList();
            pavillionsNamesList.add("Pavilhão Central");
            pavillionsNamesList.add("Pavilhão Civil");
            pavillionsNamesList.add("Pavilhão Electricidade");
            pavillionsNamesList.add("Pavilhão Pós-Graduação");
            pavillionsNamesList.add("Pavilhão Química");
            pavillionsNamesList.add("Pavilhão Mecânica I");
            pavillionsNamesList.add("Pavilhão Mecânica II");
            pavillionsNamesList.add("Pavilhão Mecânica III");
            pavillionsNamesList.add("Pavilhão Minas");
            pavillionsNamesList.add("Pavilhão Novas Licenciaturas");
            pavillionsNamesList.add("TagusPark - Bloco A - Poente");
            pavillionsNamesList.add("TagusPark - Bloco A - Nascente");
            pavillionsNamesList.add("TagusPark - Bloco B - Poente");
            pavillionsNamesList.add("TagusPark - Bloco B - Nascente");
            pavillionsNamesList.add("Torre Norte");
            pavillionsNamesList.add("Torre Sul");

            request.setAttribute(SessionConstants.PAVILLIONS_NAMES_LIST, pavillionsNamesList);

            return mapping.findForward("choose");
        }
        throw new Exception();

    }

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        if (session != null) {
            IUserView userView = getUserView(request);
            DynaActionForm chooseViewAllRoomsSchedulesContextForm = (DynaActionForm) form;

            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                    .getAttribute(SessionConstants.EXECUTION_PERIOD);

            List pavillions = new ArrayList();
            pavillions.add("Pavilhão Central");
            pavillions.add("Pavilhão Civil");
            pavillions.add("Pavilhão Electricidade");
            pavillions.add("Pavilhão Pós-Graduação");
            pavillions.add("Pavilhão Química");
            pavillions.add("Pavilhão Mecânica I");
            pavillions.add("Pavilhão Mecânica II");
            pavillions.add("Pavilhão Mecânica III");
            pavillions.add("Pavilhão Minas");
            pavillions.add("Pavilhão Novas Licenciaturas");
            pavillions.add("TagusPark - Bloco A - Poente");
            pavillions.add("TagusPark - Bloco A - Nascente");
            pavillions.add("TagusPark - Bloco B - Poente");
            pavillions.add("TagusPark - Bloco B - Nascente");
            pavillions.add("Torre Norte");
            pavillions.add("Torre Sul");

            Boolean selectAllPavillions = (Boolean) chooseViewAllRoomsSchedulesContextForm
                    .get("selectAllPavillions");
            List selectedPavillions = null;
            if (selectAllPavillions != null && selectAllPavillions.booleanValue()) {
                selectedPavillions = pavillions;
            } else {
                String[] selectedPavillionsNames = (String[]) chooseViewAllRoomsSchedulesContextForm
                        .get("selectedPavillions");
                selectedPavillions = new ArrayList();
                for (int i = 0; i < selectedPavillionsNames.length; i++) {
                    selectedPavillions.add(selectedPavillionsNames[i]);
                }
            }

            Object[] args = { selectedPavillions, infoExecutionPeriod };
            List infoViewClassScheduleList = (List) ServiceManagerServiceFactory.executeService(
                    userView, "ReadPavillionsRoomsLessons", args);

            if (infoViewClassScheduleList != null && infoViewClassScheduleList.isEmpty()) {
                request.removeAttribute(SessionConstants.ALL_INFO_VIEW_ROOM_SCHEDULE);
            } else {
                request.setAttribute(SessionConstants.ALL_INFO_VIEW_ROOM_SCHEDULE,
                        infoViewClassScheduleList);
                request.setAttribute(SessionConstants.INFO_EXECUTION_PERIOD, infoExecutionPeriod);
            }

            return mapping.findForward("list");
        }
        throw new Exception();

    }

}