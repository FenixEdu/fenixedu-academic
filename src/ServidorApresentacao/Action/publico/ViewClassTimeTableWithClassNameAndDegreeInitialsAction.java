/**
 * 
 * Project sop 
 * Package ServidorApresentacao.Action.publico 
 * Created on 24/Fev/2003
 */
package ServidorApresentacao.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorApresentacao.Action.base.FenixContextAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author tfc130
 *  
 */
public class ViewClassTimeTableWithClassNameAndDegreeInitialsAction extends
        FenixContextAction {

    /**
     * Constructor for ViewShiftTimeTableWithClassNameAndDegreeInitialsAction.
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        super.execute(mapping, form, request, response);

        String degreeInitials = request.getParameter("degreeInitials");
        String nameDegreeCurricularPlan = request
                .getParameter("nameDegreeCurricularPlan");
        String classIdString = request.getParameter("classId");
        if (degreeInitials == null && classIdString == null) { return mapping
                .getInputForward(); }
/*        Integer classId = null;        
        if (classIdString != null) {
            classId = new Integer(classIdString);
        }*/

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        Object[] args = { infoExecutionPeriod.getInfoExecutionYear(),
                degreeInitials, nameDegreeCurricularPlan};
        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) ServiceUtils
                .executeService(null,
                        "ReadExecutionDegreesByExecutionYearAndDegreeInitials",
                        args);

        request.setAttribute("exeDegree", infoExecutionDegree);
        return mapping.findForward("Sucess");
    }
}