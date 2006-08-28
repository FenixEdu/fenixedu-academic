package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

/**
 * @author David Santos
 */

public class PrepareStudentDataDispatchAction extends FenixDispatchAction {

    protected boolean getStudentByNumberAndDegreeType(ActionForm form, HttpServletRequest request)
            throws Exception {

        boolean result = false;

        DynaActionForm getStudentByNumberAndDegreeTypeForm = (DynaActionForm) form;
        HttpSession session = request.getSession();

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        Integer degreeType = null;
        Integer studentNumber = null;
        try {
            degreeType = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("degreeType"));
            studentNumber = new Integer((String) getStudentByNumberAndDegreeTypeForm
                    .get("studentNumber"));
        } catch (NumberFormatException e) {
            degreeType = (Integer) request.getAttribute("degreeType");
            studentNumber = (Integer) request.getAttribute("studentNumber");
            getStudentByNumberAndDegreeTypeForm.set("degreeType", degreeType.toString());
            getStudentByNumberAndDegreeTypeForm.set("studentNumber", studentNumber.toString());
        }

        InfoStudent infoStudent = null;

        Object args[] = { degreeType, studentNumber };
        try {
            infoStudent = (InfoStudent) ServiceUtils.executeService(userView,
                    "GetStudentByNumberAndDegreeType", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoStudent == null) {
            this.setNoStudentError(studentNumber, request);
            result = false;
        } else {
            request.setAttribute(SessionConstants.STUDENT, infoStudent);
            result = true;
        }

        return result;
    }

    protected void setNoStudentError(Integer studentNumber, HttpServletRequest request) {
        ActionErrors actionErrors = new ActionErrors();
        ActionError actionError = new ActionError("error.no.student.in.database", studentNumber
                .toString());
        actionErrors.add("error.no.student.in.database", actionError);
        saveErrors(request, actionErrors);
    }
}