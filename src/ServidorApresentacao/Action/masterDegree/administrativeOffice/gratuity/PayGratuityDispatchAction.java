package ServidorApresentacao.Action.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoContributor;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoGratuitySituation;
import DataBeans.InfoGuide;
import DataBeans.InfoGuideEntry;
import DataBeans.InfoInsuranceValue;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.transactions.InfoInsuranceTransaction;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.masterDegree.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.DocumentType;
import Util.GraduationType;
import Util.GuideRequester;
import Util.SituationOfGuide;

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

        // Read Student
        InfoStudent infoStudent = readStudent(mapping, userView, studentId);
        request.setAttribute(SessionConstants.STUDENT, infoStudent);

        if (gratuitySituationId.intValue() > 0) {
            //Read Gratuity Situation
            InfoGratuitySituation infoGratuitySituation = readGratuitySituation(userView,
                    gratuitySituationId);

            //Read Insurance Situation
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
            //Read Insurance Value
            InfoInsuranceValue infoInsuranceValue = readInsuranceValue(userView,
                    insuranceExecutionYearId);

            request.setAttribute(SessionConstants.INSURANCE_VALUE, infoInsuranceValue);
            request.setAttribute(SessionConstants.PAGE_TITLE,
                    "link.masterDegree.administrativeOffice.gratuity.payInsurance");
        }

        //Read Contributor
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

        //Read Contributor
        InfoContributor infoContributor = readContributor(mapping, userView, contributorNumber);

        // Read Student
        InfoStudent infoStudent = readStudent(mapping, userView, studentId);

        List infoGuideEntries = new ArrayList();

        if (gratuitySituationId.intValue() > 0) {
            // Read Gratuity Situation
            InfoGratuitySituation infoGratuitySituation = readGratuitySituation(userView,
                    gratuitySituationId);

            InfoGuideEntry infoGuideEntry = new InfoGuideEntry();
            infoGuideEntry.setDocumentType(new DocumentType(DocumentType.GRATUITY));
            infoGuideEntry.setPrice(infoGratuitySituation.getRemainingValue());
            infoGuideEntry.setQuantity(new Integer(1));
            infoGuideEntry.setGraduationType(GraduationType.MASTER_DEGREE_TYPE);
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
            //Read Insurance Transaction
            InfoInsuranceValue infoInsuranceValue = readInsuranceValue(userView,
                    insuranceExecutionYearId);

            InfoGuideEntry infoGuideEntry = new InfoGuideEntry();
            infoGuideEntry.setDocumentType(new DocumentType(DocumentType.INSURANCE));
            infoGuideEntry.setPrice(infoInsuranceValue.getAnnualValue());
            infoGuideEntry.setQuantity(new Integer(1));
            infoGuideEntry.setGraduationType(GraduationType.MASTER_DEGREE_TYPE);
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
                        infoExecutionDegree.setInfoDegreeCurricularPlan(infoStudentCurricularPlan
                                .getInfoDegreeCurricularPlan());
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
        infoGuide.setGuideRequester(new GuideRequester(GuideRequester.STUDENT));
        infoGuide.setInfoContributor(infoContributor);

        infoGuide.setInfoGuideEntries(infoGuideEntries);
        infoGuide.setInfoPerson(infoStudent.getInfoPerson());
        infoGuide.setVersion(new Integer(1));
        infoGuide.setYear(new Integer(Calendar.getInstance().get(Calendar.YEAR)));

        Object argsGuide[] = { infoGuide, "", null, "",
                new SituationOfGuide(SituationOfGuide.NON_PAYED), "" };
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
     * @return @throws
     *         NonExistingActionException
     * @throws FenixActionException
     */
    private InfoContributor readContributor(ActionMapping errorMapping, IUserView userView,
            Integer contributorNumber) throws NonExistingActionException, FenixActionException, FenixFilterException {

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
     * @return @throws
     *         FenixActionException
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
     * @return @throws
     *         FenixActionException
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
     * @return @throws
     *         FenixActionException
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