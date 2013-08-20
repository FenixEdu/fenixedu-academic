package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.ReadCurricularCourseListByExecutionCourseCode;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Tânia Pousão
 * @author Ângela
 * 
 */
public class ReadCurricularCourseListAction extends FenixDispatchAction {

    public ActionForward read(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        String objectCodeString = request.getParameter("objectCode");
        if (objectCodeString == null) {
            objectCodeString = (String) request.getAttribute("objectCode");
        }

        TeacherAdministrationSiteView siteView = null;
        try {
            siteView =
                    ReadCurricularCourseListByExecutionCourseCode
                            .runReadCurricularCourseListByExecutionCourseCode(objectCodeString);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("siteView", siteView);
        request.setAttribute("objectCode", objectCodeString);

        return mapping.findForward("success");
    }
}