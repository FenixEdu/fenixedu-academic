package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEmployee;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuityValues;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.gratuity.ExemptionGratuityType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixLookupDispatchAction;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Fernanda Quitério Created on 31/Aug/2004
 * 
 */
public class ExemptionGratuityLAAction extends FenixLookupDispatchAction {

    public ActionForward insertExemptionGratuity(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
	
	final IUserView userView = getUserView(request);
        InfoGratuitySituation infoGratuitySituation = fillInfoGratuityValues(userView, request, (DynaActionForm) actionForm);
        try {
            infoGratuitySituation = (InfoGratuitySituation) ServiceManagerServiceFactory.executeService(
        	    userView, "EditGratuitySituationById", new Object[] { infoGratuitySituation });

        } catch (FenixServiceException exception) {
            exception.printStackTrace();
            ActionErrors errors = new ActionErrors();
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
        String justificationExemptionGratuity = (String) exemptionForm
                .get("justificationExemptionGratuity");
        Double adHocValueExemptionGratuity = (Double) exemptionForm.get("adHocValueExemptionGratuity");
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

        // value
        if (valueExemptionGratuity != null) {
            infoGratuitySituation.setExemptionPercentage(valueExemptionGratuity);
            if (otherValueExemptionGratuity != null && valueExemptionGratuity.equals(new Integer(-1))) {
                infoGratuitySituation.setExemptionPercentage(otherValueExemptionGratuity);
            }
        }

        // adhoc value
        if (adHocValueExemptionGratuity != null) {
            infoGratuitySituation.setExemptionValue(adHocValueExemptionGratuity);
        }

        // justification
        if (justificationExemptionGratuity != null) {
            infoGratuitySituation.setExemptionType(ExemptionGratuityType.valueOf(justificationExemptionGratuity)); 
            if (justificationExemptionGratuity.equals(ExemptionGratuityType.OTHER.name())) {
                infoGratuitySituation.setExemptionDescription(otherJustificationExemptionGratuity);
            }
        }

        return infoGratuitySituation;
    }

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

        // Registration Curricular Plan
        InfoStudentCurricularPlan infoStudentCurricularPlan = new InfoStudentCurricularPlan(
        		RootDomainObject.getInstance().readStudentCurricularPlanByOID(Integer.valueOf(studentCurricularPlanID)));
        infoGratuitySituation.setInfoStudentCurricularPlan(infoStudentCurricularPlan);

        // Gratuity Values
        InfoGratuityValues infoGratuityValues = new InfoGratuityValues();
        infoGratuityValues.setIdInternal(Integer.valueOf(gratuityValuesID));
        infoGratuitySituation.setInfoGratuityValues(infoGratuityValues);

        // employee who made register
        infoGratuitySituation.setInfoEmployee(new InfoEmployee(userView.getPerson().getEmployee()));

        return infoGratuitySituation;
    }

    public ActionForward removeExemptionGratuity(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
	
        ActionErrors errors = new ActionErrors();
        final IUserView userView = getUserView(request); 
        InfoGratuitySituation infoGratuitySituation = fillGratuitySituationFromRequest(userView, request);

        // to remove an exemption is equivalent to put 0 in exemption percentage
        // this is necessary to recalculate remaining value from gratuity
        // situation
        infoGratuitySituation.setExemptionPercentage(Integer.valueOf(0));
        infoGratuitySituation.setExemptionValue(Double.valueOf(0));

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

        request.setAttribute("removeExemptionGratuity", Boolean.valueOf(infoGratuitySituation != null).toString());

        return mapping.findForward("confirmationExemptionGratuity");
    }

    protected Map getKeyMethodMap() {

        Map map = new HashMap();
        map.put("button.masterDegree.gratuity.give", "insertExemptionGratuity");
        map.put("button.masterDegree.gratuity.remove", "removeExemptionGratuity");
        return map;
    }

}
