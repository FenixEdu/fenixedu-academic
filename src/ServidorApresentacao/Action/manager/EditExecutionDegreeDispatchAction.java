/*
 * Created on 18/Ago/2003
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
public class EditExecutionDegreeDispatchAction extends FenixDispatchAction
{

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException
    {
        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm dynaForm = (DynaActionForm) form;

        Integer executionDegreeId = new Integer(request.getParameter("executionDegreeId"));

        InfoExecutionDegree oldInfoExecutionDegree = null;
        Object args[] = {executionDegreeId};

        try
        {
            oldInfoExecutionDegree = (InfoExecutionDegree) ServiceUtils.executeService(userView,
                    "ReadExecutionDegree", args);
        }
        catch (NonExistingServiceException e)
        {
            throw new NonExistingActionException("message.nonExistingExecutionDegree", mapping
                    .findForward("readDegreeCurricularPlan"));
        }
        catch (FenixServiceException fenixServiceException)
        {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        /* Needed service and creation of bean of InfoExecutionYears for use in jsp */
        List infoExecutionYearList = null;
        List infoCampusList;
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

        request.setAttribute("infoExecutionYearsList", infoExecutionYearList);
        request.setAttribute("infoCampusList", infoCampusList);

        dynaForm.set("tempExamMap", oldInfoExecutionDegree.getTemporaryExamMap().toString());
//        dynaForm.set("coordinatorNumber", oldInfoExecutionDegree.getInfoCoordinator().getTeacherNumber()
//                .toString());
        dynaForm.set("executionYearId", oldInfoExecutionDegree.getInfoExecutionYear().getIdInternal()
                .toString());
        dynaForm.set("campusId", oldInfoExecutionDegree.getInfoCampus().getIdInternal().toString());

        return mapping.findForward("editExecutionDegree");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException
    {
        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));
        Integer executionDegreeId = new Integer(request.getParameter("executionDegreeId"));

        DynaActionForm dynaForm = (DynaValidatorForm) form;
        String executionYearString = (String) dynaForm.get("executionYearId");
        //String coordinatorNumberString = (String) dynaForm.get("coordinatorNumber");
        String campusIdString = (String) dynaForm.get("campusId");
        String tempExamMapString = (String) dynaForm.get("tempExamMap");

        InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();

        InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
        infoExecutionYear.setIdInternal(new Integer(executionYearString));
        infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);

        //InfoTeacher infoTeacher = new InfoTeacher();
        //infoTeacher.setTeacherNumber(new Integer(coordinatorNumberString));
        //infoExecutionDegree.setInfoCoordinator(infoTeacher);

        infoExecutionDegree.setTemporaryExamMap(new Boolean(tempExamMapString));

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
        infoDegreeCurricularPlan.setIdInternal(degreeCurricularPlanId);
        infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
        infoExecutionDegree.setIdInternal(executionDegreeId);

        InfoCampus infoCampus = new InfoCampus(Integer.valueOf(campusIdString));
        infoExecutionDegree.setInfoCampus(infoCampus);

        Object args[] = {infoExecutionDegree};

        try
        {
            ServiceUtils.executeService(userView, "EditExecutionDegree", args);

        }
        catch (ExistingServiceException e)
        {
            throw new ExistingActionException("message.manager.existing.execution.degree");
        }
        catch (NonExistingServiceException ex)
        {
            throw new NonExistingActionException(ex.getMessage(), mapping.findForward(
                    "readDegreeCurricularPlan"));
        }
        catch (FenixServiceException fenixServiceException)
        {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        return mapping.findForward("readDegreeCurricularPlan");
    }
}