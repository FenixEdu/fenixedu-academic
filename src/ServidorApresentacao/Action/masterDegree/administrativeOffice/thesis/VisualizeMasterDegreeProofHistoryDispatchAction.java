package ServidorApresentacao.Action.masterDegree.administrativeOffice.thesis;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoMasterDegreeProofVersion;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.MasterDegreeClassification;

/**
 * 
 * @author :
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *
 */

public class VisualizeMasterDegreeProofHistoryDispatchAction extends DispatchAction
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
        Integer masterDegreeProofVersionID =
            Integer.valueOf(request.getParameter("masterDegreeProofVersionID"));

        MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
        ActionErrors actionErrors = new ActionErrors();
        boolean isSuccess = operations.getStudentByNumberAndDegreeType(form, request, actionErrors);

        if (isSuccess == false)
        {
            throw new NonExistingActionException(
                "error.exception.masterDegree.nonExistentStudent",
                mapping.findForward("error"));

        }

        InfoMasterDegreeProofVersion infoMasterDegreeProofVersion = null;

        /* * * get master degree proof history * * */
        Object argsMasterDegreeProofVersion[] = { masterDegreeProofVersionID };
        try
        {
            infoMasterDegreeProofVersion =
                (InfoMasterDegreeProofVersion) ServiceUtils.executeService(
                    userView,
                    "ReadMasterDegreeProofVersionByID",
                    argsMasterDegreeProofVersion);
        }
        catch (NonExistingServiceException e)
        {
            throw new NonExistingActionException(
                "error.exception.masterDegree.nonExistingMasterDegreeProofDataToDisplay",
                mapping.findForward("errorNonExistingProofVersion"));
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        if (infoMasterDegreeProofVersion.getInfoJuries().isEmpty() == false)
            request.setAttribute(
                SessionConstants.JURIES_LIST,
                infoMasterDegreeProofVersion.getInfoJuries());

        int classification = infoMasterDegreeProofVersion.getFinalResult().getValue();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String proofDate = null;
        String thesisDeliveryDate = null;

        if (infoMasterDegreeProofVersion.getProofDate() != null)
        {
            proofDate = simpleDateFormat.format(infoMasterDegreeProofVersion.getProofDate());
            request.setAttribute(SessionConstants.PROOF_DATE, proofDate);
        }

        if (infoMasterDegreeProofVersion.getThesisDeliveryDate() != null)
        {
            thesisDeliveryDate =
                simpleDateFormat.format(infoMasterDegreeProofVersion.getThesisDeliveryDate());
            request.setAttribute(SessionConstants.THESIS_DELIVERY_DATE, thesisDeliveryDate);

        }

        Date lastModification = new Date(infoMasterDegreeProofVersion.getLastModification().getTime());
        simpleDateFormat.applyPattern("dd-MM-yyyy k:mm:ss");
        String formattedLastModification = simpleDateFormat.format(lastModification);

        request.setAttribute(
            SessionConstants.FINAL_RESULT,
            MasterDegreeClassification.getClassificationString(classification));
        request.setAttribute(
            SessionConstants.ATTACHED_COPIES_NUMBER,
            infoMasterDegreeProofVersion.getAttachedCopiesNumber());

        request.setAttribute(
            SessionConstants.RESPONSIBLE_EMPLOYEE,
            infoMasterDegreeProofVersion.getInfoResponsibleEmployee());
        request.setAttribute(SessionConstants.LAST_MODIFICATION, formattedLastModification);

        return mapping.findForward("start");

    }

}