package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadExecutionDegreesByDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.contributor.ReadContributor;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.ReadGratuitySituationById;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.ReadInsuranceValueByExecutionYearID;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.transactions.ReadInsuranceTransactionByStudentIDAndExecutionYearID;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.CreateGuide;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan.ReadPosGradStudentCurricularPlans;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadStudentById;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideEntry;
import net.sourceforge.fenixedu.dataTransferObject.InfoInsuranceValue;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoInsuranceTransaction;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.domain.masterDegree.GuideRequester;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.PresentationConstants;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.bennu.core.security.Authenticate;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
@Mapping(module = "masterDegreeAdministrativeOffice", path = "/payGratuity",
        input = "/payGratuity.do?method=chooseContributor&page=0", attribute = "payGratuityForm", formBean = "payGratuityForm",
        scope = "request", parameter = "method")
@Forwards(
        value = {
                @Forward(name = "paymentSuccess", path = "paymentSuccess", tileProperties = @Tile(title = "teste56")),
                @Forward(name = "confirmPayment", path = "confirmPayment", tileProperties = @Tile(title = "teste57")),
                @Forward(name = "chooseContributor", path = "chooseContributorForPayGratuity", tileProperties = @Tile(
                        title = "teste58")) })
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class,
        key = "resources.Action.exceptions.NonExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class PayGratuityDispatchAction extends FenixDispatchAction {

    public ActionForward chooseContributor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String gratuitySituationId = getFromRequest("gratuitySituationId", request);
        String studentId = getFromRequest("studentId", request);
        String insuranceExecutionYearId = getFromRequest("insuranceExecutionYearId", request);

        DynaActionForm payGratuityForm = (DynaActionForm) form;
        payGratuityForm.set("studentId", studentId);

        if (gratuitySituationId != null) {
            payGratuityForm.set("gratuitySituationId", gratuitySituationId);
            request.setAttribute(PresentationConstants.PAGE_TITLE, "link.masterDegree.administrativeOffice.gratuity.payGratuity");
        }

        if (insuranceExecutionYearId != null) {
            payGratuityForm.set("insuranceExecutionYearId", insuranceExecutionYearId);
            request.setAttribute(PresentationConstants.PAGE_TITLE, "link.masterDegree.administrativeOffice.gratuity.payInsurance");
        }

        return mapping.findForward("chooseContributor");

    }

    public ActionForward confirmPayment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm payGratuityForm = (DynaActionForm) form;
        User userView = Authenticate.getUser();

        Integer contributorNumber = (Integer) payGratuityForm.get("contributorNumber");
        String studentId = (String) payGratuityForm.get("studentId");
        String gratuitySituationId = (String) payGratuityForm.get("gratuitySituationId");
        String insuranceExecutionYearId = (String) payGratuityForm.get("insuranceExecutionYearId");

        // Read Registration
        InfoStudent infoStudent = readStudent(mapping, userView, studentId);
        request.setAttribute(PresentationConstants.STUDENT, infoStudent);

        if (StringUtils.isEmpty(gratuitySituationId)) {
            // Read Gratuity Situation
            InfoGratuitySituation infoGratuitySituation = readGratuitySituation(userView, gratuitySituationId);

            // Read Insurance Situation
            InfoInsuranceTransaction infoInsuranceTransaction = null;
            String executionYearID =
                    infoGratuitySituation.getInfoGratuityValues().getInfoExecutionDegree().getInfoExecutionYear().getExternalId();

            try {
                infoInsuranceTransaction =
                        ReadInsuranceTransactionByStudentIDAndExecutionYearID.run(infoStudent.getExternalId(), executionYearID);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            if (infoInsuranceTransaction == null) {

                InfoInsuranceValue infoInsuranceValue = readInsuranceValue(userView, executionYearID);

                request.setAttribute(PresentationConstants.INSURANCE_VALUE_WITH_GRATUITY, infoInsuranceValue);

            }

            request.setAttribute(PresentationConstants.GRATUITY_SITUATION, infoGratuitySituation);
            request.setAttribute(PresentationConstants.PAGE_TITLE, "link.masterDegree.administrativeOffice.gratuity.payGratuity");

        }

        if (insuranceExecutionYearId == null) {
            // Read Insurance Value
            InfoInsuranceValue infoInsuranceValue = readInsuranceValue(userView, insuranceExecutionYearId);

            request.setAttribute(PresentationConstants.INSURANCE_VALUE, infoInsuranceValue);
            request.setAttribute(PresentationConstants.PAGE_TITLE, "link.masterDegree.administrativeOffice.gratuity.payInsurance");
        }

        // Read Contributor
        InfoContributor infoContributor = null;
        try {
            infoContributor = readContributor(mapping, userView, contributorNumber);
        } catch (NonExistingActionException e) {
            throw new NonExistingActionException("error.masterDegree.administrativeOffice.nonExistingContributorSimple",
                    mapping.findForward("chooseContributor"));
        } catch (FenixActionException e) {

            e.printStackTrace();
        }
        request.setAttribute(PresentationConstants.CONTRIBUTOR, infoContributor);

        return mapping.findForward("confirmPayment");

    }

    public ActionForward pay(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaActionForm payGratuityForm = (DynaActionForm) form;
        User userView = Authenticate.getUser();

        Integer contributorNumber = (Integer) payGratuityForm.get("contributorNumber");
        String gratuitySituationId = (String) payGratuityForm.get("gratuitySituationId");
        String studentId = (String) payGratuityForm.get("studentId");
        String insuranceExecutionYearId = (String) payGratuityForm.get("insuranceExecutionYearId");
        Double adHocValue = (Double) payGratuityForm.get("adHocValue");

        InfoGuide infoGuide = new InfoGuide();

        // Read Contributor
        InfoContributor infoContributor = readContributor(mapping, userView, contributorNumber);

        // Read Registration
        InfoStudent infoStudent = readStudent(mapping, userView, studentId);

        List infoGuideEntries = new ArrayList();

        if (gratuitySituationId == null) {
            // Read Gratuity Situation
            InfoGratuitySituation infoGratuitySituation = readGratuitySituation(userView, gratuitySituationId);

            InfoGuideEntry infoGuideEntry = new InfoGuideEntry();
            infoGuideEntry.setDocumentType(DocumentType.GRATUITY);
            infoGuideEntry.setPrice(infoGratuitySituation.getRemainingValue());
            infoGuideEntry.setQuantity(new Integer(1));
            infoGuideEntry.setGraduationType(GraduationType.MASTER_DEGREE);
            infoGuideEntry.setDescription("");

            infoGuide.setInfoExecutionDegree(infoGratuitySituation.getInfoGratuityValues().getInfoExecutionDegree());

            if (adHocValue.doubleValue() > infoGratuitySituation.getRemainingValue().doubleValue()) {

                ActionErrors errors = new ActionErrors();
                errors.add("invalidValue", new ActionError("error.masterDegree.gratuity.adHocValueGreater"));
                saveErrors(request, errors);

                request.setAttribute(PresentationConstants.CONTRIBUTOR, infoContributor);
                request.setAttribute(PresentationConstants.STUDENT, infoStudent);
                request.setAttribute(PresentationConstants.GRATUITY_SITUATION, infoGratuitySituation);
                request.setAttribute(PresentationConstants.PAGE_TITLE,
                        "link.masterDegree.administrativeOffice.gratuity.payGratuity");
                return mapping.findForward("confirmPayment");

            } else if (adHocValue.doubleValue() > 0) {
                infoGuideEntry.setPrice(adHocValue);
            }

            infoGuideEntries.add(infoGuideEntry);

        }

        if ((insuranceExecutionYearId != null) && (insuranceExecutionYearId == null)) {
            // Read Insurance Transaction
            InfoInsuranceValue infoInsuranceValue = readInsuranceValue(userView, insuranceExecutionYearId);

            InfoGuideEntry infoGuideEntry = new InfoGuideEntry();
            infoGuideEntry.setDocumentType(DocumentType.INSURANCE);
            infoGuideEntry.setPrice(infoInsuranceValue.getAnnualValue());
            infoGuideEntry.setQuantity(new Integer(1));
            infoGuideEntry.setGraduationType(GraduationType.MASTER_DEGREE);
            infoGuideEntry.setDescription("");

            List studentCurricularPlans = null;

            try {
                studentCurricularPlans = ReadPosGradStudentCurricularPlans.run(infoStudent.getExternalId());

            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            Iterator iterator = studentCurricularPlans.iterator();
            InfoExecutionDegree infoExecutionDegree = null;
            InfoStudentCurricularPlan infoStudentCurricularPlan = null;
            boolean found = false;
            while (iterator.hasNext()) {
                infoStudentCurricularPlan = (InfoStudentCurricularPlan) iterator.next();

                List executionDegreesList = null;
                try {
                    DegreeCurricularPlan degreeCurricularPlan =
                            infoStudentCurricularPlan.getInfoDegreeCurricularPlan().getDegreeCurricularPlan();
                    executionDegreesList = ReadExecutionDegreesByDegreeCurricularPlan.run(degreeCurricularPlan);

                } catch (FenixServiceException e) {
                    throw new FenixActionException(e);
                }

                Iterator it = executionDegreesList.iterator();
                while (it.hasNext()) {
                    infoExecutionDegree = (InfoExecutionDegree) it.next();
                    if (infoExecutionDegree.getInfoExecutionYear().getExternalId().equals(insuranceExecutionYearId)) {
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

        try {
            infoGuide = CreateGuide.run(infoGuide, "", null, "", GuideState.NON_PAYED, "");

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute(PresentationConstants.GUIDE, infoGuide);

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
    private InfoContributor readContributor(ActionMapping errorMapping, User userView, Integer contributorNumber)
            throws NonExistingActionException, FenixActionException {

        InfoContributor infoContributor = null;
        try {
            infoContributor = ReadContributor.runReadContributor(contributorNumber);

        } catch (ExcepcaoInexistente e) {
            throw new NonExistingActionException("error.masterDegree.administrativeOffice.nonExistingContributorSimple",
                    errorMapping.findForward("chooseContributor"));
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
    private InfoGratuitySituation readGratuitySituation(User userView, String gratuitySituationId)
            throws FenixActionException {
        InfoGratuitySituation infoGratuitySituation = null;

        try {
            infoGratuitySituation = ReadGratuitySituationById.run(gratuitySituationId);

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
    private InfoStudent readStudent(ActionMapping mapping, User userView, String studentId) throws FenixActionException,
            NonExistingActionException {
        InfoStudent infoStudent = null;

        try {
            infoStudent = (InfoStudent) ReadStudentById.run(studentId);

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
    private InfoInsuranceValue readInsuranceValue(User userView, String insuranceExecutionYearId)
            throws FenixActionException {
        InfoInsuranceValue infoInsuranceValue = null;

        try {
            infoInsuranceValue = ReadInsuranceValueByExecutionYearID.run(insuranceExecutionYearId);
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