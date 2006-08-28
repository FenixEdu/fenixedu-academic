package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ScholarshipNotFinishedServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeProofVersion;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.masterDegree.MasterDegreeClassification;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.GratuitySituationNotRegularizedActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ScholarshipNotFinishedActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.Data;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * 
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */

public class ChangeMasterDegreeProofDispatchAction extends FenixDispatchAction {

    public ActionForward getStudentAndMasterDegreeProofVersion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        String degreeType = request.getParameter("degreeType");
        Integer studentNumber = Integer.valueOf(request.getParameter("studentNumber"));

        MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
        ActionErrors actionErrors = new ActionErrors();
        boolean isSuccess = operations.getStudentByNumberAndDegreeType(form, request, actionErrors);

        if (isSuccess == false) {
            throw new NonExistingActionException("error.exception.masterDegree.nonExistentStudent",
                    mapping.findForward("error"));

        }

        InfoStudentCurricularPlan infoStudentCurricularPlan = readStudentCurricularPlan(mapping,
                userView, degreeType, studentNumber);
        InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = readMasterDegreeThesisDataVersion(
                mapping, userView, infoStudentCurricularPlan);

        putMasterDegreeThesisDataInRequest(request, infoMasterDegreeThesisDataVersion);

        //check if gratuity situation is Ok
        List infoGratuitySituations = null;
        Object args[] = { infoStudentCurricularPlan.getIdInternal() };
        try {
            infoGratuitySituations = (List) ServiceUtils.executeService(userView,
                    "ReadGratuitySituationListByStudentCurricularPlan", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        InfoGratuitySituation infoGratuitySituation = null;
        for (Iterator iter = infoGratuitySituations.iterator(); iter.hasNext();) {
            infoGratuitySituation = (InfoGratuitySituation) iter.next();
            if (infoGratuitySituation.getRemainingValue().doubleValue() > 0) {
                throw new GratuitySituationNotRegularizedActionException(
                        "error.exception.masterDegree.gratuityNotRegularized", mapping
                                .findForward("errorGratuityNotRegularized"));
            }

        }

        /* * * get master degree proof * * */
        InfoMasterDegreeProofVersion infoMasterDegreeProofVersion = null;
        Object argsMasterDegreeProofVersion[] = { infoStudentCurricularPlan.getIdInternal() };
        try {
            infoMasterDegreeProofVersion = (InfoMasterDegreeProofVersion) ServiceUtils.executeService(
                    userView, "ReadActiveMasterDegreeProofVersionByStudentCurricularPlan",
                    argsMasterDegreeProofVersion);
        } catch (NonExistingServiceException e) {
            DynaActionForm changeMasterDegreeThesisForm = (DynaActionForm) form;

            prepareFormForNewMasterDegreeProofVersion(degreeType, studentNumber,
                    infoMasterDegreeThesisDataVersion, changeMasterDegreeThesisForm);

            return mapping.findForward("start");

        } catch (ScholarshipNotFinishedServiceException e) {
            throw new ScholarshipNotFinishedActionException(
                    "error.exception.masterDegree.scholarshipNotFinished", mapping
                            .findForward("errorScholarshipNotFinished"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoMasterDegreeProofVersion.getInfoJuries().isEmpty() == false)
            request.setAttribute(SessionConstants.JURIES_LIST, infoMasterDegreeProofVersion
                    .getInfoJuries());

        if (infoMasterDegreeProofVersion.getInfoExternalJuries().isEmpty() == false)
            request.setAttribute(SessionConstants.EXTERNAL_JURIES_LIST, infoMasterDegreeProofVersion
                    .getInfoExternalJuries());

        String proofDateDay = null;
        String proofDateMonth = null;
        String proofDateYear = null;

        Date proofDate = infoMasterDegreeProofVersion.getProofDate();
        if (proofDate != null) {
            Calendar proofDateCalendar = new GregorianCalendar();
            proofDateCalendar.setTime(proofDate);
            proofDateDay = (new Integer(proofDateCalendar.get(Calendar.DAY_OF_MONTH))).toString();
            proofDateMonth = (new Integer(proofDateCalendar.get(Calendar.MONTH))).toString();
            proofDateYear = (new Integer(proofDateCalendar.get(Calendar.YEAR))).toString();

        }

        String thesisDeliveryDateDay = null;
        String thesisDeliveryDateMonth = null;
        String thesisDeliveryDateYear = null;

        Date thesisDeliveryDate = infoMasterDegreeProofVersion.getThesisDeliveryDate();
        if (thesisDeliveryDate != null) {
            Calendar thesisDeliveryDateCalendar = new GregorianCalendar();
            thesisDeliveryDateCalendar.setTime(thesisDeliveryDate);
            thesisDeliveryDateDay = new Integer(thesisDeliveryDateCalendar.get(Calendar.DAY_OF_MONTH))
                    .toString();
            thesisDeliveryDateMonth = new Integer(thesisDeliveryDateCalendar.get(Calendar.MONTH))
                    .toString();
            thesisDeliveryDateYear = new Integer(thesisDeliveryDateCalendar.get(Calendar.YEAR))
                    .toString();
        }

        prepareFormForMasterDegreeProofEdition(form, degreeType, studentNumber,
                infoMasterDegreeProofVersion, infoMasterDegreeThesisDataVersion, proofDateDay,
                proofDateMonth, proofDateYear, thesisDeliveryDateDay, thesisDeliveryDateMonth,
                thesisDeliveryDateYear);

        return mapping.findForward("start");

    }

    private void prepareFormForMasterDegreeProofEdition(ActionForm form, String degreeType,
            Integer studentNumber, InfoMasterDegreeProofVersion infoMasterDegreeProofVersion,
            InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion, String proofDateDay,
            String proofDateMonth, String proofDateYear, String thesisDeliveryDateDay,
            String thesisDeliveryDateMonth, String thesisDeliveryDateYear) {
        DynaActionForm changeMasterDegreeThesisForm = (DynaActionForm) form;

        changeMasterDegreeThesisForm.set("studentNumber", studentNumber);
        changeMasterDegreeThesisForm.set("degreeType", degreeType);
        changeMasterDegreeThesisForm.set("dissertationTitle", infoMasterDegreeThesisDataVersion
                .getDissertationTitle());
        changeMasterDegreeThesisForm.set("finalResult", infoMasterDegreeProofVersion
                .getFinalResult().name());
        changeMasterDegreeThesisForm.set("attachedCopiesNumber", infoMasterDegreeProofVersion
                .getAttachedCopiesNumber());
        changeMasterDegreeThesisForm.set("proofDateDay", proofDateDay);
        changeMasterDegreeThesisForm.set("proofDateMonth", proofDateMonth);
        changeMasterDegreeThesisForm.set("proofDateYear", proofDateYear);
        changeMasterDegreeThesisForm.set("thesisDeliveryDateDay", thesisDeliveryDateDay);
        changeMasterDegreeThesisForm.set("thesisDeliveryDateMonth", thesisDeliveryDateMonth);
        changeMasterDegreeThesisForm.set("thesisDeliveryDateYear", thesisDeliveryDateYear);
    }

    private void prepareFormForNewMasterDegreeProofVersion(String degreeType, Integer studentNumber,
            InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion,
            DynaActionForm changeMasterDegreeThesisForm) {
        changeMasterDegreeThesisForm.set("studentNumber", studentNumber);
        changeMasterDegreeThesisForm.set("degreeType", degreeType);
        changeMasterDegreeThesisForm.set("dissertationTitle", infoMasterDegreeThesisDataVersion
                .getDissertationTitle());
        changeMasterDegreeThesisForm.set("finalResult", MasterDegreeClassification.UNDEFINED.name());
        changeMasterDegreeThesisForm.set("attachedCopiesNumber", new Integer(0));
        changeMasterDegreeThesisForm.set("proofDateDay", null);
        changeMasterDegreeThesisForm.set("proofDateMonth", null);
        changeMasterDegreeThesisForm.set("proofDateYear", null);
        changeMasterDegreeThesisForm.set("thesisDeliveryDateDay", null);
        changeMasterDegreeThesisForm.set("thesisDeliveryDateMonth", null);
        changeMasterDegreeThesisForm.set("thesisDeliveryDateYear", null);
    }

    private void putMasterDegreeThesisDataInRequest(HttpServletRequest request,
            InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion) {
        request.setAttribute(SessionConstants.DISSERTATION_TITLE, infoMasterDegreeThesisDataVersion
                .getDissertationTitle());

        request.setAttribute(SessionConstants.DAYS_LIST, Data.getMonthDays());
        request.setAttribute(SessionConstants.MONTHS_LIST, Data.getMonths());
        request.setAttribute(SessionConstants.YEARS_LIST, Data.getExpirationYears());
    }

    private InfoMasterDegreeThesisDataVersion readMasterDegreeThesisDataVersion(ActionMapping mapping,
            IUserView userView, InfoStudentCurricularPlan infoStudentCurricularPlan)
            throws NonExistingActionException, FenixActionException, FenixFilterException {
        InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = null;

        /* * * get master degree thesis data * * */
        Object argsMasterDegreeThesisDataVersion[] = { infoStudentCurricularPlan };
        try {
            infoMasterDegreeThesisDataVersion = (InfoMasterDegreeThesisDataVersion) ServiceUtils
                    .executeService(userView,
                            "ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan",
                            argsMasterDegreeThesisDataVersion);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(
                    "error.exception.masterDegree.nonExistingMasterDegreeThesis", mapping
                            .findForward("error"));

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return infoMasterDegreeThesisDataVersion;
    }

    private InfoStudentCurricularPlan readStudentCurricularPlan(ActionMapping mapping,
            IUserView userView, String degreeType, Integer studentNumber) throws FenixActionException,
            NonExistingActionException, FenixFilterException {
        InfoStudentCurricularPlan infoStudentCurricularPlan = null;

        Object argsStudentCurricularPlan[] = { studentNumber, DegreeType.valueOf(degreeType)};
        try {
            infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceUtils.executeService(
                    userView, "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
                    argsStudentCurricularPlan);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoStudentCurricularPlan == null) {
            throw new NonExistingActionException(
                    "error.exception.masterDegree.nonExistentActiveStudentCurricularPlan", mapping
                            .findForward("error"));
        }
        return infoStudentCurricularPlan;
    }

    public ActionForward reloadForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
        ActionErrors actionErrors = new ActionErrors();

        transportData(form, request);

        try {
            operations.getTeachersByNumbers(form, request, "juriesNumbers",
                    SessionConstants.JURIES_LIST, actionErrors);
            operations.getStudentByNumberAndDegreeType(form, request, actionErrors);

        } catch (Exception e1) {
            throw new FenixActionException(e1);
        }

        return mapping.findForward("start");

    }

    private void transportData(ActionForm form, HttpServletRequest request) {

        // dissertation title
        DynaActionForm masterDegreeProofForm = (DynaActionForm) form;
        String dissertationTitle = (String) masterDegreeProofForm.get("dissertationTitle");
        request.setAttribute(SessionConstants.DISSERTATION_TITLE, dissertationTitle);

        // dates combo boxes options
        request.setAttribute(SessionConstants.DAYS_LIST, Data.getMonthDays());
        request.setAttribute(SessionConstants.MONTHS_LIST, Data.getMonths());
        request.setAttribute(SessionConstants.YEARS_LIST, Data.getExpirationYears());

    }

}