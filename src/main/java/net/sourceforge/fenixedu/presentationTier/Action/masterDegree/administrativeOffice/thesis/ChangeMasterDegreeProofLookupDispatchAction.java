package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ScholarshipNotFinishedServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis.ChangeMasterDegreeProof;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.masterDegree.MasterDegreeClassification;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ScholarshipNotFinishedActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.util.Data;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.LookupDispatchAction;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * 
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */

public class ChangeMasterDegreeProofLookupDispatchAction extends LookupDispatchAction {

    public ActionForward addJury(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
        ActionErrors actionErrors = new ActionErrors();

        transportData(form, request);

        try {
            operations.getTeachersByNumbers(form, request, "juriesNumbers", PresentationConstants.JURIES_LIST, actionErrors);
            operations.getExternalPersonsByIDs(form, request, "externalJuriesIDs", PresentationConstants.EXTERNAL_JURIES_LIST,
                    actionErrors);
            operations.getStudentByNumberAndDegreeType(form, request, actionErrors);

        } catch (Exception e1) {
            throw new FenixActionException(e1);
        } finally {
            saveErrors(request, actionErrors);
        }

        return mapping.findForward("start");

    }

    public ActionForward removeJuries(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        DynaActionForm changeMasterDegreeProofForm = (DynaActionForm) form;

        Integer[] teachersNumbersList = (Integer[]) changeMasterDegreeProofForm.get("juriesNumbers");
        Integer[] removedJuries = (Integer[]) changeMasterDegreeProofForm.get("removedJuriesNumbers");

        changeMasterDegreeProofForm.set("juriesNumbers", subtractArray(teachersNumbersList, removedJuries));

        MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
        ActionErrors actionErrors = new ActionErrors();

        transportData(form, request);

        try {
            operations.getTeachersByNumbers(form, request, "juriesNumbers", PresentationConstants.JURIES_LIST, actionErrors);
            operations.getExternalPersonsByIDs(form, request, "externalJuriesIDs", PresentationConstants.EXTERNAL_JURIES_LIST,
                    actionErrors);
            operations.getStudentByNumberAndDegreeType(form, request, actionErrors);

        } catch (Exception e1) {
            throw new FenixActionException(e1);
        } finally {
            saveErrors(request, actionErrors);
        }

        return mapping.findForward("start");

    }

    public ActionForward externalJury(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        // to display the external persons search form
        request.setAttribute(PresentationConstants.SEARCH_EXTERNAL_JURIES, new Boolean(true));

        MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
        ActionErrors actionErrors = new ActionErrors();

        transportData(form, request);

        try {
            operations.getTeachersByNumbers(form, request, "juriesNumbers", PresentationConstants.JURIES_LIST, actionErrors);
            operations.getExternalPersonsByIDs(form, request, "externalJuriesIDs", PresentationConstants.EXTERNAL_JURIES_LIST,
                    actionErrors);
            operations.getStudentByNumberAndDegreeType(form, request, actionErrors);

        } catch (Exception e1) {
            throw new FenixActionException(e1);
        } finally {
            saveErrors(request, actionErrors);
        }

        return mapping.findForward("start");

    }

    public ActionForward searchExternalJury(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
        ActionErrors actionErrors = new ActionErrors();

        transportData(form, request);

        try {
            operations.getTeachersByNumbers(form, request, "juriesNumbers", PresentationConstants.JURIES_LIST, actionErrors);
            operations.getExternalPersonsByIDs(form, request, "externalJuriesIDs", PresentationConstants.EXTERNAL_JURIES_LIST,
                    actionErrors);
            operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
            operations.getExternalPersonsByName(form, request, "externalJuryName",
                    PresentationConstants.EXTERNAL_JURIES_SEARCH_RESULTS, actionErrors);

        } catch (Exception e1) {
            throw new FenixActionException(e1);
        } finally {
            saveErrors(request, actionErrors);
        }

        return mapping.findForward("start");

    }

    public ActionForward addExternalJury(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
        ActionErrors actionErrors = new ActionErrors();

        transportData(form, request);

        try {
            operations.getTeachersByNumbers(form, request, "juriesNumbers", PresentationConstants.JURIES_LIST, actionErrors);
            operations.getExternalPersonsByIDs(form, request, "externalJuriesIDs", PresentationConstants.EXTERNAL_JURIES_LIST,
                    actionErrors);
            operations.getStudentByNumberAndDegreeType(form, request, actionErrors);

        } catch (Exception e1) {
            throw new FenixActionException(e1);
        } finally {
            saveErrors(request, actionErrors);
        }

        return mapping.findForward("start");

    }

    public ActionForward removeExternalJuries(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        DynaActionForm masterDegreeProofForm = (DynaActionForm) form;

        Integer[] externalPersonsIDsList = (Integer[]) masterDegreeProofForm.get("externalJuriesIDs");
        Integer[] removedExternalJuries = (Integer[]) masterDegreeProofForm.get("removedExternalJuriesIDs");

        masterDegreeProofForm.set("externalJuriesIDs", subtractArray(externalPersonsIDsList, removedExternalJuries));

        MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
        ActionErrors actionErrors = new ActionErrors();

        transportData(form, request);

        try {
            operations.getTeachersByNumbers(form, request, "juriesNumbers", PresentationConstants.JURIES_LIST, actionErrors);
            operations.getExternalPersonsByIDs(form, request, "externalJuriesIDs", PresentationConstants.EXTERNAL_JURIES_LIST,
                    actionErrors);
            operations.getStudentByNumberAndDegreeType(form, request, actionErrors);

        } catch (Exception e1) {
            throw new FenixActionException(e1);
        } finally {
            saveErrors(request, actionErrors);
        }

        return mapping.findForward("start");

    }

    public ActionForward changeMasterDegreeProof(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        IUserView userView = UserView.getUser();

        DynaActionForm changeMasterDegreeProofForm = (DynaActionForm) form;

        String degreeType = (String) changeMasterDegreeProofForm.get("degreeType");
        Integer scpID = (Integer) changeMasterDegreeProofForm.get("scpID");

        String finalResultString = (String) changeMasterDegreeProofForm.get("finalResult");
        MasterDegreeClassification finalResult =
                (finalResultString.length() == 0) ? MasterDegreeClassification.UNDEFINED : MasterDegreeClassification
                        .valueOf(finalResultString);
        Integer attachedCopiesNumber = (Integer) changeMasterDegreeProofForm.get("attachedCopiesNumber");

        String proofDateDay = (String) changeMasterDegreeProofForm.get("proofDateDay");
        String proofDateMonth = (String) changeMasterDegreeProofForm.get("proofDateMonth");
        String proofDateYear = (String) changeMasterDegreeProofForm.get("proofDateYear");

        String thesisDeliveryDateDay = (String) changeMasterDegreeProofForm.get("thesisDeliveryDateDay");
        String thesisDeliveryDateMonth = (String) changeMasterDegreeProofForm.get("thesisDeliveryDateMonth");
        String thesisDeliveryDateYear = (String) changeMasterDegreeProofForm.get("thesisDeliveryDateYear");

        Date proofDate = buildProofDate(proofDateDay, proofDateMonth, proofDateYear);

        Date thesisDeliveryDate = buildThesisDeliveryDate(thesisDeliveryDateDay, thesisDeliveryDateMonth, thesisDeliveryDateYear);

        StudentCurricularPlan studentCurricularPlan = RootDomainObject.getInstance().readStudentCurricularPlanByOID(scpID);

        MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
        ActionErrors actionErrors = new ActionErrors();
        List<String> juriesNumbers = null;
        List<Integer> externalJuriesIDs = null;

        try {
            operations.transportStudentCurricularPlan(form, request, actionErrors, studentCurricularPlan);

            juriesNumbers = operations.getTeachersNumbers(form, "juriesNumbers");
            externalJuriesIDs = operations.getExternalPersonsIDs(form, "externalJuriesIDs");

        } catch (Exception e1) {
            throw new FenixActionException(e1);
        } finally {

            saveErrors(request, actionErrors);

            if (actionErrors.isEmpty() == false) {
                transportData(form, request);
                return mapping.findForward("start");
            }

        }

        executeChangeMasterDegreeProofService(mapping, userView, finalResult, attachedCopiesNumber, proofDate,
                thesisDeliveryDate, scpID, juriesNumbers, externalJuriesIDs);

        return mapping.findForward("success");

    }

    private void executeChangeMasterDegreeProofService(ActionMapping mapping, IUserView userView,
            MasterDegreeClassification finalResult, Integer attachedCopiesNumber, Date proofDate, Date thesisDeliveryDate,
            Integer scpID, List<String> juriesNumbers, List<Integer> externalJuriesIDs) throws NonExistingActionException,
            ScholarshipNotFinishedActionException, ExistingActionException {

        try {
            ChangeMasterDegreeProof.run(userView, scpID, proofDate, thesisDeliveryDate, finalResult, attachedCopiesNumber,
                    juriesNumbers, externalJuriesIDs);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(e.getMessage(), mapping.findForward("start"));
        } catch (ScholarshipNotFinishedServiceException e) {
            throw new ScholarshipNotFinishedActionException(e.getMessage(), mapping.findForward("start"));
        } catch (FenixServiceException e) {
            throw new ExistingActionException(e.getMessage(), mapping.findForward("start"));
        }
    }

    private Date buildThesisDeliveryDate(String thesisDeliveryDateDay, String thesisDeliveryDateMonth,
            String thesisDeliveryDateYear) throws NumberFormatException {
        Date thesisDeliveryDate = null;

        if ((thesisDeliveryDateDay.length() > 0) && (thesisDeliveryDateMonth.length() > 0)
                && (thesisDeliveryDateYear.length() > 0)) {
            Calendar thesisDeliveryDateCalendar =
                    new GregorianCalendar(Integer.parseInt(thesisDeliveryDateYear), Integer.parseInt(thesisDeliveryDateMonth),
                            Integer.parseInt(thesisDeliveryDateDay));

            thesisDeliveryDate = thesisDeliveryDateCalendar.getTime();
        }
        return thesisDeliveryDate;
    }

    private Date buildProofDate(String proofDateDay, String proofDateMonth, String proofDateYear) throws NumberFormatException {
        Date proofDate = null;

        if ((proofDateDay.length() > 0) && (proofDateMonth.length() > 0) && (proofDateYear.length() > 0)) {
            Calendar proofDateCalendar =
                    new GregorianCalendar(Integer.parseInt(proofDateYear), Integer.parseInt(proofDateMonth),
                            Integer.parseInt(proofDateDay));

            proofDate = proofDateCalendar.getTime();
        }
        return proofDate;
    }

    public ActionForward cancelChangeMasterDegreeProof(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        PrepareStudentDataForThesisOperationsDispatchAction prepareStudentDataForThesisOperations =
                new PrepareStudentDataForThesisOperationsDispatchAction();
        return prepareStudentDataForThesisOperations.getStudentAndDegreeTypeForThesisOperations(mapping, form, request, response);

    }

    public void transportData(ActionForm form, HttpServletRequest request) {

        // dissertation title
        DynaActionForm masterDegreeProofForm = (DynaActionForm) form;
        String dissertationTitle = (String) masterDegreeProofForm.get("dissertationTitle");
        request.setAttribute(PresentationConstants.DISSERTATION_TITLE, dissertationTitle);

        // dates combo boxes options
        request.setAttribute(PresentationConstants.DAYS_LIST, Data.getMonthDays());
        request.setAttribute(PresentationConstants.MONTHS_LIST, Data.getMonths());
        request.setAttribute(PresentationConstants.YEARS_LIST, Data.getExpirationYears());

    }

    private Integer[] subtractArray(Integer[] originalArray, Integer[] arrayToSubtract) {
        List tmp = new ArrayList();

        for (Integer element : originalArray) {
            tmp.add(element);
        }

        for (Integer element : arrayToSubtract) {
            tmp.remove(element);
        }

        originalArray = (Integer[]) tmp.toArray(new Integer[] {});
        return originalArray;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.actions.LookupDispatchAction#getKeyMethodMap()
     */
    @Override
    protected Map getKeyMethodMap() {

        Map map = new HashMap();
        map.put("button.submit.masterDegree.thesis.addJury", "addJury");
        map.put("button.submit.masterDegree.thesis.removeJuries", "removeJuries");
        map.put("button.submit.masterDegree.thesis.changeProof", "changeMasterDegreeProof");
        map.put("button.submit.masterDegree.thesis.externalJury", "externalJury");
        map.put("button.submit.masterDegree.thesis.searchExternalJury", "searchExternalJury");
        map.put("button.submit.masterDegree.thesis.addExternalJury", "addExternalJury");
        map.put("button.submit.masterDegree.thesis.removeExternalJuries", "removeExternalJuries");
        map.put("button.cancel", "cancelChangeMasterDegreeProof");
        return map;
    }

}