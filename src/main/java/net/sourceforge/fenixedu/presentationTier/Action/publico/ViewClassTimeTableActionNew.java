/**
 * 
 * Project sop 
 * Package presentationTier.Action.publico 
 * Created on 1/Fev/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.ClassSiteComponentService;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.ReadExecutionDegreesByExecutionYearAndDegreeInitials;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteTimetable;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Jo�o Mota
 * 
 */
@Mapping(module = "publico", path = "/viewClassTimeTableNew", input = "viewClassTimeTable",
        attribute = "chooseSearchContextForm", formBean = "chooseSearchContextForm", scope = "request", validate = false)
@Forwards(value = { @Forward(name = "Sucess", path = "viewClassTimeTable") })
public class ViewClassTimeTableActionNew extends FenixAction {

    /**
     * Constructor for ViewClassTimeTableAction.
     */

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        try {
            ContextUtils.setExecutionPeriodContext(request);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        try {
            super.execute(mapping, form, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        DynaActionForm escolherContextoForm = (DynaActionForm) form;
        String className = request.getParameter("className");
        Integer indice = (Integer) escolherContextoForm.get("indice");
        escolherContextoForm.set("indice", indice);
        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);
        request.setAttribute(PresentationConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getExternalId().toString());

        String classIdString = request.getParameter("classId");
        request.setAttribute("classId", classIdString);
        String degreeInitials = request.getParameter("degreeInitials");
        request.setAttribute("degreeInitials", degreeInitials);
        String nameDegreeCurricularPlan = request.getParameter("nameDegreeCurricularPlan");
        request.setAttribute("nameDegreeCurricularPlan", nameDegreeCurricularPlan);
        String degreeCurricularPlanId = request.getParameter("degreeCurricularPlanID");
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);
        String degreeId = request.getParameter("degreeID");
        request.setAttribute("degreeID", degreeId);

        if (classIdString == null) {
            return mapping.getInputForward();
        }

        final SchoolClass schoolClass = AbstractDomainObject.fromExternalId(classIdString);
        InfoExecutionDegree infoExecutionDegree =
                ReadExecutionDegreesByExecutionYearAndDegreeInitials.getInfoExecutionDegree(schoolClass.getExecutionDegree());
        // try {
        // infoExecutionDegree = (InfoExecutionDegree)
        // ReadExecutionDegreesByExecutionYearAndDegreeInitials
        // .run(infoExecutionPeriod
        // .getInfoExecutionYear(), degreeInitials, nameDegreeCurricularPlan);
        // } catch (FenixServiceException e1) {
        // throw new FenixActionException(e1);
        // }
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = infoExecutionDegree.getInfoDegreeCurricularPlan();
        request.setAttribute(PresentationConstants.INFO_DEGREE_CURRICULAR_PLAN, infoDegreeCurricularPlan);

        InfoSiteTimetable component = new InfoSiteTimetable();

        SiteView siteView = null;

        try {
            siteView =
                    (SiteView) ClassSiteComponentService.run(component, infoExecutionPeriod.getInfoExecutionYear().getYear(),
                            infoExecutionPeriod.getName(), null, null, className, null, classIdString);
        } catch (FenixServiceException e1) {
            throw new FenixActionException(e1);
        }

        request.setAttribute("siteView", siteView);
        request.setAttribute("className", className);
        request.setAttribute("schoolClass", schoolClass);
        return mapping.findForward("Sucess");

    }

}