package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
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

public class VisualizeMasterDegreeThesisHistoryDispatchAction extends FenixDispatchAction {

    public ActionForward getStudentAndMasterDegreeThesisDataVersion(ActionMapping mapping,
            ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        Integer masterDegreeThesisDataVersionID = Integer.valueOf(request
                .getParameter("masterDegreeThesisDataVersionID"));

        MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
        ActionErrors actionErrors = new ActionErrors();
        boolean isSuccess = operations.getStudentByNumberAndDegreeType(form, request, actionErrors);

        if (!isSuccess) {
            throw new NonExistingActionException("error.exception.masterDegree.nonExistentStudent",
                    mapping.findForward("error"));

        }

        InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = null;

        /* * * get master degree thesis history data * * */
        Object argsMasterDegreeThesisDataVersion[] = { masterDegreeThesisDataVersionID };
        try {
            infoMasterDegreeThesisDataVersion = (InfoMasterDegreeThesisDataVersion) ServiceUtils
                    .executeService(userView, "ReadMasterDegreeThesisDataVersionByID",
                            argsMasterDegreeThesisDataVersion);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(
                    "error.exception.masterDegree.nonExistingMasterDegreeThesis", mapping
                            .findForward("error"));

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoMasterDegreeThesisDataVersion.getInfoGuiders().isEmpty() == false)
            request.setAttribute(SessionConstants.GUIDERS_LIST, infoMasterDegreeThesisDataVersion
                    .getInfoGuiders());

        if (infoMasterDegreeThesisDataVersion.getInfoAssistentGuiders().isEmpty() == false)
            request.setAttribute(SessionConstants.ASSISTENT_GUIDERS_LIST,
                    infoMasterDegreeThesisDataVersion.getInfoAssistentGuiders());

        if (infoMasterDegreeThesisDataVersion.getInfoExternalAssistentGuiders().isEmpty() == false)
            request.setAttribute(SessionConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST,
                    infoMasterDegreeThesisDataVersion.getInfoExternalAssistentGuiders());

        if (infoMasterDegreeThesisDataVersion.getInfoExternalGuiders().isEmpty() == false) {
            request.setAttribute(SessionConstants.EXTERNAL_GUIDERS_LIST,
                    infoMasterDegreeThesisDataVersion.getInfoExternalGuiders());
        }

        Date lastModification = new Date(infoMasterDegreeThesisDataVersion.getLastModification()
                .getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy k:mm:ss");
        String formattedLastModification = simpleDateFormat.format(lastModification);

        request.setAttribute(SessionConstants.RESPONSIBLE_EMPLOYEE, infoMasterDegreeThesisDataVersion
                .getInfoResponsibleEmployee());
        request.setAttribute(SessionConstants.LAST_MODIFICATION, formattedLastModification);
        request.setAttribute(SessionConstants.DISSERTATION_TITLE, infoMasterDegreeThesisDataVersion
                .getDissertationTitle());

        return mapping.findForward("start");

    }

}