/*
 * Created on Oct 11, 2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.commons.curriculumHistoric.InfoCurriculumHistoricReport;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author nmgo
 * @author lmre
 */
public class ShowCurriculumHistoricAction extends FenixDispatchAction {

    public ActionForward showCurriculumHistoric(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        IUserView userView = SessionUtils.getUserView(request);
        Integer semester = getFromRequest("semester", request);
        Integer curricularCourseCode = getFromRequest("curricularCourseCode", request);
        Integer executionYearID = getFromRequest("executionYearID", request);

        Object[] args = new Object[] { curricularCourseCode, semester, executionYearID };

        InfoCurriculumHistoricReport result = null;

        try {
            result = (InfoCurriculumHistoricReport) ServiceUtils.executeService(userView,
                    "ReadCurriculumHistoricReport", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain
                .addComparator(new BeanComparator("infoStudentCurricularPlan.infoStudent.number"));
        Collections.sort(result.getEnrollments(), comparatorChain);

        request.setAttribute("infoCurriculumHistoricReport", result);
        return mapping.findForward("show-report");
    }

    private Integer getFromRequest(String parameter, HttpServletRequest request) {
        Integer parameterCode = null;
        String parameterCodeString = request.getParameter(parameter);
        if (parameterCodeString == null) {
            parameterCodeString = (String) request.getAttribute(parameter);
        }
        if (parameterCodeString != null) {
            try {
                parameterCode = new Integer(parameterCodeString);
            } catch (Exception exception) {
                return null;
            }
        }
        return parameterCode;
    }

}