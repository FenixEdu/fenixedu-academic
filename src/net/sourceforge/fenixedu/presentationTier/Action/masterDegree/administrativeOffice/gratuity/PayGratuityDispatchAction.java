package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideEntry;
import net.sourceforge.fenixedu.dataTransferObject.InfoInsuranceValue;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoInsuranceTransaction;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.domain.masterDegree.GuideRequester;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class PayGratuityDispatchAction extends FenixDispatchAction {

    public ActionForward chooseContributor(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String gratuitySituationId = getFromRequest("gratuitySituationId", request);
        String studentId = getFromRequest("studentId", request);
        String insuranceExecutionYearId = getFromRequest("insuranceExecutionYearId", request);

        DynaActionForm payGratuityForm = (DynaActionForm) form;
        payGratuityForm.set("studentId", new Integer(studentId));

        if (gratuitySituationId != null) {
            payGratuityForm.set("gratuitySituationId", new Integer(gratuitySituationId));
            request.setAttribute(SessionConstants.PAGE_TITLE,
                    "link.masterDegree.administrativeOffice.gratuity.payGratuity");
        }

        if (insuranceExecutionYearId != null) {
            payGratuityForm.set("insuranceExecutionYearId", new Integer(insuranceExecutionYearId));
            request.setAttribute(SessionConstants.PAGE_TITLE,
                    "link.masterDegree.administrativeOffice.gratuity.payInsurance");
        }

        return mapping.findForward("chooseContributor");

    }

    public ActionForward confirmPayment(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm payGratuityForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);

        Integer contributorNumber = (Integer) payGratuityForm.get("contributorNumber");
        Integer studentId = (Integer) payGratuityForm.get("studentId");
        Integer gratuitySituationId = (Integer) payGratuityForm.get("gratuitySituationId");
        Integer insuranceExecutionYearId = (Integer) payGratuityForm.get("insuranceExecutionYearId");

        // Read Registration
        InfoStudent infoStudent = readStudent(mapping, userView, studentId);
        request.setAttribute(SessionConstants.STUDENT, infoStudent);

        if (gratuitySituationId.intValue() > 0) {
            // Read Gratuity Situation
            InfoGratuitySituation infoGratuitySituation = readGratuitySituation(userView,
                    gratuitySituationId);

            // Read Insurance Situation
            InfoInsuranceTransaction infoInsuranceTransaction = null;
            Integer executionYearID = infoGratuitySituation.getInfoGratuityValues()
                    .getInfoExecutionDegree().getInfoExecutionYear().getIdInternal();

            Object argsInsurance[] = { infoStudent.getIdInternal(), executionYearID };

            try {
                infoInsuranceTransaction = (InfoInsuranceTransaction) ServiceUtils
                        .executeService(userView,
                                "ReadInsuranceTransactionByStudentIDAndExecutionYearID", argsInsurance);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            if (infoInsuranceTransaction == null) {

                InfoInsuranceValue infoInsuranceValue = readInsuranceValue(userView, executionYearID);

                request.setAttribute(SessionConstants.INSURANCE_VALUE_WITH_GRATUITY, infoInsuranceValue);

            }

            request.setAttribute(SessionConstants.GRATUITY_SITUATION, infoGratuitySituation);
            request.setAttribute(SessionConstants.PAGE_TITLE,
                    "link.masterDegree.administrativeOffice.gratuity.payGratuity");

        }

        if (insuranceExecutionYearId.intValue() > 0) {
            // Read Insurance Value
            InfoInsuranceValue infoInsuranceValue = readInsuranceValue(userView,
                    insuranceExecutionYearId);

            request.setAttribute(SessionConstants.INSURANCE_VALUE, infoInsuranceValue);
            request.setAttribute(SessionConstants.PAGE_TITLE,
                    "link.masterDegree.administrativeOffice.gratuity.payInsurance");
        }

        // Read Contributor
        InfoContributor infoContributor = null;
        try {
            infoContributor = readContributor(mapping, userView, contributorNumber);
        } catch (NonExistingActionException e) {
            throw new NonExistingActionException(
                    "error.masterDegree.administrativeOffice.nonExistingContributorSimple", mapping
                            .findForward("chooseContributor"));
        } catch (FenixActionException e) {

            e.printStackTrace();
        }
        request.setAttribute(SessionConstants.CONTRIBUTOR, infoContributor);

        return mapping.findForward("confirmPayment");

    }

    public ActionForward pay(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm payGratuityForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);

        Integer contributorNumber = (Integer) payGratuityForm.get("contributorNumber");
        Integer gratuitySituationId = (Integer) payGratuityForm.get("gratuitySituationId");
        Integer studentId = (Integer) payGratuityForm.get("studentId");
        Integer insuranceExecutionYearId = (Integer) payGratuityForm.get("insuranceExecutionYearId");
        Double adHocValue = (Double) payGratuityForm.get("adHocValue");

        InfoGuide infoGuide = new InfoGuide();

        // Read Contributor
        InfoContributor infoContributor = readContributor(mapping, userView, contributorNumber);

        // Read Registration
        InfoStudent infoStudent = readStudent(mapping, userView, studentId);

        List infoGuideEntries = new ArrayList();

        if (gratuitySituationId.intValue() > 0) {
            // Read Gratuity Situation
            InfoGratuitySituation infoGratuitySituation = readGratuitySituation(userView,
                    gratuitySituationId);

            InfoGuideEntry infoGuideEntry = new InfoGuideEntry();
            infoGuideEntry.setDocumentType(DocumentType.GRATUITY);
            infoGuideEntry.setPrice(infoGratuitySituation.getRemainingValue());
            infoGuideEntry.setQuantity(new Integer(1));
            infoGuideEntry.setGraduationType(GraduationType.MASTER_DEGREE);
            infoGuideEntry.setDescription("");

            infoGuide.setInfoExecutionDegree(infoGratuitySituation.getInfoGratuityValues()
                    .getInfoExecutionDegree());

            if (adHocValue.doubleValue() > infoGratuitySituation.getRemainingValue().doubleValue()) {

                ActionErrors errors = new ActionErrors();
                errors.add("invalidValue", new ActionError(
                        "error.masterDegree.gratuity.adHocValueGreater"));
                saveErrors(request, errors);

                request.setAttribute(SessionConstants.CONTRIBUTOR, infoContributor);
                request.setAttribute(SessionConstants.STUDENT, infoStudent);
                request.setAttribute(SessionConstants.GRATUITY_SITUATION, infoGratuitySituation);
                request.setAttribute(SessionConstants.PAGE_TITLE,
                        "link.masterDegree.administrativeOffice.gratuity.payGratuity");
                return mapping.findForward("confirmPayment");

            } else if (adHocValue.doubleValue() > 0) {
                infoGuideEntry.setPrice(adHocValue);
            }

            infoGuideEntries.add(infoGuideEntry);

        }

        if ((insuranceExecutionYearId != null) && (insuranceExecutionYearId.intValue() > 0)) {
            // Read Insurance Transaction
            InfoInsuranceValue infoInsuranceValue = readInsuranceValue(userView,
                    insuranceExecutionYearId);

            InfoGuideEntry infoGuideEntry = new InfoGuideEntry();
            infoGuideEntry.setDocumentType(DocumentType.INSURANCE);
            infoGuideEntry.setPrice(infoInsuranceValue.getAnnualValue());
            infoGuideEntry.setQuantity(new Integer(1));
            infoGuideEntry.setGraduationType(GraduationType.MASTER_DEGREE);
            infoGuideEntry.setDescription("");

            List studentCurricularPlans = null;
            Object argsSCP[] = { infoStudent.getIdInternal() };
            try {
                studentCurricularPlans = (List) ServiceUtils.executeService(userView,
                        "ReadPosGradStudentCurricularPlans", argsSCP);

            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            Iterator iterator = studentCurricularPlans.iterator();
            InfoExecutionDegree infoExecutionDegree = null;
            InfoStudentCurricularPlan infoStudentCurricularPlan = null;
            boolean found = false;
            while (iterator.hasNext()) {
                infoStudentCurricularPlan = (InfoStudentCurricularPlan) iterator.next();
                Object argsDCP[] = { infoStudentCurricularPlan.getInfoDegreeCurricularPlan()
                        .getIdInternal() };
                List executionDegreesList = null;
                try {
                    executionDegreesList = (List) ServiceUtils.executeService(userView,
                            "ReadExecutionDegreesByDegreeCurricularPlan", argsDCP);

                } catch (FenixServiceException e) {
                    throw new FenixActionException(e);
                }

                Iterator it = executionDegreesList.iterator();
                while (it.hasNext()) {
                    infoExecutionDegree = (InfoExecutionDegree) it.next();
                    if (infoExecutionDegree.getInfoExecutionYear().getIdInternal().equals(
                            insuranceExecutionYearId)) {
                        found = true;
                        break;
                    }

                }

                if (found) {
                    break;
                }

            }

            infoGuideEntries.add(infoGuideEntry);

            infoGuide.setInfoExecutionDegree(infoExecutionDegree);

        }

        infoGuide.setCreationDate(Calendar.getInstance().getTime());
        infoGuide.setGuideRequester(GuideRequester.STUDENT);
        infoGuide.setInfoContributor(infoContributor);

        infoGuide.setInfoGuideEntries(infoGuideEntries);
        infoGuide.setInfoPerson(infoStudent.getInfoPerson());
        infoGuide.setVersion(new Integer(1));
        infoGuide.setYear(new Integer(Calendar.getInstance().get(Calendar.YEAR)));

        Object argsGuide[] = { infoGuide, "", null, "", GuideState.NON_PAYED, "" };
        try {
            infoGuide = (InfoGuide) ServiceUtils.executeService(userView, "CreateGuide", argsGuide);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute(SessionConstants.GUIDE, infoGuide);

        return mapping.findForward("paymentSuccess");

    }

    /**
     * @param errorMapping
     * @param userView
     * @param contributorNumber
     * @return
     * @throws NonExistingActionException
     * @throws FenixActionException
     */
    private InfoContributor readContributor(ActionMapping errorMapping, IUserView userView,
            Integer contributorNumber) throws NonExistingActionException, FenixActionException,
            FenixFilterException {

        InfoContributor infoContributor = null;
        Object argsContributor[] = { contributorNumber };
        try {
            infoContributor = (InfoContributor) ServiceUtils.executeService(userView, "ReadContributor",
                    argsContributor);

        } catch (ExcepcaoInexistente e) {
            throw new NonExistingActionException(
                    "error.masterDegree.administrativeOffice.nonExistingContributorSimple", errorMapping
                            .findForward("chooseContributor"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return infoContributor;
    }

    /**
     * @param userView
     * @param gratuitySituationId
     * @return
     * @throws FenixActionException
     */
    private InfoGratuitySituation readGratuitySituation(IUserView userView, Integer gratuitySituationId)
            throws FenixActionException, FenixFilterException {
        InfoGratuitySituation infoGratuitySituation = null;
        Object argsGratuitySituation[] = { gratuitySituationId };
        try {
            infoGratuitySituation = (InfoGratuitySituation) ServiceUtils.executeService(userView,
                    "ReadGratuitySituationById", argsGratuitySituation);

        } catch (ExcepcaoInexistente e) {
            throw new FenixActionException(e);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return infoGratuitySituation;
    }

    /**
     * @param mapping
     * @param userView
     * @param studentId
     * @return
     * @throws FenixActionException
     * @throws NonExistingActionException
     */
    private InfoStudent readStudent(ActionMapping mapping, IUserView userView, Integer studentId)
            throws FenixActionException, NonExistingActionException, FenixFilterException {
        InfoStudent infoStudent = null;
        Object argsStudent[] = { studentId };
        try {
            infoStudent = (InfoStudent) ServiceUtils.executeService(userView, "student.ReadStudentById",
                    argsStudent);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoStudent == null) {
            throw new NonExistingActionException("error.exception.masterDegree.nonExistentStudent",
                    mapping.findForward("chooseContributor"));
        }
        return infoStudent;
    }

    /**
     * @param userView
     * @param insuranceExecutionYearId
     * @return
     * @throws FenixActionException
     */
    private InfoInsuranceValue readInsuranceValue(IUserView userView, Integer insuranceExecutionYearId)
            throws FenixActionException, FenixFilterException {
        InfoInsuranceValue infoInsuranceValue = null;
        Object argsInsuranceValue[] = { insuranceExecutionYearId };
        try {
            infoInsuranceValue = (InfoInsuranceValue) ServiceUtils.executeService(userView,
                    "ReadInsuranceValueByExecutionYearID", argsInsuranceValue);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return infoInsuranceValue;
    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null) {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }

}