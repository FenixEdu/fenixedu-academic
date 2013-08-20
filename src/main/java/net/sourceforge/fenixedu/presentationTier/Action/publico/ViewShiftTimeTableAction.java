/**
 * 
 * Project sop 
 * Package presentationTier.Action.publico 
 * Created on 1/Fev/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.publico.ReadCurricularCourseListOfExecutionCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.LerAulasDeTurno;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.ShiftKey;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.RequestUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author João Mota
 * 
 */
public class ViewShiftTimeTableAction extends FenixContextAction {

    /**
     * Constructor for ViewClassTimeTableAction.
     */

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String shiftName = request.getParameter("shiftName");

        if (shiftName == null) {
            return mapping.getInputForward();
        }
        final InfoExecutionCourse infoExecutionCourse = RequestUtils.getExecutionCourseFromRequest(request);
        final ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(infoExecutionCourse.getExternalId());
        Shift shift = null;
        for (final Shift shift2 : executionCourse.getAssociatedShifts()) {
            if (shift2.getNome().equals(shiftName)) {
                shift = shift2;
            }
        }

        List lessons = LerAulasDeTurno.run(new ShiftKey(shiftName, infoExecutionCourse));

        List infoCurricularCourses = (List) ReadCurricularCourseListOfExecutionCourse.run(infoExecutionCourse);

        if (infoCurricularCourses != null && !infoCurricularCourses.isEmpty()) {
            request.setAttribute("publico.infoCurricularCourses", infoCurricularCourses);
        }

        request.setAttribute("shift", InfoShift.newInfoFromDomain(shift));
        request.setAttribute("lessonList", lessons);

        return mapping.findForward("Sucess");
    }
}