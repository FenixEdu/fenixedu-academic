package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.RequestUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Mota
 */
public class PrepareSelectExecutionCourseActionNew extends FenixContextAction {

    private static final Logger logger = LoggerFactory.getLogger(PrepareSelectExecutionCourseActionNew.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        try {
            super.execute(mapping, form, request, response);
        } catch (Exception e1) {
            logger.error(e1.getMessage(), e1);
        }

        InfoExecutionPeriod infoExecutionPeriod = RequestUtils.getExecutionPeriodFromRequest(request);

        InfoExecutionDegree infoExecutionDegree =
                (InfoExecutionDegree) request.getAttribute(PresentationConstants.EXECUTION_DEGREE);

        Integer curricularYear = (Integer) request.getAttribute("curYear");

        Object argsSelectExecutionCourse[] = { infoExecutionDegree, infoExecutionPeriod, curricularYear };

//        List infoExecutionCourses;
//        try {
//            infoExecutionCourses =
//                    (List) ServiceManagerServiceFactory.executeService("SelectExecutionCourseNew", argsSelectExecutionCourse);
//        } catch (FenixServiceException e) {
//            throw new FenixActionException(e);
//        }
//        Collections.sort(infoExecutionCourses, new BeanComparator("nome"));
//        request.setAttribute("exeCourseList", infoExecutionCourses);
//        return mapping.findForward("sucess");
        throw new UnsupportedOperationException("Service SelectExecutionCourseNew does not exist!");
    }

}