/*
 * Created on 14/Ago/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoCampus;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionYear;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author lmac1
 */
public class InsertExecutionDegreeDispatchAction extends FenixDispatchAction
{

    public ActionForward prepareInsert(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException
    {

        IUserView userView = SessionUtils.getUserView(request);

        List infoExecutionYearList = null;
        List infoCampusList = null;
        /* Needed service and creation of bean of InfoExecutionYears for use in jsp */
        try
        {
            infoExecutionYearList = (List) ServiceUtils.executeService(userView,
                    "ReadAllExecutionYears", null);
            infoCampusList = (List) ServiceUtils.executeService(userView, "ReadAllCampus", null);
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        DynaActionForm dynaForm = (DynaActionForm) form;
        dynaForm.set("tempExamMap", "true");
        request.setAttribute("infoExecutionYearsList", infoExecutionYearList);
		request.setAttribute("infoCampusList", infoCampusList);
        return mapping.findForward("insertExecutionDegree");
    }

    public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {

        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));

        DynaActionForm dynaForm = (DynaValidatorForm) form;

        InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
        infoExecutionYear.setIdInternal(new Integer((String) dynaForm.get("executionYearId")));
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
        infoDegreeCurricularPlan.setIdInternal(degreeCurricularPlanId);

        InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
        infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);
        //infoExecutionDegree.setInfoCoordinator(infoTeacher);
        infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
        
        InfoCampus infoCampus = new InfoCampus();
        infoCampus.setIdInternal(Integer.valueOf((String) dynaForm.get("campusId")));
        
        infoExecutionDegree.setInfoCampus(infoCampus);
        infoExecutionDegree.setTemporaryExamMap(new Boolean((String) dynaForm.get("tempExamMap")));

        Object args[] = {infoExecutionDegree};

        try
        {
            ServiceUtils.executeService(userView, "InsertExecutionDegreeAtDegreeCurricularPlan", args);
        }
        catch (ExistingServiceException ex)
        {
            throw new ExistingActionException(ex.getMessage(), ex);
        }
        catch (NonExistingServiceException exception)
        {
            throw new NonExistingActionException(exception.getMessage(), mapping.findForward(
                    "readDegreeCurricularPlan"));
        }

        return mapping.findForward("readDegreeCurricularPlan");
    }
}