/**
 * 
 * Project sop 
 * Package presentationTier.Action.publico 
 * Created on 24/Fev/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.publico.ReadExecutionDegreesByExecutionYearAndDegreeInitials;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author tfc130
 *  
 */
public class ViewClassTimeTableWithClassNameAndDegreeInitialsAction extends FenixContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        super.execute(mapping, form, request, response);

        String degreeInitials = request.getParameter("degreeInitials");
//        String nameDegreeCurricularPlan = request.getParameter("nameDegreeCurricularPlan");
        String classIdString = request.getParameter("classId");
        if (degreeInitials == null && classIdString == null) {
            return mapping.getInputForward();
        }

        final SchoolClass schoolClass = rootDomainObject.readSchoolClassByOID(Integer.valueOf(classIdString));
        final InfoExecutionDegree infoExecutionDegree = ReadExecutionDegreesByExecutionYearAndDegreeInitials.getInfoExecutionDegree(schoolClass.getExecutionDegree());

//        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
//                .getAttribute(SessionConstants.EXECUTION_PERIOD);
//
//        Object[] args = { infoExecutionPeriod.getInfoExecutionYear(), degreeInitials,
//                nameDegreeCurricularPlan };
//        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) ServiceUtils.executeService(
//                null, "ReadExecutionDegreesByExecutionYearAndDegreeInitials", args);

        request.setAttribute("exeDegree", infoExecutionDegree);
        return mapping.findForward("Sucess");
    }
}