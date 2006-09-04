package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author João Mota
 */
public class PrepareSelectExecutionCourseAction extends FenixContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        try {
            super.execute(mapping, form, request, response);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        InfoExecutionDegree infoExecutionDegree = RequestUtils.getExecutionDegreeFromRequest(request,
                infoExecutionPeriod.getInfoExecutionYear());

        Integer curricularYear = (Integer) request.getAttribute("curYear");

        Object argsSelectExecutionCourse[] = { infoExecutionDegree, infoExecutionPeriod, curricularYear };

        List infoExecutionCourses;
        try {
            infoExecutionCourses = (List) ServiceManagerServiceFactory.executeService(null,
                    "SelectExecutionCourse", argsSelectExecutionCourse);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Collections.sort(infoExecutionCourses, new BeanComparator("nome"));
        request.setAttribute("exeCourseList", infoExecutionCourses);
        return mapping.findForward("sucess");
    }

}