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
public class ViewClassTimeTableAction extends FenixContextAction {

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

        String className = request.getParameter("className");

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);
        request.setAttribute("degreeInitials", "");
        request.setAttribute("nameDegreeCurricularPlan", "");
        request.setAttribute("degreeCurricularPlanID", "");
        request.setAttribute("degreeID", "");
        String classIdString = request.getParameter("classId");
        Integer classId = null;
        if (classIdString != null) {
            classId = new Integer(classIdString);
        } else {
            return mapping.getInputForward();

        }
        InfoSiteTimetable component = new InfoSiteTimetable();

        Object[] args = { component, infoExecutionPeriod.getInfoExecutionYear().getYear(),
                infoExecutionPeriod.getName(), null, null, className, null, classId };
        SiteView siteView = null;

        try {
            siteView = (SiteView) ServiceUtils.executeService(null, "ClassSiteComponentService", args);
        } catch (FenixServiceException e1) {
            throw new FenixActionException(e1);
        }
        request.setAttribute("siteView", siteView);
        request.setAttribute("className", className);
        return mapping.findForward("Sucess");
    }
}