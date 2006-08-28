package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

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

public class CreateMasterDegreeThesisDispatchAction extends FenixDispatchAction {

    public ActionForward getStudentForCreateMasterDegreeThesis(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
        ActionErrors actionErrors = new ActionErrors();
        boolean isSuccess = operations.getStudentByNumberAndDegreeType(form, request, actionErrors);

        if (isSuccess) {
            return mapping.findForward("start");
        }
        throw new NonExistingActionException("error.exception.masterDegree.nonExistentStudent", mapping
                .findForward("error"));

    }

    public ActionForward reloadForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
        ActionErrors actionErrors = new ActionErrors();

        try {
            operations.getTeachersByNumbers(form, request, "guidersNumbers",
                    SessionConstants.GUIDERS_LIST, actionErrors);
            operations.getTeachersByNumbers(form, request, "assistentGuidersNumbers",
                    SessionConstants.ASSISTENT_GUIDERS_LIST, actionErrors);
            operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
            operations.getExternalPersonsByIDs(form, request, "externalAssistentGuidersIDs",
                    SessionConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST, actionErrors);
            operations.getExternalPersonsByIDs(form, request, "externalGuidersIDs",
                    SessionConstants.EXTERNAL_GUIDERS_LIST, actionErrors);

        } catch (Exception e1) {
            throw new FenixActionException(e1);
        }

        return mapping.findForward("start");

    }

}