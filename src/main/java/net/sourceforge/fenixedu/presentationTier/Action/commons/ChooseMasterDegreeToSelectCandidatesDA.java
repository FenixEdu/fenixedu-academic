package net.sourceforge.fenixedu.presentationTier.Action.commons;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/chooseMasterDegreeToSelectCandidates", module = "masterDegreeAdministrativeOffice",
        input = "/candidate/chooseMasterDegreeToSelectCandidates_bd.jsp", formBean = "chooseMasterDegreeForm",
        functionality = ChooseExecutionYearToSelectCandidatesDA.class)
@Forwards({
        @Forward(name = "PrepareSuccess",
                path = "/masterDegreeAdministrativeOffice/candidate/chooseMasterDegreeToSelectCandidates.jsp"),
        @Forward(name = "ChooseSuccess",
                path = "/masterDegreeAdministrativeOffice/displayListToSelectCandidates.do?method=prepareSelectCandidates&page=0") })
public class ChooseMasterDegreeToSelectCandidatesDA extends FenixDispatchAction {

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
