package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * 
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */

public class VisualizeMasterDegreeThesisDispatchAction extends FenixDispatchAction {

    public ActionForward getStudentAndMasterDegreeThesisDataVersion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final String scpID = request.getParameter("scpID");
        StudentCurricularPlan studentCurricularPlan = AbstractDomainObject.fromExternalId(scpID);

        new MasterDegreeThesisOperations().transportStudentCurricularPlan(form, request, new ActionErrors(),
                studentCurricularPlan);

        List<MasterDegreeThesisDataVersion> masterDegreeThesisDataHistory =
                studentCurricularPlan.readNotActiveMasterDegreeThesisDataVersions();
        if (masterDegreeThesisDataHistory.isEmpty() == false) {
            request.setAttribute(PresentationConstants.MASTER_DEGREE_THESIS_HISTORY, masterDegreeThesisDataHistory);
        }

        MasterDegreeThesisDataVersion masterDegreeThesisDataVersion =
                studentCurricularPlan.getMasterDegreeThesis().getActiveMasterDegreeThesisDataVersion();

        if (masterDegreeThesisDataVersion.getGuiders().isEmpty() == false) {
            request.setAttribute(PresentationConstants.GUIDERS_LIST, masterDegreeThesisDataVersion.getGuiders());
        }

        if (masterDegreeThesisDataVersion.getAssistentGuiders().isEmpty() == false) {
            request.setAttribute(PresentationConstants.ASSISTENT_GUIDERS_LIST,
                    masterDegreeThesisDataVersion.getAssistentGuiders());
        }

        if (masterDegreeThesisDataVersion.getExternalAssistentGuiders().isEmpty() == false) {
            request.setAttribute(PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST,
                    masterDegreeThesisDataVersion.getExternalAssistentGuiders());
        }

        if (masterDegreeThesisDataVersion.getExternalGuiders().isEmpty() == false) {
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