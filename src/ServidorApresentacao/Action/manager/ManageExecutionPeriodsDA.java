/*
 * Created on 2003/07/16
 *  
 */
package ServidorApresentacao.Action.manager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.comparators.ExecutionPeriodComparator;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.manager.CreateExecutionPeriod.ExistingExecutionPeriod;
import ServidorAplicacao.Servico.manager.CreateExecutionPeriod.InvalidExecutionPeriod;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.InvalidArgumentsActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.PeriodState;

/**
 * @author Luis Crus & Sara Ribeiro
 */
public class ManageExecutionPeriodsDA extends FenixDispatchAction {

    /**
     * Prepare information to show existing execution periods and working areas.
     */
    public ActionForward prepare(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        try {
            List infoExecutionPeriods = (List) ServiceUtils.executeService(
                    userView, "ReadExecutionPeriods", null);

            if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty()) {

                Collections.sort(infoExecutionPeriods,
                        new ExecutionPeriodComparator());

                if (infoExecutionPeriods != null
                        && !infoExecutionPeriods.isEmpty()) {
                    request.setAttribute(
                            SessionConstants.LIST_EXECUTION_PERIODS,
                            infoExecutionPeriods);
                }

            }
        } catch (FenixServiceException ex) {
            throw new FenixActionException(
                    "Problemas de comunicação com a base de dados.", ex);
        }

        return mapping.findForward("Manage");
    }

    /**
     * Prepare information to show existing execution periods and working areas.
     */
    public ActionForward createExecutionPeriod(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaValidatorForm createExecutionPeriodForm = (DynaValidatorForm) form;

        Integer semesterToCreate = new Integer(
                (String) createExecutionPeriodForm.get("semesterToCreate"));
        String yearToCreate = (String) createExecutionPeriodForm
                .get("yearToCreate");
        Integer semesterToExportDataFrom = new Integer(
                (String) createExecutionPeriodForm
                        .get("semesterToExportDataFrom"));
        String yearToExportDataFrom = (String) createExecutionPeriodForm
                .get("yearToExportDataFrom");

        IUserView userView = SessionUtils.getUserView(request);

        InfoExecutionYear infoExecutionYearToCreate = new InfoExecutionYear(
                yearToCreate);
        InfoExecutionPeriod infoExecutionPeriodToCreate = new InfoExecutionPeriod(
                "" + semesterToCreate + " Semestre", infoExecutionYearToCreate);
        infoExecutionPeriodToCreate.setSemester(new Integer(semesterToCreate
                .intValue()));
        InfoExecutionYear infoExecutionYearToExportDataFrom = new InfoExecutionYear(
                yearToExportDataFrom);
        InfoExecutionPeriod infoExecutionPeriodToExportDataFrom = new InfoExecutionPeriod(
                "" + semesterToExportDataFrom + " Semestre",
                infoExecutionYearToExportDataFrom);
        infoExecutionPeriodToExportDataFrom
                .setSemester(semesterToExportDataFrom);

        Object[] argsCreateWorkingArea = { infoExecutionPeriodToCreate,
                infoExecutionPeriodToExportDataFrom};
        try {
            ServiceUtils.executeService(userView, "CreateExecutionPeriod",
                    argsCreateWorkingArea);
        } catch (InvalidExecutionPeriod ex) {
            throw new InvalidArgumentsActionException(
                    "O periodo indicado para importar os dados", ex);
        } catch (ExistingExecutionPeriod ex) {
            throw new ExistingActionException("O periodo indicado", ex);
        } catch (FenixServiceException ex) {
            throw new FenixActionException(
                    "Problemas a criar o periodo execução.", ex);
        }

        return mapping.findForward("Sucess");
    }

    /**
     * Prepare information to show existing execution periods and working areas.
     */
    public ActionForward deleteWorkingArea(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String year = request.getParameter("year");
        Integer semester = new Integer(request.getParameter("semester"));

        InfoExecutionYear infoExecutionYear = new InfoExecutionYear(year);
        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("AT:"
                + semester + "º Semestre", infoExecutionYear);
        infoExecutionPeriod.setSemester(new Integer(semester.intValue()));

        IUserView userView = SessionUtils.getUserView(request);

        Object[] argsDeleteWorkingArea = { infoExecutionPeriod};
        try {
            ServiceUtils.executeService(userView, "DeleteWorkingArea",
                    argsDeleteWorkingArea);
        } catch (FenixServiceException ex) {
            throw new FenixActionException(
                    "Problemas a apagar a área de trabalho.", ex);
        }

        return prepare(mapping, form, request, response);
    }

    /**
     * Prepare information to show existing execution periods and working areas.
     */
    public ActionForward alterExecutionPeriodState(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String year = request.getParameter("year");
        Integer semester = new Integer(request.getParameter("semester"));
        String periodStateToSet = request.getParameter("periodState");

        InfoExecutionYear infoExecutionYear = new InfoExecutionYear(year);
        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod(
                semester + " Semestre", infoExecutionYear);
        infoExecutionPeriod.setSemester(new Integer(semester.intValue()));

        PeriodState periodState = new PeriodState(periodStateToSet);

        IUserView userView = SessionUtils.getUserView(request);

        Object[] argsAlterExecutionPeriodState = { infoExecutionPeriod,
                periodState};
        try {
            ServiceUtils.executeService(userView, "AlterExecutionPeriodState",
                    argsAlterExecutionPeriodState);
        } catch (InvalidExecutionPeriod ex) {
            throw new FenixActionException(
                    "O periodo execução selecionado não existe.", ex);
        } catch (FenixServiceException ex) {
            throw new FenixActionException(
                    "Problemas a apagar a área de trabalho.", ex);
        }

        return prepare(mapping, form, request, response);
    }

}