package net.sourceforge.fenixedu.presentationTier.Action.commons;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.ReadMasterDegrees;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author Fernanda Quitério 03/07/2003
 * 
 */
public class ChooseMasterDegreeDispatchAction extends FenixDispatchAction {

    public ActionForward prepareChooseMasterDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String executionYear = getFromRequest("executionYear", request);

        request.setAttribute("jspTitle", getFromRequest("jspTitle", request));
        request.setAttribute("executionYear", executionYear);

        // Get the Degree List

        User userView = getUserView(request);
        List degreeList = null;
        try {

            degreeList = ReadMasterDegrees.run(executionYear);
            // ver aqui o que devolvs o servico
        } catch (NonExistingServiceException e) {

            ActionErrors errors = new ActionErrors();
            errors.add("nonExisting", new ActionError("message.masterDegree.notfound.degrees", executionYear));
            saveErrors(request, errors);
            return mapping.getInputForward();

        } catch (ExistingServiceException e) {
            throw new ExistingActionException(e);
        }
        Collections.sort(degreeList, new BeanComparator("infoDegreeCurricularPlan.infoDegree.nome"));

        request.setAttribute(PresentationConstants.DEGREE_LIST, degreeList);

        return mapping.findForward("PrepareSuccess");
    }

    public ActionForward chooseMasterDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("jspTitle", getFromRequest("jspTitle", request));
        request.setAttribute("executionYear", getFromRequest("executionYear", request));
        request.setAttribute("degree", getFromRequest("degree", request));
        return mapping.findForward("ChooseSuccess");
    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null) {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }
}