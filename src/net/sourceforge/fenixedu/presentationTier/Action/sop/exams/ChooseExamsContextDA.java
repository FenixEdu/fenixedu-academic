package net.sourceforge.fenixedu.presentationTier.Action.sop.exams;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

/**
 * @author Ana & Ricardo
 *  
 */
public class ChooseExamsContextDA extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ContextUtils.prepareChangeExecutionDegreeAndCurricularYear(request);

        return mapping.findForward("ManageExams");
    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm chooseExamsContextForm = (DynaActionForm) form;

        IUserView userView = SessionUtils.getUserView(request);

        /* Determine Selected Curricular Year */
        Integer anoCurricular = new Integer((String) chooseExamsContextForm.get("curricularYear"));

        Object argsReadCurricularYearByOID[] = { anoCurricular };
        InfoCurricularYear infoCurricularYear = (InfoCurricularYear) ServiceUtils.executeService(
                userView, "ReadCurricularYearByOID", argsReadCurricularYearByOID);

        request.setAttribute(SessionConstants.CURRICULAR_YEAR, infoCurricularYear);

        /* Determine Selected Execution Degree */
        Integer executionDegreeOID = new Integer((String) chooseExamsContextForm
                .get("executionDegreeOID"));

        Object argsReadExecutionDegreeByOID[] = { executionDegreeOID };
        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) ServiceUtils.executeService(
                userView, "ReadExecutionDegreeByOID", argsReadExecutionDegreeByOID);

        if (infoExecutionDegree == null) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("errors.invalid.execution.degree", new ActionError(
                    "errors.invalid.execution.degree"));
            saveErrors(request, actionErrors);

            return mapping.getInputForward();
        }
        request.setAttribute(SessionConstants.EXECUTION_DEGREE, infoExecutionDegree);

        return mapping.findForward("ManageExams");

    }

}