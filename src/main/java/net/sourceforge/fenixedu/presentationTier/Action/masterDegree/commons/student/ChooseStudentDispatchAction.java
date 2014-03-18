package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.commons.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.ReadStudentCurricularPlansByNumberAndDegreeType;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

@Mapping(path = "/chooseStudent", module = "masterDegreeAdministrativeOffice", input = "/student/chooseStudent.jsp",
        formBean = "chooseStudentForm", validate = false)
@Forwards(value = { @Forward(name = "ShowCurricularPlans", path = "/student/showCurricularPlans.jsp"),
        @Forward(name = "StudentCurricularPlanChosen", path = "/gratuityOperations.do?method=getInformation&page=0") })
@Exceptions(value = { @ExceptionHandling(key = "resources.Action.exceptions.NonExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class) })
public class ChooseStudentDispatchAction extends FenixDispatchAction {

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        User userView = getUserView(request);
        DynaActionForm studentForm = (DynaActionForm) form;

        Integer studentNumber = (Integer) studentForm.get("number");

        List result = null;

        try {

            result = ReadStudentCurricularPlansByNumberAndDegreeType.run(studentNumber, DegreeType.MASTER_DEGREE);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("O Aluno");
        }

        if (result.size() == 1) {
            request.setAttribute("studentCPID", ((InfoStudentCurricularPlan) result.iterator().next()).getExternalId());
            return mapping.findForward("StudentCurricularPlanChosen");
        }

        request.setAttribute("studentCurricularPlans", result);
        return mapping.findForward("ShowCurricularPlans");
    }

}