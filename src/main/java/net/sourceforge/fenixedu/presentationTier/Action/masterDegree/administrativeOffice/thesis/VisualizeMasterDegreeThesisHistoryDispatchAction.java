package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */

@Mapping(path = "/visualizeMasterDegreeThesisHistory", module = "masterDegreeAdministrativeOffice")
@Forwards(value = { @Forward(name = "start", path = "/thesis/visualizeMasterDegreeThesisHistory.jsp"),
        @Forward(name = "error", path = "/thesis/chooseStudentForMasterDegreeThesisAndProof.jsp") })
@Exceptions(value = { @ExceptionHandling(key = "resources.Action.exceptions.NonExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class) })
public class VisualizeMasterDegreeThesisHistoryDispatchAction extends FenixDispatchAction {

    public ActionForward getStudentAndMasterDegreeThesisDataVersion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String masterDegreeThesisDataVersionID = request.getParameter("masterDegreeThesisDataVersionID");

        new MasterDegreeThesisOperations().getStudentByNumberAndDegreeType(form, request, new ActionErrors());

        MasterDegreeThesisDataVersion masterDegreeThesisDataVersion =
                FenixFramework.getDomainObject(masterDegreeThesisDataVersionID);

        if (!masterDegreeThesisDataVersion.getGuiders().isEmpty()) {
            request.setAttribute(PresentationConstants.GUIDERS_LIST, masterDegreeThesisDataVersion.getGuiders());
        }

        if (!masterDegreeThesisDataVersion.getAssistentGuiders().isEmpty()) {
            request.setAttribute(PresentationConstants.ASSISTENT_GUIDERS_LIST,
                    masterDegreeThesisDataVersion.getAssistentGuiders());
        }

        if (!masterDegreeThesisDataVersion.getExternalAssistentGuiders().isEmpty()) {
            request.setAttribute(PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST,
                    masterDegreeThesisDataVersion.getExternalAssistentGuiders());
        }

        if (!masterDegreeThesisDataVersion.getExternalGuiders().isEmpty()) {
            request.setAttribute(PresentationConstants.EXTERNAL_GUIDERS_LIST, masterDegreeThesisDataVersion.getExternalGuiders());
        }

        Date lastModification = new Date(masterDegreeThesisDataVersion.getLastModification().getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy k:mm:ss");
        String formattedLastModification = simpleDateFormat.format(lastModification);

        request.setAttribute(PresentationConstants.RESPONSIBLE_EMPLOYEE, masterDegreeThesisDataVersion.getResponsibleEmployee());
        request.setAttribute(PresentationConstants.LAST_MODIFICATION, formattedLastModification);
        request.setAttribute(PresentationConstants.DISSERTATION_TITLE, masterDegreeThesisDataVersion.getDissertationTitle());

        return mapping.findForward("start");

    }

}