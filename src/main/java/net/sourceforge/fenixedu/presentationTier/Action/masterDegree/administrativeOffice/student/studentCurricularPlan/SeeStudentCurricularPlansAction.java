package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan.ReadPosGradStudentCurricularPlans;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author David Santos 2/Out/2003
 */

@Mapping(module = "masterDegreeAdministrativeOffice", path = "/seeStudentCurricularPlans", scope = "request", validate = false)
@Forwards(value = { @Forward(name = "viewStudentCurricularPlans", path = "df.page.viewStudentCurricularPlans",
        tileProperties = @Tile(title = "teste")) })
public class SeeStudentCurricularPlansAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        String studentId1 = this.getFromRequest("studentId", request);

        List studentCurricularPlansList = null;
        try {
            studentCurricularPlansList = ReadPosGradStudentCurricularPlans.run(studentId1);
            if (studentCurricularPlansList != null && !studentCurricularPlansList.isEmpty()) {
                Collections.sort(studentCurricularPlansList);
            }
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("studentCurricularPlansList", studentCurricularPlansList);
        request.setAttribute("student",
                ((InfoStudentCurricularPlan) studentCurricularPlansList.iterator().next()).getInfoStudent());

        return mapping.findForward("viewStudentCurricularPlans");
    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null) {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }
}