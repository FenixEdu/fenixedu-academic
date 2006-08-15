package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEmployee;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuityValues;
import net.sourceforge.fenixedu.dataTransferObject.InfoPaymentPhase;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.Data;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Fernanda Quitério 6/Jan/2003
 * 
 */
public class InsertGratuityDataLookupDispatchAction extends LookupDispatchAction {
    public ActionForward addPhase(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ActionErrors errors = new ActionErrors();

        DynaValidatorForm gratuityForm = (DynaValidatorForm) form;
        String initialDate = (String) gratuityForm.get("initialDatePartialPayment");
        String finalDate = (String) gratuityForm.get("finalDatePartialPayment");
        String phaseValue = (String) gratuityForm.get("phaseValue");
        maintainState(request, gratuityForm);

        List<InfoPaymentPhase> infoPaymentPhases = collectPaymentPhases(gratuityForm);

        if (finalDate == null || finalDate.length() == 0 || phaseValue == null
                || phaseValue.length() == 0) {
            errors.add("wrongValues", new ActionError("error.masterDegree.gratuity.phaseValues"));
            saveErrors(request, errors);
        }
        if (errors.isEmpty()) {
            if (initialDate == null
                    || initialDate.length() == 0
                    || (initialDate.length() > 0 && !Data.convertStringDate(finalDate, "/").before(
                            Data.convertStringDate(initialDate, "/")))) {
                fillPaymentPhasesList(initialDate, finalDate, phaseValue, infoPaymentPhases);
            } else {
                errors
                        .add("wrongDates", new ActionError(
                                "error.masterDegree.gratuity.phase.wrongDates"));
                saveErrors(request, errors);
            }
        }
        Collections.sort(infoPaymentPhases, new BeanComparator("endDate"));
        request.setAttribute("infoPaymentPhases", infoPaymentPhases);

        return mapping.findForward("insertGratuityData");
    }

    private List<InfoPaymentPhase> collectPaymentPhases(DynaValidatorForm gratuityForm) {
        String[] paymentPhases = (String[]) gratuityForm.get("paymentPhases");
        List<InfoPaymentPhase> infoPaymentPhases = new ArrayList<InfoPaymentPhase>();
        for (int i = 0; i < paymentPhases.length; i = i + 3) {
            fillPaymentPhasesList(paymentPhases[i], paymentPhases[i + 1], paymentPhases[i + 2],
                    infoPaymentPhases);
        }
        return infoPaymentPhases;
    }

    private void fillPaymentPhasesList(String initialDate, String finalDate, String phaseValue,
            List<InfoPaymentPhase> infoPaymentPhases) {
        InfoPaymentPhase newInfoPaymentPhase = new InfoPaymentPhase();
        if (initialDate != null && initialDate.length() > 0) {
            newInfoPaymentPhase.setStartDate(Data.convertStringDate(initialDate, "/"));
        }
        newInfoPaymentPhase.setEndDate(Data.convertStringDate(finalDate, "/"));
        newInfoPaymentPhase.setValue(new Double(phaseValue));
        infoPaymentPhases.add(newInfoPaymentPhase);
    }

    public ActionForward removePhase(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        DynaValidatorForm gratuityForm = (DynaValidatorForm) form;
        maintainState(request, gratuityForm);

        List<InfoPaymentPhase> infoPaymentPhases = collectPaymentPhases(gratuityForm);

        removePhasesFromList(gratuityForm, infoPaymentPhases);

        request.setAttribute("infoPaymentPhases", infoPaymentPhases);

        return mapping.findForward("insertGratuityData");
    }

    private void maintainState(HttpServletRequest request, DynaValidatorForm gratuityForm) {
        String executionYear = (String) gratuityForm.get("executionYear");
        String degreeName = (String) gratuityForm.get("degreeName");
        String degree = (String) gratuityForm.get("degree");
        String gratuityId = (String) gratuityForm.get("gratuityId");
        request.setAttribute("gratuityId", gratuityId);
        request.setAttribute("degree", degree);
        request.setAttribute("degreeName", degreeName);
        request.setAttribute("executionYear", executionYear);
    }

    private void removePhasesFromList(DynaValidatorForm gratuityForm,
            List<InfoPaymentPhase> infoPaymentPhases) {

        String[] phasesToRemove = (String[]) gratuityForm.get("removedPhases");
        Collection<InfoPaymentPhase> objectsToRemove = new ArrayList<InfoPaymentPhase>(
                phasesToRemove.length);
        for (String phaseToRemoveIndex : phasesToRemove) {
            objectsToRemove.add(infoPaymentPhases.get(Integer.valueOf(phaseToRemoveIndex)));
        }

        infoPaymentPhases.removeAll(objectsToRemove);

        gratuityForm.set("removedPhases", new String[] {});

    }

    public ActionForward cancelInsertGratuityData(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("cancel");
    }

    public ActionForward insertGratuityData(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException {
        IUserView userView = SessionUtils.getUserView(request);
        ActionErrors errors = new ActionErrors();
        DynaValidatorForm gratuityForm = (DynaValidatorForm) form;
        maintainState(request, gratuityForm);

        InfoGratuityValues infoGratuityValues = fillGratuity(userView, gratuityForm);

        Object[] args = { infoGratuityValues };
        try {
            ServiceManagerServiceFactory.executeService(userView, "InsertGratuityData", args);
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("impossible.insertGratuityValues")) {
                errors.add("exception", new ActionError("error.impossible.insertGratuityValues",
                        gratuityForm.get("degreeName")));
            } else {
                errors.add("wrongValues", new ActionError(e.getMessage()));
            }
            saveErrors(request, errors);

            List infoPaymentPhases = collectPaymentPhases(gratuityForm);
            request.setAttribute("infoPaymentPhases", infoPaymentPhases);

            return mapping.getInputForward();
        }

        Integer degreeId = new Integer(infoGratuityValues.getInfoExecutionDegree().getIdInternal()
                .intValue());

        infoGratuityValues = null;
        Object argsRead[] = { degreeId };
        try {
            infoGratuityValues = (InfoGratuityValues) ServiceUtils.executeService(userView,
                    "ReadGratuityValuesByExecutionDegree", argsRead);
        } catch (FenixServiceException ex) {
            errors.add("gratuityValues", new ActionError(ex.getMessage()));
            saveErrors(request, errors);
        }

        request.setAttribute("infoGratuityValues", infoGratuityValues);

        return mapping.findForward("insertConfirmation");
    }

    private InfoGratuityValues fillGratuity(IUserView userView, DynaValidatorForm actionForm) {
        InfoPerson infoPerson = new InfoPerson();
        infoPerson.setUsername(userView.getUtilizador());
        InfoEmployee infoEmployee = new InfoEmployee();
        infoEmployee.setPerson(infoPerson);

        String degree = (String) actionForm.get("degree");
        Integer degreeId = Integer.valueOf(StringUtils.substringAfter(degree, "#"));

        InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree(RootDomainObject.getInstance().readExecutionDegreeByOID(degreeId));

        InfoGratuityValues infoGratuityValues = new InfoGratuityValues();

        infoGratuityValues.setInfoExecutionDegree(infoExecutionDegree);
        infoGratuityValues.setInfoEmployee(infoEmployee);

        if ((String) actionForm.get("gratuityId") != null
                && ((String) actionForm.get("gratuityId")).length() > 0) {
            infoGratuityValues.setIdInternal(Integer.valueOf((String) actionForm.get("gratuityId")));
        }

        if (actionForm.get("annualValue") != null
                && ((String) actionForm.get("annualValue")).length() > 0) {
            infoGratuityValues.setAnualValue(Double.valueOf((String) actionForm.get("annualValue")));
        }
        if (actionForm.get("scholarPart") != null
                && ((String) actionForm.get("scholarPart")).length() > 0) {
            infoGratuityValues.setScholarShipValue(Double
                    .valueOf((String) actionForm.get("scholarPart")));

            String thesisPart = (String) actionForm.get("thesisPart");
            infoGratuityValues
                    .setFinalProofValue((thesisPart.length() > 0) ? new Double(thesisPart) : 0);
        }
        if (actionForm.get("unitaryValueCredit") != null
                && ((String) actionForm.get("unitaryValueCredit")).length() > 0) {
            infoGratuityValues.setCreditValue(Double.valueOf((String) actionForm
                    .get("unitaryValueCredit")));
        } else {
            infoGratuityValues.setCourseValue(Double.valueOf((String) actionForm
                    .get("unitaryValueCourse")));
        }
        infoGratuityValues.setProofRequestPayment((Boolean) actionForm.get("paymentWhen"));
        if (actionForm.get("totalPayment") != null
                && ((Boolean) actionForm.get("totalPayment")).equals(Boolean.TRUE)) {
            infoGratuityValues.setEndPayment(Data.convertStringDate((String) actionForm
                    .get("finalDateTotalPayment"), "/"));
            if (actionForm.get("initialDateTotalPayment") != null
                    && ((String) actionForm.get("initialDateTotalPayment")).length() > 0) {
                infoGratuityValues.setStartPayment(Data.convertStringDate((String) actionForm
                        .get("initialDateTotalPayment"), "/"));
            }
        }
        if (actionForm.get("partialPayment") != null
                && ((Boolean) actionForm.get("partialPayment")).equals(Boolean.TRUE)) {
            List infoPaymentPhases = new ArrayList();

            if (actionForm.get("registrationPayment") != null
                    && ((Boolean) actionForm.get("registrationPayment")).equals(Boolean.TRUE)) {

                infoGratuityValues.setRegistrationPayment((Boolean) actionForm
                        .get("registrationPayment"));

                InfoPaymentPhase infoPaymentPhase = fillAPaymentPhase(actionForm);
                infoPaymentPhases.add(infoPaymentPhase);
            }
            // collect payment phases
            String[] paymentPhases = (String[]) actionForm.get("paymentPhases");
            for (int i = 0; i < paymentPhases.length; i = i + 3) {
                fillPaymentPhasesList(paymentPhases[i], paymentPhases[i + 1], paymentPhases[i + 2],
                        infoPaymentPhases);
            }

            infoGratuityValues.setInfoPaymentPhases(infoPaymentPhases);
        }

        return infoGratuityValues;
    }

    private InfoPaymentPhase fillAPaymentPhase(DynaValidatorForm actionForm) {
        // treat registration payment as a phase payment
        InfoPaymentPhase infoPaymentPhase = new InfoPaymentPhase();
        infoPaymentPhase.setEndDate(Data.convertStringDate((String) actionForm
                .get("finalDateRegistrationPayment"), "/"));
        if (actionForm.get("initialDateRegistrationPayment") != null
                && ((String) actionForm.get("initialDateRegistrationPayment")).length() > 0) {
            infoPaymentPhase.setStartDate(Data.convertStringDate((String) actionForm
                    .get("initialDateRegistrationPayment"), "/"));
        }
        infoPaymentPhase.setValue(Double.valueOf((String) actionForm.get("registrationValue")));
        return infoPaymentPhase;
    }

    protected Map getKeyMethodMap() {

        Map map = new HashMap();
        map.put("button.masterDegree.gratuity.submit", "insertGratuityData");
        map.put("button.masterDegree.gratuity.addPhase", "addPhase");
        map.put("button.masterDegree.gratuity.remove", "removePhase");
        map.put("button.cancel", "cancelInsertGratuityData");
        return map;
    }
}