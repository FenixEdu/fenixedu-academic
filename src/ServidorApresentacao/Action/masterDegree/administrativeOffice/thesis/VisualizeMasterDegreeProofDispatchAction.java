package ServidorApresentacao.Action.masterDegree.administrativeOffice.thesis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoMasterDegreeProofVersion;
import DataBeans.InfoMasterDegreeThesisDataVersion;
import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.ScholarshipNotFinishedServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.exceptions.ScholarshipNotFinishedActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.MasterDegreeClassification;
import Util.TipoCurso;

/**
 * 
 * @author :
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *
 */

public class VisualizeMasterDegreeProofDispatchAction extends DispatchAction
{

    public ActionForward getStudentAndMasterDegreeProofVersion(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeType = Integer.valueOf(request.getParameter("degreeType"));
        Integer studentNumber = Integer.valueOf(request.getParameter("studentNumber"));

        MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
        ActionErrors actionErrors = new ActionErrors();
        boolean isSuccess = operations.getStudentByNumberAndDegreeType(form, request, actionErrors);

        if (isSuccess == false)
        {
            throw new NonExistingActionException(
                "error.exception.masterDegree.nonExistentStudent",
                mapping.findForward("error"));

        }

        InfoStudentCurricularPlan infoStudentCurricularPlan = null;
        InfoMasterDegreeProofVersion infoMasterDegreeProofVersion = null;
        InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = null;

        /* * * get student curricular plan * * */
        Object argsStudentCurricularPlan[] = { studentNumber, new TipoCurso(degreeType)};
        try
        {
            infoStudentCurricularPlan =
                (InfoStudentCurricularPlan) ServiceUtils.executeService(
                    userView,
                    "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
                    argsStudentCurricularPlan);
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        if (infoStudentCurricularPlan == null)
        {
            throw new NonExistingActionException(
                "error.exception.masterDegree.nonExistentActiveStudentCurricularPlan",
                mapping.findForward("error"));
        }

        /* * * get master degree thesis data * * */
        Object argsMasterDegreeThesisDataVersion[] = { infoStudentCurricularPlan };
        try
        {
            infoMasterDegreeThesisDataVersion =
                (InfoMasterDegreeThesisDataVersion) ServiceUtils.executeService(
                    userView,
                    "ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan",
                    argsMasterDegreeThesisDataVersion);
        }
        catch (NonExistingServiceException e)
        {
            throw new NonExistingActionException(
                "error.exception.masterDegree.nonExistingMasterDegreeThesis",
                mapping.findForward("error"));

        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        /* * * get master degree proof * * */
        Object argsMasterDegreeProofVersion[] = { infoStudentCurricularPlan };
        try
        {
            infoMasterDegreeProofVersion =
                (InfoMasterDegreeProofVersion) ServiceUtils.executeService(
                    userView,
                    "ReadActiveMasterDegreeProofVersionByStudentCurricularPlan",
                    argsMasterDegreeProofVersion);
        }
        catch (NonExistingServiceException e)
        {
            throw new NonExistingActionException(
                "error.exception.masterDegree.nonExistingMasterDegreeProofDataToDisplay",
                mapping.findForward("errorNonExistingProofVersion"));

        }
        catch (ScholarshipNotFinishedServiceException e)
        {
            throw new ScholarshipNotFinishedActionException(
                e.getMessage(),
                mapping.findForward("errorScholarshipNotFinished"));

        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        /* * * get master degree proof history * * */
        Object argsMasterDegreeProofHistory[] = { infoStudentCurricularPlan };
        List masterDegreeProofHistory = null;

        try
        {
            masterDegreeProofHistory =
                (List) ServiceUtils.executeService(
                    userView,
                    "ReadNonActivesMasterDegreeProofVersionsByStudentCurricularPlan",
                    argsMasterDegreeProofHistory);
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        if (masterDegreeProofHistory.isEmpty() == false)
        {
            request.setAttribute(SessionConstants.MASTER_DEGREE_PROOF_HISTORY, masterDegreeProofHistory);
        }

        if (infoMasterDegreeProofVersion.getInfoJuries().isEmpty() == false)
            request.setAttribute(
                SessionConstants.JURIES_LIST,
                infoMasterDegreeProofVersion.getInfoJuries());

        if (infoMasterDegreeProofVersion.getInfoExternalJuries().isEmpty() == false)
        {
            request.setAttribute(
                SessionConstants.EXTERNAL_JURIES_LIST,
                infoMasterDegreeProofVersion.getInfoExternalJuries());
        }

        int classification = infoMasterDegreeProofVersion.getFinalResult().getValue();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String formattedProofDate = null;
        Date proofDate = infoMasterDegreeProofVersion.getProofDate();
        if (proofDate != null)
        {
            formattedProofDate = simpleDateFormat.format(proofDate);
        }

        String formattedThesisDeliveryDate = null;
        Date thesisDeliveryDate = infoMasterDegreeProofVersion.getThesisDeliveryDate();
        if (thesisDeliveryDate != null)
        {
            formattedThesisDeliveryDate = simpleDateFormat.format(thesisDeliveryDate);
        }

        Date lastModification = new Date(infoMasterDegreeProofVersion.getLastModification().getTime());
        simpleDateFormat.applyPattern("dd-MM-yyyy k:mm:ss");
        String formattedLastModification = simpleDateFormat.format(lastModification);

        request.setAttribute(
            SessionConstants.DISSERTATION_TITLE,
            infoMasterDegreeThesisDataVersion.getDissertationTitle());
        request.setAttribute(
            SessionConstants.FINAL_RESULT,
            MasterDegreeClassification.getClassificationString(classification));
        request.setAttribute(
            SessionConstants.ATTACHED_COPIES_NUMBER,
            infoMasterDegreeProofVersion.getAttachedCopiesNumber());
        request.setAttribute(SessionConstants.PROOF_DATE, formattedProofDate);
        request.setAttribute(SessionConstants.THESIS_DELIVERY_DATE, formattedThesisDeliveryDate);
        request.setAttribute(
            SessionConstants.RESPONSIBLE_EMPLOYEE,
            infoMasterDegreeProofVersion.getInfoResponsibleEmployee());
        request.setAttribute(SessionConstants.LAST_MODIFICATION, formattedLastModification);

        return mapping.findForward("start");

    }

}