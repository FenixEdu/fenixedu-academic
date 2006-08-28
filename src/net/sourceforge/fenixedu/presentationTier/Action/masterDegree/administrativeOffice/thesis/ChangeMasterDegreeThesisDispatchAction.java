package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
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
import org.apache.struts.action.DynaActionForm;

/**
 * 
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */

public class ChangeMasterDegreeThesisDispatchAction extends FenixDispatchAction {

    public ActionForward getStudentAndMasterDegreeThesisDataVersion(ActionMapping mapping,
            ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        //Integer degreeType = (Integer) request.getAttribute("degreeType");
        //Integer studentNumber = (Integer)
        // request.getAttribute("studentNumber");
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

        if (infoMasterDegreeThesisDataVersion.getInfoGuiders().isEmpty() == false)
            request.setAttribute(SessionConstants.GUIDERS_LIST, infoMasterDegreeThesisDataVersion
                    .getInfoGuiders());

        if (infoMasterDegreeThesisDataVersion.getInfoAssistentGuiders().isEmpty() == false)
            request.setAttribute(SessionConstants.ASSISTENT_GUIDERS_LIST,
                    infoMasterDegreeThesisDataVersion.getInfoAssistentGuiders());

        if (infoMasterDegreeThesisDataVersion.getInfoExternalAssistentGuiders().isEmpty() == false)
            request.setAttribute(SessionConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST,
                    infoMasterDegreeThesisDataVersion.getInfoExternalAssistentGuiders());

        if (infoMasterDegreeThesisDataVersion.getInfoExternalGuiders().isEmpty() == false)
            request.setAttribute(SessionConstants.EXTERNAL_GUIDERS_LIST,
                    infoMasterDegreeThesisDataVersion.getInfoExternalGuiders());

        //DynaActionForm changeMasterDegreeThesisForm = new DynaActionForm();
        DynaActionForm changeMasterDegreeThesisForm = (DynaActionForm) form;
        changeMasterDegreeThesisForm.set("dissertationTitle", infoMasterDegreeThesisDataVersion
                .getDissertationTitle());

        return mapping.findForward("start");

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