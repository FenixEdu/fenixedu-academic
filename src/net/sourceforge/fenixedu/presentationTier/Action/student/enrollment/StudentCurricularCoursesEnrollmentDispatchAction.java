package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

/*
 * 
 * @author Fernanda Quitério 7/Fev/2004
 *  
 */
public class StudentCurricularCoursesEnrollmentDispatchAction extends DispatchAction {

    public ActionForward prepareEnrollment(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaValidatorForm enrollmentForm = (DynaValidatorForm) form;
        IUserView userView = SessionUtils.getUserView(request);

        InfoStudent infoStudent = new InfoStudent();
        Object[] args = { userView.getUtilizador() };
        try {
            infoStudent = (InfoStudent) ServiceManagerServiceFactory.executeService(userView,
                    "ReadStudentByUsername", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        enrollmentForm.set("studentNumber", infoStudent.getNumber().toString());

        return mapping.findForward("curricularCoursesEnrollment");
    }
}