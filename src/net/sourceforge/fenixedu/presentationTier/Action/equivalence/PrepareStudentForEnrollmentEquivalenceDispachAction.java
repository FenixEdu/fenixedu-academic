package net.sourceforge.fenixedu.presentationTier.Action.equivalence;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author David Santos in Apr 28, 2004
 */

public class PrepareStudentForEnrollmentEquivalenceDispachAction extends DispatchAction {
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        DynaActionForm studentNumberForm = (DynaActionForm) form;
        ActionErrors errors = new ActionErrors();

        String degreeTypeCode = (String) studentNumberForm.get("degreeType");
        String studentNumberSTR = (String) studentNumberForm.get("studentNumber");

        DegreeType degreeType = DegreeType.valueOf(degreeTypeCode);
        Integer studentNumber = Integer.valueOf(studentNumberSTR);

        Object args[] = { studentNumber, degreeType };
        try {
            ServiceManagerServiceFactory.executeService(userView,
                    "ReadStudentByNumberAndDegreeTypeForEnrollmentEquivalence", args);
        } catch (NotAuthorizedException e) {
            errors.add("not.authorized", new ActionError(e.getMessage()));
            saveErrors(request, errors);
            return mapping.getInputForward();
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e);
        }

        doIt(mapping, form, request, response);

        return mapping.findForward("start");
    }

    public ActionForward error(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        doIt(mapping, form, request, response);
        return mapping.findForward("error");
    }

    private void doIt(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm studentNumberForm = (DynaActionForm) form;

        Integer degreeType = (Integer) studentNumberForm.get("degreeType");
        String studentNumber = (String) studentNumberForm.get("studentNumber");
        String backLink = (String) studentNumberForm.get("backLink");

        if (degreeType == null) {
            degreeType = (Integer) request.getAttribute("degreeType");
        }

        if (studentNumber == null) {
            studentNumber = (String) request.getAttribute("studentNumber");
        }

        if (backLink == null) {
            backLink = (String) request.getAttribute("backLink");
        }

        request.setAttribute("degreeType", degreeType);
        request.setAttribute("studentNumber", studentNumber);
        request.setAttribute("backLink", backLink);
    }
}