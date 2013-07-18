/*
 * Created on 14/Mar/2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.guide;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionDegreeByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.CreateGuide;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.PrepareCreateGuide;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.certificate.ReadCertificateList;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.accounting.CreateReceiptBean;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.masterDegree.GuideRequester;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidInformationInFormActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 *         This is the Action to create a Guide
 * 
 */

public class CreateGuideDispatchAction extends FenixDispatchAction {

    private CreateGuideBean getCreateGuideBean() {
        return getRenderedObject("createGuideBean");
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaActionForm createGuideForm = (DynaActionForm) form;

        // Transport chosen Execution Degree
        String executionDegreeIDParam = getFromRequest(PresentationConstants.EXECUTION_DEGREE_OID, request);
        Integer executionDegreeID = Integer.valueOf(executionDegreeIDParam);
        createGuideForm.set("executionDegreeID", executionDegreeID);
        request.setAttribute(PresentationConstants.EXECUTION_DEGREE_OID, executionDegreeID);

        InfoExecutionDegree infoExecutionDegree = null;
        infoExecutionDegree = ReadExecutionDegreeByOID.run(executionDegreeID);
        if (infoExecutionDegree != null) {
            request.setAttribute(PresentationConstants.EXECUTION_DEGREE, infoExecutionDegree);
        }

        // Contributor
        String unexistinngContributor = getFromRequest(PresentationConstants.UNEXISTING_CONTRIBUTOR, request);
        if (unexistinngContributor != null && unexistinngContributor.length() > 0) {
            request.setAttribute(PresentationConstants.UNEXISTING_CONTRIBUTOR, Boolean.TRUE.toString());
        }

        request.setAttribute("chooseContributorBean", new CreateReceiptBean());

        return mapping.findForward("PrepareSuccess");

    }

    public ActionForward requesterChosen(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException,  FenixServiceException {

        final DynaActionForm createGuideForm = (DynaActionForm) form;

        final CreateReceiptBean chooseContributorBean =
                (CreateReceiptBean) RenderUtils.getViewState("chooseContributorBean").getMetaObject().getObject();

        final List<DocumentType> types =
                Arrays.asList(DocumentType.CERTIFICATE, DocumentType.ENROLMENT, DocumentType.EMOLUMENT, DocumentType.FINE,
                        DocumentType.CERTIFICATE_OF_DEGREE, DocumentType.ACADEMIC_PROOF_EMOLUMENT,
                        DocumentType.RANK_RECOGNITION_AND_EQUIVALENCE_PROCESS);

        final InfoExecutionDegree infoExecutionDegree =
                InfoExecutionDegree.newInfoFromDomain(RootDomainObject.getInstance().readExecutionDegreeByOID(
                        (Integer) createGuideForm.get("executionDegreeID")));

        try {

            final InfoGuide infoGuide =
                    PrepareCreateGuide.run(createGuideForm.getString("graduationType"), infoExecutionDegree,
                            Integer.valueOf(createGuideForm.getString("number")), createGuideForm.getString("requester"),
                            chooseContributorBean.getContributorParty());

            final CreateGuideBean createGuideBean =
                    new CreateGuideBean(infoGuide, Integer.valueOf(createGuideForm.getString("number")),
                            Specialization.valueOf(createGuideForm.getString("graduationType")),
                            GuideRequester.valueOf(createGuideForm.getString("requester")));

            createGuideBean.setInfoPrices(ReadCertificateList.run(GraduationType.MASTER_DEGREE, types));

            request.setAttribute("createGuideBean", createGuideBean);

            return mapping.findForward("CreateStudentGuide");

        } catch (NonExistingServiceException e) {
            addErrorMessage(request, ActionMessages.GLOBAL_MESSAGE, "error.nonExisting.requester");
            return mapping.getInputForward();
        }

    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        if (!isTokenValid(request)) {
            return mapping.findForward("BackError");
        }
        generateToken(request);
        saveToken(request);

        DynaActionForm createGuideForm = (DynaActionForm) form;

        // Get the information
        String othersRemarks = (String) createGuideForm.get("othersRemarks");
        String othersPriceString = (String) createGuideForm.get("othersPrice");
        String remarks = (String) createGuideForm.get("remarks");
        String guideSituationString = (String) createGuideForm.get("guideSituation");
        String paymentType = (String) createGuideForm.get("paymentType");

        Double othersPrice = null;

        try {
            if ((othersPriceString != null) && (othersPriceString.length() != 0)) {
                othersPrice = new Double(othersPriceString);
                if (othersPrice.floatValue() < 0) {
                    throw new NumberFormatException();
                }
            }
        } catch (NumberFormatException e) {
            throw new InvalidInformationInFormActionException(new Throwable());
        }

        // Check if the Guide will have a "Payed" situation and if the payment
        // type has been chosen

        if ((guideSituationString.equals(GuideState.PAYED)) && (paymentType.equals(""))) {
            ActionError actionError = new ActionError("error.paymentTypeRequired");
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("Unknown", actionError);
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        GuideState situationOfGuide = GuideState.valueOf(guideSituationString);

        InfoGuide newInfoGuide =
                CreateGuide.run(getCreateGuideBean().getInfoGuide(), othersRemarks, othersPrice, remarks, situationOfGuide,
                        paymentType);

        request.setAttribute(PresentationConstants.GUIDE, newInfoGuide);
        request.setAttribute("graduationType", getCreateGuideBean().getGraduationType().name());
        request.setAttribute(PresentationConstants.REQUESTER_NUMBER, getCreateGuideBean().getRequesterNumber());

        return mapping.findForward("CreateSuccess");

    }

    private String getFromRequest(String parameter, HttpServletRequest request) {

        String parameterString = request.getParameter(parameter);
        if (parameterString == null) {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }
}