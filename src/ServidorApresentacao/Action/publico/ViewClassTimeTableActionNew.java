/**
 * 
 * Project sop 
 * Package ServidorApresentacao.Action.publico 
 * Created on 1/Fev/2003
 */
package ServidorApresentacao.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoSiteTimetable;
import DataBeans.SiteView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author João Mota
 *  
 */
public class ViewClassTimeTableActionNew extends FenixContextAction {

    /**
     * Constructor for ViewClassTimeTableAction.
     */

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        try {
            super.execute(mapping, form, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
		DynaActionForm escolherContextoForm = (DynaActionForm) form;
        String className = request.getParameter("className");
		Integer indice = (Integer) escolherContextoForm.get("indice");
		escolherContextoForm.set("indice",indice);
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);
        request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getIdInternal()
                .toString());

        String classIdString = request.getParameter("classId");
        request.setAttribute("classId", classIdString);
        String degreeInitials = request.getParameter("degreeInitials");
        request.setAttribute("degreeInitials", degreeInitials);
        String nameDegreeCurricularPlan = request.getParameter("nameDegreeCurricularPlan");
        request.setAttribute("nameDegreeCurricularPlan", nameDegreeCurricularPlan);
        Integer degreeCurricularPlanId = Integer.valueOf(request.getParameter("degreeCurricularPlanID"));
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);
        Integer degreeId = Integer.valueOf(request.getParameter("degreeID"));
        request.setAttribute("degreeID", degreeId);
        Integer classId = null;
        if (classIdString != null) {
            classId = new Integer(classIdString);
        } else {
            return mapping.getInputForward();

        }
        Object[] args = { infoExecutionPeriod.getInfoExecutionYear(), degreeInitials,
                nameDegreeCurricularPlan };
        InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
        try {
            infoExecutionDegree = (InfoExecutionDegree) ServiceUtils.executeService(null,
                    "ReadExecutionDegreesByExecutionYearAndDegreeInitials", args);
        } catch (FenixServiceException e1) {
            throw new FenixActionException(e1);
        }

        request.setAttribute(SessionConstants.INFO_DEGREE_CURRICULAR_PLAN, infoExecutionDegree
                .getInfoDegreeCurricularPlan());

        InfoSiteTimetable component = new InfoSiteTimetable();

        Object[] args1 = { component, infoExecutionPeriod.getInfoExecutionYear().getYear(),
                infoExecutionPeriod.getName(), null, null, className, null, classId };
        SiteView siteView = null;

        try {
            siteView = (SiteView) ServiceUtils.executeService(null, "ClassSiteComponentService", args1);
        } catch (FenixServiceException e1) {
            throw new FenixActionException(e1);
        }
        request.setAttribute("siteView", siteView);
        request.setAttribute("className", className);
        return mapping.findForward("Sucess");

    }
}