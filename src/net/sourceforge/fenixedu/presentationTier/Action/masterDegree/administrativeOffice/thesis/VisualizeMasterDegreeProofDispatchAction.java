package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ScholarshipNotFinishedServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeProofVersion;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ScholarshipNotFinishedActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */

public class VisualizeMasterDegreeProofDispatchAction extends FenixDispatchAction {

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

        InfoStudentCurricularPlan infoStudentCurricularPlan = null;
        InfoMasterDegreeProofVersion infoMasterDegreeProofVersion = null;
        InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = null;

        /* * * get student curricular plan * * */
        Object argsStudentCurricularPlan[] = { studentNumber, DegreeType.valueOf(degreeType) };
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

        /* * * get master degree proof * * */
        Object argsMasterDegreeProofVersion[] = { infoStudentCurricularPlan.getIdInternal() };
        try {
            infoMasterDegreeProofVersion = (InfoMasterDegreeProofVersion) ServiceUtils.executeService(
                    userView, "ReadActiveMasterDegreeProofVersionByStudentCurricularPlan",
                    argsMasterDegreeProofVersion);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(
                    "error.exception.masterDegree.nonExistingMasterDegreeProofDataToDisplay", mapping
                            .findForward("errorNonExistingProofVersion"));

        } catch (ScholarshipNotFinishedServiceException e) {
            throw new ScholarshipNotFinishedActionException(e.getMessage(), mapping
                    .findForward("errorScholarshipNotFinished"));

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        /* * * get master degree proof history * * */
        Object argsMasterDegreeProofHistory[] = { infoStudentCurricularPlan };
        List masterDegreeProofHistory = null;

        try {
            masterDegreeProofHistory = (List) ServiceUtils.executeService(userView,
                    "ReadNonActivesMasterDegreeProofVersionsByStudentCurricularPlan",
                    argsMasterDegreeProofHistory);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (masterDegreeProofHistory.isEmpty() == false) {
            request.setAttribute(SessionConstants.MASTER_DEGREE_PROOF_HISTORY, masterDegreeProofHistory);
        }

        if (infoMasterDegreeProofVersion.getInfoJuries().isEmpty() == false)
            request.setAttribute(SessionConstants.JURIES_LIST, infoMasterDegreeProofVersion
                    .getInfoJuries());

        if (infoMasterDegreeProofVersion.getInfoExternalJuries().isEmpty() == false) {
            request.setAttribute(SessionConstants.EXTERNAL_JURIES_LIST, infoMasterDegreeProofVersion
                    .getInfoExternalJuries());
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String formattedProofDate = null;
        Date proofDate = infoMasterDegreeProofVersion.getProofDate();
        if (proofDate != null) {
            formattedProofDate = simpleDateFormat.format(proofDate);
        }

        String formattedThesisDeliveryDate = null;
        Date thesisDeliveryDate = infoMasterDegreeProofVersion.getThesisDeliveryDate();
        if (thesisDeliveryDate != null) {
            formattedThesisDeliveryDate = simpleDateFormat.format(thesisDeliveryDate);
        }

        Date lastModification = new Date(infoMasterDegreeProofVersion.getLastModification().getTime());
        simpleDateFormat.applyPattern("dd-MM-yyyy k:mm:ss");
        String formattedLastModification = simpleDateFormat.format(lastModification);

        request.setAttribute(SessionConstants.DISSERTATION_TITLE, infoMasterDegreeThesisDataVersion
                .getDissertationTitle());
        request.setAttribute(SessionConstants.FINAL_RESULT, infoMasterDegreeProofVersion.getFinalResult().name());
        request.setAttribute(SessionConstants.ATTACHED_COPIES_NUMBER, infoMasterDegreeProofVersion
                .getAttachedCopiesNumber());
        request.setAttribute(SessionConstants.PROOF_DATE, formattedProofDate);
        request.setAttribute(SessionConstants.THESIS_DELIVERY_DATE, formattedThesisDeliveryDate);
        request.setAttribute(SessionConstants.RESPONSIBLE_EMPLOYEE, infoMasterDegreeProofVersion
                .getInfoResponsibleEmployee());
        request.setAttribute(SessionConstants.LAST_MODIFICATION, formattedLastModification);

        return mapping.findForward("start");

    }

}