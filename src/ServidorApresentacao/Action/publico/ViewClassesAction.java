package ServidorApresentacao.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.ISiteComponent;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoSiteClasses;
import DataBeans.SiteView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author João Mota
 */
public class ViewClassesAction extends FenixContextAction {

    /**
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        try {
            super.execute(mapping, form, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpSession session = request.getSession(true);
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);
        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request.getAttribute(SessionConstants.EXECUTION_DEGREE);
		session.setAttribute(SessionConstants.EXECUTION_DEGREE,infoExecutionDegree);
		Integer degreeCurricularPlanId = (Integer) request.getAttribute("degreeCurricularPlanID");
		request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);
		

		Integer degreeId = (Integer) request.getAttribute("degreeID");
		request.setAttribute("degreeID", degreeId);
        String degreeInitials = (String) infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getSigla();
		String nameDegreeCurricularPlan = (String) infoExecutionDegree.getInfoDegreeCurricularPlan().getName();
        Integer curricularYear = (Integer) request.getAttribute("curYear");

        ISiteComponent component = new InfoSiteClasses();
        SiteView siteView = null;
        Object[] args = { component,
                infoExecutionPeriod.getInfoExecutionYear().getYear(),
                infoExecutionPeriod.getName(), degreeInitials,
                nameDegreeCurricularPlan, null, curricularYear, null};

        try {
            siteView = (SiteView) ServiceUtils.executeService(null,
                    "ClassSiteComponentService", args);
        } catch (FenixServiceException e1) {
            throw new FenixActionException(e1);
        }
		request.setAttribute(SessionConstants.INFO_DEGREE_CURRICULAR_PLAN, infoExecutionDegree.getInfoDegreeCurricularPlan());
		request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);
		request.setAttribute(
			SessionConstants.EXECUTION_PERIOD_OID,
			infoExecutionPeriod.getIdInternal().toString());
		request.setAttribute("siteView", siteView);
        request.setAttribute("degreeInitials", degreeInitials);
        request.setAttribute("nameDegreeCurricularPlan",
                nameDegreeCurricularPlan);
        RequestUtils.setExecutionDegreeToRequest(request,infoExecutionDegree);
        return mapping.findForward("Sucess");
        

    }

}