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

import DataBeans.InfoMasterDegreeThesisDataVersion;
import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.TipoCurso;

/**
 * 
 * @author :
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *
 */

public class VisualizeMasterDegreeThesisDispatchAction extends DispatchAction
{

    public ActionForward getStudentAndMasterDegreeThesisDataVersion(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        IUserView userView = SessionUtils.getUserView(request);

        //Integer degreeType = (Integer) request.getAttribute("degreeType");
        //Integer studentNumber = (Integer) request.getAttribute("studentNumber");
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

        if (infoMasterDegreeThesisDataVersion.getInfoGuiders().isEmpty() == false)
            request.setAttribute(
                SessionConstants.GUIDERS_LIST,
                infoMasterDegreeThesisDataVersion.getInfoGuiders());

        if (infoMasterDegreeThesisDataVersion.getInfoAssistentGuiders().isEmpty() == false)
            request.setAttribute(
                SessionConstants.ASSISTENT_GUIDERS_LIST,
                infoMasterDegreeThesisDataVersion.getInfoAssistentGuiders());

        if (infoMasterDegreeThesisDataVersion.getInfoExternalAssistentGuiders().isEmpty() == false)
            request.setAttribute(
                SessionConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST,
                infoMasterDegreeThesisDataVersion.getInfoExternalAssistentGuiders());

        Date lastModification =
            new Date(infoMasterDegreeThesisDataVersion.getLastModification().getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy k:mm:ss");
        String formattedLastModification = simpleDateFormat.format(lastModification);

        request.setAttribute(
            SessionConstants.RESPONSIBLE_EMPLOYEE,
            infoMasterDegreeThesisDataVersion.getInfoResponsibleEmployee());
        request.setAttribute(SessionConstants.LAST_MODIFICATION, formattedLastModification);
        request.setAttribute(
            SessionConstants.DISSERTATION_TITLE,
            infoMasterDegreeThesisDataVersion.getDissertationTitle());

        return mapping.findForward("start");

    }

}