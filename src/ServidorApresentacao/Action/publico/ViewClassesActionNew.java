package ServidorApresentacao.Action.publico;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.ISiteComponent;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoSiteClasses;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixContextAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author João Mota
 */
public class ViewClassesActionNew extends FenixContextAction {

    /**
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        try {
            super.execute(mapping, form, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpSession session = request.getSession(true);
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        //        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree)
        // request
        //                .getAttribute(SessionConstants.EXECUTION_DEGREE);

        InfoExecutionDegree infoExecutionDegree = RequestUtils.getExecutionDegreeFromRequest(request,
                infoExecutionPeriod.getInfoExecutionYear());
        //   session.setAttribute(SessionConstants.EXECUTION_DEGREE,
        // infoExecutionDegree);

        Integer degreeCurricularPlanId = (Integer) request.getAttribute("degreeCurricularPlanID");
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

        Integer index = (Integer) request.getAttribute("index");
        request.setAttribute("index", index);

        Integer degreeId = (Integer) request.getAttribute("degreeID");
        request.setAttribute("degreeID", degreeId);

        Integer curricularYear = (Integer) request.getAttribute("curYear");

        ActionErrors errors = new ActionErrors();
        request.setAttribute(SessionConstants.INFO_DEGREE_CURRICULAR_PLAN, infoExecutionDegree
                .getInfoDegreeCurricularPlan());
        request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);
        request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getIdInternal()
                .toString());

        RequestUtils.setExecutionDegreeToRequest(request, infoExecutionDegree);

        ISiteComponent component = new InfoSiteClasses();
        List classList = new ArrayList();
        Object[] args = { infoExecutionDegree, infoExecutionPeriod, curricularYear };
        try {
            classList = (List) ServiceUtils.executeService(null, "LerTurmas", args);

        } catch (NonExistingServiceException e1) {
            errors.add("nonExisting", new ActionError("error.exception.noStudents"));
            saveErrors(request, errors);
            return mapping.findForward("Sucess");

        } catch (FenixServiceException e1) {
            throw new FenixActionException(e1);
        }

        request.setAttribute("classList", classList);
        return mapping.findForward("Sucess");

    }

}