/*
 * Created on 2/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Tânia Pousão
 *  
 */
public class TutorManagementDispatchAction extends FenixDispatchAction {
    public ActionForward prepareChooseTutor(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();

        String executionDegreeId = request.getParameter("executionDegreeId");
        request.setAttribute("executionDegreeId", executionDegreeId);
        Integer degreeCurricularPlanID = null;
        if(request.getParameter("degreeCurricularPlanID") != null){
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        //This code is temporary, it just verify if the coordinator logged is a
        // LEEC's coordinator
        HttpSession session = request.getSession();
        IUserView userView = getUserView(request);

        Object[] args = { Integer.valueOf(executionDegreeId), userView.getUtilizador(), "LEEC" };
        Boolean authorized = Boolean.FALSE;
        try {
            authorized = (Boolean) ServiceManagerServiceFactory.executeService(userView,
                    "UserCoordinatorByExecutionDegree", args);
        } catch (NotAuthorizedFilterException e) {
            errors.add("error", new ActionError("error.exception.notAuthorized"));
            saveErrors(request, errors);
            return mapping.findForward("notAuthorized");
        } catch (FenixServiceException e) {
            errors.add("error", new ActionError(e.getMessage()));
            saveErrors(request, errors);
            return mapping.findForward("notAuthorized");
        }
        if (authorized.booleanValue() == false) {
            errors.add("notAuthorized", new ActionError("error.tutor.notAuthorized.LEEC"));
            saveErrors(request, errors);
            return mapping.findForward("notAuthorized");
        }

        return mapping.findForward("chooseTutor");
    }

    public ActionForward readTutor(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();

        HttpSession httpSession = request.getSession();
        IUserView userView = (IUserView) httpSession.getAttribute(SessionConstants.U_VIEW);

        DynaActionForm tutorForm = (DynaActionForm) actionForm;
        Integer tutorNumber = Integer.valueOf((String) tutorForm.get("tutorNumber"));
        request.setAttribute("tutorNumber", tutorNumber);

        Integer executionDegreeId = Integer.valueOf((String) tutorForm.get("executionDegreeId"));
        request.setAttribute("executionDegreeId", executionDegreeId);

        Integer degreeCurricularPlanID = (Integer)tutorForm.get("degreeCurricularPlanID");
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        
        //This service returns a list with size two:
        // 		first element is infoTeacher that is tutor
        // 		second element is the list of students that this tutor tutorizes
        // 
        Object[] args = { executionDegreeId, tutorNumber };
        List infoTeacherAndStudents = null;
        try {
            infoTeacherAndStudents = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadStudentsByTutor", args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            errors.add("error", new ActionError(e.getMessage(), tutorNumber));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        InfoTeacher infoTeacher = (InfoTeacher) infoTeacherAndStudents.get(0);
        request.setAttribute("infoTeacher", infoTeacher);

        if (infoTeacherAndStudents.size() > 1) {
            //order list by number
            Collections.sort((List) infoTeacherAndStudents.get(1), new BeanComparator(
                    "infoStudent.number"));
            request.setAttribute("studentsOfTutor", infoTeacherAndStudents.get(1));
        }

        cleanForm(tutorForm);       

        return mapping.findForward("showStudentsByTutor");
    }

    /**
     * @param tutorForm
     */
    private void cleanForm(DynaActionForm tutorForm) {
        tutorForm.set("studentNumber", null);
        tutorForm.set("studentNumberFirst", null);
        tutorForm.set("studentNumberSecond", null);
    }
}