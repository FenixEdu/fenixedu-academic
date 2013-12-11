package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.commons.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
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

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

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