package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.exams;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurricularYearByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionDegreeByOID;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.bennu.core.security.Authenticate;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Ana & Ricardo
 * 
 */
@Mapping(module = "resourceAllocationManager", path = "/chooseExamsContext",
        input = "/chooseExamsContext.do?method=prepare&page=0", attribute = "chooseExamsContextForm",
        formBean = "chooseExamsContextForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "ManageExams", path = "/showExamsManagement.do?method=view") })
public class ChooseExamsContextDA extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ContextUtils.prepareChangeExecutionDegreeAndCurricularYear(request);

        return mapping.findForward("ManageExams");
    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaActionForm chooseExamsContextForm = (DynaActionForm) form;

        User userView = Authenticate.getUser();

        /* Determine Selected Curricular Year */
        String anoCurricular = (String) chooseExamsContextForm.get("curricularYear");

        InfoCurricularYear infoCurricularYear = ReadCurricularYearByOID.run(anoCurricular);

        request.setAttribute(PresentationConstants.CURRICULAR_YEAR, infoCurricularYear);

        /* Determine Selected Execution Degree */
        String executionDegreeOID = (String) chooseExamsContextForm.get("executionDegreeOID");

        InfoExecutionDegree infoExecutionDegree = ReadExecutionDegreeByOID.run(executionDegreeOID);

        if (infoExecutionDegree == null) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("errors.invalid.execution.degree", new ActionError("errors.invalid.execution.degree"));
            saveErrors(request, actionErrors);

            return mapping.getInputForward();
        }
        request.setAttribute(PresentationConstants.EXECUTION_DEGREE, infoExecutionDegree);

        return mapping.findForward("ManageExams");

    }

}