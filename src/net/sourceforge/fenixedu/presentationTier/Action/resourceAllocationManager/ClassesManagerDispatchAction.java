/**
 * Project Sop 
 * 
 * Package presentationTier.Action.sop
 * 
 * Created on 3/Dez/2002
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.LerTurmas;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixClassAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author jpvl
 * 
 * 
 */
public class ClassesManagerDispatchAction extends
        FenixClassAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {
    static public final String CLASS_LIST_KEY = "classesList";

    public ActionForward listClasses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

        InfoCurricularYear infoCurricularYear = (InfoCurricularYear) request.getAttribute(PresentationConstants.CURRICULAR_YEAR);

        Integer curricularYear = infoCurricularYear.getYear();

        InfoExecutionDegree infoExecutionDegree =
                (InfoExecutionDegree) request.getAttribute(PresentationConstants.EXECUTION_DEGREE);

        List classesList = LerTurmas.run(infoExecutionDegree, infoExecutionPeriod, curricularYear);

        if (classesList != null && !classesList.isEmpty()) {
            request.setAttribute(CLASS_LIST_KEY, classesList);
        }

        request.removeAttribute(PresentationConstants.EXECUTION_COURSE);
        request.removeAttribute(PresentationConstants.CLASS_VIEW);

        return mapping.findForward("listClasses");
    }

}