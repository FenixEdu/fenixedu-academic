package ServidorApresentacao.Action.masterDegree.administrativeOffice.gratuity;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.LookupDispatchAction;

import DataBeans.InfoEmployee;
import DataBeans.InfoGratuitySituation;
import DataBeans.InfoGratuityValues;
import DataBeans.InfoPerson;
import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.ExemptionGratuityType;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Fernanda Quitério Created on 31/Aug/2004
 *  
 */
public class ExemptionGratuityLAAction extends LookupDispatchAction {

    public ActionForward insertExemptionGratuity(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();

        HttpSession session = request.getSession();

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        DynaActionForm exemptionForm = (DynaActionForm) actionForm;
        InfoGratuitySituation infoGratuitySituation = fillInfoGratuityValues(userView, request,
                exemptionForm);

        Object[] args = { infoGratuitySituation };
        try {
            infoGratuitySituation = (InfoGratuitySituation) ServiceManagerServiceFactory.executeService(
                    userView, "EditGratuitySituationById", args);

        } catch (FenixServiceException exception) {
            exception.printStackTrace();
            errors.add("insertExemptionGratuity", new ActionError(
                    "error.impossible.insertExemptionGratuity"));
            saveErrors(request, errors);
            mapping.getInputForward();
        }
        request.setAttribute("exemptionGratuity", infoGratuitySituation);

        return mapping.findForward("confirmationExemptionGratuity");
    }

    private InfoGratuitySituation fillInfoGratuityValues(IUserView userView, HttpServletRequest request,
            DynaActionForm exemptionForm) {
        Integer valueExemptionGratuity = Integer.valueOf((String) exemptionForm
                .get("valueExemptionGratuity"));
        Integer justificationExemptionGratuity = Integer.valueOf((String) exemptionForm
                .get("justificationExemptionGratuity"));

        String otherValueExemptionGratuityString = (String) exemptionForm
                .get("otherValueExemptionGratuity");
        Integer otherValueExemptionGratuity = null;
        if (otherValueExemptionGratuityString != null && otherValueExemptionGratuityString.length() > 0) {
            otherValueExemptionGratuity = Integer.valueOf((String) exemptionForm
                    .get("otherValueExemptionGratuity"));
        }
        String otherJustificationExemptionGratuity = (String) exemptionForm
                .get("otherJustificationExemptionGratuity");

        InfoGratuitySituation infoGratuitySituation = fillGratuitySituationFromRequest(userView, request);

        //value
        if (valueExemptionGratuity != null) {
            infoGratuitySituation.setExemptionPercentage(valueExemptionGratuity);
            if (otherValueExemptionGratuity != null && valueExemptionGratuity.equals(new Integer(-1))) {
                infoGratuitySituation.setExemptionPercentage(otherValueExemptionGratuity);
            }
        }
        //justification
        if (justificationExemptionGratuity != null) {
            infoGratuitySituation.setExemptionType(ExemptionGratuityType
                    .getEnum(justificationExemptionGratuity.intValue()));
            if (justificationExemptionGratuity.equals(new Integer(ExemptionGratuityType.OTHER_TYPE))) {
                infoGratuitySituation.setExemptionDescription(otherJustificationExemptionGratuity);
            }
        }

        return infoGratuitySituation;
    }

    /**
     * @param userView
     * @param request
     * @return
     */
    private InfoGratuitySituation fillGratuitySituationFromRequest(IUserView userView,
            HttpServletRequest request) {

        String studentCurricularPlanID = request.getParameter("studentCurricularPlanID");
        request.setAttribute("studentCurricularPlanID", studentCurricularPlanID);
        String executionYear = request.getParameter("executionYear");
        request.setAttribute("executionYear", executionYear);
        String gratuitySituationID = request.getParameter("gratuitySituationID");
        String gratuityValuesID = request.getParameter("gratuityValuesID");

        InfoGratuitySituation infoGratuitySituation = new InfoGratuitySituation();
        if (gratuitySituationID != null) {
            infoGratuitySituation.setIdInternal(Integer.valueOf(gratuitySituationID));
        }

        //Student Curricular Plan
        InfoStudentCurricularPlan infoStudentCurricularPlan = new InfoStudentCurricularPlan();
        infoStudentCurricularPlan.setIdInternal(Integer.valueOf(studentCurricularPlanID));
        infoGratuitySituation.setInfoStudentCurricularPlan(infoStudentCurricularPlan);

        //Gratuity Values
        InfoGratuityValues infoGratuityValues = new InfoGratuityValues();
        infoGratuityValues.setIdInternal(Integer.valueOf(gratuityValuesID));
        infoGratuitySituation.setInfoGratuityValues(infoGratuityValues);

        //employee who made register
        InfoPerson infoPerson = new InfoPerson();
        infoPerson.setUsername(userView.getUtilizador());

        InfoEmployee infoEmployee = new InfoEmployee();
        infoEmployee.setPerson(infoPerson);

        infoGratuitySituation.setInfoEmployee(infoEmployee);
        return infoGratuitySituation;
    }

    public ActionForward removeExemptionGratuity(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        InfoGratuitySituation infoGratuitySituation = fillGratuitySituationFromRequest(userView, request);

        //to remove an exemption is equivalent to put 0 in exemption percentage
        // this is necessary to recalculate remaining value from gratuity
        // situation
        infoGratuitySituation.setExemptionPercentage(new Integer(0));

        Object[] args = { infoGratuitySituation };
        try {
            infoGratuitySituation = (InfoGratuitySituation) ServiceManagerServiceFactory.executeService(
                    userView, "EditGratuitySituationById", args);

        } catch (FenixServiceException exception) {
            exception.printStackTrace();
            errors.add("removeExemptionGratuity", new ActionError(
                    "error.impossible.removeExemptionGratuity"));
            saveErrors(request, errors);
            mapping.getInputForward();
        }

        Boolean result = Boolean.FALSE;
        if (infoGratuitySituation != null) {
            result = Boolean.TRUE;
        }

        request.setAttribute("removeExemptionGratuity", result.toString());

        return mapping.findForward("confirmationExemptionGratuity");
    }

    protected Map getKeyMethodMap() {

        Map map = new HashMap();
        map.put("button.masterDegree.gratuity.give", "insertExemptionGratuity");
        map.put("button.masterDegree.gratuity.remove", "removeExemptionGratuity");
        return map;
    }

}