package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

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

public class PrepareStudentDataForThesisOperationsDispatchAction extends FenixDispatchAction {

    public ActionForward getStudentAndDegreeTypeForThesisOperations(ActionMapping mapping,
            ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm getStudentByNumberAndDegreeTypeForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);

        String degreeType = null;
        Integer studentNumber = null;
        String forward = "success";

        try {
            degreeType = (String) getStudentByNumberAndDegreeTypeForm.get("degreeType");
            studentNumber = new Integer((String) getStudentByNumberAndDegreeTypeForm
                    .get("studentNumber"));
        } catch (Exception e) {
            //case when the user cancels the current form, and goes back to the
            // intial page of master degree thesis operations (index page)
            degreeType = (String) getStudentByNumberAndDegreeTypeForm.get("degreeType");
            studentNumber = (Integer) getStudentByNumberAndDegreeTypeForm.get("studentNumber");
            forward = "cancel";
        }

        InfoStudentCurricularPlan infoStudentCurricularPlan = null;
        InfoStudent infoStudent = null;
        //InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion =
        // null;

        /* * * get student * * */
        Object argsStudent[] = { studentNumber, DegreeType.valueOf(degreeType) };
        try {
            infoStudent = (InfoStudent) ServiceUtils.executeService(userView,
                    "ReadStudentByNumberAndDegreeType", argsStudent);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoStudent == null) {
            throw new NonExistingActionException("error.exception.masterDegree.nonExistentStudent",
                    mapping.findForward("error"));

        }

        request.setAttribute(SessionConstants.STUDENT, infoStudent);

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
            /*
             * infoMasterDegreeThesisDataVersion =
             * (InfoMasterDegreeThesisDataVersion)
             */ServiceUtils.executeService(userView,
                    "ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan",
                    argsMasterDegreeThesisDataVersion);
        } catch (NonExistingServiceException e) {
            // no active master degree thesis
            if (forward.equals("cancel")) {
                return mapping.findForward("createThesisCancel");
            }

            return mapping.findForward("createThesis");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return mapping.findForward(forward);

    }

}