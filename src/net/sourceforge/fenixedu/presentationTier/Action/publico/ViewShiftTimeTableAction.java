/**
 * 
 * Project sop 
 * Package ServidorApresentacao.Action.publico 
 * Created on 1/Fev/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.ShiftKey;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author João Mota
 *  
 */
public class ViewShiftTimeTableAction extends FenixContextAction {

    /**
     * Constructor for ViewClassTimeTableAction.
     */

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String shiftName = request.getParameter("shiftName");

        if (shiftName == null)
            return mapping.getInputForward();
        InfoExecutionCourse infoExecutionCourse = RequestUtils.getExecutionCourseFromRequest(request);

        Object[] args = { new ShiftKey(shiftName, infoExecutionCourse) };
        List lessons = (List) ServiceUtils.executeService(null, "LerAulasDeTurno", args);

        Object argsReadCurricularCourseListOfExecutionCourse[] = { infoExecutionCourse };
        List infoCurricularCourses = (List) ServiceManagerServiceFactory.executeService(null,
                "ReadCurricularCourseListOfExecutionCourse",
                argsReadCurricularCourseListOfExecutionCourse);

        if (infoCurricularCourses != null && !infoCurricularCourses.isEmpty()) {
            request.setAttribute("publico.infoCurricularCourses", infoCurricularCourses);
        }

        InfoShift shiftView = new InfoShift();
        shiftView.setNome(shiftName);
        request.setAttribute("shift", shiftView);
        request.setAttribute("lessonList", lessons);
        InfoSite site = RequestUtils.getSiteFromRequest(request);
        RequestUtils.setExecutionCourseToRequest(request, infoExecutionCourse);
        RequestUtils.setSectionsToRequest(request, site);
        RequestUtils.setSectionToRequest(request);

        return mapping.findForward("Sucess");
    }
}